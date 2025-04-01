package io.github.n1ck145.redhook.inventories;

public class WebhookSelectorPoweredInventory extends WebhookSelectorInventory {

    private static final String inventoryTitle = "Select a webhook running on state - HIGH";
    private static WebhookSelectorPoweredInventory instance;

    private WebhookSelectorPoweredInventory() {
        super(inventoryTitle);
        WebhookSelectorPoweredInventory.instance = this;
    }

    public static  WebhookSelectorPoweredInventory GetInstance(){
        if(instance == null)
            return new WebhookSelectorPoweredInventory();

        return WebhookSelectorPoweredInventory.instance;
    }
}