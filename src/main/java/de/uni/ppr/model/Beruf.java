package de.uni.ppr.model;

import java.util.Objects;

public class Beruf {
    private final String bezeichnung;

    public Beruf(String bezeichnung) {
        if (bezeichnung == null || bezeichnung.isBlank()) {
            this.bezeichnung = "Unbekannt";
        } else {
            this.bezeichnung = bezeichnung.trim();
        }
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    @Override
    public String toString() {
        return bezeichnung;
    }



}