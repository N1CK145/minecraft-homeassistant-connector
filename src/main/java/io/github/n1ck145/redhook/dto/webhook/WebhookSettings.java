package io.github.n1ck145.redhook.dto.webhook;

public class WebhookSettings {
    private String name;

    public WebhookSettings(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
