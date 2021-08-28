package com.alessiodp.oreannouncer.common.storage.sql.dao.blocksfound;

import com.alessiodp.core.common.utils.Pair;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
import com.alessiodp.oreannouncer.common.storage.sql.dao.StatsPlayerRowMapper;
import com.alessiodp.oreannouncer.common.storage.sql.dao.TopPlayerRowMapper;
import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface BlocksFoundDao {
	String QUERY_INSERT = "INSERT INTO `<prefix>blocks_found` (`player`, `material_name`, `timestamp`, `found`) VALUES (?, ?, ?, ?)";
	String QUERY_GET_ALL = "SELECT * FROM `<prefix>blocks_found` WHERE material_name in (<materials>) ORDER BY timestamp DESC LIMIT :limit OFFSET :offset";
	String QUERY_GET_ALL_BY_PLAYER = "SELECT * FROM `<prefix>blocks_found` WHERE material_name in (<materials>) AND player = :player ORDER BY timestamp DESC LIMIT :limit OFFSET :offset";
	String QUERY_GET_ALL_NUMBER = "SELECT count(*) FROM `<prefix>blocks_found` WHERE material_name in (<materials>)";
	String QUERY_GET_ALL_NUMBER_BY_PLAYER = "SELECT count(*) FROM `<prefix>blocks_found` WHERE material_name in (<materials>) AND player = :player";
	String QUERY_GET_ALL_MERGED = "SELECT `player`, `material_name`, sum(`found`) as `found`, 0 as `timestamp` FROM `<prefix>blocks_found` WHERE player = ? GROUP BY `player`, `material_name`";
	String QUERY_GET_LATEST_TOTAL = "SELECT min(`timestamp`) AS time, sum(`found`) AS total FROM `<prefix>blocks_found` WHERE player = ? AND timestamp >= ?";
	String QUERY_GET_LATEST_BLOCK = "SELECT min(`timestamp`) AS time, sum(`found`) AS total FROM `<prefix>blocks_found` WHERE player = ? AND timestamp >= ? AND material_name = ?";
	String QUERY_GET_TOP_PLAYERS = "SELECT `player`, sum(`found`) AS `total` FROM `<prefix>blocks_found` WHERE material_name in (<materials>) GROUP BY `player` ORDER BY `total` DESC LIMIT :limit OFFSET :offset";
	String QUERY_GET_TOP_PLAYERS_NUMBER = "SELECT count(DISTINCT `player`) FROM `<prefix>blocks_found` WHERE material_name in (<materials>)";
	String QUERY_GET_TOP_PLAYERS_LIST = "SELECT `player`, sum(`found`) AS `total` FROM `<prefix>blocks_found` WHERE material_name in (<materials>) GROUP BY `player` ORDER BY `total` DESC";
	String QUERY_GET_STATS_PLAYER = "SELECT `material_name`, sum(`found`) AS total FROM `<prefix>blocks_found` WHERE material_name in (<materials>) AND player = :player GROUP BY material_name";
	String QUERY_TOTAL = "SELECT sum(`found`) FROM `<prefix>blocks_found` WHERE material_name in (<materials>)";
	String QUERY_COUNT_ALL = "SELECT count(*) FROM `<prefix>blocks_found`";
	String QUERY_DELETE_ALL = "DELETE FROM `<prefix>blocks_found`";
	
	@SqlUpdate(QUERY_INSERT)
	void insert(String uuid, String materialName, long timestamp, int found);
	
	@SqlQuery(QUERY_GET_ALL)
	@RegisterRowMapper(BlocksFoundRowMapper.class)
	List<BlockFound> getAll(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery(QUERY_GET_ALL_BY_PLAYER)
	@RegisterRowMapper(BlocksFoundRowMapper.class)
	List<BlockFound> getAll(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials, @Bind("player") String uuid, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery(QUERY_GET_ALL_NUMBER)
	int getAllNumber(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials);
	
	@SqlQuery(QUERY_GET_ALL_NUMBER_BY_PLAYER)
	int getAllNumber(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials, @Bind("player") String uuid);
	
	@SqlQuery(QUERY_GET_ALL_MERGED)
	@RegisterRowMapper(BlocksFoundRowMapper.class)
	Set<BlockFound> getAllMerged(String uuid);
	
	@SqlQuery(QUERY_GET_LATEST_TOTAL)
	@RegisterRowMapper(BlocksFoundResultRowMapper.class)
	Optional<BlocksFoundResult> getLatestTotal(String uuid, long timestamp);
	
	@SqlQuery(QUERY_GET_LATEST_BLOCK)
	@RegisterRowMapper(BlocksFoundResultRowMapper.class)
	Optional<BlocksFoundResult> getLatestBlock(String uuid, long timestamp, String materialName);
	
	@SqlQuery(QUERY_GET_TOP_PLAYERS)
	@RegisterRowMapper(TopPlayerRowMapper.class)
	List<Pair<String, Integer>> getTopPlayers(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery(QUERY_GET_TOP_PLAYERS_NUMBER)
	int getTopPlayersNumber(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials);
	
	@SqlQuery(QUERY_GET_TOP_PLAYERS_LIST)
	@RegisterRowMapper(TopPlayerRowMapper.class)
	Stream<Pair<String, Integer>> getTopPlayersList(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials);
	
	@SqlQuery(QUERY_GET_STATS_PLAYER)
	@RegisterRowMapper(StatsPlayerRowMapper.class)
	List<Pair<String, Integer>> getStatsPlayer(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials, @Bind("player") String uuid);
	
	@SqlQuery(QUERY_TOTAL)
	int getTotal(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials);
	
	@SqlQuery(QUERY_COUNT_ALL)
	int countAll();
	
	@SqlUpdate(QUERY_DELETE_ALL)
	void deleteAll();
}
