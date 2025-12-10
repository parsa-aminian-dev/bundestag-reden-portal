package de.uni.ppr.uebung3.app;

import de.uni.ppr.uebung3.rest.RESTHandler;
import de.uni.ppr.uebung3.rest.RestConfig;
import de.uni.ppr.uebung3.factory.RedenPortalFactory;
import de.uni.ppr.uebung3.service.XmlProtokollLoader;

import java.nio.file.*;

/**
 * Hauptklasse der Anwendung.
 * Lädt Daten aus XML, startet den Webserver.
 *
 * @author Parsa Aminian
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("========================================");
            System.out.println("Bundestag Reden-Portal - Übung 3");
            System.out.println("========================================\n");

            // 1. Factory initialisieren
            RedenPortalFactory factory = new RedenPortalFactory();

            // 2. XML-Daten laden (falls vorhanden)
            if (args.length > 0) {
                System.out.println("Lade XML-Daten aus: " + args[0]);
                loadXmlData(factory, args[0]);
            } else {
                System.out.println("⚠️  Keine XML-Daten angegeben. Starte mit leerer Datenbank.");
                System.out.println("   Verwendung: java -jar Uebung3.jar <xml-verzeichnis>");
            }

            System.out.println("\n📊 Daten geladen:");
            System.out.println("   - Abgeordnete: " + factory.getAbgeordneterRepository().findAll().size());
            System.out.println("   - Fraktionen:  " + factory.getFraktionRepository().findAll().size());
            System.out.println("   - Reden:       " + factory.getRedeRepository().findAll().size());
            System.out.println("   - Kommentare:  " + factory.getKommentarRepository().findAll().size());

            // 3. Konfiguration laden
            RestConfig config = new RestConfig("/config.properties");

            // 4. REST-Handler initialisieren und starten
            RESTHandler handler = new RESTHandler(config, factory);
            handler.start();

            // 5. Shutdown-Hook registrieren
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n🛑 Server wird heruntergefahren...");
                handler.stop();
            }));

        } catch (Exception e) {
            System.err.println("❌ Fehler beim Starten der Applikation: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Lädt XML-Protokolle in die Factory.
     *
     * @param factory RedenPortalFactory
     * @param path Pfad zu XML-Dateien oder Verzeichnis
     */
    private static void loadXmlData(RedenPortalFactory factory, String path) {
        try {
            Path input = Paths.get(path);
            XmlProtokollLoader loader = new XmlProtokollLoader(factory);

            if (Files.isDirectory(input)) {
                System.out.println("📁 Lade alle XML-Dateien aus Verzeichnis...");
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(input, "*.xml")) {
                    int count = 0;
                    for (Path xml : stream) {
                        System.out.println("   ➜ " + xml.getFileName());
                        loader.importProtokoll(xml);
                        count++;
                    }
                    System.out.println("✅ " + count + " Protokolle erfolgreich geladen");
                }
            } else if (Files.exists(input)) {
                System.out.println("📄 Lade einzelne XML-Datei...");
                loader.importProtokoll(input);
                System.out.println("✅ Protokoll erfolgreich geladen");
            } else {
                System.err.println("⚠️  Pfad nicht gefunden: " + path);
            }

        } catch (Exception e) {
            System.err.println("❌ Fehler beim Laden der XML-Daten: " + e.getMessage());
            e.printStackTrace();
        }
    }
}