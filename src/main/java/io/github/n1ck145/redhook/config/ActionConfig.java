package io.github.n1ck145.redhook.config;

import io.github.n1ck145.redhook.actions.lib.RedstoneAction;
import io.github.n1ck145.redhook.manager.ActionFactory;
import io.github.n1ck145.redhook.manager.ActionRegistry;
import io.github.n1ck145.redhook.utils.ResponseMessage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ActionConfig {

	private final Plugin plugin;
	private final File configFile;
	private final FileConfiguration config;

	public ActionConfig(Plugin plugin, String fileName) {
		this.plugin = plugin;
		this.configFile = new File(plugin.getDataFolder(), fileName);

		if (!configFile.exists()) {
			plugin.saveResource(fileName, false);
		}
		this.config = YamlConfiguration.loadConfiguration(configFile);
	}

	public ResponseMessage loadActions() {
		List<Map<?, ?>> list = config.getMapList("actions");

		for (Map<?, ?> map : list) {
			RedstoneAction action = ActionFactory.createAction(map);

			if (action != null) {
				try {
					ActionRegistry.register(action);
				}
				catch (Exception e) {
					plugin.getLogger().warning("Failed to register action: " + e.getMessage());
					return new ResponseMessage("Failed to register action: " + e.getMessage(), false);
				}
			}
			else {
				plugin.getLogger().warning("Unknown or invalid action type in config: " + map.get("type"));
				return new ResponseMessage("Unknown or invalid action type in config: " + map.get("type"), false);
			}
		}

		return new ResponseMessage("Successfully loaded " + list.size() + " action(s)", true);
	}

	public void saveActions() {
		List<Map<String, Object>> list = new ArrayList<>();
		for (RedstoneAction action : ActionRegistry.getAll()) {
			list.add(action.serialize());
		}
		config.set("actions", list);
		try {
			config.save(configFile);
		}
		catch (IOException e) {
			plugin.getLogger().severe("Could not save actions config file!");
			e.printStackTrace();
		}
	}

	public FileConfiguration getConfig() {
		return config;
	}
}