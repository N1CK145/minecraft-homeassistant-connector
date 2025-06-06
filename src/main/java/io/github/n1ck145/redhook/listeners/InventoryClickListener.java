package io.github.n1ck145.redhook.listeners;

import io.github.n1ck145.redhook.inventories.CreateActionMenu;
import io.github.n1ck145.redhook.manager.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        MenuManager.handleClick(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            // Don't unregister the menu if it's a CreateActionMenu waiting for chat input
            if (MenuManager.getCurrentMenu((Player) event.getPlayer()) instanceof CreateActionMenu menu) {
                if (menu.getCurrentEditingField() != null) {
                    return;
                }
            }
            MenuManager.handleClose(event);
        }
    }
}
