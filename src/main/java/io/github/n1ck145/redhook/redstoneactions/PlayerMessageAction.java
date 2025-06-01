package io.github.n1ck145.redhook.redstoneactions;

import io.github.n1ck145.redhook.manager.ActionRegistry;
import io.github.n1ck145.redhook.utils.ColorMapper;
import io.github.n1ck145.redhook.utils.ItemBuilder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PlayerMessageAction implements RedstoneAction {
    private final String message;
    private final String targetPlayerName; // nullable
    private final String id;
    private final String label;
    private String[] description;

    public PlayerMessageAction(String id, String message, String targetPlayerName, String label, String[] description) {
        this.id = id;
        this.message = message;
        this.targetPlayerName = targetPlayerName;
        this.label = label;
        this.description = description;
    }

    @Override
    public void execute(Player trigger) {
        if (targetPlayerName != null) {
            Player target = Bukkit.getPlayer(targetPlayerName);
            if (target != null && target.isOnline()) {
                target.sendMessage("§e[Action] §r" + message);
            } else {
                if (trigger != null) trigger.sendMessage("§cTarget player is not online.");
            }
        } else {
            Bukkit.broadcastMessage("§e[Broadcast] §r" + message);
        }
    }

    @Override
    public ItemStack getIcon() {
        String title = ColorMapper.map(label);
        String[] lore = Arrays.stream(description).map(ColorMapper::map).toArray(String[]::new);
        return new ItemBuilder(Material.PAPER)
            .name(title)
            .lore(lore)
            .build();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("id", id);
        map.put("type", ActionRegistry.getTypeName(this));
        map.put("label", label);
        map.put("description", description);

        map.put("message", message);
        map.put("target", targetPlayerName == null ? null : targetPlayerName);

        return map;
    }

    public static PlayerMessageAction deserialize(Map<?, ?> map) {
        String id = (String) map.get("id");
        String message = (String) map.get("message");
        String targetStr = (String) map.get("target");
        String label = (String) map.get("label");
        ArrayList<String> description = (ArrayList<String>) map.get("description");

        String[] descriptionArray = description == null ? new String[0] : description.toArray(new String[0]);

        return new PlayerMessageAction(id, message, targetStr, label, descriptionArray);
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

