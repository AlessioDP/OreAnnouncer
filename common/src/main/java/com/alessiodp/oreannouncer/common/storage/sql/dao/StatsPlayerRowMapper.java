package com.alessiodp.oreannouncer.common.storage.sql.dao;

import com.alessiodp.core.common.utils.Pair;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsPlayerRowMapper implements RowMapper<Pair<String, Integer>> {
	public Pair<String, Integer> map(ResultSet rs, StatementContext ctx) throws SQLException {
		return new Pair<>(rs.getString("material_name"), rs.getInt("total"));
	}
}
