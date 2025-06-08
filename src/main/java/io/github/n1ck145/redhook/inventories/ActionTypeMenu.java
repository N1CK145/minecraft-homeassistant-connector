package io.github.n1ck145.redhook.inventories;

import io.github.n1ck145.redhook.RedhookPlugin;
import io.github.n1ck145.redhook.inventories.lib.AbstractPaginatedMenu;
import io.github.n1ck145.redhook.manager.ActionFactory;
import io.github.n1ck145.redhook.manager.MenuManager;
import io.github.n1ck145.redhook.redstoneactions.lib.RedstoneActionType;
import io.github.n1ck145.redhook.utils.ItemBuilder;
import io.github.n1ck145.redhook.redstoneactions.lib.ActionTypeRepresentation;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;

public class ActionTypeMenu extends AbstractPaginatedMenu<RedstoneActionType> {
  private static final String TITLE = RedhookPlugin.getPrefix() + "Select Action Type";

  public ActionTypeMenu(Player player) {
    super(player, ActionFactory.getRegisteredActionTypes());
  }

  @Override
  protected String getTitle() {
    return TITLE;
  }

  @Override
  protected ItemStack getItemRepresentation(RedstoneActionType item) {
    ActionTypeRepresentation representation = item.getTypeClass().getAnnotation(ActionTypeRepresentation.class);

    if (representation == null) {
      return new ItemStack(Material.BARRIER);
    }

    return new ItemBuilder(representation.icon())
        .name("§7" + splitCamelCase(item.getName()))
        .lore(splitLongString(representation.description(), 60))
        .build();
  }

  @Override
  protected void onItemClick(RedstoneActionType item) {
    player.closeInventory();

    String newActionId = player.getName() + "_" + UUID.randomUUID();
    CreateActionMenu menu = new CreateActionMenu(player, ActionFactory.create(item.getTypeClass(), newActionId));
    MenuManager.openMenu(player, menu);
  }

  private List<String> splitLongString(String str, int lineLength) {
    List<String> lines = new ArrayList<>();
    int lastBreak = 0;
    int lastSpace = 0;

    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);

      if (c == ' ') {
        lastSpace = i;
      }

      if (c == '\n') {
        lines.add(str.substring(lastBreak, i));
        lastBreak = i + 1;
        lastSpace = lastBreak;
      }

      if (i - lastBreak >= lineLength) {
        if (lastSpace > lastBreak) {
          lines.add(str.substring(lastBreak, lastSpace));
          lastBreak = lastSpace + 1;
        } else {
          lines.add(str.substring(lastBreak, i));
          lastBreak = i;
        }
        lastSpace = lastBreak;
      }
    }

    if (lastBreak < str.length()) {
      lines.add(str.substring(lastBreak));
    }

    return lines;
  }

  private String splitCamelCase(String original) {
    if (original == null || original.isEmpty()) {
      return original;
    }

    StringBuilder result = new StringBuilder();
    result.append(original.charAt(0));

    for (int i = 1; i < original.length(); i++) {
      char current = original.charAt(i);
      if (Character.isUpperCase(current)) {
        result.append(" ");
      }
      result.append(current);
    }

    return result.toString();
  }
}
