package de.uni.ppr.model;
import java.util.Locale;
import java.util.List;

public class Abgeordnete {
    private String ID;
    private String name;
    private String vorname;
    private String beruf;
    private List<String> membership;
    private String wp;


// Getter und Setter


    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }



    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }



    public String getVorname(){
        return vorname;
    }
    public void setVorname(String vorname){
        this.vorname = vorname;
    }





    public String getBeruf(){
        return beruf;
    }
    public void setBeruf(String beruf){
        this.beruf = beruf;
    }



    public List<String> getMembership(){
        return membership;
    }
    public void setMembership(List<String> membership){
        this.membership = membership;
    }



    public String getWp(){
        return wp;
    }
    public void setWp(String wp){
        this.wp = wp;
    }





    public String getFraktion() {
        if (membership == null) return "Unbekannt";
        for (String s : membership) {
            String lower = s.toLowerCase(Locale.ROOT).trim();
            /* Robuster und zukunftssicher aber ohne Parameter wird systemsprache benutzt */
            if (lower.startsWith("fraktion")) {
                int idx = s.indexOf(':');                       //wenns kein ":" gefunden wird (-1) --> False
                if (idx >= 0 && idx + 1 < s.length()) {         //wenns nachdem ":" kein Zeichen gibt --> False
                    return s.substring(idx + 1).trim();
                } else {
                    // Versuch: "Fraktion " entfernen, falls ohne Doppelpunkt
                    String after = s.replaceFirst("(?i)^\\s*Fraktion\\s*", "").trim();
                    return after.isEmpty() ? "Unbekannt" : after;
                }
            }
        }
        return "Unbekannt";
    }






    @Override
    public String toString(){
        return name + ", " + vorname + " (" + getFraktion() + ")";
    }









}
