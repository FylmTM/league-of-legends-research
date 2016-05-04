package com.neueda4j.neo4j.research.leagueofgraph;

import com.google.common.base.Preconditions;
import com.neueda4j.neo4j.research.leagueofgraph.runner.DataImport;
import org.neo4j.graphdb.Transaction;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class LeagueOfGraphs {

    private static final String DATABASE_PATH = System.getProperty("dbPath");
    private static String API_KEY = System.getProperty("riotApiKey");

    private static List<String> PLAYERS = Arrays.asList(
            "FylmTM",
            "Cryptael",
            "ErwinRudolf",
            "Henua",
            "Iger",
            "XXpoMMou",
            "yesCold",
            "eskiuzi",
            "MrMgr"
    );

    public static void main(String[] args) throws IOException {
        Preconditions.checkNotNull(DATABASE_PATH);
        Preconditions.checkNotNull(API_KEY);
        new LeagueOfGraphs().run();
    }

    private void run() throws IOException {
        RiotAPISetup.setup(API_KEY);

        GraphDatabase database = GraphDatabase.create(DATABASE_PATH);

        try {
            DataImport dataImport = new DataImport(database);

            for (String summonerName : PLAYERS) {
                try (Transaction tx = database.db().beginTx()) {
                    dataImport.importSummoner(summonerName);
                    tx.success();
                }
            }
        } finally {
            database.db().shutdown();
        }
    }
}
