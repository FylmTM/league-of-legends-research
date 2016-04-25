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

    private static final String KEY_PARTICIPANT_ID = "participant_id";
    private static final String KEY_SUMMONER_ID = "summoner_id";
    private static final String KEY_SUMMONER_NAME = "summoner_name";
    private static final String KEY_CHAMPION_ID = "champion_id";
    private static final String KEY_MATCH_HISTORY_URI = "match_history_uri";
    private static final String KEY_PREVIOUS_SEASON_TIER = "previous_season_tier";
    private static final String KEY_PROFILE_ICON_ID = "profile_icon_id";
    private static final String KEY_SUMMONER_SPELL_1_ID = "summoner_spell_1_id";
    private static final String KEY_SUMMONER_SPELL_2_ID = "summoner_spell_2_id";
    private static final String KEY_TEAM = "team";

    private Node node;

    public GraphParticipant(Node node) {
        this.node = node;
    }

    public static GraphParticipant create(GraphDatabase graphDatabase, Node node, Participant participant) {
        node.addLabel(Labels.Participant);
        node.setProperty(KEY_PARTICIPANT_ID, participant.getParticipantID());
        node.setProperty(KEY_SUMMONER_ID, participant.getSummonerID());
        node.setProperty(KEY_SUMMONER_NAME, participant.getSummonerName());
        node.setProperty(KEY_CHAMPION_ID, participant.getChampionID());
        node.setProperty(KEY_MATCH_HISTORY_URI, participant.getMatchHistoryURI());
        node.setProperty(KEY_PREVIOUS_SEASON_TIER, participant.getPreviousSeasonTier().toString());
        node.setProperty(KEY_PROFILE_ICON_ID, participant.getProfileIconID());
        node.setProperty(KEY_SUMMONER_SPELL_1_ID, participant.getSummonerSpell1ID());
        node.setProperty(KEY_SUMMONER_SPELL_2_ID, participant.getSummonerSpell2ID());
        node.setProperty(KEY_TEAM, participant.getTeam().toString());

        GraphParticipant graphParticipant = new GraphParticipant(node);

        // Summoner
        Summoner summoner = safelyExecute(participant::getSummoner);
        GraphSummoner graphSummoner = graphDatabase.createSummoner(summoner);
        graphSummoner.participated(graphParticipant);

        // Champion
        Champion champion = safelyExecute(participant::getChampion);
        GraphChampion graphChampion = graphDatabase.createChampion(champion);
        graphParticipant.participatedWithChampion(graphChampion);

        // todo: masteries
        // todo: runes
        // todo: stats
        // todo: summoner spell 1 & 2
        // todo: timeline

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
