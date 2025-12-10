package de.uni.ppr.uebung3.service;

import de.uni.ppr.uebung3.factory.RedenPortalFactory;
import de.uni.ppr.uebung3.model.Abgeordneter;
import de.uni.ppr.uebung3.model.Fraktion;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service-Klasse für Abgeordneten-Operationen.
 * Nutzt die RedenPortalFactory aus Übung 2.
 *
 * @author Parsa Aminian
 * @version 1.0
 */
public class AbgeordnetenService {

    private final RedenPortalFactory factory;

    /**
     * Konstruktor mit Factory-Injection.
     *
     * @param factory RedenPortalFactory aus Übung 2
     */
    public AbgeordnetenService(RedenPortalFactory factory) {
        this.factory = factory;
        System.out.println("[AbgeordnetenService] Initialisiert mit " + getAnzahl() + " Abgeordneten");
    }

    /**
     * Gibt alle Abgeordneten zurück.
     *
     * @return Liste aller Abgeordneten
     */
    public List<Abgeordneter> getAllAbgeordnete() {
        return new ArrayList<>(factory.getAbgeordneterRepository().findAll());
    }

    /**
     * Gibt einen Abgeordneten anhand der ID zurück.
     *
     * @param id Abgeordneten-ID
     * @return Abgeordneter oder null
     */
    public Abgeordneter getAbgeordneterById(String id) {
        return factory.getAbgeordneterRepository().findById(id).orElse(null);
    }

    /**
     * Sucht Abgeordnete nach Name (Vorname oder Nachname).
     *
     * @param query Suchbegriff
     * @return Liste gefundener Abgeordneter
     */
    public List<Abgeordneter> searchAbgeordnete(String query) {
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String trimmed = query.trim();

        // 1) Wenn die Eingabe direkt einer ID entspricht, gebe den Abgeordneten zurück
        Optional<Abgeordneter> byId = factory.getAbgeordneterRepository().findById(trimmed);
        if (byId.isPresent()) {
            return Collections.singletonList(byId.get());
        }

        // 2) Sonst wie bisher per Namen suchen (Teilstring, case-insensitive)
        String searchTerm = trimmed.toLowerCase();

        return getAllAbgeordnete().stream()
                .filter(a -> {
                    String fullName = (a.getVorname() + " " + a.getNachname()).toLowerCase();
                    return a.getNachname().toLowerCase().contains(searchTerm) ||
                            a.getVorname().toLowerCase().contains(searchTerm) ||
                            fullName.contains(searchTerm);
                })
                .collect(Collectors.toList());
    }

    /**
     * Filtert Abgeordnete nach Fraktion.
     *
     * @param fraktionName Name der Fraktion
     * @return Liste der Abgeordneten dieser Fraktion
     */
    public List<Abgeordneter> getAbgeordneteByFraktion(String fraktionName) {
        if (fraktionName == null || fraktionName.trim().isEmpty()) {
            return getAllAbgeordnete();
        }

        return getAllAbgeordnete().stream()
                .filter(a -> a.getFraktion() != null &&
                        a.getFraktion().getName().equalsIgnoreCase(fraktionName.trim()))
                .collect(Collectors.toList());
    }

    /**
     * Sortiert Abgeordnete nach Nachname (A-Z).
     *
     * @return Sortierte Liste
     */
    public List<Abgeordneter> getAbgeordneteSortedByName() {
        return getAllAbgeordnete().stream()
                .sorted(Comparator.comparing(Abgeordneter::getNachname)
                        .thenComparing(Abgeordneter::getVorname))
                .collect(Collectors.toList());
    }

    /**
     * Sortiert Abgeordnete nach Anzahl der Reden (absteigend).
     *
     * @return Sortierte Liste
     */
    public List<Abgeordneter> getAbgeordneteSortedByRedenCount() {
        return getAllAbgeordnete().stream()
                .sorted(Comparator.comparing(
                        (Abgeordneter a) -> a.getReden().size()
                ).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Gibt alle eindeutigen Fraktionen zurück.
     *
     * @return Collection aller Fraktionen
     */
    public Collection<Fraktion> getAllFraktionen() {
        return factory.getFraktionRepository().findAll();
    }

    /**
     * Gibt die Anzahl aller Abgeordneten zurück.
     *
     * @return Anzahl
     */
    public int getAnzahl() {
        return getAllAbgeordnete().size();
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
