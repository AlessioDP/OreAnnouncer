package com.alessiodp.oreannouncer.common.storage.sql.dao.blocks;

import com.alessiodp.core.common.utils.Pair;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockDestroy;
import com.alessiodp.oreannouncer.common.storage.sql.dao.StatsPlayerRowMapper;
import com.alessiodp.oreannouncer.common.storage.sql.dao.TopPlayerRowMapper;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface BlocksDao {
	String QUERY_UPDATE = "INSERT INTO `<prefix>blocks` (`player`, `material_name`, `destroyed`)" +
			" VALUES (?, ?, ?)" +
			" ON DUPLICATE KEY" +
			" UPDATE destroyed=`destroyed` + VALUES(`destroyed`)";
	String QUERY_SET = "INSERT INTO `<prefix>blocks` (`player`, `material_name`, `destroyed`)" +
			" VALUES (?, ?, ?)" +
			" ON DUPLICATE KEY" +
			" UPDATE destroyed=VALUES(`destroyed`)";
	String QUERY_GET = "SELECT * FROM `<prefix>blocks` WHERE player = ? AND material_name = ?";
	String QUERY_GET_ALL = "SELECT * FROM `<prefix>blocks` WHERE player = ?";
	String QUERY_GET_TOP_PLAYERS = "SELECT `player`, sum(`destroyed`) AS `total` FROM `<prefix>blocks` WHERE material_name in (<materials>) GROUP BY `player` ORDER BY `total` DESC LIMIT :limit OFFSET :offset";
	String QUERY_GET_TOP_PLAYERS_NUMBER = "SELECT count(DISTINCT `player`) FROM `<prefix>blocks` WHERE material_name in (<materials>)";
	String QUERY_GET_TOP_PLAYERS_LIST = "SELECT `player`, sum(`destroyed`) AS `total` FROM `<prefix>blocks` WHERE material_name in (<materials>) GROUP BY `player` ORDER BY `total` DESC";
	String QUERY_GET_STATS = "SELECT `material_name`, `destroyed` AS total FROM `<prefix>blocks` WHERE material_name in (<materials>) AND player = :player";
	String QUERY_TOTAL = "SELECT sum(`destroyed`) FROM `<prefix>blocks` WHERE material_name in (<materials>)";
	String QUERY_COUNT_ALL = "SELECT count(*) FROM `<prefix>blocks`";
	String QUERY_DELETE_ALL = "DELETE FROM `<prefix>blocks`";
	
	@SqlUpdate(QUERY_UPDATE)
	void update(String uuid, String materialName, int destroyed);
	
	@SqlUpdate(QUERY_SET)
	void set(String uuid, String materialName, int destroyed);
	
	@SqlQuery(QUERY_GET)
	@RegisterRowMapper(BlockDestroyRowMapper.class)
	Optional<BlockDestroy> get(String uuid, String materialName);
	
	@SqlQuery(QUERY_GET_ALL)
	@RegisterRowMapper(BlockDestroyRowMapper.class)
	Set<BlockDestroy> getAll(String uuid);
	
	@SqlQuery(QUERY_GET_TOP_PLAYERS)
	@RegisterRowMapper(TopPlayerRowMapper.class)
	List<Pair<String, Integer>> getTopPlayers(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery(QUERY_GET_TOP_PLAYERS_NUMBER)
	int getTopPlayersNumber(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials);
	
	@SqlQuery(QUERY_GET_TOP_PLAYERS_LIST)
	@RegisterRowMapper(TopPlayerRowMapper.class)
	Stream<Pair<String, Integer>> getTopPlayersList(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials);
	
	@SqlQuery(QUERY_GET_STATS)
	@RegisterRowMapper(StatsPlayerRowMapper.class)
	List<Pair<String, Integer>> getStatsPlayer(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials, @Bind("player") String uuid);
	
	@SqlQuery(QUERY_TOTAL)
	int getTotal(@BindList(value = "materials", onEmpty = BindList.EmptyHandling.NULL_VALUE) List<String> materials);
	
	@SqlQuery(QUERY_COUNT_ALL)
	int countAll();
	
	@SqlUpdate(QUERY_DELETE_ALL)
	void deleteAll();
}
