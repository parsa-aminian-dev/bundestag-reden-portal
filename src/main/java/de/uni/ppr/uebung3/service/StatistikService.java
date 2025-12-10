package de.uni.ppr.uebung3.service;

import de.uni.ppr.uebung3.model.Abgeordneter;
import de.uni.ppr.uebung3.model.Fraktion;
import de.uni.ppr.uebung3.model.Rede;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service-Klasse für statistische Auswertungen.
 *
 * @author Parsa Aminian
 * @version 1.0
 */
public class StatistikService {

    private final AbgeordnetenService abgeordnetenService;
    private final RedenService redenService;

    /**
     * Konstruktor mit Dependency Injection.
     *
     * @param abgeordnetenService Service für Abgeordnete
     * @param redenService Service für Reden
     */
    public StatistikService(AbgeordnetenService abgeordnetenService, RedenService redenService) {
        this.abgeordnetenService = abgeordnetenService;
        this.redenService = redenService;
        System.out.println("[StatistikService] Initialisiert");
    }

    /**
     * Erstellt ein JSON-Objekt mit allen Statistiken.
     *
     * @return JSONObject mit Statistiken
     */
    public JSONObject getGesamtStatistiken() {
        JSONObject stats = new JSONObject();

        // Grundzahlen
        int gesamtAbgeordnete = abgeordnetenService.getAnzahl();
        int gesamtReden = redenService.getAnzahl();
        int gesamtFraktionen = abgeordnetenService.getAllFraktionen().size();

        stats.put("gesamtAbgeordnete", gesamtAbgeordnete);
        stats.put("gesamtReden", gesamtReden);
        stats.put("gesamtFraktionen", gesamtFraktionen);

        // Durchschnittswerte
        double avgRedelaenge = redenService.getDurchschnittRedelaenge();
        double avgRedenProAbg = gesamtAbgeordnete > 0 ? (double) gesamtReden / gesamtAbgeordnete : 0;

        stats.put("durchschnittRedelaenge", Math.round(avgRedelaenge));
        stats.put("durchschnittRedenProAbgeordneter", Math.round(avgRedenProAbg * 100) / 100.0);

        // Top-Redner
        stats.put("topRedner", getTopRedner(5));

        // Fraktions-Statistiken
        stats.put("fraktionsStatistiken", getFraktionsStatistiken());

        return stats;
    }

    /**
     * Gibt die Top-Redner nach Anzahl der Reden zurück.
     * KORRIGIERT: Fraktionsinformation wird korrekt geholt
     *
     * @param limit Maximale Anzahl
     * @return JSONArray mit Top-Rednern
     */
    public JSONArray getTopRedner(int limit) {
        JSONArray topRedner = new JSONArray();

        abgeordnetenService.getAllAbgeordnete().stream()
                .filter(a -> !a.getReden().isEmpty())
                .sorted(Comparator.comparing(
                        (Abgeordneter a) -> a.getReden().size()
                ).reversed())
                .limit(limit)
                .forEach(abg -> {
                    JSONObject obj = new JSONObject();
                    obj.put("name", abg.getVorname() + " " + abg.getNachname());
                    obj.put("id", abg.getId());
                    obj.put("redenAnzahl", abg.getReden().size());

                    // KORRIGIERT: Korrekte Fraktionszuweisung
                    String fraktionName = "Fraktionslos";
                    if (abg.getFraktion() != null && abg.getFraktion().getName() != null) {
                        fraktionName = abg.getFraktion().getName();
                    }
                    obj.put("fraktion", fraktionName);

                    topRedner.put(obj);
                });

        return topRedner;
    }

    /**
     * Erstellt Statistiken pro Fraktion.
     * KORRIGIERT: Null-Checks hinzugefügt
     *
     * @return JSONArray mit Fraktions-Statistiken
     */
    public JSONArray getFraktionsStatistiken() {
        JSONArray stats = new JSONArray();

        Map<String, List<Abgeordneter>> fraktionMap =
                abgeordnetenService.getAllAbgeordnete().stream()
                        .filter(a -> a.getFraktion() != null && a.getFraktion().getName() != null)
                        .collect(Collectors.groupingBy(
                                a -> a.getFraktion().getName()
                        ));

        fraktionMap.forEach((fraktionName, abgeordnete) -> {
            JSONObject fraktionStats = new JSONObject();
            fraktionStats.put("name", fraktionName);
            fraktionStats.put("mitgliederAnzahl", abgeordnete.size());

            int gesamtReden = abgeordnete.stream()
                    .mapToInt(a -> a.getReden().size())
                    .sum();
            fraktionStats.put("gesamtReden", gesamtReden);

            double avgReden = abgeordnete.isEmpty() ? 0 : (double) gesamtReden / abgeordnete.size();
            fraktionStats.put("durchschnittRedenProMitglied", Math.round(avgReden * 100) / 100.0);

            double avgLaenge = abgeordnete.stream()
                    .flatMap(a -> a.getReden().stream())
                    .mapToInt(Rede::getTextLaenge)
                    .average()
                    .orElse(0.0);
            fraktionStats.put("durchschnittRedelaenge", Math.round(avgLaenge));

            stats.put(fraktionStats);
        });

        // Sortiere nach Anzahl Mitglieder (absteigend)
        List<JSONObject> sortedStats = new ArrayList<>();
        for (int i = 0; i < stats.length(); i++) {
            sortedStats.add(stats.getJSONObject(i));
        }
        sortedStats.sort(Comparator.comparing(
                (JSONObject o) -> o.getInt("mitgliederAnzahl")
        ).reversed());

        JSONArray sortedArray = new JSONArray();
        sortedStats.forEach(sortedArray::put);

        return sortedArray;
    }

    /**
     * Berechnet die durchschnittliche Redelänge pro Abgeordnetem.
     *
     * @param abgeordneterId ID des Abgeordneten
     * @return Durchschnittliche Länge
     */
    public double getDurchschnittRedelaengeProAbgeordneter(String abgeordneterId) {
        List<Rede> reden = redenService.getRedenByAbgeordneter(abgeordneterId);

        if (reden.isEmpty()) {
            return 0.0;
        }

        return reden.stream()
                .mapToInt(Rede::getTextLaenge)
                .average()
                .orElse(0.0);
    }

    /**
     * Gibt den aktivsten Redner zurück.
     *
     * @return Abgeordneter mit den meisten Reden
     */
    public Abgeordneter getAktivsterRedner() {
        return abgeordnetenService.getAllAbgeordnete().stream()
                .filter(a -> !a.getReden().isEmpty())
                .max(Comparator.comparing(a -> a.getReden().size()))
                .orElse(null);
    }

    /**
     * Gibt die längste Rede zurück.
     *
     * @return Rede mit der größten Textlänge
     */
    public Rede getLaengsteRede() {
        return redenService.getAllReden().stream()
                .max(Comparator.comparing(Rede::getTextLaenge))
                .orElse(null);
    }
}