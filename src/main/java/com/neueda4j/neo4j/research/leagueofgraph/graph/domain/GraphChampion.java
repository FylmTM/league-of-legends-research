package com.neueda4j.neo4j.research.leagueofgraph.graph.domain;

import com.neueda4j.neo4j.research.leagueofgraph.graph.Labels;
import com.robrua.orianna.type.core.staticdata.Champion;
import org.neo4j.graphdb.Node;

public class GraphChampion {

    public static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_KEY = "key";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LORE = "lore";
    private static final String KEY_PARTY_TYPE = "party_type";
    private static final String KEY_BLURB = "blurb";
    private static final String KEY_TAGS = "tags";
    private static final String KEY_ALLY_TIPS = "ally_tips";
    private static final String KEY_ENEMY_TIPS = "enemy_tips";
    private static final String KEY_IMAGE_FULL = "image-full";
    private static final String KEY_IMAGE_GROUP = "image-group";
    private static final String KEY_IMAGE_SPRITE = "image-sprinte";
    private static final String KEY_IMAGE_H = "image-h";
    private static final String KEY_IMAGE_W = "image-w";
    private static final String KEY_IMAGE_X = "image-x";
    private static final String KEY_IMAGE_Y = "image-y";
    private static final String KEY_INFO_ATTACK = "info-attack";
    private static final String KEY_INFO_DEFENSE = "info-defense";
    private static final String KEY_INFO_MAGIC = "info-magic";
    private static final String KEY_INFO_DIFFICULTY = "info-difficulty";

    public static GraphChampion create(Node node, Champion champion) {
        node.addLabel(Labels.Champion);
        node.setProperty(KEY_ID, champion.getID());
        node.setProperty(KEY_KEY, champion.getKey());
        node.setProperty(KEY_NAME, champion.getName());
        node.setProperty(KEY_TITLE, champion.getTitle());
        node.setProperty(KEY_LORE, champion.getLore());
        node.setProperty(KEY_PARTY_TYPE, champion.getPartype());

        node.setProperty(KEY_BLURB, champion.getBlurb());
        node.setProperty(KEY_TAGS, champion.getTags());
        node.setProperty(KEY_ALLY_TIPS, champion.getAllyTips());
        node.setProperty(KEY_ENEMY_TIPS, champion.getEnemyTips());

        node.setProperty(KEY_IMAGE_FULL, champion.getImage().getFull());
        node.setProperty(KEY_IMAGE_GROUP, champion.getImage().getGroup());
        node.setProperty(KEY_IMAGE_SPRITE, champion.getImage().getSprite());
        node.setProperty(KEY_IMAGE_H, champion.getImage().getH());
        node.setProperty(KEY_IMAGE_W, champion.getImage().getW());
        node.setProperty(KEY_IMAGE_X, champion.getImage().getX());
        node.setProperty(KEY_IMAGE_Y, champion.getImage().getY());

        node.setProperty(KEY_INFO_ATTACK, champion.getInfo().getAttack());
        node.setProperty(KEY_INFO_DEFENSE, champion.getInfo().getDefense());
        node.setProperty(KEY_INFO_MAGIC, champion.getInfo().getMagic());
        node.setProperty(KEY_INFO_DIFFICULTY, champion.getInfo().getDifficulty());

        // todo: getPassive
        // todo: getRecommendedItems
        // todo: getSkins
        // todo: getSpells
        // todo: getStats
        // todo: getStatus

        return new GraphChampion(node);
    }

    private final Node node;

    public GraphChampion(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }
}
