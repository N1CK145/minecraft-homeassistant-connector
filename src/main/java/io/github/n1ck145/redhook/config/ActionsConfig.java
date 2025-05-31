package io.github.n1ck145.redhook.config;

import io.github.n1ck145.redhook.manager.ActionFactory;
import io.github.n1ck145.redhook.manager.ActionRegistry;
import io.github.n1ck145.redhook.redstoneactions.PlayerMessageAction;
import io.github.n1ck145.redhook.redstoneactions.RedstoneAction;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ActionsConfig {

    private final Plugin plugin;
    private final File configFile;
    private final FileConfiguration config;

    public ActionsConfig(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), fileName);

        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void loadActions() {
        List<Map<?, ?>> list = config.getMapList("actions");

        for (Map<?, ?> map : list) {
            RedstoneAction action = ActionFactory.create(map);

            if (action != null) {
                ActionRegistry.register(action);
            } else {
                plugin.getLogger().warning("Unknown or invalid action type in config: " + map.get("type"));
            }
        }
    }

    public void saveActions() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RedstoneAction action : ActionRegistry.getAll()) {
            list.add(action.serialize());
        }
        config.set("actions", list);
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save actions config file!");
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }
}