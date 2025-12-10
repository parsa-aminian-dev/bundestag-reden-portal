package de.uni.ppr.model;

import java.util.ArrayList;
import java.util.List;

public class Fraktion {
    private final String name;
    private final List<Abgeordnete> mitglieder = new ArrayList<>();

    public Fraktion(String name) {
        this.name = (name == null || name.isBlank()) ? "Unbekannt" : name;
    }

    public String getName() { return name; }

    public List<Abgeordnete> getMitglieder() { return mitglieder; }

    public void add(Abgeordnete a) {
        if (a != null) mitglieder.add(a);
    }

    public int size() { return mitglieder.size(); }

    @Override
    public String toString() {
        return "Fraktion " + name + " (" + size() + ")";
    }
}
