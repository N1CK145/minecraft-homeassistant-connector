package io.github.n1ck145.redhook.redstoneactions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface RedstoneAction {
    String getId();
    String getLabel();
    String[] getDescription();
    ItemStack getIcon();
    
    void execute(Player trigger);
    Map<String, Object> serialize();
}

