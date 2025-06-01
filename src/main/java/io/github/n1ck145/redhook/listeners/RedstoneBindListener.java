package io.github.n1ck145.redhook.listeners;

import io.github.n1ck145.redhook.inventories.RedstoneActionMenu;
import io.github.n1ck145.redhook.manager.ActionRegistry;
import io.github.n1ck145.redhook.manager.MenuManager;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;


public class RedstoneBindListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        if (isWandItem(tool)) {
            Block clickedBlock = event.getBlock();
            event.setCancelled(true);
            MenuManager.openMenu(player, new RedstoneActionMenu(player, clickedBlock, ActionRegistry.getAll()));
        }
    }

    private boolean isWandItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey("redhook", "wand");

        Byte value = meta.getPersistentDataContainer().get(key, PersistentDataType.BYTE);
        return value != null && value == 1;
    }
}
