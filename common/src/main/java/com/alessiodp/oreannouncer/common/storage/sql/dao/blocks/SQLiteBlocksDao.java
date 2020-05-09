package com.alessiodp.oreannouncer.common.storage.sql.dao.blocks;

import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface SQLiteBlocksDao extends BlocksDao {
	@Override
	@SqlUpdate("INSERT INTO `<prefix>blocks` (`player`, `material_name`, `destroyed`) VALUES (?, ?, ?) " +
			"ON CONFLICT (`player`, `material_name`) DO " +
			"UPDATE SET destroyed = destroyed + excluded.destroyed")
	void update(String uuid, String materialName, int destroyed);
	
	@Override
	@SqlUpdate("INSERT INTO `<prefix>blocks` (`player`, `material_name`, `destroyed`) VALUES (?, ?, ?) " +
			"ON CONFLICT (`player`, `material_name`) DO " +
			"UPDATE SET destroyed = excluded.destroyed")
	void set(String uuid, String materialName, int destroyed);
}
