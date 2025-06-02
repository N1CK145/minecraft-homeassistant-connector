package io.github.n1ck145.redhook.inventories;

import io.github.n1ck145.redhook.manager.MenuManager;
import io.github.n1ck145.redhook.manager.ActionFactory;
import io.github.n1ck145.redhook.redstoneactions.RedstoneAction;
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
import java.util.ArrayList;
import java.util.Arrays;

public class CreateActionMenu implements Menu {

  private final Player player;
  private String label;
  private String description;
  private String command;
  private String message;
  private String target;
  private String url;
  private String method;
  private String body;
  private Map<String, String> headers;
  private final Class<? extends RedstoneAction> actionType;
  private static final String TITLE = "§6Configure Action";
  private String currentEditingField = null;
  private static final File ACTIONS_FILE = new File("plugins/Redhook/actions.yml");

  //public CreateActionMenu(Player player) {
    // this(player, ActionType.COMMAND);
  //}

  public CreateActionMenu(Player player, Class<? extends RedstoneAction> actionType) {
    this.player = player;
    this.actionType = actionType;
    this.command = "";
    this.message = "";
    this.target = "";
    this.url = "";
    this.method = "GET";
    this.body = "";
    this.headers = new HashMap<>();
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
            "§7Current: §f" + (label != null ? label : "None"),
            "§7Click to set a new label"
        )
        .build();
    inv.setItem(10, labelButton);

    ItemStack descButton = new ItemBuilder(Material.BOOK)
        .name("§eSet Description")
        .lore(
            "§7Current: §f" + (description != null ? description : "None"),
            "§7Click to set a new description"
        )
        .build();
    inv.setItem(12, descButton);

    // Action-specific buttons
    String actionTypeName = actionType.getSimpleName();
    switch (actionTypeName) {
      case "CommandAction":
        ItemStack cmdButton = new ItemBuilder(Material.COMMAND_BLOCK)
            .name("§eSet Command")
            .lore(
                "§7Current: §f" + (command.isEmpty() ? "None" : command),
                "§7Click to set a new command"
            )
            .build();
        inv.setItem(14, cmdButton);
        break;
      case "PlayerMessageAction":
        ItemStack msgButton = new ItemBuilder(Material.BOOK)
            .name("§eSet Message")
            .lore(
                "§7Current: §f" + (message.isEmpty() ? "None" : message),
                "§7Click to set the message"
            )
            .build();
        inv.setItem(14, msgButton);

        ItemStack targetButton = new ItemBuilder(Material.PLAYER_HEAD)
            .name("§eSet Target Player")
            .lore(
                "§7Current: §f" + (target.isEmpty() ? "All Players" : target),
                "§7Click to set target player",
                "§7Leave empty to broadcast to all players"
            )
            .build();
        inv.setItem(16, targetButton);
        break;
      case "HttpAction":
        ItemStack urlButton = new ItemBuilder(Material.PAPER)
            .name("§eSet URL")
            .lore(
                "§7Current: §f" + (url.isEmpty() ? "None" : url),
                "§7Click to set the URL"
            )
            .build();
        inv.setItem(10, urlButton);

        ItemStack methodButton = new ItemBuilder(Material.COMPARATOR)
            .name("§eSet Method")
            .lore(
                "§7Current: §f" + method,
                "§7Click to set the HTTP method"
            )
            .build();
        inv.setItem(11, methodButton);

        ItemStack bodyButton = new ItemBuilder(Material.BOOK)
            .name("§eSet Body")
            .lore(
                "§7Current: §f" + (body.isEmpty() ? "None" : body),
                "§7Click to set the request body"
            )
            .build();
        inv.setItem(12, bodyButton);

        ItemStack headersButton = new ItemBuilder(Material.BOOK)
            .name("§eSet Headers")
            .lore(
                "§7Current Headers:",
                headers.isEmpty() ? "§7None" : String.join("\n", headers.entrySet().stream()
                    .map(e -> "§7" + e.getKey() + ": " + e.getValue())
                    .toArray(String[]::new))
            )
            .build();
        inv.setItem(13, headersButton);
        break;
      default:
        player.sendMessage("§cUnknown action type: " + actionTypeName);
        player.closeInventory();
        return;
    }

