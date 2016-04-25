package com.neueda4j.neo4j.research.leagueofgraph;

import com.neueda4j.neo4j.research.leagueofgraph.runner.DataImport;
import org.neo4j.graphdb.Transaction;
import org.neo4j.io.fs.FileUtils;

import java.io.File;
import java.io.IOException;

public class LeagueOfGraphs {

    public static final String DATABASE_PATH = "/Users/fylmtm/tools/neo4j/neo4j-community-2.3.3/data/lol";
    private static String API_KEY = System.getProperty("riotApiKey");

    public static void main(String[] args) throws IOException {
        new LeagueOfGraphs().run();
    }

    private void run() throws IOException {
        RiotAPISetup.setup(API_KEY);

        FileUtils.deleteRecursively(new File(DATABASE_PATH));
        GraphDatabase database = GraphDatabase.create(DATABASE_PATH);

        DataImport dataImport = new DataImport(database);

        try (Transaction tx = database.db().beginTx()) {
            dataImport.importSummoner("FylmTM");
            dataImport.importSummoner("Cryptael");

            tx.success();
        }

        database.db().shutdown();
    }
}
