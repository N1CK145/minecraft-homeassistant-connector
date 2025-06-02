package io.github.n1ck145.redhook.redstoneactions;

import io.github.n1ck145.redhook.lib.ActionConfigurationItem;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface RedstoneAction {
  String getId();
  String getLabel();
  String[] getDescription();
  ItemStack getIcon();
  Map<String, ActionConfigurationItem> getConfigurationItems();

  void execute(Player trigger);
  Map<String, Object> serialize();
}
