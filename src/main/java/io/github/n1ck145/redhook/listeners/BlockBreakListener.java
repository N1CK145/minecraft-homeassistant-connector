package io.github.n1ck145.redhook.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import io.github.n1ck145.redhook.manager.RedstoneLinkManager;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        Location location = block.getLocation();

        if (RedstoneLinkManager.hasBinding(location)) {
            if (player.isSneaking()) {
                RedstoneLinkManager.unbindBlock(location);
                player.sendMessage("§eUnbound redstone action from block.");
                // allow breaking
            } else {
                event.setCancelled(true);
                player.sendMessage("§cThis block has a bound action. Sneak to remove and break.");
            }
        }
    }
}