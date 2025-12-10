package de.uni.ppr.uebung3.database;

import de.uni.ppr.uebung3.factory.RedenPortalFactory;
import de.uni.ppr.uebung3.model.Abgeordneter;
import de.uni.ppr.uebung3.model.Fraktion;
import de.uni.ppr.uebung3.model.Sitzung;
import de.uni.ppr.uebung3.model.Tagesordnungspunkt;
import de.uni.ppr.uebung3.model.Rede;
import de.uni.ppr.uebung3.model.Kommentar;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;

import java.io.File;
import java.nio.file.Path;
import java.util.*;






public class Neo4jConnection implements AutoCloseable {

    private final DatabaseManagementService managementService;
    private final GraphDatabaseService db;


    public Neo4jConnection(String dbDirectory) {
        Path dbPath = new File(dbDirectory).toPath();


        this.managementService = new DatabaseManagementServiceBuilder(dbPath).build();


        this.db = managementService.database("neo4j");

        registerShutdownHook(managementService);
    }




    public long createNode(String label, Map<String, Object> props) {
        String cypher = "CREATE (n:" + label + " $props) RETURN id(n) AS id";

        try (Transaction tx = db.beginTx()) {
            Result result = tx.execute(cypher, Map.of("props", props));
            long id = (long) result.next().get("id");
            tx.commit();
            return id;
        }
    }


    public long createRelationship(long fromId,
                                   long toId,
                                   String type,
                                   Map<String, Object> props) {

        String cypher =
                "MATCH (a),(b) " +
                        "WHERE id(a) = $fromId AND id(b) = $toId " +
                        "CREATE (a)-[r:" + type + " $props]->(b) " +
                        "RETURN id(r) AS id";

        try (Transaction tx = db.beginTx()) {
            Result result = tx.execute(cypher, Map.of(
                    "fromId", fromId,
                    "toId", toId,
                    "props", props
            ));
            long id = (long) result.next().get("id");
            tx.commit();
            return id;
        }
    }


    public void executeWrite(String cypher, Map<String, Object> params) {
        try (Transaction tx = db.beginTx()) {
            tx.execute(cypher, params != null ? params : Collections.emptyMap());
            tx.commit();
        }
    }


    public List<Map<String, Object>> executeRead(String cypher, Map<String, Object> params) {
        try (Transaction tx = db.beginTx()) {
            Result result = tx.execute(cypher, params != null ? params : Collections.emptyMap());

            List<Map<String, Object>> rows = new ArrayList<>();
            while (result.hasNext()) {
                Map<String, Object> row = result.next();
                rows.add(new LinkedHashMap<>(row)); // Kopie
            }
            tx.commit();
            return rows;
        }
    }


    public void clearDatabase() {
        executeWrite("MATCH (n) DETACH DELETE n", Collections.emptyMap());
    }


    public void deleteNodeById(long nodeId) {
        executeWrite(
                "MATCH (n) WHERE id(n) = $id DETACH DELETE n",
                Map.of("id", nodeId)
        );
    }


    public void updateNodeProperties(long nodeId, Map<String, Object> newProps) {
        executeWrite(
                "MATCH (n) WHERE id(n) = $id SET n += $props",
                Map.of(
                        "id", nodeId,
                        "props", newProps
                )
        );
    }



