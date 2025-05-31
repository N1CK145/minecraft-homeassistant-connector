package io.github.n1ck145.redhook.redstoneactions;

import io.github.n1ck145.redhook.utils.ActionDeserializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface RedstoneAction {

    String getName();
    void execute(Player trigger);
    ItemStack getIcon();
    String getId(); // unique action ID for config storage

    Map<String, Object> serialize();
}

