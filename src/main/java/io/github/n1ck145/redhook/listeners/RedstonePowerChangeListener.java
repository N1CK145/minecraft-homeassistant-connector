package io.github.n1ck145.redhook.listeners;

import io.github.n1ck145.redhook.manager.RedstoneLinkManager;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

public class RedstonePowerChangeListener implements Listener {
	@EventHandler
	public void onRedstone(BlockRedstoneEvent event) {
		Block block = event.getBlock();

		if (event.getOldCurrent() == 0 && event.getNewCurrent() > 0) {
			// Block is being powered now
			RedstoneLinkManager.triggerOn(block, null);
		}
		else if (event.getOldCurrent() > 0 && event.getNewCurrent() == 0) {
			RedstoneLinkManager.triggerOff(block, null);
		}
	}
}
