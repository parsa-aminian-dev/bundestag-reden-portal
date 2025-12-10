package de.uni.ppr.app;

import de.uni.ppr.loader.AbgeordneteLoader;
import de.uni.ppr.model.Abgeordnete;
import de.uni.ppr.model.Fraktion;
import de.uni.ppr.util.FraktionGrouper;
import de.uni.ppr.util.NameComparator;


import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {

        URL res = Main.class.getClassLoader().getResource("mdb.json");
        if (res == null) {
            System.err.println("mdb.json nicht gefunden (src/main/resources).");
            return;
        }
        String path = Paths.get(res.toURI()).toString();

        List<Abgeordnete> alle = AbgeordneteLoader.loadFromJson(path);


        Map<String, Fraktion> gruppiert = FraktionGrouper.groupByFraktion(alle);


        NameComparator byName = new NameComparator();
        gruppiert.values().forEach(fr -> fr.getMitglieder().sort(byName));

        System.out.println("*********** Abgeordnete nach Fraktion ***********");
        gruppiert.values().stream()
                .sorted(Comparator.comparing(Fraktion::getName, String.CASE_INSENSITIVE_ORDER))
                .forEach(fr -> {
                    System.out.println("\n[Fraktion] " + fr.getName() + " (" + fr.size() + ")");
                    fr.getMitglieder().forEach(a -> System.out.println("  - " + a));
                });


    }
}




