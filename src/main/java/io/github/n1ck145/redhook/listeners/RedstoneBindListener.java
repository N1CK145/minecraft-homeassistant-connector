package io.github.n1ck145.redhook.listeners;

import io.github.n1ck145.redhook.inventories.RedstoneActionMenu;
import io.github.n1ck145.redhook.manager.ActionRegistry;
import io.github.n1ck145.redhook.manager.MenuManager;
import io.github.n1ck145.redhook.redstoneactions.RedstoneAction;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class RedstoneBindListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        // Check if tool is Calibrated Sculk Sensor
        if (tool.getType() == Material.CALIBRATED_SCULK_SENSOR
                && tool.hasItemMeta()
                && tool.getItemMeta().getDisplayName().contains("§cRedstone Action Wand")) {
            Block clickedBlock = event.getBlock();

            // Cancel breaking
            event.setCancelled(true);

            // Optional feedback
            player.sendMessage("§7Binding action to block at " + clickedBlock.getLocation().toVector());

            // Open the menu to bind an action
            MenuManager.openMenu(player, new RedstoneActionMenu(player, clickedBlock, ActionRegistry.getAll()));
        }
    }
}
