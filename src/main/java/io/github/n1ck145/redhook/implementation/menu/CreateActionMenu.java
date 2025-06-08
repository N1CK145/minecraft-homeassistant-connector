package io.github.n1ck145.redhook.implementation.menu;

import io.github.n1ck145.redhook.RedhookPlugin;
import io.github.n1ck145.redhook.api.action.RedstoneAction;
import io.github.n1ck145.redhook.config.ActionConfig;
import io.github.n1ck145.redhook.config.ConfigManager;
import io.github.n1ck145.redhook.constants.PlayerState;
import io.github.n1ck145.redhook.core.inventory.PaginatedMenu;
import io.github.n1ck145.redhook.manager.action.ActionFactory;
import io.github.n1ck145.redhook.manager.action.ActionRegistry;
import io.github.n1ck145.redhook.manager.menu.MenuManager;
import io.github.n1ck145.redhook.manager.state.PlayerStateManager;
import io.github.n1ck145.redhook.model.ValidationResult;
import io.github.n1ck145.redhook.model.action.ActionFieldConfiguration;
import io.github.n1ck145.redhook.util.ItemBuilder;
import it.unimi.dsi.fastutil.Arrays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class CreateActionMenu extends PaginatedMenu<ActionFieldConfiguration> {
	private static final String TITLE = RedhookPlugin.getPrefix() + "Configure Action";
	private ActionFieldConfiguration currentEditingItem;
	private final RedstoneAction action;

	public CreateActionMenu(Player player, RedstoneAction action) {
		super(player, new ArrayList<>(
				action.getConfigurationItems().keySet().stream().filter(item -> !item.isHidden()).toList()));
		this.action = action;

		PlayerStateManager.setPlayerState(player, PlayerState.REDHOOK_PERSIST_INVENTORY_STATE, true);
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected ItemStack getItemRepresentation(ActionFieldConfiguration item) {
		List<String> lore = new ArrayList<>();
		lore.addAll(item.getDescription());
		lore.add("");
		lore.add("§7Current Value: §e" + item.toString());
		lore.add("§7Value Type: §e" + (item.getValueType().getSimpleName()));
		lore.add("§7Required: " + (item.isRequired() ? "§aYes" : "§cNo"));

		ItemBuilder builder = new ItemBuilder(item.getMaterial()).name("§7" + item.getLabel()).lore(lore);

		if (item.getValue() != null)
			builder.addGlint();

		return builder.build();
	}

	@Override
	public void handleClick(InventoryClickEvent event) {
		super.handleClick(event);

		boolean lastSlotClicked = event.getRawSlot() == inventory.getSize() - 1;
		if (lastSlotClicked)
			submitAction();
	}

	@Override
	protected void onItemClick(ActionFieldConfiguration item) {
		player.sendMessage(RedhookPlugin.getPrefix() + "§8===== §6Chat input commands §8=====");
		player.sendMessage(RedhookPlugin.getPrefix());
		player.sendMessage(RedhookPlugin.getPrefix() + "§7:q §8- §rQuit edit dialog");
		player.sendMessage(RedhookPlugin.getPrefix() + "§7:clear §8- §rClears the value from the item");
		player.sendMessage(RedhookPlugin.getPrefix());

		player.sendMessage(RedhookPlugin.getPrefix() + "§aEnter a §e" + item.getValueType().getSimpleName()
				+ " §afor §e" + item.getLabel());

		currentEditingItem = item;
		player.closeInventory();
	}

	@Override
	protected void renderPage() {
		super.renderPage();

		ValidationResult validation = action.validate();

		ItemBuilder builder = new ItemBuilder(validation.isValid() ? Material.LIME_WOOL : Material.RED_WOOL);

		if (validation.isValid()) {
			builder.addGlint();
			builder.name("§aPublish action");
		}
		else {
			builder.name("§cInvalid action configuration");
			builder.lore(validation.getErrorMessage());
		}

		inventory.setItem(inventory.getSize() - 1, builder.build());
	}

	public void handleChatInput(String message) {
		if (currentEditingItem == null)
			return;

		// Quit edit dialog
		if (message.equals(":q")) {
			currentEditingItem = null;
			player.sendMessage(RedhookPlugin.getPrefix() + "§cCanceled");
			MenuManager.openMenu(player, this);
			return;
		}
		else if (message.equals(":clear")) {
			currentEditingItem.setValue(null);
			currentEditingItem = null;
			player.sendMessage(RedhookPlugin.getPrefix() + "§aCleared!");
			MenuManager.openMenu(player, this);
			return;
		}

		boolean valueComplete = false;

		if (currentEditingItem.getValueType() == String.class) {
			currentEditingItem.setValue(message);
			valueComplete = true;
		}
		else if (currentEditingItem.getValueType() == Integer.class) {
			try {
				currentEditingItem.setValue(Integer.parseInt(message));
				valueComplete = true;
			}
			catch (NumberFormatException e) {
				player.sendMessage(RedhookPlugin.getPrefix() + "§cInvalid number");
			}
		}
		else if (currentEditingItem.getValueType() == Boolean.class) {
			try {
				currentEditingItem.setValue(Boolean.parseBoolean(message));
				valueComplete = true;
			}
			catch (IllegalArgumentException e) {
				player.sendMessage(RedhookPlugin.getPrefix() + "§cInvalid value. Use true or false");
			}
		}
		else if (List.class.isAssignableFrom(currentEditingItem.getValueType())) {
			if (message.equalsIgnoreCase(":wq")) {
				valueComplete = true;
			}
			else {
				List<String> currentList = (List<String>) currentEditingItem.getValue();
				if (currentList == null) {
					currentList = new ArrayList<>();
				}
				currentList.add(message.trim());
				currentEditingItem.setValue(currentList);
				player.sendMessage(RedhookPlugin.getPrefix() + "§aAdded §6'" + message.trim()
						+ "'§a to list. Type §6':wq'§a when finished.");
			}
		}

		if (valueComplete) {
			updateFieldValue(currentEditingItem);

			player.sendMessage(RedhookPlugin.getPrefix() + "§6" + currentEditingItem.getLabel() + "§a set to §6"
					+ currentEditingItem.getValue());

			currentEditingItem = null;
			MenuManager.openMenu(player, this);
		}
	}

	private void updateFieldValue(ActionFieldConfiguration configurationItem) {
		try {
			Field field = action.getConfigurationItems().get(configurationItem);

			if (field == null) {
				return;
			}
			field.set(action, configurationItem.getValue());
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void submitAction() {
		ValidationResult result = action.validate();

		if (!result.isValid()) {
			player.sendMessage(RedhookPlugin.getPrefix() + "§c" + result.getErrorMessage());
			player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.natural(0, Note.Tone.C));
			return;
		}

		String actionId = action.getId();
		String actionLabel = action.getLabel();
		ActionRegistry.register(action);
		player.sendMessage(RedhookPlugin.getPrefix() + "§aAction created successfully!");
		player.sendMessage(RedhookPlugin.getPrefix() + "§7ID: §6" + actionId + " §7Label: §6" + actionLabel);
		player.playNote(player.getLocation(), Instrument.PLING, Note.natural(1, Note.Tone.C));
		PlayerStateManager.removePlayerState(player, PlayerState.REDHOOK_PERSIST_INVENTORY_STATE);
		player.closeInventory();

		ConfigManager.getInstance().getActionsConfig().saveActions();
	}
}
