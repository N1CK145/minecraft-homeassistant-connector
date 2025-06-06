package io.github.n1ck145.redhook.listeners;

import io.github.n1ck145.redhook.inventories.ActionTypeMenu;
import io.github.n1ck145.redhook.manager.MenuManager;
import io.github.n1ck145.redhook.utils.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WandInteractionListener implements Listener {

    @EventHandler
    public void onWandInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        if (!player.isSneaking()) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        if (!ItemUtil.isWandItem(item)) {
            return;
        }

        event.setCancelled(true);
        ActionTypeMenu menu = new ActionTypeMenu(player);
        MenuManager.openMenu(player, menu);
    }
} 