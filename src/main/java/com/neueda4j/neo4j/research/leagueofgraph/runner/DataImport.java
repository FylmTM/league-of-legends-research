package com.neueda4j.neo4j.research.leagueofgraph.runner;

import com.google.common.collect.Lists;
import com.neueda4j.neo4j.research.leagueofgraph.GraphDatabase;
import com.neueda4j.neo4j.research.leagueofgraph.graph.domain.GraphMatch;
import com.neueda4j.neo4j.research.leagueofgraph.graph.domain.GraphMatchReference;
import com.neueda4j.neo4j.research.leagueofgraph.graph.domain.GraphSummoner;
import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.core.common.QueueType;
import com.robrua.orianna.type.core.common.Season;
import com.robrua.orianna.type.core.match.Match;
import com.robrua.orianna.type.core.matchlist.MatchReference;
import com.robrua.orianna.type.core.summoner.Summoner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static com.neueda4j.neo4j.research.leagueofgraph.runner.SafelyRunnable.safelyExecute;

public class DataImport {

    private static final Logger log = LoggerFactory.getLogger(DataImport.class);

    private static final List<QueueType> QUEUE_TYPES = Lists.newArrayList(
            QueueType.TEAM_BUILDER_DRAFT_RANKED_5x5
    );
    private static final List<Season> SEASONS = Collections.singletonList(
            Season.SEASON2016
    );

    private final GraphDatabase database;

    public DataImport(GraphDatabase database) {
        this.database = database;
    }

    public void importSummoner(String summonerName) {
        Summoner summoner = safelyExecute(() -> RiotAPI.getSummonerByName(summonerName));
        log.info("Importing Summoner[{}]", summoner.getName());
        GraphSummoner graphSummoner = database.createSummoner(summoner);

        List<MatchReference> matchList = getMatchList(summoner);

        GraphMatchReference lastGraphMatchReference = null;
        for (int i = 0; i < matchList.size(); i++) {
            // Import MatchReference
            MatchReference matchReference = matchList.get(i);
            log.info("Importing Summoner[{}] - MatchReference[{}] {}/{}",
                    summoner.getName(), matchReference.getID(), i, matchList.size());
            GraphMatchReference graphMatchReference = database.createMatchReference(matchReference);
            if (lastGraphMatchReference != null) {
                graphMatchReference.addNextGraphMatchReference(lastGraphMatchReference);
            }
            lastGraphMatchReference = graphMatchReference;
            graphSummoner.addGraphMatchReference(graphMatchReference);

            // Import Match
            Match match = safelyExecute(() -> matchReference.getMatch(false));
            GraphMatch graphMatch = database.createMatch(match);
            graphMatchReference.setGraphMatch(graphMatch);
        }
    }

    private List<MatchReference> getMatchList(Summoner summoner) {
        //return safelyExecute(() -> summoner.getMatchList(null, null, QUEUE_TYPES, null, SEASONS));
        return safelyExecute(() -> summoner.getMatchList(15, 0, null, null, QUEUE_TYPES, null, SEASONS));
    }
}

//            Timeline timeline = match.getTimeline();
//            for (Frame frame : timeline.getFrames()) {
//                for (Event event : frame.getEvents()) {
//                    System.out.println(event.getEventType()
//                            + " :: " + (event.getParticipant() == null ? "-" : event.getParticipant().getSummonerName()));
//                }
//            }
