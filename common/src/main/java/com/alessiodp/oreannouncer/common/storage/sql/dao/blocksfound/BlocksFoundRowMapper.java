package com.alessiodp.oreannouncer.common.storage.sql.dao.blocksfound;

import com.alessiodp.oreannouncer.common.blocks.objects.BlockFound;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BlocksFoundRowMapper implements RowMapper<BlockFound> {
	@Override
	public BlockFound map(ResultSet rs, StatementContext ctx) throws SQLException {
		return new BlockFound(
				UUID.fromString(rs.getString("player")),
				rs.getString("material_name"),
				rs.getInt("timestamp"),
				rs.getInt("found")
		);
	}
}
