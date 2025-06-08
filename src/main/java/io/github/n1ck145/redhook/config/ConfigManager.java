package io.github.n1ck145.redhook.config;

import org.bukkit.plugin.Plugin;

import io.github.n1ck145.redhook.RedhookPlugin;

public class ConfigManager {
	private final ActionsConfig actionsConfig;
	private final BindingsConfig bindingsConfig;

	private static ConfigManager instance;

	private ConfigManager(Plugin plugin) {
		actionsConfig = new ActionsConfig(plugin, "actions.yml");
		bindingsConfig = new BindingsConfig(plugin, "bindings.yml");
	}

	public ActionsConfig getActionsConfig() {
		return actionsConfig;
	}

	public BindingsConfig getBindingsConfig() {
		return bindingsConfig;
	}

	public static ConfigManager getInstance() {
		if (instance == null) {
			instance = new ConfigManager(RedhookPlugin.getInstance());
		}

		return instance;
	}	
}
