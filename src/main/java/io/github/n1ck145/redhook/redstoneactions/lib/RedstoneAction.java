package io.github.n1ck145.redhook.redstoneactions.lib;

import io.github.n1ck145.redhook.lib.ActionConfigurationItem;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface RedstoneAction {
  String getId();

  String getLabel();

  List<String> getDescription();

  ItemStack getIcon();

  Map<ActionConfigurationItem, Field> getConfigurationItems();

  void execute(Player trigger);

  Map<String, Object> serialize();

  ValidationResult validate();
}