package io.github.n1ck145.redhook.inventories;

import io.github.n1ck145.redhook.manager.MenuManager;
import io.github.n1ck145.redhook.utils.ItemBuilder;
import io.github.n1ck145.redhook.redstoneactions.CommandAction;
import io.github.n1ck145.redhook.redstoneactions.PlayerMessageAction;
import io.github.n1ck145.redhook.redstoneactions.HttpAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ActionTypeMenu implements Menu {

  private final Player player;
  private static final String TITLE = "§6Select Action Type";

  public ActionTypeMenu(Player player) {
    this.player = player;
  }

  @Override
  public void open() {
    Inventory inv = Bukkit.createInventory(null, 27, TITLE);

    // Set background
    ItemStack background = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
      .name(" ")
      .build();

    for (int i = 0; i < inv.getSize(); i++) {
      inv.setItem(i, background);
    }

    // Action type buttons
    ItemStack commandAction = new ItemBuilder(Material.COMMAND_BLOCK)
      .name("§eCommand Action")
      .lore(
        "Execute a command when triggered",
        "§7Click to create a command action"
      )
      .build();
    inv.setItem(13, commandAction);

    ItemStack playerMessageAction = new ItemBuilder(Material.PLAYER_HEAD)
      .name("§ePlayer Message Action")
      .lore(
        "Send a message to a player or all players",
        "§7Click to create a player message action"
      )
      .build();
    inv.setItem(15, playerMessageAction);

    ItemStack httpAction = new ItemBuilder(Material.NETHER_STAR)
      .name("§eHTTP Action")
      .lore(
        "Send a HTTP request when triggered",
        "§7Click to create an HTTP action"
      )
      .build();
    inv.setItem(11, httpAction);

    player.openInventory(inv);
  }

  @Override
  public void handleClick(InventoryClickEvent event) {
    if (!event.getView().getTitle().equals(TITLE)) return;
    event.setCancelled(true);

    ItemStack clickedItem = event.getCurrentItem();

    if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
    if (
      clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()
    ) {
      if (
        clickedItem.getItemMeta().getDisplayName().equals("§eCommand Action")
      ) {
        player.closeInventory();
        MenuManager.openMenu(
          player,
          new CreateActionMenu(player, CommandAction.class)
        );
      } else if (
        clickedItem
          .getItemMeta()
          .getDisplayName()
          .equals("§ePlayer Message Action")
      ) {
        player.closeInventory();
        MenuManager.openMenu(
          player,
          new CreateActionMenu(player, PlayerMessageAction.class)
        );
      } else if (
        clickedItem.getItemMeta().getDisplayName().equals("§eHTTP Action")
      ) {
        player.closeInventory();
        MenuManager.openMenu(
          player,
          new CreateActionMenu(player, HttpAction.class)
        );
      }
    }
  }
}
