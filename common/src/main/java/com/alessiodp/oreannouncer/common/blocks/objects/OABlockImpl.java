package com.alessiodp.oreannouncer.common.blocks.objects;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.common.configuration.OAConfigurationManager;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.locks.ReentrantLock;

@EqualsAndHashCode
public class OABlockImpl implements OABlock {
	@EqualsAndHashCode.Exclude private final ADPPlugin plugin;
	
	// Interface fields
	@Getter private final String materialName;
	@Getter private boolean enabled;
	@Getter private boolean alertingUsers;
	@Getter private boolean alertingAdmins;
	@Getter private String singularName;
	@Getter private String pluralName;
	@Getter private int countNumber;
	@Getter private int countTime;
	@Getter private String countTimeFormat;
	@Getter private String messageUser;
	@Getter private String messageAdmin;
	@Getter private String messageConsole;
	@Getter private String countMessageUser;
	@Getter private String countMessageAdmin;
	@Getter private String countMessageConsole;
	@Getter private String sound;
	@Getter private int lightLevel;
	@Getter private boolean countingOnDestroy;
	private boolean tntEnabled;
	
	@EqualsAndHashCode.Exclude private final ReentrantLock lock = new ReentrantLock();
	@EqualsAndHashCode.Exclude private boolean accessible = false;
	
	public OABlockImpl(@NonNull ADPPlugin plugin, @NonNull String materialName) {
		this.plugin = plugin;
		this.materialName = materialName;
		this.enabled = true;
		this.alertingUsers = false;
		this.alertingAdmins = false;
		this.singularName = "";
		this.pluralName = "";
		this.countNumber = 0;
		this.countTime = 0;
		this.countTimeFormat = null;
		this.messageUser = null;
		this.messageAdmin = null;
		this.messageConsole = null;
		this.countMessageUser = null;
		this.countMessageAdmin = null;
		this.countMessageConsole = null;
		this.sound = null;
		this.lightLevel = 15;
		this.countingOnDestroy = false;
		this.tntEnabled = true;
	}
	
	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}
	
	public void updateBlock() {
		if (!singularName.isEmpty() && !pluralName.isEmpty()) {
			((OAConfigurationManager) plugin.getConfigurationManager()).getBlocks().updateBlock(this);
		}
	}
	
	public void removeBlock() {
		((OAConfigurationManager) plugin.getConfigurationManager()).getBlocks().removeBlock(this);
	}
	
	private void updateValue(Runnable runnable) {
		if (accessible) {
			runnable.run();
		} else {
			lock.lock();
			runnable.run();
			updateBlock();
			lock.unlock();
		}
	}
	
	@Override
	public void setEnabled(boolean enable) {
		updateValue(() -> {
			this.enabled = enable;
		});
	}
	
	@Override
	public void setAlertingUsers(boolean alertingUsers) {
		updateValue(() -> {
			this.alertingUsers = alertingUsers;
		});
	}
	
	@Override
	public void setAlertingAdmins(boolean alertingAdmins) {
		updateValue(() -> {
			this.alertingAdmins = alertingAdmins;
		});
	}
	
	@Override
	public void setSingularName(String singularName) {
		updateValue(() -> {
			this.singularName = singularName;
		});
	}
	
	@Override
	public void setPluralName(String pluralName) {
		updateValue(() -> {
			this.pluralName = pluralName;
		});
	}
	
	@Override
	public void setCountNumber(int countNumber) {
		updateValue(() -> {
			this.countNumber = countNumber;
		});
	}
	
	@Override
	public void setCountTime(int countTime) {
		updateValue(() -> {
			this.countTime = countTime;
		});
	}
	
	@Override
	public void setCountTimeFormat(String countTimeFormat) {
		updateValue(() -> {
			this.countTimeFormat = countTimeFormat;
		});
	}
	
	@Override
	public void setMessageUser(String messageUser) {
		updateValue(() -> {
			this.messageUser = messageUser;
		});
	}
	
	@Override
	public void setMessageAdmin(String messageAdmin) {
		updateValue(() -> {
			this.messageAdmin = messageAdmin;
		});
	}
	
	@Override
	public void setMessageConsole(String messageConsole) {
		updateValue(() -> {
			this.messageConsole = messageConsole;
		});
	}
	
	@Override
	public void setCountMessageUser(String countMessageUser) {
		updateValue(() -> {
			this.countMessageUser = countMessageUser;
		});
	}
	
	@Override
	public void setCountMessageAdmin(String countMessageAdmin) {
		updateValue(() -> {
			this.countMessageAdmin = countMessageAdmin;
		});
	}
	
	@Override
	public void setCountMessageConsole(String countMessageConsole) {
		updateValue(() -> {
			this.countMessageConsole = countMessageConsole;
		});
	}
	
	@Override
	public void setSound(String sound) {
		updateValue(() -> {
			this.sound = sound;
		});
	}
	
	@Override
	public void setLightLevel(int lightLevel) {
		updateValue(() -> {
			this.lightLevel = lightLevel;
		});
	}
	
	@Override
	public void setCountingOnDestroy(boolean countingOnDestroy) {
		updateValue(() -> {
			this.countingOnDestroy = countingOnDestroy;
		});
	}
	
	@Override
	public void setTNTEnabled(boolean tntEnabled) {
		updateValue(() -> {
			this.tntEnabled = tntEnabled;
		});
	}
	
	@Override
	public boolean isTNTEnabled() {
		return tntEnabled;
	}
}
