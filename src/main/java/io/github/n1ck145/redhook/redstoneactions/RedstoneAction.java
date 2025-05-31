package io.github.n1ck145.redhook.redstoneactions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface RedstoneAction {
    String getName();
    String getLabel();
    void execute(Player trigger);
    ItemStack getIcon();
    String getId(); // unique action ID for config storage
    String[] getDescription();

    Map<String, Object> serialize();
}

