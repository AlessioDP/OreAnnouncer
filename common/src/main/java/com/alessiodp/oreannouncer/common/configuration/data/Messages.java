package com.alessiodp.oreannouncer.common.configuration.data;

import com.alessiodp.core.common.configuration.ConfigurationFile;
import com.alessiodp.core.common.configuration.adapter.ConfigurationAdapter;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;

import java.util.ArrayList;
import java.util.List;

public abstract class Messages extends ConfigurationFile {
	// OreAnnouncer messages
	public static String OREANNOUNCER_UPDATEAVAILABLE;
	public static String OREANNOUNCER_NOPERMISSION;
	
	public static String OREANNOUNCER_COMMON_INVALIDCMD;
	public static String OREANNOUNCER_COMMON_CONFIGRELOAD;
	
	// Event messages
	public static String ALERTS_USER;
	public static String ALERTS_ADMIN;
	public static String ALERTS_CONSOLE;
	
	
	// Command messages
	public static String CMD_ALERTS_TOGGLEON;
	public static String CMD_ALERTS_TOGGLEOFF;
	public static String CMD_ALERTS_WRONGCMD;
	
	public static List<String> CMD_STATS_CONTENT;
	public static String CMD_STATS_PLAYERNOTFOUND;
	
	public static String CMD_TOP_HEADER;
	public static String CMD_TOP_FOOTER;
	public static String CMD_TOP_NOONE;
	public static String CMD_TOP_FORMATPLAYER;
	public static String CMD_TOP_WRONGCMD;
	
	public static String CMD_VERSION_UPDATED;
	public static String CMD_VERSION_OUTDATED;
	
	
	// Help messages
	public static String HELP_HEADER;
	public static String HELP_FOOTER;
	public static List<String> HELP_CONSOLEHELP;
	
	public static String HELP_CMD_HELP;
	public static String HELP_CMD_ALERTS;
	public static String HELP_CMD_RELOAD;
	public static String HELP_CMD_STATS;
	public static String HELP_CMD_TOP;
	public static String HELP_CMD_VERSION;
	
	protected Messages(OreAnnouncerPlugin plugin) {
		super(plugin);
	}
	
