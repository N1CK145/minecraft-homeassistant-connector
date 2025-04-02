package io.github.n1ck145.redhook.listeners;

import io.github.n1ck145.redhook.utils.webclient.WebClient;
import io.github.n1ck145.redhook.utils.webclient.WebhookBlocks;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

import java.util.EventListener;

public class RedstonePowerChangeListener implements Listener {
    @EventHandler
    public void onPowerChange(BlockRedstoneEvent event){
        if(event.getOldCurrent() == 0 && event.getNewCurrent() > 0) {
            powerOn(event.getBlock());
        }

        if(event.getOldCurrent() > 0 && event.getNewCurrent() == 0) {
            powerOff(event.getBlock());
        }
    }

    private void powerOn(Block block){
        var loc = block.getLocation();
        WebhookBlocks.Block b = WebhookBlocks.getInstance().getBlock(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        Bukkit.getPlayer("N1CK145").sendMessage("Power On");

        if(b == null || !b.triggerOnPowerOn())
            return;

        WebClient.getInstance().RunWebhook(b.getWebhook());
    }

    private void powerOff(Block block){
        var loc = block.getLocation();
        WebhookBlocks.Block b = WebhookBlocks.getInstance().getBlock(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        if(b == null || !b.triggerOnPowerOff())
            return;

        WebClient.getInstance().RunWebhook(b.getWebhook());
    }
}
