package de.uni.ppr.uebung3.rest;

import de.uni.ppr.uebung3.factory.RedenPortalFactory;
import de.uni.ppr.uebung3.service.AbgeordnetenService;
import de.uni.ppr.uebung3.service.RedenService;
import de.uni.ppr.uebung3.service.StatistikService;
import de.uni.ppr.uebung3.model.Abgeordneter;
import de.uni.ppr.uebung3.model.Rede;
import freemarker.template.Configuration;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinFreemarker;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Hauptklasse für REST-Routen und Template-Rendering.
 * Implementiert alle geforderten Endpunkte für Übung 3.
 *
 * @author Parsa Aminian
 * @version 1.0
 */
public class RESTHandler {

    private final Javalin app;
    private final RestConfig config;
    private final AbgeordnetenService abgeordnetenService;
    private final RedenService redenService;
    private final StatistikService statistikService;

    /**
     * Konstruktor initialisiert alle Services und Konfigurationen.
     *
     * @param config Konfigurationsobjekt
     * @param factory RedenPortalFactory aus Übung 2
     * @throws Exception bei Initialisierungsfehlern
     */
    public RESTHandler(RestConfig config, RedenPortalFactory factory) throws Exception {
        this.config = config;

        // Javalin 6.x Konfiguration
        this.app = Javalin.create(javalinConfig -> {
            // Static Files
            javalinConfig.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/static";
                staticFiles.directory = "/static";
                staticFiles.location = Location.CLASSPATH;
            });

            // FreeMarker Template Engine
            Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_34);
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), config.getTemplatePath());
            freemarkerConfig.setDefaultEncoding("UTF-8");
            freemarkerConfig.setTemplateUpdateDelayMilliseconds(config.isTemplateCache() ? 60000 : 0);

            javalinConfig.fileRenderer(new JavalinFreemarker(freemarkerConfig));
        });

        // Services initialisieren (mit eigener Factory)
        this.abgeordnetenService = new AbgeordnetenService(factory);
        this.redenService = new RedenService(factory);
        this.statistikService = new StatistikService(abgeordnetenService, redenService);

        // Routen einrichten
        setupRoutes();

        System.out.println("\n[RESTHandler] Initialisiert");
        System.out.println("   - Abgeordnete: " + abgeordnetenService.getAnzahl());
        System.out.println("   - Reden:       " + redenService.getAnzahl());
    }

    /**
     * Definiert alle Routen der Applikation.
     * Implementiert HTML-Routen (FreeMarker) und REST-API (JSON).
     */
    private void setupRoutes() {
        // ==================== HTML-ROUTEN (FreeMarker) ====================

        app.get("/", this::renderIndex);
        app.get("/abgeordneter/{id}", this::renderAbgeordneter);
        app.get("/rede/{id}", this::renderRede);
        app.get("/statistiken", this::renderStatistiken);

        // ==================== REST-API-ROUTEN (JSON) ====================

        // Abgeordnete
        app.get("/api/abgeordnete", this::getAbgeordneteJSON);
        app.get("/api/abgeordnete/search", this::searchAbgeordneteJSON);
        app.get("/api/abgeordnete/filter", this::filterAbgeordneteJSON);
        app.get("/api/abgeordnete/sort/{criteria}", this::sortAbgeordneteJSON);
        app.get("/api/abgeordneter/{id}", this::getAbgeordneterJSON);

        // Reden
        app.get("/api/reden", this::getRedenJSON);
        app.get("/api/reden/abgeordneter/{id}", this::getRedenByAbgeordneterJSON);
        app.get("/api/rede/{id}", this::getRedeJSON);

        // Statistiken
        app.get("/api/statistiken", this::getStatistikenJSON);
        app.get("/api/statistiken/top-redner", this::getTopRednerJSON);
        app.get("/api/statistiken/fraktionen", this::getFraktionsStatistikenJSON);

        // Fraktionen
        app.get("/api/fraktionen", this::getFraktionenJSON);
    }

    // ==================== HTML-ROUTEN IMPLEMENTIERUNG ====================

    /**
     * Rendert die Startseite mit Suchfunktion und Filter.
     *
     * @param ctx Javalin Context
     */
    private void renderIndex(Context ctx) {
        try {
            Map<String, Object> model = new HashMap<>();

            model.put("title", config.getAppName());
            model.put("version", config.getAppVersion());

            // Filter-Parameter auswerten
            String sortBy = ctx.queryParam("sort");
            String fraktionFilter = ctx.queryParam("fraktion");

            List<Abgeordneter> abgeordnete;

            if (fraktionFilter != null && !fraktionFilter.isEmpty()) {
                abgeordnete = abgeordnetenService.getAbgeordneteByFraktion(fraktionFilter);
            } else if ("reden".equals(sortBy)) {
                abgeordnete = abgeordnetenService.getAbgeordneteSortedByRedenCount();
            } else {
                abgeordnete = abgeordnetenService.getAbgeordneteSortedByName();
            }

            model.put("abgeordnete", abgeordnete);
            model.put("fraktionen", abgeordnetenService.getAllFraktionen());
            model.put("selectedFraktion", fraktionFilter);
            model.put("sortBy", sortBy);

            ctx.render("index.ftl", model);

        } catch (Exception e) {
            ctx.status(500).result("Fehler beim Rendern: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Rendert die Abgeordneten-Detailseite.
     *
     * @param ctx Javalin Context
     */
    private void renderAbgeordneter(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            Abgeordneter abg = abgeordnetenService.getAbgeordneterById(id);

            if (abg == null) {
                ctx.status(404).result("Abgeordneter nicht gefunden");
                return;
            }

            Map<String, Object> model = new HashMap<>();
            model.put("abgeordneter", abg);
            model.put("reden", redenService.sortRedenByDate(new ArrayList<>(abg.getReden())));
            model.put("durchschnittRedelaenge",
                    statistikService.getDurchschnittRedelaengeProAbgeordneter(id));

            ctx.render("abgeordneter.ftl", model);

        } catch (Exception e) {
            ctx.status(500).result("Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Rendert die Rede-Detailseite mit Volltext.
     *
     * @param ctx Javalin Context
     */
    private void renderRede(Context ctx) {
        try {
            String id = ctx.pathParam("id");
            Rede rede = redenService.getRedeById(id);

            if (rede == null) {
                ctx.status(404).result("Rede nicht gefunden");
                return;
            }

            Map<String, Object> model = new HashMap<>();
            model.put("rede", rede);
            model.put("redner", rede.getRedner());

            ctx.render("rede.ftl", model);

        } catch (Exception e) {
            ctx.status(500).result("Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Rendert die Statistiken-Seite.
     *
     * @param ctx Javalin Context
     */
    private void renderStatistiken(Context ctx) {
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("title", "Statistiken - " + config.getAppName());
            model.put("statistiken", statistikService.getGesamtStatistiken().toMap());

            ctx.render("statistiken.ftl", model);

        } catch (Exception e) {
            ctx.status(500).result("Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ==================== REST-API IMPLEMENTIERUNG ====================

    /**
     * Gibt alle Abgeordnete als JSON zurück.
     *
     * @param ctx Javalin Context
     */
    private void getAbgeordneteJSON(Context ctx) {
        JSONArray result = new JSONArray();

        abgeordnetenService.getAllAbgeordnete().forEach(abg -> {
            JSONObject obj = new JSONObject();
            obj.put("id", abg.getId());
            obj.put("vorname", abg.getVorname());
            obj.put("nachname", abg.getNachname());
            obj.put("titel", abg.getTitel());
            obj.put("fraktion", abg.getFraktion() != null ? abg.getFraktion().getName() : "Unbekannt");
            obj.put("redenAnzahl", abg.getReden().size());
            result.put(obj);
        });

        ctx.json(result.toString());
        ctx.contentType("application/json; charset=utf-8");
    }

    /**
     * Sucht Abgeordnete nach Namen (Query-Parameter: q).
     *
     * @param ctx Javalin Context
     */
    private void searchAbgeordneteJSON(Context ctx) {
        String query = ctx.queryParam("q");
        JSONArray result = new JSONArray();

        if (query != null && !query.trim().isEmpty()) {
            abgeordnetenService.searchAbgeordnete(query).forEach(abg -> {
                JSONObject obj = new JSONObject();
                obj.put("id", abg.getId());
                obj.put("vorname", abg.getVorname());
                obj.put("nachname", abg.getNachname());
                obj.put("fraktion", abg.getFraktion() != null ? abg.getFraktion().getName() : "Unbekannt");
                obj.put("redenAnzahl", abg.getReden().size());
                result.put(obj);
            });
        }

        ctx.json(result.toString());
        ctx.contentType("application/json; charset=utf-8");
    }

    /**
     * Filtert Abgeordnete nach Fraktion (Query-Parameter: fraktion).
     *
     * @param ctx Javalin Context
     */
    private void filterAbgeordneteJSON(Context ctx) {
        String fraktion = ctx.queryParam("fraktion");
        JSONArray result = new JSONArray();

        if (fraktion != null && !fraktion.trim().isEmpty()) {
            abgeordnetenService.getAbgeordneteByFraktion(fraktion).forEach(abg -> {
                JSONObject obj = new JSONObject();
                obj.put("id", abg.getId());
                obj.put("vorname", abg.getVorname());
                obj.put("nachname", abg.getNachname());
                obj.put("fraktion", abg.getFraktion() != null ? abg.getFraktion().getName() : "Unbekannt");
                obj.put("redenAnzahl", abg.getReden().size());
                result.put(obj);
            });
        }

        ctx.json(result.toString());
        ctx.contentType("application/json; charset=utf-8");
    }

    /**
     * Sortiert Abgeordnete (Path-Parameter: criteria).
     * Mögliche Werte: name, reden
     *
     * @param ctx Javalin Context
     */
    private void sortAbgeordneteJSON(Context ctx) {
        String criteria = ctx.pathParam("criteria");
        JSONArray result = new JSONArray();

        List<Abgeordneter> sorted;

        switch (criteria.toLowerCase()) {
            case "reden":
                sorted = abgeordnetenService.getAbgeordneteSortedByRedenCount();
                break;
            case "name":
            default:
                sorted = abgeordnetenService.getAbgeordneteSortedByName();
                break;
        }

        sorted.forEach(abg -> {
            JSONObject obj = new JSONObject();
            obj.put("id", abg.getId());
            obj.put("vorname", abg.getVorname());
            obj.put("nachname", abg.getNachname());
            obj.put("fraktion", abg.getFraktion() != null ? abg.getFraktion().getName() : "Unbekannt");
            obj.put("redenAnzahl", abg.getReden().size());
            result.put(obj);
        });

        ctx.json(result.toString());
        ctx.contentType("application/json; charset=utf-8");
    }

    /**
     * Gibt einen einzelnen Abgeordneten als JSON zurück.
     *
     * @param ctx Javalin Context
     */
    private void getAbgeordneterJSON(Context ctx) {
        String id = ctx.pathParam("id");
        Abgeordneter abg = abgeordnetenService.getAbgeordneterById(id);

        if (abg == null) {
            ctx.status(404).json("{\"error\": \"Abgeordneter nicht gefunden\"}");
            return;
        }

        JSONObject obj = new JSONObject();
        obj.put("id", abg.getId());
        obj.put("vorname", abg.getVorname());
        obj.put("nachname", abg.getNachname());
        obj.put("titel", abg.getTitel());
        obj.put("fraktion", abg.getFraktion() != null ? abg.getFraktion().getName() : "Unbekannt");
        obj.put("redenAnzahl", abg.getReden().size());

        ctx.json(obj.toString());
        ctx.contentType("application/json; charset=utf-8");
    }

    /**
     * Gibt alle Reden als JSON zurück.
     *
     * @param ctx Javalin Context
     */
    private void getRedenJSON(Context ctx) {
        JSONArray result = new JSONArray();

        redenService.getAllReden().forEach(rede -> {
            JSONObject obj = new JSONObject();
            obj.put("id", rede.getId());
            obj.put("redner", rede.getRedner().getVorname() + " " + rede.getRedner().getNachname());
            obj.put("rednerId", rede.getRedner().getId());
            obj.put("start", rede.getStart().toString());
            obj.put("ende", rede.getEnde().toString());
            obj.put("textLaenge", rede.getTextLaenge());
            obj.put("kommentarAnzahl", rede.getKommentarAnzahl());
            result.put(obj);
        });

        ctx.json(result.toString());
        ctx.contentType("application/json; charset=utf-8");
    }

    /**
     * Gibt alle Reden eines Abgeordneten als JSON zurück.
     *
     * @param ctx Javalin Context
     */
    private void getRedenByAbgeordneterJSON(Context ctx) {
        String id = ctx.pathParam("id");
        JSONArray result = new JSONArray();

        redenService.getRedenByAbgeordneter(id).forEach(rede -> {
            JSONObject obj = new JSONObject();
            obj.put("id", rede.getId());
            obj.put("start", rede.getStart().toString());
            obj.put("ende", rede.getEnde().toString());
            obj.put("textLaenge", rede.getTextLaenge());
            obj.put("kommentarAnzahl", rede.getKommentarAnzahl());
            result.put(obj);
        });

        ctx.json(result.toString());
        ctx.contentType("application/json; charset=utf-8");
    }

    /**
     * Gibt eine einzelne Rede als JSON zurück.
     *
     * @param ctx Javalin Context
     */
    private void getRedeJSON(Context ctx) {
        String id = ctx.pathParam("id");
        Rede rede = redenService.getRedeById(id);

        if (rede == null) {
            ctx.status(404).json("{\"error\": \"Rede nicht gefunden\"}");
            return;
        }

        JSONObject obj = new JSONObject();
        obj.put("id", rede.getId());
        obj.put("redner", rede.getRedner().getVorname() + " " + rede.getRedner().getNachname());
        obj.put("rednerId", rede.getRedner().getId());
        obj.put("start", rede.getStart().toString());
        obj.put("ende", rede.getEnde().toString());
        obj.put("text", rede.getText());
        obj.put("textLaenge", rede.getTextLaenge());
        obj.put("kommentarAnzahl", rede.getKommentarAnzahl());

        ctx.json(obj.toString());
        ctx.contentType("application/json; charset=utf-8");
    }

    /**
     * Gibt Gesamt-Statistiken als JSON zurück.
     *
     * @param ctx Javalin Context
     */
    private void getStatistikenJSON(Context ctx) {
        ctx.json(statistikService.getGesamtStatistiken().toString());
        ctx.contentType("application/json; charset=utf-8");
    }

    /**
     * Gibt Top-Redner als JSON zurück.
     * Query-Parameter: limit (Standard: 10)
     *
     * @param ctx Javalin Context
     */
    private void getTopRednerJSON(Context ctx) {
        String limitParam = ctx.queryParam("limit");
        int limit = limitParam != null ? Integer.parseInt(limitParam) : 10;

        ctx.json(statistikService.getTopRedner(limit).toString());
        ctx.contentType("application/json; charset=utf-8");
    }

    /**
     * Gibt Fraktions-Statistiken als JSON zurück.
     *
     * @param ctx Javalin Context
     */
    private void getFraktionsStatistikenJSON(Context ctx) {
        ctx.json(statistikService.getFraktionsStatistiken().toString());
        ctx.contentType("application/json; charset=utf-8");
    }

    /**
     * Gibt alle Fraktionen als JSON zurück.
     *
     * @param ctx Javalin Context
     */
    private void getFraktionenJSON(Context ctx) {
        JSONArray result = new JSONArray();

        abgeordnetenService.getAllFraktionen().forEach(fraktion -> {
            JSONObject obj = new JSONObject();
            obj.put("id", fraktion.getId());
            obj.put("name", fraktion.getName());
            obj.put("mitgliederAnzahl", fraktion.getMitglieder().size());
            result.put(obj);
        });

        ctx.json(result.toString());
        ctx.contentType("application/json; charset=utf-8");
    }

    /**
     * Startet den Webserver.
     */
    public void start() {
        app.start(config.getServerHost(), config.getServerPort());
        System.out.println("\n========================================");
        System.out.println("🚀 " + config.getAppName() + " v" + config.getAppVersion());
        System.out.println("========================================");
        System.out.println("📍 Server: http://" + config.getServerHost() + ":" + config.getServerPort());
        System.out.println("📊 Statistiken: http://" + config.getServerHost() + ":" + config.getServerPort() + "/statistiken");
        System.out.println("🔌 API: http://" + config.getServerHost() + ":" + config.getServerPort() + "/api/");
        System.out.println("========================================\n");
    }

    /**
     * Stoppt den Webserver.
     */
    public void stop() {
        app.stop();
    }
}