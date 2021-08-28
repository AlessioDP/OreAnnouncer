package com.alessiodp.oreannouncer.common.storage.sql.dao.players;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface H2PlayersDao extends PlayersDao {
	String QUERY_INSERT = "MERGE INTO `<prefix>players` (`uuid`, `alerts`, `whitelisted`) VALUES (?, ?, ?)";
	
	@Override
	@SqlUpdate(QUERY_INSERT)
	void insert(String uuid, boolean alerts, boolean whitelisted);
}
