package com.neueda4j.neo4j.research.leagueofgraph;

import com.neueda4j.neo4j.research.leagueofgraph.graph.Labels;
import com.neueda4j.neo4j.research.leagueofgraph.graph.domain.GraphChampion;
import com.neueda4j.neo4j.research.leagueofgraph.graph.domain.GraphMatch;
import com.neueda4j.neo4j.research.leagueofgraph.graph.domain.GraphMatchReference;
import com.neueda4j.neo4j.research.leagueofgraph.graph.domain.GraphMatchTeam;
import com.neueda4j.neo4j.research.leagueofgraph.graph.domain.GraphParticipant;
import com.neueda4j.neo4j.research.leagueofgraph.graph.domain.GraphParticipantTimeline;
import com.neueda4j.neo4j.research.leagueofgraph.graph.domain.GraphParticipantTimelineData;
import com.neueda4j.neo4j.research.leagueofgraph.graph.domain.GraphSummoner;
import com.robrua.orianna.type.core.match.Match;
import com.robrua.orianna.type.core.match.MatchTeam;
import com.robrua.orianna.type.core.match.Participant;
import com.robrua.orianna.type.core.match.ParticipantTimeline;
import com.robrua.orianna.type.core.match.ParticipantTimelineData;
import com.robrua.orianna.type.core.matchlist.MatchReference;
import com.robrua.orianna.type.core.staticdata.Champion;
import com.robrua.orianna.type.core.summoner.Summoner;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class GraphDatabase {

    private final GraphDatabaseService db;

    public static GraphDatabase create(String databasePath) {
        return new GraphDatabase(databasePath);
    }

    private GraphDatabase(String databasePath) {
        db = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(new File(databasePath))
                .newGraphDatabase();
    }

    public GraphDatabaseService db() {
        return db;
    }

    public GraphSummoner createSummoner(Summoner summoner) {
        Node node = db.findNode(Labels.Summoner, GraphSummoner.KEY_ID, summoner.getID());

        if (node == null) {
            return GraphSummoner.create(db.createNode(), summoner);
        } else {
            return new GraphSummoner(node);
        }
    }

    public GraphMatchReference findMatchReference(GraphSummoner graphSummoner, MatchReference matchReference) {
        Map<String, Object> params = new HashMap<>();
        params.put("summonerName", graphSummoner.getName());
        params.put("matchId", matchReference.getID());

        try (Result result = db.execute(
                "MATCH (n:Summoner {name: {summonerName}})-[:PLAYED_MATCH_REFERENCE]->(mr:MatchReference {match_id: {matchId}}) RETURN mr",
                params)) {
            if (result.hasNext()) {
                Node node = (Node) result.next().get("mr");
                return new GraphMatchReference(node);
            }
        }

        return null;
    }

    public GraphMatchReference createMatchReference(GraphSummoner graphSummoner, MatchReference matchReference) {
        GraphMatchReference graphMatchReference = findMatchReference(graphSummoner, matchReference);

        if (graphMatchReference == null) {
            return GraphMatchReference.create(this, db.createNode(), matchReference);
        } else {
            return graphMatchReference;
        }
    }

    public GraphMatch createMatch(Match match) {
        Node node = db.findNode(Labels.Match, GraphMatch.KEY_ID, match.getID());

        if (node == null) {
            return GraphMatch.create(this, db.createNode(), match);
        } else {
            return new GraphMatch(node);
        }
    }

    public GraphChampion createChampion(Champion champion) {
        Node node = db.findNode(Labels.Champion, GraphChampion.KEY_ID, champion.getID());

        if (node == null) {
            return GraphChampion.create(db.createNode(), champion);
        } else {
            return new GraphChampion(node);
        }
    }

    public GraphParticipant createParticipant(Participant participant) {
        return GraphParticipant.create(this, db.createNode(), participant);
    }

    public GraphMatchTeam createMatchTeam(MatchTeam matchTeam) {
        return GraphMatchTeam.create(this, db.createNode(), matchTeam);
    }

    public GraphParticipantTimeline createParticipantTimeline(ParticipantTimeline participantTimeline) {
        return GraphParticipantTimeline.create(this, db.createNode(), participantTimeline);
    }

    public GraphParticipantTimelineData createParticipantTimelineData(ParticipantTimelineData participantTimelineData) {
        return GraphParticipantTimelineData.create(this, db.createNode(), participantTimelineData);
    }
}
