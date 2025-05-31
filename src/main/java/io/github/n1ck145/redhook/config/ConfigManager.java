package io.github.n1ck145.redhook.config;

import org.bukkit.plugin.Plugin;

public class ConfigManager {
    ActionsConfig actionsConfig;

    public ConfigManager(Plugin plugin){
        actionsConfig = new ActionsConfig(plugin, "actions.yml");
    }

    public ActionsConfig getActionsConfig() {
        return actionsConfig;
    }
}
