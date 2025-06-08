package io.github.n1ck145.redhook.manager;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import io.github.n1ck145.redhook.RedhookPlugin;
import io.github.n1ck145.redhook.utils.PlayerState;

public class PlayerStateManager {
	public static <T> T getPlayerState(Player player, PlayerState state, T defaultValue) {
		var metadata = player.getMetadata(state.name());
		if (metadata.isEmpty()) {
			return defaultValue;
		}
		return (T) metadata.get(0).value();
	}

	public static <T> T getPlayerState(Player player, PlayerState state) {
		return getPlayerState(player, state, null);
	}

	public static void setPlayerState(Player player, PlayerState state, Object value) {
		player.setMetadata(state.name(), new FixedMetadataValue(RedhookPlugin.getInstance(), value));
	}

	public static void removePlayerState(Player player, PlayerState state) {
		player.removeMetadata(state.name(), RedhookPlugin.getInstance());
	}
}
