package io.github.n1ck145.redhook.inventories;

import io.github.n1ck145.redhook.RedhookPlugin;
import io.github.n1ck145.redhook.lib.ActionConfigurationItem;
import io.github.n1ck145.redhook.manager.ActionFactory;
import io.github.n1ck145.redhook.redstoneactions.RedstoneAction;
import io.github.n1ck145.redhook.utils.ItemBuilder;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
  private static final File ACTIONS_FILE = new File(
    "plugins/Redhook/actions.yml"
  );

  //public CreateActionMenu(Player player) {
  // this(player, ActionType.COMMAND);
  //}

  public CreateActionMenu(
    Player player,
    Class<? extends RedstoneAction> actionType
  ) {
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
    Inventory inv = Bukkit.createInventory(null, 36, TITLE);

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
    inv.setItem(2, labelButton);

    ItemStack descButton = new ItemBuilder(Material.BOOK)
      .name("§eSet Description")
      .lore(
        "§7Current: §f" + (description != null ? description : "None"),
        "§7Click to set a new description"
      )
      .build();
    inv.setItem(4, descButton);

    ItemStack saveButton = new ItemBuilder(Material.EMERALD_BLOCK)
      .name("§aSave Action")
      .lore(
        "§7Click to save this action",
        isReadyToSave() ? "§aReady to save!" : "§cRequired fields not set!"
      )
      .build();
    inv.setItem(6, saveButton);

    // Get position of the action
    Map<Integer, ItemStack> configItems = getConfigurationItems();
    configItems.forEach(inv::setItem);

    player.openInventory(inv);
  }

  private boolean isReadyToSave() {
    try {
      // Create a temporary action instance to get its configuration items
      Map<String, Object> tempMap = new HashMap<>();
      tempMap.put("id", "temp");
      tempMap.put("label", "temp");
      tempMap.put("description", new String[0]);
      tempMap.put("type", actionType.getSimpleName());

      // Add required fields based on action type
      String actionTypeName = actionType.getSimpleName();
      switch (actionTypeName) {
        case "CommandAction":
          tempMap.put("command", "");
          break;
        case "PlayerMessageAction":
          tempMap.put("message", "");
          tempMap.put("target", null);
          break;
        case "HttpAction":
          tempMap.put("url", "");
          tempMap.put("method", "GET");
          tempMap.put("body", "");
          tempMap.put("headers", new HashMap<String, String>());
          break;
      }

      // Create the action and get its configuration items
      RedstoneAction action = ActionFactory.create(tempMap);
      if (action != null) {
        Map<String, ActionConfigurationItem> configItems =
          action.getConfigurationItems();

        // Check if all required fields are set
        for (String key : configItems.keySet()) {
          String value = getCurrentValue(key);
          if (value == null || value.isEmpty()) {
            return false;
          }
        }
        return true;
      }
    } catch (Exception e) {
      player.sendMessage(
        "§cError checking if ready to save: " + e.getMessage()
      );
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public void handleClick(InventoryClickEvent event) {
    if (!event.getView().getTitle().equals(TITLE)) return;
    event.setCancelled(true);

    int slot = event.getRawSlot();
    ItemStack clickedItem = event.getCurrentItem();
    Map<Integer, ItemStack> configItems = getConfigurationItems();

    switch (slot) {
      case 2: // Label button
        currentEditingField = "label";
        player.closeInventory();
        player.sendMessage("§ePlease enter the label in chat:");
        break;
      case 4: // Description button
        currentEditingField = "description";
        player.closeInventory();
        player.sendMessage("§ePlease enter the description in chat:");
        break;
      case 6: // Save button
        if (!isReadyToSave()) {
          player.sendMessage("§cRequired fields are not set!");
          return;
        }
        saveAction();
        player.closeInventory();
        player.sendMessage("§aAction saved successfully!");
        break;
      default:
        // Check if the clicked slot contains a configuration item
        if (configItems.containsKey(slot) && clickedItem != null) {
          handleActionSpecificButton(event);
        }
        break;
    }
  }

  private void handleActionSpecificButton(InventoryClickEvent event) {
    try {
      // Create a temporary action instance to get its configuration items
      Map<String, Object> tempMap = new HashMap<>();
      tempMap.put("id", "temp");
      tempMap.put("label", "temp");
      tempMap.put("description", new String[0]);
      tempMap.put("type", actionType.getSimpleName());

      // Add required fields based on action type
      String actionTypeName = actionType.getSimpleName();
      switch (actionTypeName) {
        case "CommandAction":
          tempMap.put("command", "");
          break;
        case "PlayerMessageAction":
          tempMap.put("message", "");
          tempMap.put("target", null);
          break;
        case "HttpAction":
          tempMap.put("url", "");
          tempMap.put("method", "GET");
          tempMap.put("body", "");
          tempMap.put("headers", new HashMap<String, String>());
          break;
      }

      // Create the action and get its configuration items
      RedstoneAction action = ActionFactory.create(tempMap);
      if (action != null) {
        Map<String, ActionConfigurationItem> configItems =
          action.getConfigurationItems();

        // Find the configuration item for the clicked slot
        int slot = event.getRawSlot();
        int currentSlot = 10;
        for (Map.Entry<
          String,
          ActionConfigurationItem
        > entry : configItems.entrySet()) {
          if (currentSlot == slot) {
            String key = entry.getKey();
            currentEditingField = key;
            player.closeInventory();
            player.sendMessage(
              "§ePlease enter the " +
              entry.getValue().getLabel().toLowerCase() +
              " in chat:"
            );
            return;
          }
          currentSlot++;
        }
      }
    } catch (Exception e) {
      player.sendMessage(
        "§cError handling configuration item: " + e.getMessage()
      );
      e.printStackTrace();
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
          player.sendMessage(
            "§aHeader added! Enter another header or type 'done' to finish."
          );
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
      YamlConfiguration config = YamlConfiguration.loadConfiguration(
        ACTIONS_FILE
      );
      String id = "action_" + UUID.randomUUID().toString().substring(0, 8);

      // Create a new action map
      Map<String, Object> actionMap = new HashMap<>();
      actionMap.put("id", id);
      actionMap.put("label", label);
      actionMap.put("description", description);
      actionMap.put("type", actionType.getSimpleName());

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
      List<Map<String, Object>> actions = (List<Map<String, Object>>) (List<
          ?
        >) config.getMapList("actions");
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

  private Map<Integer, ItemStack> getConfigurationItems() {
    Map<Integer, ItemStack> items = new HashMap<>();

    try {
      // Create a temporary action instance to get its configuration items
      Map<String, Object> tempMap = new HashMap<>();
      tempMap.put("id", "temp");
      tempMap.put("label", "temp");
      tempMap.put("description", new String[0]);
      tempMap.put("type", actionType.getSimpleName());

      // Create the action and get its configuration items
      RedstoneAction action = ActionFactory.create(tempMap);
      if (action != null) {
        Map<String, ActionConfigurationItem> configItems =
          action.getConfigurationItems();

        // Map each configuration item to a slot
        int slot = 10;
        for (Map.Entry<
          String,
          ActionConfigurationItem
        > entry : configItems.entrySet()) {
          String key = entry.getKey();
          ActionConfigurationItem configItem = entry.getValue();

          // Get the current value for this field
          String currentValue = getCurrentValue(key);

          // Create the item with current value
          ItemStack item = new ItemBuilder(configItem.getIcon().getType())
            .name(configItem.getLabel())
            .lore(
              "§7Current: §f" + (currentValue != null ? currentValue : "None"),
              configItem.getDescription(),
              "§7Click to set a new value"
            )
            .build();

          items.put(slot++, item);
        }
      }
    } catch (Exception e) {
      player.sendMessage(
        "§cError creating configuration items: " + e.getMessage()
      );
      e.printStackTrace();
    }

    return items;
  }

  private String getCurrentValue(String key) {
    switch (key) {
      case "command":
        return command;
      case "message":
        return message;
      case "target":
        return target;
      case "url":
        return url;
      case "method":
        return method;
      case "body":
        return body;
      case "headers":
        return headers.isEmpty() ? null : headers.toString();
      default:
        return null;
    }
  }
}
