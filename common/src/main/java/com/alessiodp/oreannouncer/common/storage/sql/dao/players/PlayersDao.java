package com.alessiodp.oreannouncer.common.storage.sql.dao.players;

import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface PlayersDao {
	@SqlUpdate("INSERT INTO `<prefix>players` (`uuid`, `alerts`) VALUES (?, ?) ON DUPLICATE KEY UPDATE alerts=VALUES(alerts)")
	void insert(String uuid, boolean alerts); // MySQL syntax
	
	@SqlUpdate("DELETE FROM `<prefix>players` WHERE uuid = ?")
	void remove(String uuid);
	
	@SqlQuery("SELECT * FROM `<prefix>players` WHERE uuid = ?")
	@RegisterRowMapper(OAPlayerRowMapper.class)
	Optional<OAPlayerImpl> getPlayer(String uuid);
	
	@SqlQuery("SELECT count(*) FROM `<prefix>players`")
	int countAll();
}
