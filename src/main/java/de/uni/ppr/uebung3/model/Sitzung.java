package de.uni.ppr.uebung3.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;






public class Sitzung extends Entity {

    private final int nummer;
    private final LocalDate datum;
    private final String ort;
    private final LocalTime start;
    private final LocalTime ende;
    private final List<Tagesordnungspunkt> tops = new ArrayList<>();






    public Sitzung(String id,
                   int nummer,
                   LocalDate datum,
                   String ort,
                   LocalTime start,
                   LocalTime ende) {
        super(id);
        this.nummer = nummer;
        this.datum = datum;
        this.ort = ort;
        this.start = start;
        this.ende = ende;
    }

    public int getNummer() {
        return nummer;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public String getOrt() {
        return ort;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnde() {
        return ende;
    }

    public void addTop(Tagesordnungspunkt top) {
        tops.add(top);
    }

    public List<Tagesordnungspunkt> getTops() {
        return Collections.unmodifiableList(tops);
    }
}
