package com.alessiodp.oreannouncer.common.storage.sql.dao.blocksfound;

import com.alessiodp.oreannouncer.common.utils.BlocksFoundResult;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BlocksFoundResultRowMapper implements RowMapper<BlocksFoundResult> {
	@Override
	public BlocksFoundResult map(ResultSet rs, StatementContext ctx) throws SQLException {
		return new BlocksFoundResult(rs.getLong("time"), rs.getInt("total"));
	}
}
