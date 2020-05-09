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

public interface BlocksFoundDao {
	@SqlUpdate("INSERT INTO `<prefix>blocks_found` (`player`, `material_name`, `timestamp`, `found`) VALUES (?, ?, ?, ?)")
	void insert(String uuid, String materialName, long timestamp, int found);
	
	@SqlQuery("SELECT * FROM `<prefix>blocks_found` WHERE material_name in (<materials>) ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(BlocksFoundRowMapper.class)
	List<BlockFound> getAll(@BindList("materials") List<String> materials, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery("SELECT * FROM `<prefix>blocks_found` WHERE material_name in (<materials>) AND player = :player ORDER BY timestamp DESC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(BlocksFoundRowMapper.class)
	List<BlockFound> getAll(@BindList("materials") List<String> materials, @Bind("player") String uuid, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery("SELECT count(*) FROM `<prefix>blocks_found` WHERE material_name in (<materials>)")
	int getAllNumber(@BindList("materials") List<String> materials);
	
	@SqlQuery("SELECT count(*) FROM `<prefix>blocks_found` WHERE material_name in (<materials>) AND player = :player")
	int getAllNumber(@BindList("materials") List<String> materials, @Bind("player") String uuid);
	
	@SqlQuery("SELECT `player`, `material_name`, sum(`found`) as `found`, 0 as `timestamp` FROM `<prefix>blocks_found` WHERE player = ? GROUP BY `player`, `material_name`")
	@RegisterRowMapper(BlocksFoundRowMapper.class)
	Set<BlockFound> getAllMerged(String uuid);
	
	@SqlQuery("SELECT min(`timestamp`) AS time, sum(`found`) AS total FROM `<prefix>blocks_found` WHERE player = ? AND timestamp >= ?")
	@RegisterRowMapper(BlocksFoundResultRowMapper.class)
	Optional<BlocksFoundResult> getLatestTotal(String uuid, long timestamp);
	
	@SqlQuery("SELECT min(`timestamp`) AS time, sum(`found`) AS total FROM `<prefix>blocks_found` WHERE player = ? AND timestamp >= ? AND material_name = ?")
	@RegisterRowMapper(BlocksFoundResultRowMapper.class)
	Optional<BlocksFoundResult> getLatestBlock(String uuid, long timestamp, String materialName);
	
	@SqlQuery("SELECT `player`, sum(`found`) AS `total` FROM `<prefix>blocks_found` WHERE material_name in (<materials>) GROUP BY `player` ORDER BY `total` DESC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(TopPlayerRowMapper.class)
	List<Pair<String, Integer>> getTopPlayers(@BindList("materials") List<String> materials, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery("SELECT count(DISTINCT `player`) FROM `<prefix>blocks_found` WHERE material_name in (<materials>)")
	int getTopPlayersNumber(@BindList("materials") List<String> materials);
	
	@SqlQuery("SELECT `material_name`, sum(`found`) AS total FROM `<prefix>blocks_found` WHERE material_name in (<materials>) AND player = :player GROUP BY material_name")
	@RegisterRowMapper(StatsPlayerRowMapper.class)
	List<Pair<String, Integer>> getStatsPlayer(@BindList("materials") List<String> materials, @Bind("player") String uuid);
	
	@SqlQuery("SELECT count(*) FROM `<prefix>blocks_found`")
	int countAll();
}
