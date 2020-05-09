package com.alessiodp.oreannouncer.common.storage.sql.dao.players;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface H2PlayersDao extends PlayersDao {
	@Override
	@SqlUpdate("MERGE INTO `<prefix>players` (`uuid`, `alerts`) VALUES (?, ?)")
	void insert(String uuid, boolean alerts);
}
