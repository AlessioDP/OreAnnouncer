package com.alessiodp.oreannouncer.common.utils;

import com.alessiodp.core.common.utils.ADPLocation;
import com.alessiodp.oreannouncer.common.OreAnnouncerPlugin;
import com.alessiodp.oreannouncer.common.configuration.data.ConfigMain;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@RequiredArgsConstructor
public class CoordinateUtils {
	private final OreAnnouncerPlugin plugin;
	
	public String replaceCoordinates(String message, ADPLocation location, boolean hideEnabled) {
		String ret = message;
		if (ConfigMain.ALERTS_COORDINATES_ENABLE) {
			ArrayList<Coordinate> coords = new ArrayList<>();
			if (ret.contains(CoordinateAxis.X.toString()))
				coords.add(new Coordinate(
						CoordinateAxis.X,
						location.getX(),
						ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_X ? false : null));
			if (ret.contains(CoordinateAxis.Y.toString()))
				coords.add(new Coordinate(
						CoordinateAxis.Y,
						location.getY(),
						ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Y ? false : null));
			if (ret.contains(CoordinateAxis.Z.toString()))
				coords.add(new Coordinate(
						CoordinateAxis.Z,
						location.getZ(),
						ConfigMain.ALERTS_COORDINATES_HIDE_HIDE_Z ? false : null));
			
			if (ConfigMain.ALERTS_COORDINATES_HIDE_ENABLE && hideEnabled) {
				for (int c=0; c < ConfigMain.ALERTS_COORDINATES_HIDE_COUNT; c++) {
					ArrayList<Coordinate> visibleCoordinates = getVisibleCoordinates(coords);
					
					if (visibleCoordinates.size() == 0)
						break;
					
					// Randomly choose one of them
					int chosen = (int) (Math.random() * visibleCoordinates.size());
					
					// Set choosen one into hidden
					visibleCoordinates.get(chosen).hidden = true;
				}
			}
			boolean isJson = plugin.getJsonHandler().isJson(message);
			for (Coordinate coord : coords) {
				ret = ret.replace(coord.axis.placeholder, coord.parseValue(isJson));
			}
		}
		return ret;
	}
	
	private ArrayList<Coordinate> getVisibleCoordinates(ArrayList<Coordinate> coordinates) {
		ArrayList<Coordinate> ret = new ArrayList<>();
		for (Coordinate coord : coordinates) {
			if (coord.hidden != null && !coord.hidden) {
				ret.add(coord);
			}
		}
		return ret;
	}
	
	@AllArgsConstructor
	private final class Coordinate {
		private CoordinateAxis axis;
		private double value;
		// If null, this coordinate does not support hide
		private Boolean hidden;
		
		private String parseValue(boolean isJson) {
			String ret = Integer.toString((int) value);
			if (hidden != null && hidden) {
				if (ConfigMain.ALERTS_COORDINATES_HIDE_OBFUSCATION_ENABLE) {
					int num = ConfigMain.ALERTS_COORDINATES_HIDE_OBFUSCATION_FIXEDLENGTH;
					if (num <= 0)
						num = ret.length() - (value < 0 ? 1 : 0);
					
					ret = Strings.repeat(ConfigMain.ALERTS_COORDINATES_HIDE_OBFUSCATION_CHARACTER, num);
				}
				
				if (isJson) {
					ret = ConfigMain.ALERTS_COORDINATES_HIDE_FORMAT_JSON.replace("%coordinate%", ret);
				} else {
					ret = ConfigMain.ALERTS_COORDINATES_HIDE_FORMAT_TEXT.replace("%coordinate%", ret);
				}
			}
			return ret;
		}
	}
	
	@AllArgsConstructor
	public enum CoordinateAxis {
		X("%x%"),
		Y("%y%"),
		Z("%z%");
		
		private final String placeholder;
		
		@Override
		public String toString() {return placeholder;}
	}
}
