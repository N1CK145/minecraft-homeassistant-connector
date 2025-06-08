package io.github.n1ck145.redhook;

import io.github.n1ck145.redhook.api.action.RedstoneAction;
import io.github.n1ck145.redhook.commands.RedhookCommand;
import io.github.n1ck145.redhook.config.ConfigManager;
import io.github.n1ck145.redhook.implementation.action.CommandAction;
import io.github.n1ck145.redhook.implementation.action.HttpAction;
import io.github.n1ck145.redhook.implementation.action.PlayerMessageAction;
import io.github.n1ck145.redhook.listeners.ChatInputListener;
import io.github.n1ck145.redhook.listeners.DebugBlockBreakListener;
import io.github.n1ck145.redhook.listeners.InventoryClickListener;
import io.github.n1ck145.redhook.listeners.RedhookToolsInteractionListener;
import io.github.n1ck145.redhook.listeners.RedstonePowerChangeListener;
import io.github.n1ck145.redhook.listeners.DebugHologramListener;
import io.github.n1ck145.redhook.manager.action.ActionFactory;
import io.github.n1ck145.redhook.manager.action.ActionRegistry;
import io.github.n1ck145.redhook.manager.action.RedstoneLinkManager;
import io.github.n1ck145.redhook.model.action.RedstoneActionType;
import io.github.n1ck145.redhook.util.ResponseMessage;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public final class RedhookPlugin extends JavaPlugin {
	private static RedhookPlugin instance;

	public RedhookPlugin() {
		instance = this;
	}

	@Override
	public void onEnable() {
		registerActionTypes();
		registerEvents();
		loadConfigs();

		RedstoneLinkManager.initialize(this);

		this.getCommand("redhook").setExecutor(new RedhookCommand());

		printRegisterStatus();
	}

	@Override
	public void onDisable() {
		this.getLogger().info("Disabled!");
	}

	private void printRegisterStatus() {
		List<RedstoneActionType> actionTypes = ActionFactory.getRegisteredActionTypes();
		this.getLogger().info("Registered " + actionTypes.size() + " action type(s):");
		for (RedstoneActionType type : actionTypes) {
			this.getLogger().info("- " + type.getName());
		}

		List<RedstoneAction> actions = ActionRegistry.getAll();
		this.getLogger().info("Registered " + actions.size() + " action(s)");
	}

	public static String getPrefix() {
		return "§8[§4Red§cHook§8] §r";
	}

	public static RedhookPlugin getInstance() {
		return instance;
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new RedstonePowerChangeListener(), this);
		getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		getServer().getPluginManager().registerEvents(new RedhookToolsInteractionListener(), this);
		getServer().getPluginManager().registerEvents(new DebugBlockBreakListener(), this);
		getServer().getPluginManager().registerEvents(new DebugHologramListener(), this);
		getServer().getPluginManager().registerEvents(new ChatInputListener(), this);
	}

	private void registerActionTypes() {
		ActionFactory.registerType(PlayerMessageAction.class);
		ActionFactory.registerType(HttpAction.class);
		ActionFactory.registerType(CommandAction.class);
	}

	private void loadConfigs() {
		ConfigManager configManager = ConfigManager.getInstance();

		configManager.getActionsConfig().loadActions();
	}

	public boolean reloadConfigs() {
		boolean success = true;

		ActionRegistry.clear();
		RedstoneLinkManager.clear();

		ConfigManager configManager = ConfigManager.getInstance();
		ResponseMessage actionMessage = configManager.getActionsConfig().loadActions();

		if (!actionMessage.isSuccess()) {
			this.getLogger().severe(actionMessage.getMessage());
			success = false;
		}

		configManager.getBindingsConfig().loadBindings();

		RedstoneLinkManager.initialize(this);

		return success;
	}
}
