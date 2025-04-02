package io.github.n1ck145.redhook.inventories;

import io.github.n1ck145.redhook.dto.webhook.WebhookSettings;
import io.github.n1ck145.redhook.utils.webclient.WebClient;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;

public class WebhookSelectorViewerInventory extends WebhookSelectorInventory {

    private static final String inventoryTitle = "Available webhooks";

    private static WebhookSelectorViewerInventory instance;

    private WebhookSelectorViewerInventory() {
        super(inventoryTitle);
        WebhookSelectorViewerInventory.instance = this;
    }

    public static WebhookSelectorViewerInventory GetInstance(){
        if(instance == null)
            return new WebhookSelectorViewerInventory();

        return WebhookSelectorViewerInventory.instance;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);

            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                // Webhook
                WebhookSettings webhookSettings = itemsToWebhook.get(event.getCurrentItem());

                if(webhookSettings == null)
                    return;

                WebClient.getInstance().RunWebhook(webhookSettings);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        }
    }
}
