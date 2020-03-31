package com.alessiodp.oreannouncer.common.configuration;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.core.common.configuration.ConfigurationManager;
import com.alessiodp.core.common.storage.StorageType;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.utils.OreAnnouncerPermission;
import com.alessiodp.oreannouncer.common.configuration.data.Blocks;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;

public abstract class OAConfigurationManager extends ConfigurationManager {
	public OAConfigurationManager(ADPPlugin plugin) {
		super(plugin);
		
		getConfigs().add(new Blocks((OreAnnouncerPlugin) plugin));
	}
	
	@Override
	protected void performChanges() {
		plugin.getDatabaseManager().setDatabaseType(StorageType.getEnum(ConfigMain.STORAGE_TYPE_DATABASE));
		plugin.getLoginAlertsManager().setPermission(OreAnnouncerPermission.ADMIN_WARNINGS);
		checkOutdatedConfigs(Messages.OREANNOUNCER_CONFIGURATION_OUTDATED);
	}
	
	public Blocks getBlocks() {
		for (ConfigurationFile cf : getConfigs()) {
			if (cf instanceof Blocks)
				return (Blocks) cf;
		}
		throw new IllegalStateException("No Blocks configuration file found");
	}
	
	public ConfigMain getConfigMain() {
		for (ConfigurationFile cf : getConfigs()) {
			if (cf instanceof ConfigMain)
				return (ConfigMain) cf;
		}
		throw new IllegalStateException("No ConfigMain configuration file found");
	}
	
	public Messages getMessages() {
		for (ConfigurationFile cf : getConfigs()) {
			if (cf instanceof Messages)
				return (Messages) cf;
		}
		throw new IllegalStateException("No Messages configuration file found");
	}
}
