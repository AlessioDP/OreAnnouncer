package com.alessiodp.oreannouncer.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.oreannouncer.bukkit.addons.external.hooks.DiscordSRVHook;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.blocks.BlockManager;
import com.alessiodp.oreannouncer.common.blocks.objects.BlockData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public class DiscordSRVHandler {
	private final OreAnnouncerPlugin plugin;
	private static final String ADDON_NAME = "DiscordSRV";
	private static boolean active;
	private static DiscordSRVHook hook;
	
	public void init() {
		active = false;
		if (BukkitConfigMain.ALERTS_DISCORDSRV_ENABLE
				&& Bukkit.getPluginManager().isPluginEnabled(ADDON_NAME)) {
			hook = new DiscordSRVHook(plugin);
			if (hook.register()) {
				active = true;
				
				plugin.getLoggerManager().log(Constants.DEBUG_ADDON_HOOKED
						.replace("{addon}", ADDON_NAME), true);
			}
		}
	}
	
	
	public static void dispatchAlerts(BlockData data, BlockManager.AlertType alertType) {
		if (active && data != null) {
			String channelUser = CommonUtils.getNoEmptyOr(BukkitConfigMain.ALERTS_DISCORDSRV_CHANNELS_USER, null);
			String channelAdmin = CommonUtils.getNoEmptyOr(BukkitConfigMain.ALERTS_DISCORDSRV_CHANNELS_ADMIN, null);
			
			if (BukkitConfigMain.ALERTS_DISCORDSRV_MESSAGE_FORMAT.equalsIgnoreCase("embed")) {
				// Embed
				if (channelUser != null) {
					hook.sendMessageEmbed(
							data.getPlayer(),
							(msg) -> ((OreAnnouncerPlugin) OreAnnouncerPlugin.getInstance()).getBlockManager().parseMessage(msg, data, BlockManager.AlerterType.USER),
							channelUser,
							parseEmbed(BlockManager.AlerterType.USER, alertType)
					);
				}
				
				if (channelAdmin != null) {
					hook.sendMessageEmbed(
							data.getPlayer(),
							(msg) -> ((OreAnnouncerPlugin) OreAnnouncerPlugin.getInstance()).getBlockManager().parseMessage(msg, data, BlockManager.AlerterType.ADMIN),
							channelAdmin,
							parseEmbed(BlockManager.AlerterType.ADMIN, alertType)
					);
				}
			} else {
				// Text
				if (channelUser != null) {
					hook.sendMessageText(
							(msg) -> ((OreAnnouncerPlugin) OreAnnouncerPlugin.getInstance()).getBlockManager().parseMessage(msg, data, BlockManager.AlerterType.USER),
							channelUser,
							parseText(BlockManager.AlerterType.USER, alertType)
					);
				}
				
				if (channelAdmin != null) {
					hook.sendMessageText(
							(msg) -> ((OreAnnouncerPlugin) OreAnnouncerPlugin.getInstance()).getBlockManager().parseMessage(msg, data, BlockManager.AlerterType.ADMIN),
							channelAdmin,
							parseText(BlockManager.AlerterType.ADMIN, alertType)
					);
				}
			}
		}
	}
	
	private static DiscordSRVAlertEmbed parseEmbed(BlockManager.AlerterType alerterType, BlockManager.AlertType alertType) {
		switch (alertType) {
			case NORMAL:
				if (alerterType == BlockManager.AlerterType.USER)
					return new DiscordSRVAlertEmbed(
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_NORMAL_USER_COLOR,
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_NORMAL_USER_AUTHOR_NAME,
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_NORMAL_USER_TITLE,
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_NORMAL_USER_DESCRIPTION,
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_NORMAL_USER_FOOTER
					);
				return new DiscordSRVAlertEmbed(
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_NORMAL_ADMIN_COLOR,
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_NORMAL_ADMIN_AUTHOR_NAME,
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_NORMAL_ADMIN_TITLE,
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_NORMAL_ADMIN_DESCRIPTION,
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_NORMAL_ADMIN_FOOTER
				);
			case COUNT:
				if (alerterType == BlockManager.AlerterType.USER)
					return new DiscordSRVAlertEmbed(
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_COUNT_USER_COLOR,
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_COUNT_USER_AUTHOR_NAME,
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_COUNT_USER_TITLE,
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_COUNT_USER_DESCRIPTION,
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_COUNT_USER_FOOTER
					);
				return new DiscordSRVAlertEmbed(
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_COUNT_ADMIN_COLOR,
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_COUNT_ADMIN_AUTHOR_NAME,
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_COUNT_ADMIN_TITLE,
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_COUNT_ADMIN_DESCRIPTION,
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_COUNT_ADMIN_FOOTER
				);
			case TNT:
				if (alerterType == BlockManager.AlerterType.USER)
					return new DiscordSRVAlertEmbed(
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_TNT_USER_COLOR,
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_TNT_USER_AUTHOR_NAME,
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_TNT_USER_TITLE,
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_TNT_USER_DESCRIPTION,
							BukkitMessages.ALERTS_DISCORDSRV_EMBED_TNT_USER_FOOTER
					);
				return new DiscordSRVAlertEmbed(
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_TNT_ADMIN_COLOR,
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_TNT_ADMIN_AUTHOR_NAME,
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_TNT_ADMIN_TITLE,
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_TNT_ADMIN_DESCRIPTION,
						BukkitMessages.ALERTS_DISCORDSRV_EMBED_TNT_ADMIN_FOOTER
				);
			default:
				return null;
		}
	}
	
	private static String parseText(BlockManager.AlerterType alerterType, BlockManager.AlertType alertType) {
		switch (alertType) {
			case NORMAL:
				if (alerterType == BlockManager.AlerterType.USER)
					return BukkitMessages.ALERTS_DISCORDSRV_TEXT_NORMAL_USER;
				return BukkitMessages.ALERTS_DISCORDSRV_TEXT_NORMAL_ADMIN;
			case COUNT:
				if (alerterType == BlockManager.AlerterType.USER)
					return BukkitMessages.ALERTS_DISCORDSRV_TEXT_COUNT_USER;
				return BukkitMessages.ALERTS_DISCORDSRV_TEXT_COUNT_ADMIN;
			case TNT:
				if (alerterType == BlockManager.AlerterType.USER)
					return BukkitMessages.ALERTS_DISCORDSRV_TEXT_TNT_USER;
				return BukkitMessages.ALERTS_DISCORDSRV_TEXT_TNT_ADMIN;
			default:
				return null;
		}
	}
	
	@AllArgsConstructor
	@Getter
	public static class DiscordSRVAlertEmbed {
		private final String color;
		private final String authorName;
		private final String title;
		private final String description;
		private final String footer;
	}
}
