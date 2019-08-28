package com.alessiodp.oreannouncer.common.configuration;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.ConfigurationManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.oreannouncer.common.commands.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;

public abstract class OAConfigurationManager extends ConfigurationManager {
	
	public OAConfigurationManager(ADPPlugin plugin) {
		super(plugin);
	}
	
	@Override
	protected void performChanges() {
		plugin.getDatabaseManager().setDatabaseType(StorageType.getEnum(ConfigMain.STORAGE_TYPE_DATABASE));
		plugin.getLoginAlertsManager().setPermission(OreAnnouncerPermission.ADMIN_WARNINGS);
		checkOutdatedConfigs(Messages.OREANNOUNCER_CONFIGURATION_OUTDATED);
	}
}
