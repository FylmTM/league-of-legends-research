package com.neueda4j.neo4j.research.leagueofgraph.graph.domain;

import com.neueda4j.neo4j.research.leagueofgraph.graph.Labels;
import com.robrua.orianna.type.core.staticdata.Champion;
import org.neo4j.graphdb.Node;

public class GraphChampion {

    public static String KEY_ID = "id";
    public static String KEY_NAME = "name";

    public static GraphChampion create(Node node, Champion champion) {
        node.addLabel(Labels.Champion);
        node.setProperty(KEY_ID, champion.getID());
        node.setProperty(KEY_NAME, champion.getName());
        node.setProperty(KEY_NAME, champion.getKey());

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
