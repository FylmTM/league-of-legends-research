package com.neueda4j.neo4j.research.leagueofgraph.graph.domain;

import com.neueda4j.neo4j.research.leagueofgraph.GraphDatabase;
import com.neueda4j.neo4j.research.leagueofgraph.graph.Labels;
import com.robrua.orianna.type.core.match.ParticipantTimelineData;
import org.neo4j.graphdb.Node;

public class GraphParticipantTimelineData {

    private static final String KEY_ZERO_TO_TEN = "zero_to_ten";
    private static final String KEY_TEN_TO_TWENTY = "ten_to_twenty";
    private static final String KEY_TWENTY_TO_THIRTY = "twenty_to_thirty";
    private static final String KEY_THIRTY_TO_END = "thirty_to_end";

    private final Node node;

    public static GraphParticipantTimelineData create(GraphDatabase graphDatabase, Node node, ParticipantTimelineData participantTimelineData) {
        node.addLabel(Labels.ParticipantTimelineData);

        node.setProperty(KEY_ZERO_TO_TEN, participantTimelineData.getZeroToTen());
        node.setProperty(KEY_TEN_TO_TWENTY, participantTimelineData.getTenToTwenty());
        node.setProperty(KEY_TWENTY_TO_THIRTY, participantTimelineData.getTwentyToThirty());
        node.setProperty(KEY_THIRTY_TO_END, participantTimelineData.getThirtyToEnd());

        return new GraphParticipantTimelineData(node);
    }

    public GraphParticipantTimelineData(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }
}
