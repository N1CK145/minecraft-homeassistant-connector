package io.github.n1ck145.redhook.config;

import org.bukkit.plugin.Plugin;

import io.github.n1ck145.redhook.RedhookPlugin;

public class ConfigManager {
	private final ActionConfig actionsConfig;
	private final LinkConfig bindingsConfig;

	private static ConfigManager instance;

	private ConfigManager(Plugin plugin) {
		actionsConfig = new ActionConfig(plugin, "actions.yml");
		bindingsConfig = new LinkConfig(plugin, "bindings.yml");
	}

	public ActionConfig getActionsConfig() {
		return actionsConfig;
	}

	public LinkConfig getBindingsConfig() {
		return bindingsConfig;
	}

	public static ConfigManager getInstance() {
		if (instance == null) {
			instance = new ConfigManager(RedhookPlugin.getInstance());
		}

		return instance;
	}
}
