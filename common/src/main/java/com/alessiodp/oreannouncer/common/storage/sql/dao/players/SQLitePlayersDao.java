package com.alessiodp.oreannouncer.common.storage.sql.dao.players;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface SQLitePlayersDao extends PlayersDao {
	@Override
	@SqlUpdate("INSERT OR REPLACE INTO `<prefix>players` (`uuid`, `alerts`) VALUES (?, ?)")
	void insert(String uuid, boolean alerts);
}
