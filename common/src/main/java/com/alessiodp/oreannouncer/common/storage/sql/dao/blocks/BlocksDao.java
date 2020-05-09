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

public interface BlocksDao {
	@SqlUpdate("INSERT INTO `<prefix>blocks` (`player`, `material_name`, `destroyed`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE destroyed=`destroyed` + destroyed")
	void update(String uuid, String materialName, int destroyed);
	
	@SqlUpdate("INSERT INTO `<prefix>blocks` (`player`, `material_name`, `destroyed`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE destroyed=destroyed")
	void set(String uuid, String materialName, int destroyed);
	
	@SqlQuery("SELECT * FROM `<prefix>blocks` WHERE player = ? AND material_name = ?")
	@RegisterRowMapper(BlockDestroyRowMapper.class)
	Optional<BlockDestroy> get(String uuid, String materialName);
	
	@SqlQuery("SELECT * FROM `<prefix>blocks` WHERE player = ?")
	@RegisterRowMapper(BlockDestroyRowMapper.class)
	Set<BlockDestroy> getAll(String uuid);
	
	@SqlQuery("SELECT `player`, sum(`destroyed`) AS `total` FROM `<prefix>blocks` WHERE material_name in (<materials>) GROUP BY `player` ORDER BY `total` DESC LIMIT :limit OFFSET :offset")
	@RegisterRowMapper(TopPlayerRowMapper.class)
	List<Pair<String, Integer>> getTopPlayers(@BindList("materials") List<String> materials, @Bind("limit") int limit, @Bind("offset") int offset);
	
	@SqlQuery("SELECT count(DISTINCT `player`) FROM `<prefix>blocks` WHERE material_name in (<materials>)")
	int getTopPlayersNumber(@BindList("materials") List<String> materials);
	
	@SqlQuery("SELECT `material_name`, `destroyed` AS total FROM `<prefix>blocks` WHERE material_name in (<materials>) AND player = :player")
	@RegisterRowMapper(StatsPlayerRowMapper.class)
	List<Pair<String, Integer>> getStatsPlayer(@BindList("materials") List<String> materials, @Bind("player") String uuid);
	
	@SqlQuery("SELECT count(*) FROM `<prefix>blocks`")
	int countAll();
}
