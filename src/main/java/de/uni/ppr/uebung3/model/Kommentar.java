package de.uni.ppr.uebung3.model;

public class Kommentar extends Entity {




    private final Rede rede;
    private final String inhalt;


    public Kommentar(String id, Rede rede, String inhalt) {
        super(id);
        this.rede = rede;
        this.inhalt = inhalt;
    }

    public Rede getRede() {
        return rede;
    }

    public String getInhalt() {
        return inhalt;
    }
}
