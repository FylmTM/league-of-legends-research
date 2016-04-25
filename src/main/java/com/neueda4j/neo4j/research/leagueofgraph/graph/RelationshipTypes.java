package com.neueda4j.neo4j.research.leagueofgraph.graph;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType {
    PLAYED_MATCH_REFERENCE, HAS_MATCH, PLAYED_WITH_CHAMPION, HAS_TEAM, PARTICIPATED_IN_MATCH, PLAYED_PARTICIPATED, PLAYED_FOR_TEAM, PARTICIPATED_WITH_CHAMPION, NEXT_MATCH_REFERENCE
}
