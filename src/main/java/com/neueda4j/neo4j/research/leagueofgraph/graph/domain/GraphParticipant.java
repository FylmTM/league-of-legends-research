package com.neueda4j.neo4j.research.leagueofgraph.graph.domain;

import com.neueda4j.neo4j.research.leagueofgraph.GraphDatabase;
import com.neueda4j.neo4j.research.leagueofgraph.graph.Labels;
import com.neueda4j.neo4j.research.leagueofgraph.graph.RelationshipTypes;
import com.robrua.orianna.type.core.match.Participant;
import com.robrua.orianna.type.core.staticdata.Champion;
import com.robrua.orianna.type.core.summoner.Summoner;
import org.neo4j.graphdb.Node;

import static com.neueda4j.neo4j.research.leagueofgraph.runner.SafelyRunnable.safelyExecute;

public class GraphParticipant {

    private Node node;

    public GraphParticipant(Node node) {
        this.node = node;
    }

    public static GraphParticipant create(GraphDatabase graphDatabase, Node node, Participant participant) {
        node.addLabel(Labels.Participant);

        GraphParticipant graphParticipant = new GraphParticipant(node);

        // Summoner
        Summoner summoner = safelyExecute(participant::getSummoner);
        GraphSummoner graphSummoner = graphDatabase.createSummoner(summoner);
        graphSummoner.participatedWith(graphParticipant);

        // Champion
        Champion champion = safelyExecute(participant::getChampion);
        GraphChampion graphChampion = graphDatabase.createChampion(champion);
        graphParticipant.participatedWithChampion(graphChampion);

        return graphParticipant;
    }

    public Node getNode() {
        return node;
    }

    public void participatedInGraphMatch(GraphMatch graphMatch) {
        node.createRelationshipTo(graphMatch.getNode(), RelationshipTypes.PARTICIPATED_IN_MATCH);
    }

    public void playedForGraphMatchTeam(GraphMatchTeam graphMatchTeam) {
        node.createRelationshipTo(graphMatchTeam.getNode(), RelationshipTypes.PLAYED_FOR_TEAM);
    }

    public void participatedWithChampion(GraphChampion graphChampion) {
        node.createRelationshipTo(graphChampion.getNode(), RelationshipTypes.PARTICIPATED_WITH_CHAMPION);

    }
}
