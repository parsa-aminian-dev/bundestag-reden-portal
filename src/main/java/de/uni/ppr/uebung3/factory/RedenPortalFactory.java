package de.uni.ppr.uebung3.factory;

import de.uni.ppr.uebung3.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Optional;

public class RedenPortalFactory {

    private final AbgeordneterRepository abgeordneterRepo = new AbgeordneterRepository();
    private final FraktionRepository fraktionRepo = new FraktionRepository();
    private final SitzungRepository sitzungRepo = new SitzungRepository();
    private final TOPRepository topRepo = new TOPRepository();
    private final RedeRepository redeRepo = new RedeRepository();
    private final KommentarRepository kommentarRepo = new KommentarRepository();





    /**
     * Normalisiert Fraktionsnamen für konsistente Zuordnung.
     *
     * @param raw Roher Fraktionsname aus XML
     * @return Normalisierter Fraktionsname
     */
    private String normalizeFraktionsName(String raw) {
        // NULL oder leer → Fraktionslos
        if (raw == null || raw.isBlank()) {
            return "Fraktionslos";
        }

        // Bereinige Whitespace und Sonderzeichen
        String cleaned = raw
                .replace('\u00A0', ' ')      // Non-breaking space
                .replace('\n', ' ')           // Newlines
                .replace('\r', ' ')           // Carriage returns
                .replace('\t', ' ')           // Tabs
                .replaceAll("\\s+", " ")      // Multiple spaces → single space
                .trim();                      // Führende/trailing spaces

        // Wenn nach Bereinigung leer → Fraktionslos
        if (cleaned.isEmpty()) {
            return "Fraktionslos";
        }

        String upper = cleaned.toUpperCase();

        // ==================== EXAKTE ZUORDNUNGEN ====================

        // SPD
        if (upper.equals("SPD") || upper.contains("SOZIALDEMOKRAT") || upper.contains("SPDCDU/CSU")) {
            return "SPD";
        }

        // CDU/CSU
        if (upper.equals("CDU/CSU") ||
                upper.equals("CDU") ||
                upper.equals("CSU") ||
                upper.contains("CHRISTLICH DEMOKRAT") ||
                upper.contains("CHRISTLICH-SOZIAL")) {
            return "CDU/CSU";
        }

        // Bündnis 90/Die Grünen
        if (upper.contains("GRÜN") ||
                upper.contains("BÜNDNIS") ||
                upper.contains("BUENDNIS") ||
                upper.contains("B90") ||
                upper.contains("B'90") ||
                upper.equals("BÜNDNIS 90/DIE GRÜNEN")) {
            return "BÜNDNIS 90/DIE GRÜNEN";
        }

        // FDP
        if (upper.equals("FDP") ||
                upper.contains("FREIE DEMOKRAT") ||
                upper.contains("F.D.P")) {
            return "FDP";
        }

        // Die Linke
        if (upper.equals("DIE LINKE") ||
                upper.equals("LINKE") ||
                upper.contains("DIE LINKE") ||
                upper.equals("PDS")) {
            return "DIE LINKE";
        }

        // AfD
        if (upper.equals("AFD") ||
                upper.equals("AF D") ||
                upper.contains("ALTERNATIVE") ||
                upper.equals("ALTERNATIVE FÜR DEUTSCHLAND")) {
            return "AfD";
        }



        // BSW (Bündnis Sahra Wagenknecht)
        if (upper.equals("BSW") ||
                upper.contains("WAGENKNECHT") ||
                upper.contains("BÜNDNIS SAHRA WAGENKNECHT")) {
            return "BSW";
        }


        // Fraktionslos (explizit)
        if (upper.contains("FRAKTIONSLOS") ||
                upper.equals("PARTEILOS") ||
                upper.equals("OHNE FRAKTION")) {
            return "Fraktionslos";
        }

        // ==================== FALLBACK ====================

        // Wenn keine Zuordnung gefunden → Original zurückgeben (mit Warning)
        System.out.println("⚠️  Unbekannte Fraktion: '" + cleaned + "' → Bitte prüfen!");
        return cleaned;
    }









    public Fraktion getOrCreateFraktion(String name) {

        String fraktionsId = "FRAKTION_" + name.toUpperCase(Locale.ROOT)
                .replaceAll("\\s+", "_");

        Optional<Fraktion> existing = fraktionRepo.findById(fraktionsId);
        if (existing.isPresent()) {
            return existing.get();
        }

        Fraktion fraktion = new Fraktion(fraktionsId, name);
        fraktionRepo.save(fraktion);
        return fraktion;
    }



    public Abgeordneter getOrCreateAbgeordneter(String id,
                                                String vorname,
                                                String nachname,
                                                String titel,
                                                String fraktionsName) {

        String normName = normalizeFraktionsName(fraktionsName);
        Fraktion fraktion = getOrCreateFraktion(normName);

        return abgeordneterRepo.findById(id).orElseGet(() -> {
            Abgeordneter a = new Abgeordneter(id, vorname, nachname, titel, fraktion);
            abgeordneterRepo.save(a);
            fraktion.addMitglied(a);
            return a;
        });
    }






    public Sitzung createSitzung(String id,
                                 int nummer,
                                 LocalDate datum,
                                 String ort,
                                 LocalTime start,
                                 LocalTime ende) {
        Sitzung s = new Sitzung(id, nummer, datum, ort, start, ende);
        sitzungRepo.save(s);
        return s;
    }


    public Tagesordnungspunkt createTop(String id,
                                        String nummer,
                                        String titel,
                                        Sitzung sitzung) {
        Tagesordnungspunkt top = new Tagesordnungspunkt(id, nummer, titel, sitzung);
        topRepo.save(top);
        sitzung.addTop(top);
        return top;
    }




    public Rede createRede(String id,
                           Abgeordneter redner,
                           Sitzung sitzung,
                           Tagesordnungspunkt top,
                           LocalDateTime start,
                           LocalDateTime ende,
                           String text) {
        Rede rede = new Rede(id, redner, sitzung, top, start, ende, text);
        redeRepo.save(rede);
        top.addRede(rede);
        redner.addRede(rede);
        return rede;
    }

    public Kommentar createKommentar(String id, Rede rede, String inhalt) {
        Kommentar kommentar = new Kommentar(id, rede, inhalt);
        kommentarRepo.save(kommentar);
        rede.addKommentar(kommentar);
        return kommentar;
    }





    public Repository<Abgeordneter> getAbgeordneterRepository() {
        return abgeordneterRepo;
    }

    public Repository<Fraktion> getFraktionRepository() {
        return fraktionRepo;
    }

    public Repository<Sitzung> getSitzungRepository() {
        return sitzungRepo;
    }

    public Repository<Tagesordnungspunkt> getTopRepository() {
        return topRepo;
    }

    public Repository<Rede> getRedeRepository() {
        return redeRepo;
    }

    public Repository<Kommentar> getKommentarRepository() {
        return kommentarRepo;
    }
}
