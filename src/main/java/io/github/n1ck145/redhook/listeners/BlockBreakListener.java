package io.github.n1ck145.redhook.listeners;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.github.n1ck145.redhook.manager.RedstoneLinkManager;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        Location location = block.getLocation();
        
        if(isWandItem(player.getInventory().getItemInMainHand())){
            return;
        }

        if (RedstoneLinkManager.hasBinding(location)) {
            if (player.isSneaking()) {
                RedstoneLinkManager.unbindBlock(location);
                player.sendMessage("§eUnbound redstone action from block.");
            } else {
                event.setCancelled(true);
                player.sendMessage("§cThis block has a bound action. Sneak to remove and break.");
            }
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