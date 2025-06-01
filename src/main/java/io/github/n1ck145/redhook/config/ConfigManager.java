package io.github.n1ck145.redhook.config;

import org.bukkit.plugin.Plugin;

public class ConfigManager {
    private final ActionsConfig actionsConfig;
    private final BindingsConfig bindingsConfig;

    public ConfigManager(Plugin plugin) {
        actionsConfig = new ActionsConfig(plugin, "actions.yml");
        bindingsConfig = new BindingsConfig(plugin, "bindings.yml");
    }

    public ActionsConfig getActionsConfig() {
        return actionsConfig;
    }

    public BindingsConfig getBindingsConfig() {
        return bindingsConfig;
    }
}
