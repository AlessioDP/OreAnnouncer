package com.alessiodp.oreannouncer.common.storage.sql.dao.blocks;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface H2BlocksDao extends BlocksDao {
	String QUERY_UPDATE = "MERGE INTO `<prefix>blocks`" +
			" USING DUAL ON player = :player AND material_name = :material_name" +
			" WHEN MATCHED THEN UPDATE SET `destroyed`=`destroyed` + :destroyed" +
			" WHEN NOT MATCHED THEN INSERT VALUES (:player, :material_name, :destroyed)";
	String QUERY_SET = "MERGE INTO `<prefix>blocks` (`player`, `material_name`, `destroyed`) VALUES (?, ?, ?)";
	
	@Override
	@SqlUpdate(QUERY_UPDATE)
	void update(@Bind("player") String uuid, @Bind("material_name") String materialName, @Bind("destroyed") int destroyed);
	
	@Override
	@SqlUpdate(QUERY_SET)
	void set(String uuid, String materialName, int destroyed);
}
