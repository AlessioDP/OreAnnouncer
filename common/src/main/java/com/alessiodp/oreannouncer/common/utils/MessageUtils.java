package com.alessiodp.oreannouncer.common.utils;

import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.addons.internal.OAPlaceholder;
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
	
	public String formatDate(long timestamp, String format) {
		Instant instant = Instant.ofEpochSecond(timestamp);
		LocalDateTime date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		
		String ret = format;
		try {
			ret = DateTimeFormatter.ofPattern(format).format(date);
		} catch (IllegalArgumentException ignored) {}
		return ret;
	}
	
	public String formatElapsed(long timestamp, String format) {
		return DurationFormatUtils.formatDuration(System.currentTimeMillis() - (timestamp * 1000L), format);
	}
	
	public String stripPlaceholder(String placeholder) {
		return placeholder.substring(1, placeholder.length() - 1);
	}
}
