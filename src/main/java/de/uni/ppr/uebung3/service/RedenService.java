package de.uni.ppr.uebung3.service;

import de.uni.ppr.uebung3.factory.RedenPortalFactory;
import de.uni.ppr.uebung3.model.Abgeordneter;
import de.uni.ppr.uebung3.model.Rede;

import java.util.*;
import java.util.stream.Collectors;



/**
 * Service-Klasse für Reden-Operationen.
 * Nutzt die RedenPortalFactory aus Übung 2.
 *
 * @author Parsa Aminian
 * @version 1.0
 */
public class RedenService {

    private final RedenPortalFactory factory;

    /**
     * Konstruktor mit Factory-Injection.
     *
     * @param factory RedenPortalFactory aus Übung 2
     */
    public RedenService(RedenPortalFactory factory) {
        this.factory = factory;
        System.out.println("[RedenService] Initialisiert mit " + getAnzahl() + " Reden");
    }

    /**
     * Gibt alle Reden zurück.
     *
     * @return Liste aller Reden
     */
    public List<Rede> getAllReden() {
        return new ArrayList<>(factory.getRedeRepository().findAll());
    }

    /**
     * Gibt eine Rede anhand der ID zurück.
     *
     * @param id Rede-ID
     * @return Rede oder null
     */
    public Rede getRedeById(String id) {
        return factory.getRedeRepository().findById(id).orElse(null);
    }

    /**
     * Gibt alle Reden eines Abgeordneten zurück.
     *
     * @param abgeordneterId ID des Abgeordneten
     * @return Liste der Reden
     */
    public List<Rede> getRedenByAbgeordneter(String abgeordneterId) {
        Abgeordneter abg = factory.getAbgeordneterRepository().findById(abgeordneterId).orElse(null);
        if (abg == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(abg.getReden());
    }

    /**
     * Sortiert Reden nach Datum (neueste zuerst).
     *
     * @param reden Liste der zu sortierenden Reden
     * @return Sortierte Liste
     */
    public List<Rede> sortRedenByDate(List<Rede> reden) {
        return reden.stream()
                .sorted(Comparator.comparing(Rede::getStart).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Filtert Reden nach minimaler Textlänge.
     *
     * @param minLength Minimale Textlänge
     * @return Gefilterte Liste
     */
    public List<Rede> filterRedenByLength(int minLength) {
        return getAllReden().stream()
                .filter(r -> r.getTextLaenge() >= minLength)
                .collect(Collectors.toList());
    }

    /**
     * Gibt die Anzahl aller Reden zurück.
     *
     * @return Anzahl
     */
    public int getAnzahl() {
        return getAllReden().size();
    }

    /**
     * Berechnet die durchschnittliche Redelänge.
     *
     * @return Durchschnittliche Länge in Zeichen
     */
    public double getDurchschnittRedelaenge() {
        return getAllReden().stream()
                .mapToInt(Rede::getTextLaenge)
                .average()
                .orElse(0.0);
    }

    /**
     * Gibt Zugriff auf die Factory.
     *
     * @return RedenPortalFactory
     */
    public RedenPortalFactory getFactory() {
        return factory;
    }
}