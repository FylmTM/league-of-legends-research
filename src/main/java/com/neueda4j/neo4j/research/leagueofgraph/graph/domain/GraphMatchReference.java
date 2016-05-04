package com.neueda4j.neo4j.research.leagueofgraph.graph.domain;

import com.neueda4j.neo4j.research.leagueofgraph.GraphDatabase;
import com.neueda4j.neo4j.research.leagueofgraph.graph.Labels;
import com.neueda4j.neo4j.research.leagueofgraph.graph.RelationshipTypes;
import com.robrua.orianna.type.core.match.Match;
import com.robrua.orianna.type.core.matchlist.MatchReference;
import com.robrua.orianna.type.core.staticdata.Champion;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.neueda4j.neo4j.research.leagueofgraph.runner.SafelyRunnable.safelyExecute;

public class GraphMatchReference {

    private static final Logger log = LoggerFactory.getLogger(GraphMatchReference.class);

    private static final String KEY_MATCH_ID = "match_id";
    private static final String KEY_SEASON = "season";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_DATE = "date";
    private static final String KEY_QUEUE_TYPE = "queue_type";
    private static final String KEY_ROLE = "role";
    private static final String KEY_LANE = "lane";
    private static final String KEY_CHAMPION_ID = "champion_id";
    private static final String KEY_PLATFORM_ID = "platform_id";

    private final Node node;

    public static GraphMatchReference create(GraphDatabase graphDatabase, Node node, MatchReference matchReference) {
        node.addLabel(Labels.MatchReference);
        node.setProperty(KEY_MATCH_ID, matchReference.getID());
        node.setProperty(KEY_SEASON, matchReference.getSeason().toString());
        node.setProperty(KEY_TIMESTAMP, matchReference.getTimestamp().getTime());
        node.setProperty(KEY_DATE, matchReference.getTimestamp().toString());
        node.setProperty(KEY_QUEUE_TYPE, matchReference.getQueueType().toString());
        node.setProperty(KEY_ROLE, matchReference.getRole().toString());
        node.setProperty(KEY_LANE, matchReference.getLane().toString());
        node.setProperty(KEY_CHAMPION_ID, matchReference.getChampionID());
        node.setProperty(KEY_PLATFORM_ID, matchReference.getPlatformID().toString());

        GraphMatchReference graphMatchReference = new GraphMatchReference(node);

        // Champion
        Champion champion = safelyExecute(matchReference::getChampion);
        GraphChampion graphChampion = graphDatabase.createChampion(champion);
        graphMatchReference.setGraphChampion(graphChampion);

        // Match
        try {
            Match match = safelyExecute(() -> matchReference.getMatch(true));
            GraphMatch graphMatch = graphDatabase.createMatch(match);
            graphMatchReference.setGraphMatch(graphMatch);
        } catch (Exception exception){
            log.warn("Exception during match import", exception);
        }

        return graphMatchReference;
    }

    public GraphMatchReference(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public boolean addNextGraphMatchReference(GraphMatchReference lastGraphMatchReference) {
        if (node.hasRelationship(RelationshipTypes.NEXT_MATCH_REFERENCE, Direction.OUTGOING)) {
            return false;
        } else {
            node.createRelationshipTo(lastGraphMatchReference.getNode(), RelationshipTypes.NEXT_MATCH_REFERENCE);
            return true;
        }
    }

    public void setGraphMatch(GraphMatch graphMatch) {
        node.createRelationshipTo(graphMatch.getNode(), RelationshipTypes.HAS_MATCH);
    }

    public void setGraphChampion(GraphChampion graphChampion) {
        node.createRelationshipTo(graphChampion.getNode(), RelationshipTypes.PLAYED_WITH_CHAMPION);
    }
}
