package com.alessiodp.oreannouncer.common.utils;

import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.internal.OAPlaceholder;
import com.alessiodp.oreannouncer.common.configuration.OAConstants;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.alessiodp.oreannouncer.common.players.objects.OAPlayerImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.time.DurationFormatUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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
				switch (identifier.toLowerCase(Locale.ENGLISH)) {
					case "%player%":
						replacement = player.getName();
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
		long elapsed = System.currentTimeMillis() - (timestamp * 1000L);
		String format = elapsed >= 86400000 ?
				ConfigMain.STATS_ADVANCED_COUNT_LOG_FORMAT_DATE_ELAPSED_LARGE
				: (elapsed >= 3600000 ? ConfigMain.STATS_ADVANCED_COUNT_LOG_FORMAT_DATE_ELAPSED_MEDIUM
						: (elapsed >= 60000 ? ConfigMain.STATS_ADVANCED_COUNT_LOG_FORMAT_DATE_ELAPSED_SMALL
								: ConfigMain.STATS_ADVANCED_COUNT_LOG_FORMAT_DATE_ELAPSED_SMALLEST));
		return DurationFormatUtils.formatDuration(elapsed, format);
	}
	
	public String stripPlaceholder(String placeholder) {
		return placeholder.substring(1, placeholder.length() - 1);
	}
}
