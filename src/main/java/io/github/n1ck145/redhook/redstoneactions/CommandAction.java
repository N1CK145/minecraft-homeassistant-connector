package io.github.n1ck145.redhook.redstoneactions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.n1ck145.redhook.manager.ActionRegistry;
import io.github.n1ck145.redhook.utils.ColorMapper;
import io.github.n1ck145.redhook.utils.ItemBuilder;

public class CommandAction implements RedstoneAction {
    private static final Material material = Material.COMMAND_BLOCK;
    private final String id;
    private final String label;
    private String[] description;
    private final String command;

    public CommandAction(String id, String label, String[] description, String command) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.command = command;
    }

    @Override
    public void execute(Player trigger) {
        // Execute the command as console to ensure it has proper permissions
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
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
        map.put("type", ActionRegistry.getTypeName(this));
        map.put("label", label);
        map.put("description", description);
        map.put("command", command);

        return map;
    }

    public static CommandAction deserialize(Map<?, ?> map) {
        String id = (String) map.get("id");
        String label = (String) map.get("label");
        Object descriptionObj = map.get("description");
        String[] descriptionArray;
        
        if (descriptionObj instanceof List<?>) {
            List<?> list = (List<?>) descriptionObj;
            descriptionArray = list.stream()
                .filter(obj -> obj instanceof String)
                .map(obj -> (String) obj)
                .toArray(String[]::new);
        } else if (descriptionObj instanceof String) {
            descriptionArray = new String[]{(String) descriptionObj};
        } else {
            descriptionArray = new String[0];
        }
        
        String command = (String) map.get("command");

        return new CommandAction(id, label, descriptionArray, command);
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