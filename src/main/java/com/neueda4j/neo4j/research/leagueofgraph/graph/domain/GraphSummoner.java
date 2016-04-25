package com.neueda4j.neo4j.research.leagueofgraph.graph.domain;

import com.neueda4j.neo4j.research.leagueofgraph.graph.Labels;
import com.neueda4j.neo4j.research.leagueofgraph.graph.RelationshipTypes;
import com.robrua.orianna.type.core.summoner.Summoner;
import org.neo4j.graphdb.Node;

/**
 * {"fylmtm": {
 * "id": 24297397,
 * "name": "FylmTM",
 * "profileIconId": 661,
 * "revisionDate": 1460577310000,
 * "summonerLevel": 30
 * }}
 */
public class GraphSummoner {

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";

    private final Node node;

    public static GraphSummoner create(Node node, Summoner summoner) {
        node.addLabel(Labels.Summoner);
        node.setProperty(KEY_ID, summoner.getID());
        node.setProperty(KEY_NAME, summoner.getName());

        return new GraphSummoner(node);
    }

    public GraphSummoner(Node node) {
        this.node = node;
    }

    public void addGraphMatchReference(GraphMatchReference graphMatchReference) {
        node.createRelationshipTo(graphMatchReference.getNode(), RelationshipTypes.PLAYED_MATCH_REFERENCE);
    }

    public void participatedWith(GraphParticipant graphParticipant) {
        node.createRelationshipTo(graphParticipant.getNode(), RelationshipTypes.PLAYED_PARTICIPATED);
    }
}
