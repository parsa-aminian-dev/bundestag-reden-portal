package de.uni.ppr.uebung3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;









public class Abgeordneter extends Entity {




    private final String vorname;
    private final String nachname;
    private final String titel;


    private final Fraktion fraktion;
    private final List<Rede> reden = new ArrayList<>();


    public Abgeordneter(String id,
                        String vorname,
                        String nachname,
                        String titel,
                        Fraktion fraktion) {
        super(id);
        this.vorname = vorname;
        this.nachname = nachname;
        this.titel = titel;
        this.fraktion = fraktion;
    }

    public Abgeordneter(String id,
                        String vorname,
                        String nachname,
                        String titel) {
        this(id, vorname, nachname, titel, null);
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public String getTitel() {
        return titel;
    }

    public Fraktion getFraktion() {
        return fraktion;
    }

    public void addRede(Rede rede) {
        if (rede != null) {
            reden.add(rede);
        }
    }

    public List<Rede> getReden() {
        return Collections.unmodifiableList(reden);
    }

    @Override
    public String toString() {
        return "Abgeordneter{" +
                "id='" + getId() + '\'' +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", titel='" + titel + '\'' +
                ", fraktion=" + (fraktion != null ? fraktion.getName() : "keine") +
                '}';
    }
}
