package de.uni.ppr.uebung3.service;

import de.uni.ppr.uebung3.factory.RedenPortalFactory;
import de.uni.ppr.uebung3.model.Abgeordneter;
import de.uni.ppr.uebung3.model.Rede;
import de.uni.ppr.uebung3.model.Sitzung;
import de.uni.ppr.uebung3.model.Tagesordnungspunkt;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Service-Klasse für das Laden und Parsen von XML-Plenarprotokollen des Bundestags.
 * Importiert Sitzungen, Tagesordnungspunkte, Reden und Kommentare aus XML-Dateien.
 */
public class XmlProtokollLoader {

    private final RedenPortalFactory factory;
    private final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("H:mm[:ss]");

    public XmlProtokollLoader(RedenPortalFactory factory) {
        this.factory = factory;
    }

    public void importProtokoll(Path xmlFile) throws Exception {
        System.out.println("Lade Protokoll: " + xmlFile);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(false);

        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(xmlFile.toFile());
        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();
        if (!"dbtplenarprotokoll".equals(root.getTagName())) {
            System.err.println("Unerwartete Wurzel: " + root.getTagName());
            return;
        }


        Sitzung sitzung = createSitzung(doc);


        Element sitzungsverlauf = (Element) doc.getElementsByTagName("sitzungsverlauf").item(0);
        if (sitzungsverlauf == null) {
            System.err.println("Keine <sitzungsverlauf>-Sektion gefunden.");
            return;
        }


        NodeList topNodes = sitzungsverlauf.getElementsByTagName("tagesordnungspunkt");
        for (int i = 0; i < topNodes.getLength(); i++) {
            Node node = topNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element topElem = (Element) node;
            importTagesordnungspunkt(topElem, sitzung);
        }
    }



    private Sitzung createSitzung(Document doc) {
        Element kopfdaten = (Element) doc.getElementsByTagName("kopfdaten").item(0);
        if (kopfdaten == null) {
            throw new IllegalStateException("Keine <kopfdaten>-Sektion gefunden.");
        }


        Element plenarNum = (Element) kopfdaten.getElementsByTagName("plenarprotokoll-nummer").item(0);
        Element wahlperiodeElem = (Element) plenarNum.getElementsByTagName("wahlperiode").item(0);
        Element sitzungsnrElem = (Element) plenarNum.getElementsByTagName("sitzungsnr").item(0);

        int wahlperiode = parseIntegerField(wahlperiodeElem.getTextContent());
        int sitzungsnummer = parseIntegerField(sitzungsnrElem.getTextContent());



        Element veranstaltungsdaten = (Element) kopfdaten.getElementsByTagName("veranstaltungsdaten").item(0);
        Element ortElem = (Element) veranstaltungsdaten.getElementsByTagName("ort").item(0);
        Element datumElem = (Element) veranstaltungsdaten.getElementsByTagName("datum").item(0);

        String ort = ortElem.getTextContent().trim();
        String dateAttr = datumElem.getAttribute("date");

        if (dateAttr == null || dateAttr.isBlank()) {
            dateAttr = datumElem.getTextContent();
        }
        LocalDate datum = parseDatum(dateAttr);


        Element sitzungsbeginn = (Element) doc.getElementsByTagName("sitzungsbeginn").item(0);
        Element sitzungsende = (Element) doc.getElementsByTagName("sitzungsende").item(0);

        LocalTime start = LocalTime.MIDNIGHT;
        LocalTime ende = LocalTime.MIDNIGHT;

        if (sitzungsbeginn != null) {
            String startStr = sitzungsbeginn.getAttribute("sitzung-start-uhrzeit");
            start = parseTime(startStr);
        }
        if (sitzungsende != null) {
            String endeStr = sitzungsende.getAttribute("sitzung-ende-uhrzeit");
            ende = parseTime(endeStr);
        }



        String sitzungsId = "WP" + wahlperiode + "_S" + sitzungsnummer;

        return factory.createSitzung(sitzungsId, sitzungsnummer, datum, ort, start, ende);
    }






    private LocalDate parseDatum(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Datum ist null");
        }

        String text = raw.trim();


        if (text.contains(".")) {
            String[] parts = text.split("\\.");
            if (parts.length >= 3) {
                int tag = Integer.parseInt(parts[0].trim());
                int monat = Integer.parseInt(parts[1].trim());
                int jahr = Integer.parseInt(parts[2].trim());
                return LocalDate.of(jahr, monat, tag);
            }
        }


        if (text.contains("-")) {
            return LocalDate.parse(text);
        }


