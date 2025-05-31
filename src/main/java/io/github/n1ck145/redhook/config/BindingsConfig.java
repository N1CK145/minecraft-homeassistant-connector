package io.github.n1ck145.redhook.config;

import io.github.n1ck145.redhook.manager.ActionRegistry;
import io.github.n1ck145.redhook.redstoneactions.RedstoneActionInstance;
import io.github.n1ck145.redhook.redstoneactions.TriggerCondition;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BindingsConfig {
    private final Plugin plugin;
    private final File configFile;
    private final FileConfiguration config;

    public BindingsConfig(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), fileName);

        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveBindings(Map<Location, ArrayList<RedstoneActionInstance>> bindings) {
        List<Map<String, Object>> bindingsList = new ArrayList<>();
        
        for (Map.Entry<Location, ArrayList<RedstoneActionInstance>> entry : bindings.entrySet()) {
            Location loc = entry.getKey();
            ArrayList<RedstoneActionInstance> instances = entry.getValue();
            
            Map<String, Object> bindingMap = new HashMap<>();
            bindingMap.put("world", loc.getWorld().getName());
            bindingMap.put("x", loc.getX());
            bindingMap.put("y", loc.getY());
            bindingMap.put("z", loc.getZ());
            
            List<Map<String, Object>> instancesList = new ArrayList<>();
            for (RedstoneActionInstance instance : instances) {
                Map<String, Object> instanceMap = new HashMap<>();
                instanceMap.put("actionId", instance.getAction().getId());
                instanceMap.put("triggerCondition", instance.getTriggerCondition().name());
                instancesList.add(instanceMap);
            }
            bindingMap.put("instances", instancesList);
            
            bindingsList.add(bindingMap);
        }
        
        config.set("bindings", bindingsList);
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save bindings config file!");
            e.printStackTrace();
        }
    }

    public Map<Location, ArrayList<RedstoneActionInstance>> loadBindings() {
        Map<Location, ArrayList<RedstoneActionInstance>> bindings = new HashMap<>();
        List<Map<?, ?>> bindingsList = config.getMapList("bindings");

        for (Map<?, ?> bindingMap : bindingsList) {
            String worldName = (String) bindingMap.get("world");
            double x = ((Number) bindingMap.get("x")).doubleValue();
            double y = ((Number) bindingMap.get("y")).doubleValue();
            double z = ((Number) bindingMap.get("z")).doubleValue();
            
            Location loc = new Location(plugin.getServer().getWorld(worldName), x, y, z);
            ArrayList<RedstoneActionInstance> instances = new ArrayList<>();
            
            List<Map<?, ?>> instancesList = (List<Map<?, ?>>) bindingMap.get("instances");
            for (Map<?, ?> instanceMap : instancesList) {
                String actionId = (String) instanceMap.get("actionId");
                String triggerConditionStr = (String) instanceMap.get("triggerCondition");
                
                // Get the action from registry and create instance
                // Note: You'll need to ensure the action exists in the registry
                // This might need to be handled after actions are loaded
                // For now, we'll just log a warning if the action isn't found
                if (ActionRegistry.get(actionId) != null) {
                    TriggerCondition triggerCondition = TriggerCondition.valueOf(triggerConditionStr);
                    instances.add(new RedstoneActionInstance(ActionRegistry.get(actionId), triggerCondition));
                } else {
                    plugin.getLogger().warning("Action not found for ID: " + actionId);
                }
            }
            
            bindings.put(loc, instances);
        }

        return bindings;
    }
} 