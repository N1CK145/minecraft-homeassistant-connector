package io.github.n1ck145.redhook.listeners;

import io.github.n1ck145.redhook.RedhookPlugin;
import io.github.n1ck145.redhook.inventories.CreateActionMenu;
import io.github.n1ck145.redhook.manager.MenuManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatInputListener implements Listener {

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (MenuManager.getCurrentMenu(event.getPlayer()) instanceof CreateActionMenu) {
			event.setCancelled(true);
			CreateActionMenu menu = (CreateActionMenu) MenuManager.getCurrentMenu(event.getPlayer());
			String message = event.getMessage();

			// Schedule the inventory opening on the main thread
			Bukkit.getScheduler().runTask(RedhookPlugin.getInstance(), () -> {
				menu.handleChatInput(message);
			});
		}
	}
}