        throw new IllegalArgumentException("Unbekanntes Datumsformat: '" + raw + "'");
    }







    private LocalTime parseTime(String raw) {
        if (raw == null || raw.isBlank()) {

            return LocalTime.MIDNIGHT;
        }

        String text = raw.trim();


        if (text.contains(".") && !text.contains(":")) {
            text = text.replace('.', ':');
        }


        if (text.endsWith("Uhr")) {
            text = text.replace("Uhr", "").trim();
        }

        return LocalTime.parse(text, TIME_FORMAT);
    }







    private int parseIntegerField(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Zahl ist null");
        }

        String text = raw.trim();


        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isDigit(c)) {
                digits.append(c);
            } else {

                break;
            }
        }

        if (digits.length() == 0) {
            throw new IllegalArgumentException("Keine Ziffern in: '" + raw + "'");
        }

        return Integer.parseInt(digits.toString());
    }







    private void importTagesordnungspunkt(Element topElem, Sitzung sitzung) {
        String topIdAttr = topElem.getAttribute("top-id");
        String nummer = topIdAttr;
        if (nummer == null || nummer.isEmpty()) {
            nummer = "TOP-" + (factory.getTopRepository().findAll().size() + 1);
        }

        String titel = extractTopTitel(topElem);
        String topId = sitzung.getId() + "_" + nummer.replace(' ', '_');

        Tagesordnungspunkt top = factory.createTop(topId, nummer, titel, sitzung);


        NodeList redeNodes = topElem.getElementsByTagName("rede");
        for (int i = 0; i < redeNodes.getLength(); i++) {
            Node node = redeNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element redeElem = (Element) node;
            importRede(redeElem, sitzung, top);
        }
    }


    private String extractTopTitel(Element topElem) {
        NodeList pNodes = topElem.getElementsByTagName("p");
        StringBuilder titel = new StringBuilder();

        for (int i = 0; i < pNodes.getLength(); i++) {
            Node node = pNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element p = (Element) node;
            String klasse = p.getAttribute("klasse");
            if (klasse != null && klasse.startsWith("T_")) {
                String text = p.getTextContent().trim();
                if (!text.isEmpty()) {
                    if (titel.length() > 0) {
                        titel.append(" ");
                    }
                    titel.append(text);
                }
            }
        }
        return titel.toString();
    }



    private void importRede(Element redeElem, Sitzung sitzung, Tagesordnungspunkt top) {
        String redeId = redeElem.getAttribute("id");
        if (redeId == null || redeId.isEmpty()) {
            redeId = sitzung.getId() + "_" + top.getId() + "_REDE_" +
                    (factory.getRedeRepository().findAll().size() + 1);
        }

        Abgeordneter redner = importRedner(redeElem);
        if (redner == null) {
            System.err.println("Keine Redner-Information für Rede " + redeId + " gefunden. Überspringe Rede.");
            return;
        }

        String text = extractRedeText(redeElem);

        LocalDate datum = sitzung.getDatum();
        LocalDateTime start = datum.atTime(sitzung.getStart());
        LocalDateTime ende = datum.atTime(sitzung.getEnde()); // exakte Redezeiten haben wir nicht

        Rede rede = factory.createRede(redeId, redner, sitzung, top, start, ende, text);

        // Kommentare zur Rede
        NodeList kommentarNodes = redeElem.getElementsByTagName("kommentar");
        for (int i = 0; i < kommentarNodes.getLength(); i++) {
            Node node = kommentarNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element kElem = (Element) node;
            String inhalt = kElem.getTextContent().trim();
            if (!inhalt.isEmpty()) {
                String kommentarId = redeId + "_K" + (i + 1);
                factory.createKommentar(kommentarId, rede, inhalt);
            }
        }
    }






    private Abgeordneter importRedner(Element redeElem) {
        NodeList rednerNodes = redeElem.getElementsByTagName("redner");
        if (rednerNodes.getLength() == 0) {
            return null;
        }

        Element rednerElem = (Element) rednerNodes.item(0);
        String id = rednerElem.getAttribute("id");

        Element nameElem = (Element) rednerElem.getElementsByTagName("name").item(0);

        String vorname = "";
        String nachname = "";
        String fraktion = "";

        if (nameElem != null) {
            NodeList children = nameElem.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node node = children.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                Element e = (Element) node;
                switch (e.getTagName()) {
                    case "vorname":
                        vorname = e.getTextContent().trim();
                        break;
                    case "nachname":
                        nachname = e.getTextContent().trim();
                        break;
                    case "fraktion":
                        fraktion = e.getTextContent().trim();
                        break;
                    default:

                        break;
                }
            }
        }

        if (id == null || id.isEmpty()) {

            String fallbackId = (vorname + "_" + nachname).trim();
            if (fallbackId.isEmpty()) {
                fallbackId = "UNBEKANNT";
            }
            id = fallbackId;
        }

        return factory.getOrCreateAbgeordneter(id, vorname, nachname, "", fraktion);
    }


    private String extractRedeText(Element redeElem) {
        NodeList pNodes = redeElem.getElementsByTagName("p");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < pNodes.getLength(); i++) {
            Node node = pNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            Element p = (Element) node;
            String klasse = p.getAttribute("klasse");
            if ("redner".equals(klasse)) {

                continue;
            }
            String text = p.getTextContent().trim();
            if (!text.isEmpty()) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(text);
            }
        }
        return sb.toString();
    }
}
