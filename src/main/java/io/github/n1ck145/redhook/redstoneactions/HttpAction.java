package io.github.n1ck145.redhook.redstoneactions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.n1ck145.redhook.RedhookPlugin;
import io.github.n1ck145.redhook.utils.ColorMapper;
import io.github.n1ck145.redhook.utils.ItemBuilder;

public class HttpAction implements RedstoneAction {
    public static final String name = "WebhookAction";
    private static final Material material = Material.NETHER_STAR;
    private final String id;
    private final String label;
    private String[] description;

    private final String url;
    private final Map<String, String> headers;
    private final String body;
    private final String method;


    public HttpAction(String id, String label, String[] description, String url, Map<String, String> headers, String body, String method) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.url = url;
        this.headers = headers;
        this.body = body;
        this.method = method;
    }

    @Override
    public void execute(Player trigger) {
         Bukkit.getScheduler().runTaskAsynchronously(RedhookPlugin.getInstance(), () -> {
            try {
                java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
                java.net.http.HttpRequest.Builder requestBuilder = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(url));

                // Set method and body if body is not null
                if (body != null) {
                    requestBuilder.method(method, java.net.http.HttpRequest.BodyPublishers.ofString(body));
                } else {
                    requestBuilder.method(method, java.net.http.HttpRequest.BodyPublishers.noBody());
                }

                // Add headers if they exist
                if (headers != null) {
                    headers.forEach(requestBuilder::header);
                }

                java.net.http.HttpRequest request = requestBuilder.build();
                java.net.http.HttpResponse<String> response = client.send(request, 
                    java.net.http.HttpResponse.BodyHandlers.ofString());

                Bukkit.getLogger().info("Webhook sent to " + url + " with status code " + response.statusCode());
            } catch (Exception e) {
                Bukkit.getLogger().severe("Failed to execute webhook to " + url + ": " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Override
    public ItemStack getIcon() {
        String title = ColorMapper.map(label);
        String[] lore = Arrays.stream(description).map(ColorMapper::map).toArray(String[]::new);
        return new ItemBuilder(material)
            .name(title)
            .lore(lore)
            .build();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("id", id);
        map.put("type", name);
        map.put("label", label);
        map.put("description", description);

        map.put("url", url);
        map.put("headers", headers);
        map.put("body", body);
        map.put("method", method);

        return map;
    }

    public static HttpAction deserialize(Map<?, ?> map) {
        String id = (String) map.get("id");
        String label = (String) map.get("label");
        ArrayList<String> description = (ArrayList<String>) map.get("description");
        String[] descriptionArray = description == null ? new String[0] : description.toArray(new String[0]);

        String url = (String) map.get("url");
        Map<String, String> headers = (Map<String, String>) map.get("headers");
        String body = (String) map.get("body");
        String method = (String) map.get("method");

        return new HttpAction(id, label, descriptionArray, url, headers, body, method);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String[] getDescription() {
        return description;
    }
}
