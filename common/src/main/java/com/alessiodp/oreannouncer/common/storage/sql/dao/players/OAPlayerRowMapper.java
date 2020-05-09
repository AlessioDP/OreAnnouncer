package com.alessiodp.oreannouncer.common.storage.sql.dao.players;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class OAPlayerRowMapper implements RowMapper<OAPlayerImpl> {
	@Override
	public OAPlayerImpl map(ResultSet rs, StatementContext ctx) throws SQLException {
		OAPlayerImpl ret = ((OreAnnouncerPlugin) ADPPlugin.getInstance()).getPlayerManager().initializePlayer(UUID.fromString(rs.getString("uuid")));
		ret.setAccessible(true);
		ret.setAlertsOn(rs.getBoolean("alerts"));
		ret.setAccessible(false);
		return ret;
	}
}
