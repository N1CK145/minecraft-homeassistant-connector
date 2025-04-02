package io.github.n1ck145.redhook.dto.webhook;

import io.github.n1ck145.redhook.utils.webclient.HttpMethod;

import java.net.URI;
import java.util.UUID;

public class WebhookSettings {
    private UUID id;
    private String name;
    private final URI uri;
    private HttpMethod method;
    private int maxTimeoutSeconds = 5;
    private String description;

    public WebhookSettings(UUID id, String name, String description, URI uri){
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.method = HttpMethod.PUT;
        this.description = description;
    }

    public WebhookSettings(UUID id, String name, String description, URI uri, HttpMethod method){
        this(id, name, description, uri);
        this.method = method;
    }

    public void setMaxTimeoutSeconds(int maxTimeoutSeconds) {
        this.maxTimeoutSeconds = maxTimeoutSeconds;
    }

    public int getMaxTimeoutSeconds() {
        return maxTimeoutSeconds;
    }

    public String getName() {
        return name;
    }

    public URI getUri() {
        return uri;
    }

    public UUID getId() {
        if(id == null)
            id = UUID.randomUUID();

        return id;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getDescription() {
        return description;
    }
}
