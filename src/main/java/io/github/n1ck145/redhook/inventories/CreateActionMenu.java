package io.github.n1ck145.redhook.inventories;

import io.github.n1ck145.redhook.manager.MenuManager;
import io.github.n1ck145.redhook.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.github.n1ck145.redhook.RedhookPlugin;

public class CreateActionMenu implements Menu {

  private final Player player;
  private String label;
  private String description;
  private String command;
  private static final String TITLE = "§6Configure Action";
  private String currentEditingField = null;
  private static final File ACTIONS_FILE = new File("plugins/Redhook/actions.yml");

  public CreateActionMenu(Player player) {
    this.player = player;
    this.label = "New Command Action";
    this.description = "Execute a command when triggered";
    this.command = "";
  }

  public String getCurrentEditingField() {
    return currentEditingField;
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

    // Action buttons
    ItemStack labelButton = new ItemBuilder(Material.NAME_TAG)
        .name("§eSet Label")
        .lore(
            "§7Current: §f" + label,
            "§7Click to set a new label"
        )
        .build();
    inv.setItem(10, labelButton);

    ItemStack descButton = new ItemBuilder(Material.BOOK)
        .name("§eSet Description")
        .lore(
            "§7Current: §f" + description,
            "§7Click to set a new description"
        )
        .build();
    inv.setItem(12, descButton);

    ItemStack cmdButton = new ItemBuilder(Material.COMMAND_BLOCK)
        .name("§eSet Command")
        .lore(
            "§7Current: §f" + (command.isEmpty() ? "None" : command),
            "§7Click to set a new command"
        )
        .build();
    inv.setItem(14, cmdButton);

    ItemStack saveButton = new ItemBuilder(Material.EMERALD_BLOCK)
        .name("§aSave Action")
        .lore(
            "§7Click to save this action",
            command.isEmpty() ? "§cNo command set!" : "§aReady to save!"
        )
        .build();
    inv.setItem(16, saveButton);

    player.openInventory(inv);
  }

  @Override
  public void handleClick(InventoryClickEvent event) {
    if (!event.getView().getTitle().equals(TITLE)) return;
    event.setCancelled(true);

    int slot = event.getRawSlot();
    
    switch (slot) {
      case 10: // Label button
        currentEditingField = "label";
        player.closeInventory();
        player.sendMessage("§ePlease enter the label in chat:");
        break;
      case 12: // Description button
        currentEditingField = "description";
        player.closeInventory();
        player.sendMessage("§ePlease enter the description in chat:");
        break;
      case 14: // Command button
        currentEditingField = "command";
        player.closeInventory();
        player.sendMessage("§ePlease enter the command in chat:");
        break;
      case 16: // Save button
        if (command.isEmpty()) {
          player.sendMessage("§cYou must set a command before saving!");
          return;
        }
        saveAction();
        player.closeInventory();
        player.sendMessage("§aAction saved successfully!");
        break;
    }
  }

  public void handleChatInput(String message) {
    if (currentEditingField == null) return;

    switch (currentEditingField) {
      case "label":
        this.label = message;
        break;
      case "description":
        this.description = message;
        break;
      case "command":
        this.command = message;
        break;
    }

    currentEditingField = null;
    open();
  }

  private void saveAction() {
    try {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(ACTIONS_FILE);
      String id = "action_" + UUID.randomUUID().toString().substring(0, 8);
      
      // Create a new action map
      Map<String, Object> actionMap = new HashMap<>();
      actionMap.put("id", id);
      actionMap.put("type", "CommandAction");
      actionMap.put("label", label);
      actionMap.put("description", description);
      actionMap.put("command", command);
      
      // Get existing actions list or create new one
      @SuppressWarnings("unchecked")
      List<Map<String, Object>> actions = (List<Map<String, Object>>) (List<?>) config.getMapList("actions");
      actions.add(actionMap);
      
      // Set the updated actions list
      config.set("actions", actions);
      config.save(ACTIONS_FILE);

      // Reload actions after saving
      RedhookPlugin.getInstance().reloadConfigs();
    } catch (IOException e) {
        player.sendMessage("§cError saving action: " + e.getMessage());
    }
  }
}

