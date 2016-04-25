package com.neueda4j.neo4j.research.leagueofgraph.graph.domain;

import com.neueda4j.neo4j.research.leagueofgraph.GraphDatabase;
import com.neueda4j.neo4j.research.leagueofgraph.graph.Labels;
import com.neueda4j.neo4j.research.leagueofgraph.graph.RelationshipTypes;
import com.robrua.orianna.type.core.matchlist.MatchReference;
import com.robrua.orianna.type.core.staticdata.Champion;
import org.neo4j.graphdb.Node;

import static com.neueda4j.neo4j.research.leagueofgraph.runner.SafelyRunnable.safelyExecute;

public class GraphMatchReference {

    public static final String KEY_ID = "id";
    public static final String KEY_SEASON = "season";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_DATE = "date";
    public static final String KEY_QUEUE_TYPE = "queue_type";
    public static final String KEY_ROLE = "role";
    public static final String KEY_LANE = "lane";

    private final Node node;

    public static GraphMatchReference create(GraphDatabase graphDatabase, Node node, MatchReference matchReference) {
        node.addLabel(Labels.MatchReference);
        node.setProperty(GraphMatchReference.KEY_ID, matchReference.getID());
        node.setProperty(GraphMatchReference.KEY_SEASON, matchReference.getSeason().toString());
        node.setProperty(GraphMatchReference.KEY_TIMESTAMP, matchReference.getTimestamp().getTime());
        node.setProperty(GraphMatchReference.KEY_DATE, matchReference.getTimestamp().toString());
        node.setProperty(GraphMatchReference.KEY_QUEUE_TYPE, matchReference.getQueueType().toString());
        node.setProperty(GraphMatchReference.KEY_ROLE, matchReference.getRole().toString());
        node.setProperty(GraphMatchReference.KEY_LANE, matchReference.getLane().toString());

        GraphMatchReference graphMatchReference = new GraphMatchReference(node);

        // Champion
        Champion champion = safelyExecute(matchReference::getChampion);
        GraphChampion graphChampion = graphDatabase.createChampion(champion);
        graphMatchReference.setGraphChampion(graphChampion);

        return graphMatchReference;
    }

    public GraphMatchReference(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public void addNextGraphMatchReference(GraphMatchReference lastGraphMatchReference) {
        node.createRelationshipTo(lastGraphMatchReference.getNode(), RelationshipTypes.NEXT_MATCH_REFERENCE);
    }

    public void setGraphMatch(GraphMatch graphMatch) {
        node.createRelationshipTo(graphMatch.getNode(), RelationshipTypes.HAS_MATCH);
    }

    public void setGraphChampion(GraphChampion graphChampion) {
        node.createRelationshipTo(graphChampion.getNode(), RelationshipTypes.PLAYED_WITH_CHAMPION);
    }
}
