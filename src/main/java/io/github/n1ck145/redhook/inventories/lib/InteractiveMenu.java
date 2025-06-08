package io.github.n1ck145.redhook.inventories.lib;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface InteractiveMenu {
	void handleClick(InventoryClickEvent event);

	void open();
}
