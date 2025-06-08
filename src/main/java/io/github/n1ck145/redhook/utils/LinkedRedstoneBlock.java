package io.github.n1ck145.redhook.utils;

import org.bukkit.Location;

public class LinkedRedstoneBlock {
	private final Location location;
	private final String actionId;

	public LinkedRedstoneBlock(Location location, String actionId) {
		this.location = location;
		this.actionId = actionId;
	}

	public Location getLocation() {
		return location;
	}

	public String getActionId() {
		return actionId;
	}
}
