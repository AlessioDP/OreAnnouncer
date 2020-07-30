package com.alessiodp.oreannouncer.common.utils;

import com.alessiodp.core.common.utils.Color;
import com.alessiodp.core.common.utils.CommonUtils;
import com.alessiodp.core.common.utils.DurationUtils;
import com.alessiodp.oreannouncer.api.interfaces.OABlock;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.internal.OAPlaceholder;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.configuration.data.Messages;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public abstract class MessageUtils {
	private final OreAnnouncerPlugin plugin;
	private final Pattern PLACEHOLDER_PATTERN = Pattern.compile("([%][^%]+[%])");
	
	public String convertPlayerPlaceholders(String message, OAPlayerImpl player) {
		String ret = message;
		if (player != null) {
			String replacement;
			Matcher matcher = PLACEHOLDER_PATTERN.matcher(ret);
			while (matcher.find()) {
				String identifier = matcher.group(1);
				// Match basic placeholders
				switch (CommonUtils.toLowerCase(identifier)) {
					case "%player%":
						replacement = CommonUtils.getNoEmptyOr(player.getName(), Messages.OREANNOUNCER_SYNTAX_UNKNOWN);
						ret = ret.replace(identifier, replacement);
						break;
					default: // Nothing to do
				}
				
				OAPlaceholder placeholder = OAPlaceholder.getPlaceholder(stripPlaceholder(identifier));
				if (placeholder != null) {
					replacement = placeholder.formatPlaceholder(player, stripPlaceholder(identifier));
					if (replacement != null)
						ret = ret.replace(identifier, replacement);
				}
			}
		}
		return ret;
	}
	
	public String convertBlockPlaceholders(String message, OABlock block) {
		String ret = message;
		if (block != null) {
			Matcher matcher = PLACEHOLDER_PATTERN.matcher(ret);
			while (matcher.find()) {
				String identifier = matcher.group(1);
				// Match basic placeholders
				switch (CommonUtils.toLowerCase(identifier)) {
					case "%material%":
						ret = ret.replace(identifier, block.getMaterialName());
					case "%enabled%":
						ret = ret.replace(identifier, formatEnabledDisabled(block.isEnabled()));
						break;
					case "%material_name%":
						ret = ret.replace(identifier, block.getMaterialName());
						break;
					case "%display_name%":
						ret = ret.replace(identifier, block.getDisplayName());
						break;
					case "%display_color%":
						ret = ret.replace(identifier, block.getDisplayColor() != null ? Color.formatColorByNameOrText(Color.translateAlternateColorCodes(block.getDisplayColor())) : "");
						break;
					case "%alert_users%":
						ret = ret.replace(identifier, formatOnOff(block.isAlertingUsers()));
						break;
					case "%alert_admins%":
						ret = ret.replace(identifier, formatOnOff(block.isAlertingAdmins()));
						break;
					case "%name_singular%":
						ret = ret.replace(identifier, block.getSingularName());
						break;
					case "%name_plural%":
						ret = ret.replace(identifier, block.getPluralName());
						break;
					case "%count_number%":
						ret = ret.replace(identifier, Integer.toString(block.getCountNumber()));
						break;
					case "%count_time%":
						ret = ret.replace(identifier, Integer.toString(block.getCountTime()));
						break;
					case "%message_user%":
						ret = ret.replace(identifier, block.getMessageUser());
						break;
					case "%message_admin%":
						ret = ret.replace(identifier, block.getMessageAdmin());
						break;
					case "%message_console%":
						ret = ret.replace(identifier, block.getMessageConsole());
						break;
					case "%message_count_user%":
						ret = ret.replace(identifier, block.getCountMessageUser());
						break;
					case "%message_count_admin%":
						ret = ret.replace(identifier, block.getCountMessageAdmin());
						break;
					case "%message_count_console%":
						ret = ret.replace(identifier, block.getCountMessageConsole());
						break;
					case "%sound%":
						ret = ret.replace(identifier, block.getSound());
						break;
					case "%light_level%":
						ret = ret.replace(identifier, Integer.toString(block.getLightLevel()));
						break;
					case "%count_on_destroy%":
						ret = ret.replace(identifier, formatOnOff(block.isCountingOnDestroy()));
						break;
					case "%tnt%":
						ret = ret.replace(identifier, formatOnOff(block.isTNTEnabled()));
						break;
					case "%priority%":
						ret = ret.replace(identifier, Integer.toString(block.getPriority()));
						break;
					default: // Nothing to do
				}
			}
		}
		return ret;
	}
	
	public String convertBlockFormattedPlaceholders(String message, OABlock block) {
		String ret = message;
		if (block != null) {
			Matcher matcher = PLACEHOLDER_PATTERN.matcher(ret);
			while (matcher.find()) {
				String identifier = matcher.group(1);
				// Match basic placeholders
				switch (CommonUtils.toLowerCase(identifier)) {
					case "%material%":
						ret = ret.replace(identifier, block.getMaterialName());
					case "%enabled%":
						ret = ret.replace(identifier, formatEnabledDisabled(block.isEnabled()));
						break;
					case "%material_name%":
						ret = ret.replace(identifier, block.getMaterialName());
						break;
					case "%display_name%":
						ret = ret.replace(identifier, formatText(block.getDisplayName()));
						break;
					case "%display_color%":
						ret = ret.replace(identifier, formatText(block.getDisplayColor() != null ? Color.formatColorNamesByNameOrText(Color.translateAlternateColorCodes(block.getDisplayColor())) : ""));
						break;
					case "%display_color_code%":
						ret = ret.replace(identifier, block.getDisplayColor() != null ? Color.formatColorByNameOrText(Color.translateAlternateColorCodes(block.getDisplayColor())) : "");
						break;
					case "%alert_users%":
						ret = ret.replace(identifier, formatOnOff(block.isAlertingUsers()));
						break;
					case "%alert_admins%":
						ret = ret.replace(identifier, formatOnOff(block.isAlertingAdmins()));
						break;
					case "%name_singular%":
						ret = ret.replace(identifier, formatText(block.getSingularName()));
						break;
					case "%name_plural%":
						ret = ret.replace(identifier, formatText(block.getPluralName()));
						break;
					case "%count_number%":
						ret = ret.replace(identifier, formatNumber(block.getCountNumber()));
						break;
					case "%count_time%":
						ret = ret.replace(identifier, formatNumber(block.getCountTime()));
						break;
					case "%message_user%":
						ret = ret.replace(identifier, formatText(block.getMessageUser()));
						break;
					case "%message_admin%":
						ret = ret.replace(identifier, formatText(block.getMessageAdmin()));
						break;
					case "%message_console%":
						ret = ret.replace(identifier, formatText(block.getMessageConsole()));
						break;
					case "%message_count_user%":
						ret = ret.replace(identifier, formatText(block.getCountMessageUser()));
						break;
					case "%message_count_admin%":
						ret = ret.replace(identifier, formatText(block.getCountMessageAdmin()));
						break;
					case "%message_count_console%":
						ret = ret.replace(identifier, formatText(block.getCountMessageConsole()));
						break;
					case "%sound%":
						ret = ret.replace(identifier, formatText(block.getSound()));
						break;
					case "%light_level%":
						ret = ret.replace(identifier, formatNumber(block.getLightLevel()));
						break;
					case "%count_on_destroy%":
						ret = ret.replace(identifier, formatOnOff(block.isCountingOnDestroy()));
						break;
					case "%tnt%":
						ret = ret.replace(identifier, formatOnOff(block.isTNTEnabled()));
						break;
					case "%priority%":
						ret = ret.replace(identifier, formatNumber(block.getPriority()));
						break;
					default: // Nothing to do
				}
			}
		}
		return ret;
	}
	
	public String formatDate(long timestamp) {
		Instant instant = Instant.ofEpochSecond(timestamp);
		LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		
		String ret = ConfigMain.STATS_ADVANCED_COUNT_LOG_FORMAT_DATE;
		try {
			ret = DateTimeFormatter.ofPattern(ret).format(date);
		} catch (IllegalArgumentException ex) {
			plugin.getLoggerManager().printErrorStacktrace(OAConstants.DEBUG_CMD_LOG_FAILED_PARSE_DATE, ex);
		}
		return ret;
	}
	
	public String formatElapsed(long timestamp) {
		return DurationUtils.formatWith(
				(System.currentTimeMillis() / 1000L) - timestamp,
				ConfigMain.STATS_ADVANCED_COUNT_LOG_FORMAT_DATE_ELAPSED_LARGE,
				ConfigMain.STATS_ADVANCED_COUNT_LOG_FORMAT_DATE_ELAPSED_MEDIUM,
				ConfigMain.STATS_ADVANCED_COUNT_LOG_FORMAT_DATE_ELAPSED_SMALL,
				ConfigMain.STATS_ADVANCED_COUNT_LOG_FORMAT_DATE_ELAPSED_SMALLEST
		);
	}
	
	public String formatEnabledDisabled(boolean value) {
		return value ? Messages.OREANNOUNCER_BLOCKS_ENABLED : Messages.OREANNOUNCER_BLOCKS_DISABLED;
	}
	
	public String formatOnOff(boolean value) {
		return value ? Messages.OREANNOUNCER_BLOCKS_TOGGLED_ON : Messages.OREANNOUNCER_BLOCKS_TOGGLED_OFF;
	}
	
	public String formatYesNo(boolean value) {
		return value ? Messages.OREANNOUNCER_BLOCKS_WORD_YES : Messages.OREANNOUNCER_BLOCKS_WORD_NO;
	}
	
	public String formatText(String text) {
		return text == null ? Messages.OREANNOUNCER_BLOCKS_NONE : (text.isEmpty() ? Messages.OREANNOUNCER_BLOCKS_EMPTY : text);
	}
	
	public String formatNumber(int number) {
		return number == -1 ? Messages.OREANNOUNCER_BLOCKS_NONE : Integer.toString(number);
	}
	
	public String stripPlaceholder(String placeholder) {
		return placeholder.substring(1, placeholder.length() - 1);
	}
}
