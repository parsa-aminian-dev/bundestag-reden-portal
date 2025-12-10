package de.uni.ppr.uebung3.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Konfigurationsklasse für REST-Einstellungen.
 * Erbt von Properties gemäß Aufgabenstellung.
 *
 * @author Parsa Aminian
 * @version 1.0
 */
public class RestConfig extends Properties {

    /**
     * Konstruktor lädt die Konfigurationsdatei.
     *
     * @param configFile Pfad zur config.properties
     * @throws IOException wenn die Datei nicht gefunden wird
     */
    public RestConfig(String configFile) throws IOException {
        try (InputStream input = getClass().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new IOException("Config-Datei nicht gefunden: " + configFile);
            }
            this.load(input);
        }
    }

    /**
     * Gibt den Server-Port zurück.
     *
     * @return Port-Nummer (Standard: 7070)
     */
    public int getServerPort() {
        return Integer.parseInt(getProperty("server.port", "7070"));
    }

    /**
     * Gibt den Server-Host zurück.
     *
     * @return Hostname (Standard: localhost)
     */
    public String getServerHost() {
        return getProperty("server.host", "localhost");
    }

    /**
     * Gibt den Template-Pfad zurück.
     *
     * @return Template-Verzeichnis
     */
    public String getTemplatePath() {
        return getProperty("template.path", "/templates");
    }

    /**
     * Prüft ob Template-Caching aktiviert ist.
     *
     * @return true wenn Caching aktiviert
     */
    public boolean isTemplateCache() {
        return Boolean.parseBoolean(getProperty("template.cache", "false"));
    }

    /**
     * Gibt den Static-Files-Pfad zurück.
     *
     * @return Static-Verzeichnis
     */
    public String getStaticPath() {
        return getProperty("static.path", "/static");
    }

    /**
     * Gibt den Anwendungsnamen zurück.
     *
     * @return Anwendungsname
     */
    public String getAppName() {
        return getProperty("app.name", "Bundestag Reden-Portal");
    }

    /**
     * Gibt die Anwendungsversion zurück.
     *
     * @return Version
     */
    public String getAppVersion() {
        return getProperty("app.version", "1.0");
    }

    /**
     * Gibt den Neo4j-Datenbankpfad zurück.
     *
     * @return Datenbank-Pfad
     */
    public String getDatabasePath() {
        return getProperty("database.path", "data/neo4j");
    }
}