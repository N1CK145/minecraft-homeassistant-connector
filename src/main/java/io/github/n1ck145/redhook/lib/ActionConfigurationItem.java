package io.github.n1ck145.redhook.lib;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.n1ck145.redhook.utils.ItemBuilder;

public class ActionConfigurationItem {
    private final ItemStack icon;
    private final String label;
    private final String description;
    private final Class<?> valueType;

    public ActionConfigurationItem(Material icon, String label, String description, Class<?> valueType) {
        this.icon = new ItemBuilder(icon).name(label).lore(description).build();
        this.label = label;
        this.description = description;
        this.valueType = valueType;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public Class<?> getValueType() {
        return valueType;
    }
}
