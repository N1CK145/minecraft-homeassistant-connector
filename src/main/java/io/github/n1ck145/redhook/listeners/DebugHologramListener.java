package io.github.n1ck145.redhook.listeners;

import io.github.n1ck145.redhook.RedhookPlugin;
import io.github.n1ck145.redhook.manager.RedstoneLinkManager;
import io.github.n1ck145.redhook.redstoneactions.lib.RedstoneActionInstance;
import io.github.n1ck145.redhook.utils.HologramManager;
import io.github.n1ck145.redhook.utils.StateColor;
import io.github.n1ck145.redhook.utils.ItemUtil;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DebugHologramListener implements Listener {
    private final Map<Player, BukkitRunnable> hologramTasks = new HashMap<>();

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        
        // Remove existing hologram task
        BukkitRunnable existingTask = hologramTasks.remove(player);
        if (existingTask != null) {
            existingTask.cancel();
        }

        // If holding debug stick, start showing holograms
        if (ItemUtil.isDebugItem(newItem)) {
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline() || !ItemUtil.isDebugItem(player.getInventory().getItemInMainHand())) {
                        HologramManager.removeHologram(player);
                        cancel();
                        hologramTasks.remove(player);
                        return;
                    }

                    // Get block player is looking at
                    Block targetBlock = player.getTargetBlockExact(5);
                    if (targetBlock != null) {
                        ArrayList<RedstoneActionInstance> instances = RedstoneLinkManager.getActionInstances(targetBlock);
                        if (instances != null && !instances.isEmpty()) {
                            StringBuilder hologramText = new StringBuilder();
                            hologramText.append("§6Total Actions: §e").append(instances.size()).append("\n");
                            hologramText.append("§8§m").append("─".repeat(15)).append("\n");
                            
                            for (RedstoneActionInstance instance : instances) {
                                String stateColor = StateColor.valueOf(instance.getTriggerCondition().name()).getColorCode();
                                
                                hologramText.append("§f").append(instance.getAction().getLabel())
                                    .append("§8@").append(stateColor)
                                    .append(instance.getTriggerCondition().name()).append("\n");
                            }

                            hologramText.append("§8§m").append("─".repeat(15));

                            HologramManager.showHologram(player, targetBlock.getLocation(), hologramText.toString());
                        } else {
                            HologramManager.removeHologram(player);
                        }
                    } else {
                        HologramManager.removeHologram(player);
                    }
                }
            };
            task.runTaskTimer(RedhookPlugin.getInstance(), 0L, 5L);
            hologramTasks.put(player, task);
        } else {
            HologramManager.removeHologram(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BukkitRunnable task = hologramTasks.remove(player);
        if (task != null) {
            task.cancel();
        }
        HologramManager.removeHologram(player);
    }
}
