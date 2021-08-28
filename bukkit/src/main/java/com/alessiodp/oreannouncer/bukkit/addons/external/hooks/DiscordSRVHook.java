package com.alessiodp.oreannouncer.bukkit.addons.external.hooks;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.oreannouncer.bukkit.addons.external.DiscordSRVHandler;
import com.alessiodp.oreannouncer.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.objects.MessageFormat;
import github.scarsz.discordsrv.util.DiscordUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.function.Function;

@RequiredArgsConstructor
public class DiscordSRVHook {
	@NonNull private final OreAnnouncerPlugin plugin;
	
	private DiscordSRV api;
	
	public boolean register() {
		boolean ret = false;
		try {
			api = DiscordSRV.getPlugin();
			
			ret = true;
		} catch (Exception ex) {
			plugin.getLoggerManager().printError(String.format(Constants.DEBUG_ADDON_OUTDATED, "DiscordSRV"));
			ex.printStackTrace();
		}
		return ret;
	}
	
	public void sendMessageText(Function<String, String> parser, String channel, String message) {
		if (message != null && !message.isEmpty()) {
			TextChannel textChannel = api.getDestinationTextChannelForGameChannelName(channel);
			if (textChannel != null) {
				DiscordUtil.queueMessage(textChannel, parser.apply(message));
			}
		}
	}
	
	public void sendMessageEmbed(OAPlayerImpl player, Function<String, String> parser, String channel, DiscordSRVHandler.DiscordSRVAlertEmbed embed) {
		if (embed != null && embed.hasContent()) {
			TextChannel textChannel = api.getDestinationTextChannelForGameChannelName(channel);
			if (textChannel != null) {
				MessageFormat mf = new MessageFormat();
				CommonUtils.ifNonNullDo(parseHexColor(embed.getColor()), mf::setColor);
				CommonUtils.ifNonEmptyDo(embed.getAuthorName(), txt -> mf.setAuthorName(parser.apply(txt)));
				CommonUtils.ifNonEmptyDo(embed.getTitle(), txt -> mf.setTitle(parser.apply(txt)));
				CommonUtils.ifNonEmptyDo(embed.getDescription(), txt -> mf.setDescription(parser.apply(txt)));
				CommonUtils.ifNonEmptyDo(embed.getFooter(), txt -> mf.setFooterText(parser.apply(txt)));

				if (BukkitConfigMain.ALERTS_DISCORDSRV_EMBED_AVATARS && player != null) {
					mf.setAuthorImageUrl(DiscordSRV.getAvatarUrl(player.getName(), player.getPlayerUUID()));
				}
				
				DiscordUtil.queueMessage(textChannel, DiscordSRV.translateMessage(mf, (content, needEscape) -> content));
			}
		}
	}
	
	private Color parseHexColor(String hex) {
		Color ret = null;
		if (hex != null && !hex.isEmpty()) {
			String parsedHex = hex.trim();
			if (!parsedHex.startsWith("#")) parsedHex = "#" + parsedHex;
			if (parsedHex.length() == 7) {
				ret = new Color(
							Integer.valueOf(parsedHex.substring(1, 3), 16),
							Integer.valueOf(parsedHex.substring(3, 5), 16),
							Integer.valueOf(parsedHex.substring(5, 7), 16)
					);
			}
		}
		return ret;
	}
}
