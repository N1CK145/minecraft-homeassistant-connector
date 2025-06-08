package io.github.n1ck145.redhook.manager.hologram;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HologramManager {
	private static final Map<UUID, List<ArmorStand>> activeHolograms = new HashMap<>();
	private static final double LINE_HEIGHT = 0.25;

	public static void showHologram(Player player, Location location, String text) {
		// Remove existing hologram if any
		removeHologram(player);

		// Split text into lines
		String[] lines = text.split("\n");
		List<ArmorStand> holograms = new ArrayList<>();

		// Create an armor stand for each line
		for (int i = 0; i < lines.length; i++) {
			ArmorStand hologram = (ArmorStand) location.getWorld().spawnEntity(
					location.clone().add(0.5, 1.2 + (lines.length - 1 - i) * LINE_HEIGHT, 0.5), EntityType.ARMOR_STAND);

			// Configure hologram
			hologram.setVisible(false);
			hologram.setGravity(false);
			hologram.setCanPickupItems(false);
			hologram.setCustomName(lines[i]);
			hologram.setCustomNameVisible(true);
			hologram.setMarker(true);
			hologram.setSmall(true);
			hologram.setInvulnerable(true);
			hologram.setBasePlate(false);
			hologram.setArms(false);

			holograms.add(hologram);
		}

		// Store holograms
		activeHolograms.put(player.getUniqueId(), holograms);
	}

	public static void removeHologram(Player player) {
		List<ArmorStand> holograms = activeHolograms.remove(player.getUniqueId());
		if (holograms != null) {
			holograms.forEach(hologram -> {
				if (!hologram.isDead()) {
					hologram.remove();
				}
			});
		}
	}

	public static void removeAllHolograms() {
		activeHolograms.values().forEach(holograms -> {
			holograms.forEach(hologram -> {
				if (!hologram.isDead()) {
					hologram.remove();
				}
			});
		});
		activeHolograms.clear();
	}
}
