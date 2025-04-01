package io.github.n1ck145.redhook.inventories;

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
}
