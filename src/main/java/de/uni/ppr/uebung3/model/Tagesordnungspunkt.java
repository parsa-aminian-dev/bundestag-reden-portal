package de.uni.ppr.uebung3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;




public class Tagesordnungspunkt extends Entity {



    private final String nummer;
    private final String titel;
    private final Sitzung sitzung;
    private final List<Rede> reden = new ArrayList<>();



    public Tagesordnungspunkt(String id,
                              String nummer,
                              String titel,
                              Sitzung sitzung) {
        super(id);
        this.nummer = nummer;
        this.titel = titel;
        this.sitzung = sitzung;
    }

    public String getNummer() {
        return nummer;
    }







    public String getTitel() {
        return titel;
    }






    public Sitzung getSitzung() {
        return sitzung;
    }






    public void addRede(Rede rede) {
        reden.add(rede);
    }






    public List<Rede> getReden() {
        return Collections.unmodifiableList(reden);
    }
}
