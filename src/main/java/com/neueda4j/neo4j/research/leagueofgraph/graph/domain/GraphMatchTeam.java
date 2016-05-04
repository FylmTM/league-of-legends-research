package com.neueda4j.neo4j.research.leagueofgraph.graph.domain;

import com.neueda4j.neo4j.research.leagueofgraph.GraphDatabase;
import com.neueda4j.neo4j.research.leagueofgraph.graph.Labels;
import com.neueda4j.neo4j.research.leagueofgraph.graph.RelationshipTypes;
import com.robrua.orianna.type.core.match.BannedChampion;
import com.robrua.orianna.type.core.match.MatchTeam;
import com.robrua.orianna.type.core.staticdata.Champion;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import static com.neueda4j.neo4j.research.leagueofgraph.runner.SafelyRunnable.safelyExecute;

public class GraphMatchTeam {

    public static final String KEY_SIDE = "side";
    public static final String KEY_WINNER = "winner";
    private static final String KEY_DOMINION_VICTORY_SCORE = "dominion_victory_score";
    private static final String KEY_BARON_KILLS = "baron_kills";
    private static final String KEY_DRAGON_KILLS = "dragon_kills";
    private static final String KEY_RIFT_HERALD_KILLS = "rift_herald_kills";
    private static final String KEY_VILEMAW_KILLS = "vilemaw_kills";
    private static final String KEY_FIRST_BARON = "first_baron";
    private static final String KEY_FIRST_BLOOD = "first_blood";
    private static final String KEY_FIRST_DRAGON = "first_dragon";
    private static final String KEY_FIRST_INHIBITOR = "first_inhibitor";
    private static final String KEY_FIRST_RIFT_HERALD = "first_rift_herald";
    private static final String KEY_FIRST_TOWER = "first_tower";
    private static final String KEY_TOWER_KILLS = "tower_kills";
    private static final String KEY_INHIBITOR_KILLS = "inhibitor_kills";

    private final Node node;

    public GraphMatchTeam(Node node) {
        this.node = node;
    }

    public static GraphMatchTeam create(GraphDatabase graphDatabase, Node node, MatchTeam matchTeam) {
        node.addLabel(Labels.MatchTeam);
        node.setProperty(KEY_SIDE, matchTeam.getSide().toString());
        node.setProperty(KEY_WINNER, matchTeam.getWinner());

        node.setProperty(KEY_DOMINION_VICTORY_SCORE, matchTeam.getDominionVictoryScore());

        node.setProperty(KEY_BARON_KILLS, matchTeam.getBaronKills());
        node.setProperty(KEY_DRAGON_KILLS, matchTeam.getDragonKills());
        node.setProperty(KEY_RIFT_HERALD_KILLS, matchTeam.getRiftHeraldKills());
        node.setProperty(KEY_VILEMAW_KILLS, matchTeam.getVilemawKills());

        node.setProperty(KEY_FIRST_BARON, matchTeam.getFirstBaron());
        node.setProperty(KEY_FIRST_BLOOD, matchTeam.getFirstBlood());
        node.setProperty(KEY_FIRST_DRAGON, matchTeam.getFirstDragon());
        node.setProperty(KEY_FIRST_INHIBITOR, matchTeam.getFirstInhibitor());
        node.setProperty(KEY_FIRST_RIFT_HERALD, matchTeam.getFirstRiftHerald());
        node.setProperty(KEY_FIRST_TOWER, matchTeam.getFirstTower());

        node.setProperty(KEY_TOWER_KILLS, matchTeam.getTowerKills());
        node.setProperty(KEY_INHIBITOR_KILLS, matchTeam.getInhibitorKills());

        GraphMatchTeam graphMatchTeam = new GraphMatchTeam(node);

        if (matchTeam.getDto().getBans() != null) {
            for (BannedChampion bannedChampion : matchTeam.getBans()) {
                Champion champion = safelyExecute(bannedChampion::getChampion);
                graphMatchTeam.bannedChampion(bannedChampion, graphDatabase.createChampion(champion));
            }
        }

        return graphMatchTeam;
    }

    public void bannedChampion(BannedChampion bannedChampion, GraphChampion graphChampion) {
        Relationship rel = node.createRelationshipTo(graphChampion.getNode(), RelationshipTypes.BANNED);
        rel.setProperty("pick_turn", bannedChampion.getPickTurn());
    }

    public Node getNode() {
        return node;
    }
}
