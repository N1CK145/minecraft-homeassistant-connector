package io.github.n1ck145.redhook.utils.webclient;

import io.github.n1ck145.redhook.dto.webhook.WebhookSettings;
import io.github.n1ck145.redhook.inventories.WebhookSelectorInventory;

import java.net.URI;
import java.util.ArrayList;
import java.util.UUID;

public class WebhookSettingRepository {
    private static WebhookSettingRepository instance;
    private ArrayList<WebhookSettings> webhookSettings;

    private WebhookSettingRepository(){
        this.webhookSettings = new ArrayList<>();

        webhookSettings.add(new WebhookSettings(
                UUID.randomUUID(),
                "Toogle office light",
                "Toggles office light.",
                URI.create("http://homeassistant.service.lan/api/webhook/test-webhook-12"),
                HttpMethod.GET));
    }

    public static WebhookSettingRepository getInstance() {
        if(instance == null)
            instance = new WebhookSettingRepository();

        return instance;
    }

    public ArrayList<WebhookSettings> getWebhookSettings() {
        return webhookSettings;
    }
}
