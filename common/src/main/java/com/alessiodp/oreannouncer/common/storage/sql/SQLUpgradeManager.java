package com.alessiodp.oreannouncer.common.storage.sql;

import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.core.common.storage.dispatchers.SQLDispatcher;
import com.alessiodp.core.common.storage.interfaces.ISQLUpgradeManager;
import com.alessiodp.core.common.storage.sql.ISQLTable;

import java.sql.Connection;

public class SQLUpgradeManager implements ISQLUpgradeManager {
	@Override
	public void checkUpgrades(SQLDispatcher sqlDispatcher, Connection connection, ISQLTable isqlTable, StorageType storageType) {
		// Still not necessary
	}
}
