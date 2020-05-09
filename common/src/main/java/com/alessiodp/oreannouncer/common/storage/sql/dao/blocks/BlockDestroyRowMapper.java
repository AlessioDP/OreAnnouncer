package com.alessiodp.oreannouncer.common.storage.sql.dao.blocks;

import com.alessiodp.oreannouncer.common.blocks.objects.BlockDestroy;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BlockDestroyRowMapper implements RowMapper<BlockDestroy> {
	@Override
	public BlockDestroy map(ResultSet rs, StatementContext ctx) throws SQLException {
		return new BlockDestroy(
				UUID.fromString(rs.getString("player")),
				rs.getString("material_name"),
				rs.getInt("destroyed")
		);
	}
}
