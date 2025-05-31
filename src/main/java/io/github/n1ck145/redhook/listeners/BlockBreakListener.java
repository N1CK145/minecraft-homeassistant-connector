package io.github.n1ck145.redhook.listeners;

import java.util.ArrayList;
import java.util.Map;

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

import io.github.n1ck145.redhook.RedhookPlugin;
import io.github.n1ck145.redhook.manager.RedstoneLinkManager;
import io.github.n1ck145.redhook.redstoneactions.RedstoneActionInstance;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        Location location = block.getLocation();
        
        if(isWandItem(player.getInventory().getItemInMainHand())){
            return;
        }

        if(isDebugItem(player.getInventory().getItemInMainHand())){
            event.setCancelled(true);
            debug(block, player);
            return;
        }

        if (RedstoneLinkManager.hasBinding(location)) {
            if (player.isSneaking()) {
                RedstoneLinkManager.unbindBlock(location);
                player.sendMessage(RedhookPlugin.getPrefix() + "§eUnbound redstone action from block.");
            } else {
                event.setCancelled(true);
                player.sendMessage(RedhookPlugin.getPrefix() + "§cThis block has a bound action. Sneak to remove and break.");
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

    private boolean isDebugItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey("redhook", "debug");

        Byte value = meta.getPersistentDataContainer().get(key, PersistentDataType.BYTE);
        return value != null && value == 1;
    }

    private void debug(Block block, Player player) {
        ArrayList<RedstoneActionInstance> instances = RedstoneLinkManager.getActionInstances(block);
        if(instances == null || instances.isEmpty()){
            player.sendMessage(RedhookPlugin.getPrefix() + "§cNo actions bound to this block");
            return;
        }

        player.sendMessage(RedhookPlugin.getPrefix() + "§8===== §l§7" + block.getType().name() + " §r§8=====");
        player.sendMessage(RedhookPlugin.getPrefix() + "Total Actions: §7" + instances.size());

        for(RedstoneActionInstance instance : instances){
            String colorCode = "§r";

            switch(instance.getTriggerCondition()){
                case ON -> colorCode = "§2";
                case OFF -> colorCode = "§4";
                case BOTH -> colorCode = "§9";
            }
            
            Map<String, Object> actionData = instance.getAction().serialize();
            actionData.remove("id");
            actionData.remove("type"); 
            actionData.remove("label");

            player.sendMessage(RedhookPlugin.getPrefix());
            player.sendMessage(RedhookPlugin.getPrefix() + "§6" + instance.getAction().getLabel() + " (" + instance.getAction().getId() + ")§8@" + colorCode + instance.getTriggerCondition().name());

            for (Map.Entry<String, Object> entry : actionData.entrySet()) {
                String key = entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1);
                Object value = entry.getValue();
                
                if (value instanceof Iterable<?> || value instanceof Object[]) {
                    player.sendMessage(RedhookPlugin.getPrefix() + key + ":");
                    if (value instanceof Iterable<?>) {
                        for (Object item : (Iterable<?>) value) {
                            player.sendMessage(RedhookPlugin.getPrefix() + "  §7- " + item);
                        }
                    } else {
                        for (Object item : (Object[]) value) {
                            player.sendMessage(RedhookPlugin.getPrefix() + "  §7- " + item);
                        }
                    }
                } else {
                    player.sendMessage(RedhookPlugin.getPrefix() + key + ": §7" + value);
                }
            }
        }
    }
}