    ItemStack saveButton = new ItemBuilder(Material.EMERALD_BLOCK)
        .name("§aSave Action")
        .lore(
            "§7Click to save this action",
            isReadyToSave() ? "§aReady to save!" : "§cRequired fields not set!"
        )
        .build();
    inv.setItem(22, saveButton);

    player.openInventory(inv);
  }

  private boolean isReadyToSave() {
    String actionTypeName = actionType.getSimpleName();
    switch (actionTypeName) {
      case "CommandAction":
        return !command.isEmpty();
      case "PlayerMessageAction":
        return !message.isEmpty();
      case "HttpAction":
        return !url.isEmpty();
      default:
        return false;
    }
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
      case 14, 16: // Action-specific button
        handleActionSpecificButton(event);
        break;
      case 22: // Save button
        if (!isReadyToSave()) {
          player.sendMessage("§cRequired fields are not set!");
          return;
        }
        saveAction();
        player.closeInventory();
        player.sendMessage("§aAction saved successfully!");
        break;
    }
  }

  private void handleActionSpecificButton(InventoryClickEvent event) {
    String actionTypeName = actionType.getSimpleName();
    switch (actionTypeName) {
      case "CommandAction":
        currentEditingField = "command";
        player.closeInventory();
        player.sendMessage("§ePlease enter the command in chat:");
        break;
      case "PlayerMessageAction":
        if (event.getRawSlot() == 14) {
          currentEditingField = "message";
          player.closeInventory();
          player.sendMessage("§ePlease enter the message in chat:");
        } else if (event.getRawSlot() == 16) {
          currentEditingField = "target";
          player.closeInventory();
          player.sendMessage("§ePlease enter the target player name (or leave empty for all players):");
        }
        break;
      case "HttpAction":
        int slot = event.getRawSlot();
        switch (slot) {
          case 10: // URL
            currentEditingField = "url";
            player.closeInventory();
            player.sendMessage("§ePlease enter the URL in chat:");
            break;
          case 11: // Method
            currentEditingField = "method";
            player.closeInventory();
            player.sendMessage("§ePlease enter the HTTP method (GET, POST, PUT, DELETE):");
            break;
          case 12: // Body
            currentEditingField = "body";
            player.closeInventory();
            player.sendMessage("§ePlease enter the request body in chat:");
            break;
          case 13: // Headers
            currentEditingField = "headers";
            player.closeInventory();
            player.sendMessage("§ePlease enter a header in the format 'key:value' (or 'done' to finish):");
            break;
        }
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
      case "message":
        this.message = message;
        break;
      case "target":
        this.target = message;
        break;
      case "url":
        this.url = message;
        break;
      case "method":
        this.method = message.toUpperCase();
        break;
      case "body":
        this.body = message;
        break;
      case "headers":
        if (message.equalsIgnoreCase("done")) {
          currentEditingField = null;
          open();
          return;
        }
        String[] parts = message.split(":", 2);
        if (parts.length == 2) {
          headers.put(parts[0].trim(), parts[1].trim());
          player.sendMessage("§aHeader added! Enter another header or type 'done' to finish.");
          return;
        }
        player.sendMessage("§cInvalid header format! Use 'key:value'");
        return;
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
      actionMap.put("type", actionType.getSimpleName());
      actionMap.put("label", label);
      actionMap.put("description", new ArrayList<>(Arrays.asList(description)));
      
      // Add action-specific fields
      String actionTypeName = actionType.getSimpleName();
      switch (actionTypeName) {
        case "CommandAction":
          actionMap.put("command", command);
          break;
        case "PlayerMessageAction":
          actionMap.put("message", message);
          if (!target.isEmpty()) {
            actionMap.put("target", target);
          }
          break;
        case "HttpAction":
          actionMap.put("url", url);
          actionMap.put("method", method);
          if (!body.isEmpty()) {
            actionMap.put("body", body);
          }
          if (!headers.isEmpty()) {
            actionMap.put("headers", headers);
          }
          break;
      }
      
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
