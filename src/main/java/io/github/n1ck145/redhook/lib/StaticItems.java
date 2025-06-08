package io.github.n1ck145.redhook.lib;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import io.github.n1ck145.redhook.utils.ItemBuilder;

public class StaticItems {
	public static final ItemStack WAND = new ItemBuilder(Material.CALIBRATED_SCULK_SENSOR)
			.name("§cRedstone Action Wand")
			.lore("§7Use this to bind redstone actions", "§8(Break a block)")
			.persistentData("redhook", "wand", PersistentDataType.BYTE, (byte) 1)
			.build();

	public static final ItemStack DEBUG = new ItemBuilder(Material.BRUSH)
			.name("§cRedhook Debug Stick")
			.lore("§7Use this to debug redstone actions", "§8(Break a block)")
			.persistentData("redhook", "debug", PersistentDataType.BYTE, (byte) 1)
			.build();
}
