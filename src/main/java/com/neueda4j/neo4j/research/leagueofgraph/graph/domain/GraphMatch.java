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
    public static final String KEY_VERSION = "version";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_MODE = "mode";

    private Node node;

    public static GraphMatch create(GraphDatabase graphDatabase, Node node, Match match) {
        node.addLabel(Labels.Match);
        node.setProperty(KEY_ID, match.getID());
        node.setProperty(KEY_VERSION, match.getVersion());
        node.setProperty(KEY_DURATION, match.getDuration());
        node.setProperty(KEY_MODE, match.getMode().toString());

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
