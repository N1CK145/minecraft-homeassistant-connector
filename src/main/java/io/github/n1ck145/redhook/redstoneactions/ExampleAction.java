package io.github.n1ck145.redhook.redstoneactions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.n1ck145.redhook.manager.ActionRegistry;
import io.github.n1ck145.redhook.utils.ColorMapper;
import io.github.n1ck145.redhook.utils.ItemBuilder;

// To create your own action, copy this class and modify it
//
// To register the action, you need to register it in the ActionFactory class
// ActionFactory.register("ExampleAction", ExampleAction::deserialize);
// You should register it in the RedhookPlugin class
public class ExampleAction implements RedstoneAction {
    private static final Material material = Material.DIAMOND;
    private final String id;
    private final String label;
    private String[] description;

    public ExampleAction(String id, String label, String[] description) {
        this.id = id;
        this.label = label;
        this.description = description;
    }

    @Override
    public void execute(Player trigger) {
        Bukkit.broadcastMessage("Example action executed!");
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

        return map;
    }

    public static ExampleAction deserialize(Map<?, ?> map) {
        String id = (String) map.get("id");
        String label = (String) map.get("label");
        ArrayList<String> description = (ArrayList<String>) map.get("description");
        String[] descriptionArray = description == null ? new String[0] : description.toArray(new String[0]);

        return new ExampleAction(id, label, descriptionArray);
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
