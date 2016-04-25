package com.neueda4j.neo4j.research.leagueofgraph;

import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.api.LoadPolicy;
import com.robrua.orianna.type.api.RateLimit;
import com.robrua.orianna.type.core.common.Region;

public class RiotAPISetup {

    public static void setup(String apiKey) {
        RiotAPI.setAPIKey(apiKey);
        RiotAPI.setRegion(Region.EUNE);
        RiotAPI.setLoadPolicy(LoadPolicy.LAZY);
        RiotAPI.setRateLimit(new RateLimit(10, 10), new RateLimit(500, 10 * 60));
        RiotAPI.printCalls(true);
    }
}
