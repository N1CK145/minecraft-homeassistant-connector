package io.github.n1ck145.redhook.util.common;

import net.md_5.bungee.api.ChatColor;

public class ColorMapper {
	public static String map(String input) {
		return ChatColor.translateAlternateColorCodes('&', input == null ? "" : input);
	}
}
