package de.uni.ppr.util;

import de.uni.ppr.model.Abgeordnete;
import de.uni.ppr.model.Fraktion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FraktionGrouper {

    public static Map<String, Fraktion> groupByFraktion(List<Abgeordnete> alle) {
        Map<String, Fraktion> map = new HashMap<>();
        for (Abgeordnete a : alle) {
            String frName = a.getFraktion();
            Fraktion fr = map.computeIfAbsent(frName, Fraktion::new);
            fr.add(a);
        }
        return map;
    }
}
