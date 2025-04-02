package io.github.n1ck145.redhook.listeners;

import io.github.n1ck145.redhook.inventories.WebhookSelectorUnpoweredInventory;
import io.github.n1ck145.redhook.utils.webclient.WebhookBlocks;
import io.github.n1ck145.redhook.utils.webclient.WebhookSettingRepository;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EventListener;

public class BindBlockToWebhookListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        Player p = event.getPlayer();
        Block b = event.getBlock();
        ItemStack breakTool = p.getItemInHand();
        p.sendMessage(breakTool.getType().name());

        if(breakTool.getType() != Material.CALIBRATED_SCULK_SENSOR)
            return;

        event.setCancelled(true);

        if(p.isSneaking()){
            // TODO: Remove event from block
            p.sendMessage("TODO: Remove webhook action from block");
            return;
        }

        // p.openInventory(WebhookSelectorUnpoweredInventory.GetInstance().getInventory());
        // TODO: Get Webhook
        var webhook = WebhookSettingRepository.getInstance().getWebhookSettings().getFirst();

        WebhookBlocks.getInstance().addBlock(b, webhook);
    }
}
