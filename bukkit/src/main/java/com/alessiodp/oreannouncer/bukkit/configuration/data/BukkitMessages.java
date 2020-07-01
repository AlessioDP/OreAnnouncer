package com.alessiodp.oreannouncer.bukkit.configuration.data;

import com.alessiodp.core.common.configuration.ConfigOption;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import lombok.Getter;

public class BukkitMessages extends Messages {
	@Getter private final String fileName = "messages.yml";
	@Getter private final String resourceName = "bukkit/messages.yml";
	@Getter private final int latestVersion = OAConstants.VERSION_BUKKIT_MESSAGES;
	
	// Alerts settings
	@ConfigOption(path = "alerts.discordsrv.text.normal.user")
	public static String		ALERTS_DISCORDSRV_TEXT_NORMAL_USER;
	@ConfigOption(path = "alerts.discordsrv.text.normal.admin")
	public static String		ALERTS_DISCORDSRV_TEXT_NORMAL_ADMIN;
	@ConfigOption(path = "alerts.discordsrv.text.count.user")
	public static String		ALERTS_DISCORDSRV_TEXT_COUNT_USER;
	@ConfigOption(path = "alerts.discordsrv.text.count.admin")
	public static String		ALERTS_DISCORDSRV_TEXT_COUNT_ADMIN;
	@ConfigOption(path = "alerts.discordsrv.text.tnt.user")
	public static String		ALERTS_DISCORDSRV_TEXT_TNT_USER;
	@ConfigOption(path = "alerts.discordsrv.text.tnt.admin")
	public static String		ALERTS_DISCORDSRV_TEXT_TNT_ADMIN;
	
	@ConfigOption(path = "alerts.discordsrv.embed.normal.user.color")
	public static String		ALERTS_DISCORDSRV_EMBED_NORMAL_USER_COLOR;
	@ConfigOption(path = "alerts.discordsrv.embed.normal.user.author-name")
	public static String		ALERTS_DISCORDSRV_EMBED_NORMAL_USER_AUTHOR_NAME;
	@ConfigOption(path = "alerts.discordsrv.embed.normal.user.title")
	public static String		ALERTS_DISCORDSRV_EMBED_NORMAL_USER_TITLE;
	@ConfigOption(path = "alerts.discordsrv.embed.normal.user.description")
	public static String		ALERTS_DISCORDSRV_EMBED_NORMAL_USER_DESCRIPTION;
	@ConfigOption(path = "alerts.discordsrv.embed.normal.user.footer")
	public static String		ALERTS_DISCORDSRV_EMBED_NORMAL_USER_FOOTER;
	@ConfigOption(path = "alerts.discordsrv.embed.normal.admin.color")
	public static String		ALERTS_DISCORDSRV_EMBED_NORMAL_ADMIN_COLOR;
	@ConfigOption(path = "alerts.discordsrv.embed.normal.admin.author-name")
	public static String		ALERTS_DISCORDSRV_EMBED_NORMAL_ADMIN_AUTHOR_NAME;
	@ConfigOption(path = "alerts.discordsrv.embed.normal.admin.title")
	public static String		ALERTS_DISCORDSRV_EMBED_NORMAL_ADMIN_TITLE;
	@ConfigOption(path = "alerts.discordsrv.embed.normal.admin.description")
	public static String		ALERTS_DISCORDSRV_EMBED_NORMAL_ADMIN_DESCRIPTION;
	@ConfigOption(path = "alerts.discordsrv.embed.normal.admin.footer")
	public static String		ALERTS_DISCORDSRV_EMBED_NORMAL_ADMIN_FOOTER;
	
	@ConfigOption(path = "alerts.discordsrv.embed.count.user.color")
	public static String		ALERTS_DISCORDSRV_EMBED_COUNT_USER_COLOR;
	@ConfigOption(path = "alerts.discordsrv.embed.count.user.author-name")
	public static String		ALERTS_DISCORDSRV_EMBED_COUNT_USER_AUTHOR_NAME;
	@ConfigOption(path = "alerts.discordsrv.embed.count.user.title")
	public static String		ALERTS_DISCORDSRV_EMBED_COUNT_USER_TITLE;
	@ConfigOption(path = "alerts.discordsrv.embed.count.user.description")
	public static String		ALERTS_DISCORDSRV_EMBED_COUNT_USER_DESCRIPTION;
	@ConfigOption(path = "alerts.discordsrv.embed.count.user.footer")
	public static String		ALERTS_DISCORDSRV_EMBED_COUNT_USER_FOOTER;
	@ConfigOption(path = "alerts.discordsrv.embed.count.admin.color")
	public static String		ALERTS_DISCORDSRV_EMBED_COUNT_ADMIN_COLOR;
	@ConfigOption(path = "alerts.discordsrv.embed.count.admin.author-name")
	public static String		ALERTS_DISCORDSRV_EMBED_COUNT_ADMIN_AUTHOR_NAME;
	@ConfigOption(path = "alerts.discordsrv.embed.count.admin.title")
	public static String		ALERTS_DISCORDSRV_EMBED_COUNT_ADMIN_TITLE;
	@ConfigOption(path = "alerts.discordsrv.embed.count.admin.description")
	public static String		ALERTS_DISCORDSRV_EMBED_COUNT_ADMIN_DESCRIPTION;
	@ConfigOption(path = "alerts.discordsrv.embed.count.admin.footer")
	public static String		ALERTS_DISCORDSRV_EMBED_COUNT_ADMIN_FOOTER;
	
	@ConfigOption(path = "alerts.discordsrv.embed.tnt.user.color")
	public static String		ALERTS_DISCORDSRV_EMBED_TNT_USER_COLOR;
	@ConfigOption(path = "alerts.discordsrv.embed.tnt.user.author-name")
	public static String		ALERTS_DISCORDSRV_EMBED_TNT_USER_AUTHOR_NAME;
	@ConfigOption(path = "alerts.discordsrv.embed.tnt.user.title")
	public static String		ALERTS_DISCORDSRV_EMBED_TNT_USER_TITLE;
	@ConfigOption(path = "alerts.discordsrv.embed.tnt.user.description")
	public static String		ALERTS_DISCORDSRV_EMBED_TNT_USER_DESCRIPTION;
	@ConfigOption(path = "alerts.discordsrv.embed.tnt.user.footer")
	public static String		ALERTS_DISCORDSRV_EMBED_TNT_USER_FOOTER;
	@ConfigOption(path = "alerts.discordsrv.embed.tnt.admin.color")
	public static String		ALERTS_DISCORDSRV_EMBED_TNT_ADMIN_COLOR;
	@ConfigOption(path = "alerts.discordsrv.embed.tnt.admin.author-name")
	public static String		ALERTS_DISCORDSRV_EMBED_TNT_ADMIN_AUTHOR_NAME;
	@ConfigOption(path = "alerts.discordsrv.embed.tnt.admin.title")
	public static String		ALERTS_DISCORDSRV_EMBED_TNT_ADMIN_TITLE;
	@ConfigOption(path = "alerts.discordsrv.embed.tnt.admin.description")
	public static String		ALERTS_DISCORDSRV_EMBED_TNT_ADMIN_DESCRIPTION;
	@ConfigOption(path = "alerts.discordsrv.embed.tnt.admin.footer")
	public static String		ALERTS_DISCORDSRV_EMBED_TNT_ADMIN_FOOTER;
	
	public BukkitMessages(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
}