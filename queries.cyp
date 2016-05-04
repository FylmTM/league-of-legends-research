// Match count
MATCH (s:Summoner {name: "FylmTM"})
MATCH (s)-[:PARTICIPATED|:PARTICIPATED_IN_MATCH]->(sMatch)
RETURN count(sMatch); // 59 ms

// My friends
MATCH (s:Summoner {name: "FylmTM"})
MATCH (s)-[:PARTICIPATED]-()-[:PARTICIPATED_IN_MATCH]
      ->(sMatch)<-
      [:PARTICIPATED_IN_MATCH]-()-[:PARTICIPATED]-(f)
WITH f.name as friendName, count(f) as gamesTogether
WHERE gamesTogether > 2
RETURN friendName, gamesTogether
ORDER BY gamesTogether DESC // 55 ms

// Enemies
MATCH (s:Summoner {name: "FylmTM"})
MATCH
    (s)-[:PARTICIPATED]->()-[:PLAYED_FOR_TEAM]->
        (team {winner: true})<-[:HAS_TEAM]-()-[:HAS_TEAM]->(o)
    <-[:PLAYED_FOR_TEAM]-()-[:PARTICIPATED_WITH_CHAMPION]->(c)
RETURN c.name as championName, count(c) as winCount
ORDER BY winCount DESC

// Examines the full graph to create the meta-graph
CALL apoc.meta.graph();

// Statistics
MATCH (s:Summoner) WHERE s.name IN ["MrMgr", "Cryptael"]
MATCH (s)-[:PARTICIPATED|:PARTICIPANT_TIMELINE*2]->(pt)-[r]->(ptd)
WHERE pt.role = "SOLO" AND pt.lane = "MIDDLE"
AND r.name IN ["getCreepsPerMinDeltas", "getGoldPerMinDeltas"]
WITH s.name as name, r.name as stat,
  sum(ptd.zero_to_ten) as `sum_0-10`,
  size(filter(x IN collect(ptd.zero_to_ten) WHERE x <> 0))
    as `size_0-10`,
  sum(ptd.ten_to_twenty) as `sum_10-20`,
  size(filter(x IN collect(ptd.ten_to_twenty) WHERE x <> 0))
    as `size_10-20`,
  sum(ptd.twenty_to_thirty) as `sum_20-30`,
  size(filter(x IN collect(ptd.twenty_to_thirty) WHERE x <> 0))
    as `size_20-30`,
  sum(ptd.thirty_to_end) as `sum_30+`,
  size(filter(x IN collect(ptd.thirty_to_end) WHERE x <> 0))
    as `size_30+`
RETURN name, stat,
  `sum_0-10` / `size_0-10` as `0-10`,
  `sum_10-20` / `size_10-20` as `10-20`,
  `sum_20-30` / `size_20-30` as `20-30`,
  `sum_30+` / `size_30+` as `30+`
ORDER BY name, stat
