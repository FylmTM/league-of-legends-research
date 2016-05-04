package com.neueda4j.neo4j.research.leagueofgraph.graph.domain;

import com.neueda4j.neo4j.research.leagueofgraph.graph.Labels;
import com.neueda4j.neo4j.research.leagueofgraph.graph.RelationshipTypes;
import com.robrua.orianna.type.core.summoner.Summoner;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

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
    private static final String KEY_NAME = "name";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_PROFILE_ICON_ID = "profile_icon_id";
    private static final String KEY_REVISION_DATE = "revision_date";
    private static final String KEY_REVISION_DATE_TIMESTAMP = "revision_date_timestamp";

    private final Node node;

    public static GraphSummoner create(Node node, Summoner summoner) {
        node.addLabel(Labels.Summoner);
        node.setProperty(KEY_ID, summoner.getID());
        node.setProperty(KEY_NAME, summoner.getName());
        node.setProperty(KEY_LEVEL, summoner.getLevel());
        node.setProperty(KEY_PROFILE_ICON_ID, summoner.getProfileIconID());
        node.setProperty(KEY_REVISION_DATE, summoner.getRevisionDate().getTime());
        node.setProperty(KEY_REVISION_DATE_TIMESTAMP, summoner.getRevisionDate().toString());

        // todo: league entires
        // todo: leagues
        // todo: mastery pages
        // todo: ranked stats
        // todo: rune pages
        // todo: stats
        // todo: teams

        return new GraphSummoner(node);
    }

    public GraphSummoner(Node node) {
        this.node = node;
    }

    public void addGraphMatchReference(GraphMatchReference graphMatchReference) {
        node.createRelationshipTo(graphMatchReference.getNode(), RelationshipTypes.PLAYED_MATCH_REFERENCE);
    }

    public void setLastMatchReference(GraphMatchReference lastGraphMatchReference) {
        Relationship lastRel = node.getSingleRelationship(RelationshipTypes.LAST_PLAYED_MATCH_REFERENCE, Direction.OUTGOING);
        if (lastRel != null) {
            lastRel.delete();
        }
        node.createRelationshipTo(lastGraphMatchReference.getNode(), RelationshipTypes.LAST_PLAYED_MATCH_REFERENCE);
    }

    public void participated(GraphParticipant graphParticipant) {
        node.createRelationshipTo(graphParticipant.getNode(), RelationshipTypes.PARTICIPATED);
    }

    public GraphMatchReference getLastMatchReference() {
        Relationship relationship = node.getSingleRelationship(RelationshipTypes.LAST_PLAYED_MATCH_REFERENCE, Direction.OUTGOING);

        if (relationship != null) {
            return new GraphMatchReference(relationship.getEndNode());
        } else {
            return null;
        }
    }

    public String getName() {
        return (String) node.getProperty(KEY_NAME);
    }
}