	public void loadDefaults() {
		// OreAnnouncer messages
		OREANNOUNCER_UPDATEAVAILABLE = "&2New version of OreAnnouncer found: %version% (Current: %thisversion%)";
		OREANNOUNCER_NOPERMISSION = "&cYou do not have access to that command";
		
		OREANNOUNCER_COMMON_INVALIDCMD = "&cInvalid command";
		OREANNOUNCER_COMMON_CONFIGRELOAD = "&aConfiguration reloaded";
		
		
		// Event messages
		ALERTS_USER = "&6%player% &efound %number% %block%!";
		ALERTS_ADMIN = "&6%player% &efound %number% %block%! %coordinates%";
		ALERTS_CONSOLE = "%player% found %number% %block%! %coordinates%";
		
		
		// Command messages
		CMD_ALERTS_TOGGLEON = "&aNow you will be able to see alerts";
		CMD_ALERTS_TOGGLEOFF = "&aYou won't see alerts anymore";
		CMD_ALERTS_WRONGCMD = "&cWrong variables: Type &7/oa alerts [on/off]";
		
		CMD_STATS_CONTENT = new ArrayList<>();
		CMD_STATS_CONTENT.add("&2============ &l%player%'s Stats &r&2============");
		CMD_STATS_CONTENT.add("&bDiamonds&7: %player_destroyed_diamond_ore%");
		CMD_STATS_CONTENT.add("&aEmeralds&7: %player_destroyed_emerald_ore%");
		CMD_STATS_PLAYERNOTFOUND = "&c%player% not found!";
		
		CMD_TOP_HEADER = "&2============ &lTop Players &r&2============";
		CMD_TOP_FOOTER = "";
		CMD_TOP_NOONE = "&7No one";
		CMD_TOP_FORMATPLAYER = "&a%player%&7: %player_destroyed% blocks";
		CMD_TOP_WRONGCMD = "&cWrong variables: Type &7/oa top [page]";
		
		CMD_VERSION_UPDATED = "&2&lOreAnnouncer &2%version% &7(%platform%) - Developed by &6AlessioDP";
		CMD_VERSION_OUTDATED = "&2&lOreAnnouncer &2%version% &7(%platform%) - Developed by &6AlessioDP\n&aNew version found: &2%newversion%";
		
		
		// Help messages
		HELP_HEADER = "&2============ &lOreAnnouncer Help Page &r&2============";
		HELP_FOOTER = "";
		HELP_CONSOLEHELP = new ArrayList<>();
		HELP_CONSOLEHELP.add("You can only make these commands:");
		HELP_CONSOLEHELP.add(" > oa reload - Reload the configuration");
		HELP_CONSOLEHELP.add(" > oa stats <player> - Show player stats");
		HELP_CONSOLEHELP.add(" > oa top - List of top players");
		HELP_CONSOLEHELP.add(" > oa version - Show OreAnnouncer information");
		
		HELP_CMD_HELP = "{\"text\":\"\",\"extra\":[{\"text\":\"/oa help\",\"color\":\"dark_green\"},{\"text\":\" - Show help pages\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/oa help \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_CMD_ALERTS = "{\"text\":\"\",\"extra\":[{\"text\":\"/oa alerts [on/off]\",\"color\":\"dark_green\"},{\"text\":\" - Toggle alerts\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/oa alerts \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_CMD_RELOAD = "{\"text\":\"\",\"extra\":[{\"text\":\"/oa reload\",\"color\":\"dark_green\"},{\"text\":\" - Reload OreAnnouncer configuration files\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/oa reload\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_CMD_STATS = "{\"text\":\"\",\"extra\":[{\"text\":\"/oa stats [player]\",\"color\":\"dark_green\"},{\"text\":\" - Show player stats\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/oa stats \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_CMD_TOP = "{\"text\":\"\",\"extra\":[{\"text\":\"/oa top [page]\",\"color\":\"dark_green\"},{\"text\":\" - List of top players\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/oa top \"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
		HELP_CMD_VERSION = "{\"text\":\"\",\"extra\":[{\"text\":\"/oa version\",\"color\":\"dark_green\"},{\"text\":\" - Show OreAnnouncer information\",\"color\":\"gray\"}],\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/oa version\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"Perform the command\",\"color\":\"gold\"}}}";
	}
	
	@Override
	public void loadConfiguration(ConfigurationAdapter confAdapter) {
		// OreAnnouncer messages
		OREANNOUNCER_UPDATEAVAILABLE = confAdapter.getString("oreannouncer.update-available", OREANNOUNCER_UPDATEAVAILABLE);
		OREANNOUNCER_NOPERMISSION = confAdapter.getString("oreannouncer.no-permission", OREANNOUNCER_NOPERMISSION);
		
		OREANNOUNCER_COMMON_INVALIDCMD = confAdapter.getString("oreannouncer.common-messages.invalid-command", OREANNOUNCER_COMMON_INVALIDCMD);
		OREANNOUNCER_COMMON_CONFIGRELOAD = confAdapter.getString("oreannouncer.common-messages.configuration-reloaded", OREANNOUNCER_COMMON_CONFIGRELOAD);
		
		
		// Event messages
		ALERTS_USER = confAdapter.getString("alerts.user", ALERTS_USER);
		ALERTS_ADMIN = confAdapter.getString("alerts.admin", ALERTS_ADMIN);
		ALERTS_CONSOLE = confAdapter.getString("alerts.console", ALERTS_CONSOLE);
		
		
		// Command messages
		CMD_ALERTS_TOGGLEON = confAdapter.getString("commands.alerts.toggle-on", CMD_ALERTS_TOGGLEON);
		CMD_ALERTS_TOGGLEOFF = confAdapter.getString("commands.alerts.toggle-off", CMD_ALERTS_TOGGLEOFF);
		CMD_ALERTS_WRONGCMD = confAdapter.getString("commands.alerts.wrong-command", CMD_ALERTS_WRONGCMD);
		
		CMD_STATS_CONTENT = confAdapter.getStringList("commands.stats.content", CMD_STATS_CONTENT);
		CMD_STATS_PLAYERNOTFOUND = confAdapter.getString("commands.stats.player-not-found", CMD_STATS_PLAYERNOTFOUND);
		
		CMD_TOP_HEADER = confAdapter.getString("commands.top.header", CMD_TOP_HEADER);
		CMD_TOP_FOOTER = confAdapter.getString("commands.top.footer", CMD_TOP_FOOTER);
		CMD_TOP_NOONE = confAdapter.getString("commands.top.no-one", CMD_TOP_NOONE);
		CMD_TOP_FORMATPLAYER = confAdapter.getString("commands.top.format-player", CMD_TOP_FORMATPLAYER);
		CMD_TOP_WRONGCMD = confAdapter.getString("commands.top.wrong-command", CMD_TOP_WRONGCMD);
		
		CMD_VERSION_UPDATED = confAdapter.getString("commands.version.updated", CMD_VERSION_UPDATED);
		CMD_VERSION_OUTDATED = confAdapter.getString("commands.version.outdated", CMD_VERSION_OUTDATED);
		
		
		// Help messages
		HELP_HEADER = confAdapter.getString("help.header", HELP_HEADER);
		HELP_FOOTER = confAdapter.getString("help.footer", HELP_FOOTER);
		HELP_CONSOLEHELP = confAdapter.getStringList("help.console-help", HELP_CONSOLEHELP);
		
		HELP_CMD_HELP = confAdapter.getString("help.commands.help", HELP_CMD_HELP);
		HELP_CMD_ALERTS = confAdapter.getString("help.commands.alerts", HELP_CMD_ALERTS);
		HELP_CMD_RELOAD = confAdapter.getString("help.commands.reload", HELP_CMD_RELOAD);
		HELP_CMD_STATS = confAdapter.getString("help.commands.stats", HELP_CMD_STATS);
		HELP_CMD_TOP = confAdapter.getString("help.commands.top", HELP_CMD_TOP);
		HELP_CMD_VERSION = confAdapter.getString("help.commands.version", HELP_CMD_VERSION);
	}
}
