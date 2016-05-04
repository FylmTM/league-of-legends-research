package com.neueda4j.neo4j.research.leagueofgraph.graph;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType {
    PLAYED_MATCH_REFERENCE,
    HAS_MATCH,
    PLAYED_WITH_CHAMPION,
    HAS_TEAM,
    PARTICIPATED_IN_MATCH,
    PARTICIPATED,
    PLAYED_FOR_TEAM,
    PARTICIPATED_WITH_CHAMPION,
    LAST_PLAYED_MATCH_REFERENCE,
    PARTICIPANT_TIMELINE,
    PARTICIPANT_TIMELINE_DATA, BANNED, NEXT_MATCH_REFERENCE
}
