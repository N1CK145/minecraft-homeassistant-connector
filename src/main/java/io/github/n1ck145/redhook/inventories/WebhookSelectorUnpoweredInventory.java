package io.github.n1ck145.redhook.inventories;

public class WebhookSelectorUnpoweredInventory extends WebhookSelectorInventory {

    private static final String inventoryTitle = "Select a webhook running on state - LOW";

    private static WebhookSelectorUnpoweredInventory instance;

    private WebhookSelectorUnpoweredInventory() {
        super(inventoryTitle);
        WebhookSelectorUnpoweredInventory.instance = this;
    }

    public static WebhookSelectorUnpoweredInventory GetInstance(){
        if(instance == null)
            return new WebhookSelectorUnpoweredInventory();

        return WebhookSelectorUnpoweredInventory.instance;
    }
}