    public void exportFromFactory(RedenPortalFactory factory) {


        clearDatabase();


        Map<String, Long> fraktionNodes     = new HashMap<>();
        Map<String, Long> abgeordnetenNodes = new HashMap<>();
        Map<String, Long> sitzungsNodes     = new HashMap<>();
        Map<String, Long> topNodes          = new HashMap<>();
        Map<String, Long> redeNodes         = new HashMap<>();
        Map<String, Long> kommentarNodes    = new HashMap<>();


        for (Fraktion f : factory.getFraktionRepository().findAll()) {
            Map<String, Object> props = new HashMap<>();
            props.put("id", f.getId());
            props.put("name", f.getName());

            long nodeId = createNode("Fraktion", props);
            fraktionNodes.put(f.getId(), nodeId);
        }


        for (Abgeordneter a : factory.getAbgeordneterRepository().findAll()) {
            Map<String, Object> props = new HashMap<>();
            props.put("id", a.getId());
            props.put("vorname", a.getVorname());
            props.put("nachname", a.getNachname());
            props.put("titel", a.getTitel());

            long nodeId = createNode("Abgeordneter", props);
            abgeordnetenNodes.put(a.getId(), nodeId);


            Fraktion fr = a.getFraktion();
            if (fr != null) {
                Long frNode = fraktionNodes.get(fr.getId());
                if (frNode != null) {
                    createRelationship(nodeId, frNode, "GEHOERT_ZU", Map.of());
                }
            }
        }


        for (Sitzung s : factory.getSitzungRepository().findAll()) {
            Map<String, Object> props = new HashMap<>();
            props.put("id", s.getId());
            props.put("nummer", s.getNummer());
            props.put("datum", s.getDatum().toString());
            props.put("ort", s.getOrt());
            props.put("start", s.getStart().toString());
            props.put("ende", s.getEnde().toString());

            long nodeId = createNode("Sitzung", props);
            sitzungsNodes.put(s.getId(), nodeId);
        }


        for (Tagesordnungspunkt t : factory.getTopRepository().findAll()) {
            Map<String, Object> props = new HashMap<>();
            props.put("id", t.getId());
            props.put("nummer", t.getNummer());
            props.put("titel", t.getTitel());

            long nodeId = createNode("TOP", props);
            topNodes.put(t.getId(), nodeId);

            Sitzung s = t.getSitzung();
            if (s != null) {
                Long sitNode = sitzungsNodes.get(s.getId());
                if (sitNode != null) {
                    createRelationship(sitNode, nodeId, "HAT_TOP", Map.of());
                }
            }
        }


        for (Rede r : factory.getRedeRepository().findAll()) {
            Map<String, Object> props = new HashMap<>();
            props.put("id", r.getId());
            props.put("start", r.getStart().toString());
            props.put("ende", r.getEnde().toString());
            props.put("textLaenge", r.getTextLaenge());
            props.put("kommentarAnzahl", r.getKommentarAnzahl());

            long nodeId = createNode("Rede", props);
            redeNodes.put(r.getId(), nodeId);


            Abgeordneter redner = r.getRedner();
            if (redner != null) {
                Long abgNode = abgeordnetenNodes.get(redner.getId());
                if (abgNode != null) {
                    createRelationship(abgNode, nodeId, "HAT_REDE", Map.of());
                }
            }


            Sitzung sit = r.getSitzung();
            if (sit != null) {
                Long sitNode = sitzungsNodes.get(sit.getId());
                if (sitNode != null) {
                    createRelationship(nodeId, sitNode, "IN_SITZUNG", Map.of());
                }
            }


            Tagesordnungspunkt top = r.getTop();
            if (top != null) {
                Long topNode = topNodes.get(top.getId());
                if (topNode != null) {
                    createRelationship(nodeId, topNode, "ZU_TOP", Map.of());
                }
            }
        }


        for (Kommentar k : factory.getKommentarRepository().findAll()) {
            Map<String, Object> props = new HashMap<>();
            props.put("id", k.getId());
            props.put("inhalt", k.getInhalt());

            long nodeId = createNode("Kommentar", props);
            kommentarNodes.put(k.getId(), nodeId);


            Rede r = k.getRede();
            if (r != null) {
                Long redeNode = redeNodes.get(r.getId());
                if (redeNode != null) {
                    createRelationship(redeNode, nodeId, "HAT_KOMMENTAR", Map.of());
                }
            }
        }

        System.out.println("[Neo4j] Export abgeschlossen. Knoten:");
        System.out.printf("  Fraktionen:   %d%n", fraktionNodes.size());
        System.out.printf("  Abgeordnete:  %d%n", abgeordnetenNodes.size());
        System.out.printf("  Sitzungen:    %d%n", sitzungsNodes.size());
        System.out.printf("  TOPs:         %d%n", topNodes.size());
        System.out.printf("  Reden:        %d%n", redeNodes.size());
        System.out.printf("  Kommentare:   %d%n", kommentarNodes.size());
    }



    @Override
    public void close() {
        managementService.shutdown();
    }

    private static void registerShutdownHook(final DatabaseManagementService managementService) {
        Runtime.getRuntime().addShutdownHook(new Thread(managementService::shutdown));
    }
}
