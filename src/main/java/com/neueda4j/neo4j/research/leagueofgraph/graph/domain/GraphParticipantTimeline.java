package com.neueda4j.neo4j.research.leagueofgraph.graph.domain;

import com.neueda4j.neo4j.research.leagueofgraph.GraphDatabase;
import com.neueda4j.neo4j.research.leagueofgraph.graph.Labels;
import com.neueda4j.neo4j.research.leagueofgraph.graph.RelationshipTypes;
import com.robrua.orianna.type.core.match.ParticipantTimeline;
import com.robrua.orianna.type.core.match.ParticipantTimelineData;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class GraphParticipantTimeline {

    private static final String KEY_LANE = "lane";
    private static final String KEY_ROLE = "role";

    private final Node node;

    public static GraphParticipantTimeline create(GraphDatabase graphDatabase, Node node, ParticipantTimeline participantTimeline) {
        node.addLabel(Labels.ParticipantTimeline);
        node.setProperty(KEY_LANE, participantTimeline.getLane().toString());
        node.setProperty(KEY_ROLE, participantTimeline.getRole().toString());

        GraphParticipantTimeline graphParticipantTimeline = new GraphParticipantTimeline(node);

        ConstructGraphData data = graphDatabase::createParticipantTimelineData;
        graphParticipantTimeline
                .setParticipantTimelineData("getAncientGolemAssistsPerMinCounts", data.create(participantTimeline.getAncientGolemAssistsPerMinCounts()))
                .setParticipantTimelineData("getAncientGolemKillsPerMinCounts", data.create(participantTimeline.getAncientGolemKillsPerMinCounts()))
                .setParticipantTimelineData("getAssistedLaneDeathsPerMinDeltas", data.create(participantTimeline.getAssistedLaneDeathsPerMinDeltas()))
                .setParticipantTimelineData("getAssistedLaneKillsPerMinDeltas", data.create(participantTimeline.getAssistedLaneKillsPerMinDeltas()))
                .setParticipantTimelineData("getBaronAssistsPerMinCounts", data.create(participantTimeline.getBaronAssistsPerMinCounts()))
                .setParticipantTimelineData("getBaronKillsPerMinCounts", data.create(participantTimeline.getBaronKillsPerMinCounts()))
                .setParticipantTimelineData("getCreepsPerMinDeltas", data.create(participantTimeline.getCreepsPerMinDeltas()))
                .setParticipantTimelineData("getCSDiffPerMinDeltas", data.create(participantTimeline.getCSDiffPerMinDeltas()))
                .setParticipantTimelineData("getDamageTakenDiffPerMinDeltas", data.create(participantTimeline.getDamageTakenDiffPerMinDeltas()))
                .setParticipantTimelineData("getDamageTakenPerMinDeltas", data.create(participantTimeline.getDamageTakenPerMinDeltas()))
                .setParticipantTimelineData("getDragonAssistsPerMinCounts", data.create(participantTimeline.getDragonAssistsPerMinCounts()))
                .setParticipantTimelineData("getDragonKillsPerMinCounts", data.create(participantTimeline.getDragonKillsPerMinCounts()))
                .setParticipantTimelineData("getElderLizardAssistsPerMinCounts", data.create(participantTimeline.getElderLizardAssistsPerMinCounts()))
                .setParticipantTimelineData("getElderLizardKillsPerMinCounts", data.create(participantTimeline.getElderLizardKillsPerMinCounts()))
                .setParticipantTimelineData("getGoldPerMinDeltas", data.create(participantTimeline.getGoldPerMinDeltas()))
                .setParticipantTimelineData("getInhibitorAssistsPerMinCounts", data.create(participantTimeline.getInhibitorAssistsPerMinCounts()))
                .setParticipantTimelineData("getInhibitorKillsPerMinCounts", data.create(participantTimeline.getInhibitorKillsPerMinCounts()))
                .setParticipantTimelineData("getTowerAssistsPerMinCounts", data.create(participantTimeline.getTowerAssistsPerMinCounts()))
                .setParticipantTimelineData("getTowerKillsPerMinCounts", data.create(participantTimeline.getTowerKillsPerMinCounts()))
                .setParticipantTimelineData("getTowerKillsPerMinDeltas", data.create(participantTimeline.getTowerKillsPerMinDeltas()))
                .setParticipantTimelineData("getVilemawAssistsPerMinCounts", data.create(participantTimeline.getVilemawAssistsPerMinCounts()))
                .setParticipantTimelineData("getVilemawKillsPerMinCounts", data.create(participantTimeline.getVilemawKillsPerMinCounts()))
                .setParticipantTimelineData("getWardsPerMinDeltas", data.create(participantTimeline.getWardsPerMinDeltas()))
                .setParticipantTimelineData("getXPDiffPerMinDeltas", data.create(participantTimeline.getXPDiffPerMinDeltas()))
                .setParticipantTimelineData("getXPPerMinDeltas", data.create(participantTimeline.getXPPerMinDeltas()));

        return graphParticipantTimeline;
    }

    public GraphParticipantTimeline(Node node) {
        this.node = node;
    }

    public GraphParticipantTimeline setParticipantTimelineData(String name, GraphParticipantTimelineData timelineData) {
        Relationship rel = node.createRelationshipTo(timelineData.getNode(), RelationshipTypes.PARTICIPANT_TIMELINE_DATA);
        rel.setProperty("name", name);
        return this;
    }

    public Node getNode() {
        return node;
    }

    private interface ConstructGraphData {
        GraphParticipantTimelineData create(ParticipantTimelineData participantTimelineData);
    }
}
