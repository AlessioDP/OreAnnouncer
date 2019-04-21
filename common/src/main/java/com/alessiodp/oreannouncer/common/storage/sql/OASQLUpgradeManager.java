package com.alessiodp.oreannouncer.common.storage.sql;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.dispatchers.SQLDispatcher;
import com.alessiodp.core.common.storage.sql.ISQLTable;
import com.alessiodp.core.common.storage.sql.mysql.SQLUpgradeManager;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OASQLUpgradeManager extends SQLUpgradeManager {
	
	public OASQLUpgradeManager(@NonNull ADPPlugin plugin, @NonNull SQLDispatcher dispatcher, @NonNull StorageType storageType) {
		super(plugin, dispatcher, storageType, ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_UPGRADE_SAVEOLD, ConfigMain.STORAGE_SETTINGS_GENERAL_SQL_UPGRADE_OLDSUFFIX);
	}
	
	@Override
	protected void upgradeTable(Connection connection, ResultSet rs, ISQLTable table, int currentVersion) throws SQLException {
		switch ((SQLTable) table) {
			case PLAYERS:
				// Parties
				upgradeTablePlayers(connection, rs, currentVersion);
				break;
			case BLOCKS:
				// Players
				upgradeTableBlocks(connection, rs, currentVersion);
				break;
			default:
				// Nothing to upgrade
		}
	}
	
	@Override
	protected boolean isVersionTable(ISQLTable table) {
		return table.equals(SQLTable.VERSIONS);
	}
	
	@Override
	protected String formatQuery(String query) {
		return SQLTable.formatGenericQuery(query);
	}
	
	private void upgradeTablePlayers(Connection connection, ResultSet rs, int currentVersion) throws SQLException {
		if (currentVersion == 1) {
			String query = storageType.equals(StorageType.MYSQL) ? OAConstants.QUERY_PLAYER_INSERT_MYSQL : OAConstants.QUERY_PLAYER_INSERT_SQLITE;
			while (rs.next()) {
				try (PreparedStatement preStatement = connection.prepareStatement(formatQuery(query))) {
					preStatement.setString(1, rs.getString("uuid"));
					// Removed name column
					preStatement.setInt(2, rs.getInt("alerts"));
					preStatement.executeUpdate();
				}
			}
		}
	}
	
	private void upgradeTableBlocks(Connection connection, ResultSet rs, int currentVersion) {
		// Nothing to upgrade
	}
}
