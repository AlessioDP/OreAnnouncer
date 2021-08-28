package com.alessiodp.oreannouncer.common.storage.sql.dao.blocks;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface SQLiteBlocksDao extends BlocksDao {
	String QUERY_UPDATE = "INSERT INTO `<prefix>blocks` (`player`, `material_name`, `destroyed`)" +
			" VALUES (?, ?, ?)" +
			" ON CONFLICT (`player`, `material_name`) DO" +
			" UPDATE SET destroyed = destroyed + excluded.destroyed";
	String QUERY_SET = "INSERT INTO `<prefix>blocks` (`player`, `material_name`, `destroyed`)" +
			" VALUES (?, ?, ?)" +
			" ON CONFLICT (`player`, `material_name`) DO" +
			" UPDATE SET destroyed = excluded.destroyed";
	
	@Override
	@SqlUpdate(QUERY_UPDATE)
	void update(String uuid, String materialName, int destroyed);
	
	@Override
	@SqlUpdate(QUERY_SET)
	void set(String uuid, String materialName, int destroyed);
}
