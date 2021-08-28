package com.alessiodp.oreannouncer.common.storage.sql.dao.players;

import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface PlayersDao {
	String QUERY_INSERT = "INSERT INTO `<prefix>players` (`uuid`, `alerts`, `whitelisted`) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE alerts=VALUES(alerts), whitelisted=VALUES(whitelisted)";
	String QUERY_REMOVE = "DELETE FROM `<prefix>players` WHERE uuid = ?";
	String QUERY_GET = "SELECT * FROM `<prefix>players` WHERE uuid = ?";
	String QUERY_COUNT_ALL = "SELECT count(*) FROM `<prefix>players`";
	String QUERY_DELETE_ALL = "DELETE FROM `<prefix>players`";
	
	@SqlUpdate(QUERY_INSERT)
	void insert(String uuid, boolean alerts, boolean whitelisted);
	
	@SqlUpdate(QUERY_REMOVE)
	void remove(String uuid);
	
	@SqlQuery(QUERY_GET)
	@RegisterRowMapper(OAPlayerRowMapper.class)
	Optional<OAPlayerImpl> getPlayer(String uuid);
	
	@SqlQuery(QUERY_COUNT_ALL)
	int countAll();
	
	@SqlUpdate(QUERY_DELETE_ALL)
	void deleteAll();
}
