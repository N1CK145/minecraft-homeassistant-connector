package io.github.n1ck145.redhook.inventories;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Menu {
    void handleClick(InventoryClickEvent event);
    void open();
}
