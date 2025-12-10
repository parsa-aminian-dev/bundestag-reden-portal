package de.uni.ppr.util;

import de.uni.ppr.model.Abgeordnete;

import java.util.Comparator;

public class NameComparator implements Comparator<Abgeordnete> {
    @Override
    public int compare(Abgeordnete a, Abgeordnete b) {
        String aLast = a.getName() == null ? "" : a.getName();
        String bLast = b.getName() == null ? "" : b.getName();
        int byLast = aLast.compareToIgnoreCase(bLast);
        if (byLast != 0) return byLast;

        String aFirst = a.getVorname() == null ? "" : a.getVorname();
        String bFirst = b.getVorname() == null ? "" : b.getVorname();
        return aFirst.compareToIgnoreCase(bFirst);
    }
}
