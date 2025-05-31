package io.github.n1ck145.redhook.redstoneactions;

import io.github.n1ck145.redhook.utils.ActionDeserializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMessageAction implements RedstoneAction {
    private static final String name = "PlayerMessageAction";

    private final String message;
    private final UUID targetPlayerUUID; // nullable
    private final String id;

    public PlayerMessageAction(String id, String message, UUID targetPlayerUUID) {
        this.id = id;
        this.message = message;
        this.targetPlayerUUID = targetPlayerUUID;
    }

    @Override
    public void execute(Player trigger) {
        if (targetPlayerUUID != null) {
            Player target = Bukkit.getPlayer(targetPlayerUUID);
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
        String title = targetPlayerUUID == null ? "§bBroadcast Message" : "§aMessage Player";
        String lore = "§7\"" + message + "\"";
        return createItem(Material.PAPER, title, lore);
    }

    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("type", "PlayerMessageAction");
        map.put("message", message);
        map.put("target", targetPlayerUUID == null ? null : targetPlayerUUID.toString());
        return map;
    }

    public static PlayerMessageAction deserialize(Map<?, ?> map) {
        String id = (String) map.get("id");
        String message = (String) map.get("message");
        String targetStr = (String) map.get("target");
        UUID target = null;

        if (targetStr != null && !targetStr.equalsIgnoreCase("null")) {
            try {
                target = UUID.fromString(targetStr);
            } catch (IllegalArgumentException e) {
                // log warning if needed
            }
        }

        return new PlayerMessageAction(id, message, target);
    }

    @Override
    public String getName(){
        return name;
    }
}

