package de.uni.ppr.uebung3.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rede extends Entity {

    private final Abgeordneter redner;
    private final Sitzung sitzung;
    private final Tagesordnungspunkt top;
    private final LocalDateTime start;
    private final LocalDateTime ende;
    private final String text;
    private final List<Kommentar> kommentare = new ArrayList<>();


    public Rede(String id,
                Abgeordneter redner,
                Sitzung sitzung,
                Tagesordnungspunkt top,
                LocalDateTime start,
                LocalDateTime ende,
                String text) {
        super(id);
        this.redner = redner;
        this.sitzung = sitzung;
        this.top = top;
        this.start = start;
        this.ende = ende;
        this.text = text;
    }




    public Abgeordneter getRedner() {
        return redner;
    }

    public Sitzung getSitzung() {
        return sitzung;
    }



    public Tagesordnungspunkt getTop() {
        return top;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnde() {
        return ende;
    }

    public String getText() {
        return text;
    }

    public void addKommentar(Kommentar kommentar) {
        kommentare.add(kommentar);
    }

    public List<Kommentar> getKommentare() {
        return Collections.unmodifiableList(kommentare);
    }


    public int getKommentarAnzahl() {
        return kommentare.size();
    }


    public int getTextLaenge() {
        return text != null ? text.length() : 0;
    }
}
