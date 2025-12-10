package de.uni.ppr.uebung3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class Fraktion extends Entity {



    private final String name;
    private final List<Abgeordneter> mitglieder = new ArrayList<>();


    public Fraktion(String id, String name) {
        super(id);
        this.name = name;
    }

    public String getFraktionName() {
        return name;
    };

    public String getName() {
        return name;
    }


    public List<Abgeordneter> getMitglieder() {
        return Collections.unmodifiableList(mitglieder);
    }


    public void addMitglied(Abgeordneter abgeordneter) {
        if (!mitglieder.contains(abgeordneter)) {
            mitglieder.add(abgeordneter);
        }
    }
}
