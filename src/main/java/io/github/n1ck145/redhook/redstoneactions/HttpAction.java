package io.github.n1ck145.redhook.redstoneactions;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import io.github.n1ck145.redhook.RedhookPlugin;
import io.github.n1ck145.redhook.annotations.ActionField;
import io.github.n1ck145.redhook.redstoneactions.lib.AbstractRedstoneAction;
import io.github.n1ck145.redhook.redstoneactions.lib.ActionTypeRepresentation;

@ActionTypeRepresentation(icon = Material.NETHER_STAR, description = "Sends HTTP requests to specified URLs when triggered by redstone. Supports GET, POST, and other HTTP methods with custom headers and body content.")
public class HttpAction extends AbstractRedstoneAction {
	private static final Material material = Material.NETHER_STAR;

	@ActionField(label = "URL", description = "The URL to send the request to", icon = Material.OAK_SIGN, required = true)
	private String url;

	@ActionField(label = "Headers", description = "The headers to send with the request", icon = Material.BUNDLE)
	private Map<String, String> headers;

	@ActionField(label = "Body", description = "The body to send with the request", icon = Material.WRITABLE_BOOK)
	private String body;

	@ActionField(label = "Method", description = "The method to use for the request", icon = Material.COMMAND_BLOCK, defaultValue = "GET")
	private String method;

	public HttpAction(String id, String label, List<String> description) {
		super(id, label, description, material);
	}

	@Override
	public void execute(Player trigger) {
		Bukkit.getScheduler().runTaskAsynchronously(RedhookPlugin.getInstance(), () -> {
			try {
				java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
				java.net.http.HttpRequest.Builder requestBuilder =
						java.net.http.HttpRequest.newBuilder().uri(java.net.URI.create(url));

				// Set method and body if body is not null
				if (body != null) {
					requestBuilder.method(method, java.net.http.HttpRequest.BodyPublishers.ofString(body));
				}
				else {
					requestBuilder.method(method, java.net.http.HttpRequest.BodyPublishers.noBody());
				}

				// Add headers if they exist
				if (headers != null) {
					headers.forEach(requestBuilder::header);
				}

				java.net.http.HttpRequest request = requestBuilder.build();
				java.net.http.HttpResponse<String> response =
						client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

				Bukkit.getLogger().info("Webhook sent to " + url + " with status code " + response.statusCode());
			}
			catch (Exception e) {
				Bukkit.getLogger().severe("Failed to execute webhook to " + url + ": " + e.getMessage());
				e.printStackTrace();
			}
		});
	}
}
