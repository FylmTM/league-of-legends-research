package com.neueda4j.neo4j.research.leagueofgraph.graph.domain;

import com.neueda4j.neo4j.research.leagueofgraph.GraphDatabase;
import com.neueda4j.neo4j.research.leagueofgraph.graph.Labels;
import com.neueda4j.neo4j.research.leagueofgraph.graph.RelationshipTypes;
import com.robrua.orianna.type.core.common.Side;
import com.robrua.orianna.type.core.match.Match;
import com.robrua.orianna.type.core.match.MatchTeam;
import com.robrua.orianna.type.core.match.Participant;
import org.neo4j.graphdb.Node;

import java.util.HashMap;
import java.util.Map;


public class GraphMatch {

    public static final String KEY_ID = "id";
    private static final String KEY_VERSION = "version";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_MODE = "mode";
    private static final String KEY_QUEUE_TYPE = "queue_type";
    private static final String KEY_SEASON = "season";
    private static final String KEY_CREATION = "creation";
    private static final String KEY_CREATION_TIMESTAMP = "creation_timestamp";
    private static final String KEY_GAME_MAP = "map";
    private static final String KEY_PLATFORM_ID = "platform_id";
    private static final String KEY_REGION = "region";
    private static final String KEY_TYPE = "type";

    private Node node;

    public static GraphMatch create(GraphDatabase graphDatabase, Node node, Match match) {
        node.addLabel(Labels.Match);
        node.setProperty(KEY_ID, match.getID());
        node.setProperty(KEY_VERSION, match.getVersion());
        node.setProperty(KEY_CREATION, match.getCreation().getTime());
        node.setProperty(KEY_CREATION_TIMESTAMP, match.getCreation().toString());
        node.setProperty(KEY_DURATION, match.getDuration());
        node.setProperty(KEY_GAME_MAP, match.getMap().toString());
        node.setProperty(KEY_MODE, match.getMode().toString());
        node.setProperty(KEY_PLATFORM_ID, match.getPlatformID().toString());
        node.setProperty(KEY_QUEUE_TYPE, match.getQueueType().toString());
        node.setProperty(KEY_REGION, match.getRegion().toString());
        node.setProperty(KEY_SEASON, match.getSeason().toString());
        node.setProperty(KEY_TYPE, match.getType().toString());

        GraphMatch graphMatch = new GraphMatch(node);

        // MatchTeam
        Map<Side, GraphMatchTeam> graphMatchTeamMap = new HashMap<>();
        for (MatchTeam matchTeam : match.getTeams()) {
            GraphMatchTeam graphMatchTeam = graphDatabase.createMatchTeam(matchTeam);
            graphMatch.addGraphMatchTeam(graphMatchTeam);
            graphMatchTeamMap.put(matchTeam.getSide(), graphMatchTeam);
        }

        // Participants
        for (Participant participant : match.getParticipants()) {
            GraphParticipant graphParticipant = graphDatabase.createParticipant(participant);
            graphMatch.addParticipant(graphParticipant);

            graphParticipant.playedForGraphMatchTeam(graphMatchTeamMap.get(participant.getTeam()));
        }

        // todo: timeline

        return graphMatch;
    }

    public GraphMatch(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public void addGraphMatchTeam(GraphMatchTeam graphMatchTeam) {
        node.createRelationshipTo(graphMatchTeam.getNode(), RelationshipTypes.HAS_TEAM);
    }

    public void addParticipant(GraphParticipant graphParticipant) {
        graphParticipant.participatedInGraphMatch(this);
    }
}
