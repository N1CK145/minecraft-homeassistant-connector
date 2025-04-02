package io.github.n1ck145.redhook.utils.webclient;

import io.github.n1ck145.redhook.dto.webhook.WebhookSettings;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.concurrent.Flow;

public class WebClient {
    private static WebClient instance;

    private HttpClient client;

    private WebClient(){
        if(instance != null){
            throw new RuntimeException("Web client can be instantiated only once!");
        }

        this.client = HttpClient.newHttpClient();
    }

    public HttpResponse RunWebhook(WebhookSettings webhook){
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(webhook.getUri())
                .timeout(Duration.ofSeconds(webhook.getMaxTimeoutSeconds()));

        switch (webhook.getMethod()){
            case GET:
                builder.GET();
                break;
            case DELETE:
                builder.DELETE();
                break;
            case POST:
                builder.POST(HttpRequest.BodyPublishers.noBody());
                break;
            case PUT:
                builder.PUT(HttpRequest.BodyPublishers.noBody());
                break;
            default:
                throw new RuntimeException("Http Method not supported!");
        }

        try {
            return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Request failed", e);
        }
    }

    public static WebClient getInstance() {
        if(instance == null) {
            instance = new WebClient();
        }

        return instance;
    }
}
