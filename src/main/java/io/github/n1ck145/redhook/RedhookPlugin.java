package io.github.n1ck145.redhook;

import io.github.n1ck145.redhook.commands.RedhookCommand;
import io.github.n1ck145.redhook.config.ConfigManager;
import io.github.n1ck145.redhook.listeners.ChatInputListener;
import io.github.n1ck145.redhook.listeners.DebugBlockBreakListener;
import io.github.n1ck145.redhook.listeners.InventoryClickListener;
import io.github.n1ck145.redhook.listeners.RedstoneBindListener;
import io.github.n1ck145.redhook.listeners.RedstonePowerChangeListener;
import io.github.n1ck145.redhook.listeners.DebugHologramListener;
import io.github.n1ck145.redhook.listeners.WandInteractionListener;
import io.github.n1ck145.redhook.manager.ActionFactory;
import io.github.n1ck145.redhook.manager.ActionRegistry;
import io.github.n1ck145.redhook.manager.RedstoneLinkManager;
import io.github.n1ck145.redhook.redstoneactions.CommandAction;
import io.github.n1ck145.redhook.redstoneactions.PlayerMessageAction;
import io.github.n1ck145.redhook.redstoneactions.lib.RedstoneAction;
import io.github.n1ck145.redhook.utils.ResponseMessage;
import io.github.n1ck145.redhook.redstoneactions.HttpAction;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public final class RedhookPlugin extends JavaPlugin {
    private static RedhookPlugin instance;

    public RedhookPlugin(){
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerEvents();
        registerActionTypes();
        loadConfigs();
        
        RedstoneLinkManager.initialize(this);

        this.getCommand("redhook").setExecutor(new RedhookCommand());
        
        printRegisterStatus();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("Disabled!");
    }

    private void printRegisterStatus(){
        List<String> actionTypes = ActionFactory.getRegisteredActions();
        this.getLogger().info("Registered " + actionTypes.size() + " action type(s):");
        for (String type : actionTypes) {
            this.getLogger().info("- " + type);
        }
        
        List<RedstoneAction> actions = ActionRegistry.getAll();
        this.getLogger().info("Registered " + actions.size() + " action(s)");
    }

    public static String getPrefix(){
        return "§8[§4Red§cHook§8] §r";
    }

    public static RedhookPlugin getInstance(){
        return instance;
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new RedstonePowerChangeListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new RedstoneBindListener(), this);
        getServer().getPluginManager().registerEvents(new DebugBlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new DebugHologramListener(), this);
        getServer().getPluginManager().registerEvents(new ChatInputListener(), this);
        getServer().getPluginManager().registerEvents(new WandInteractionListener(), this);
    }

    private void registerActionTypes() {
        ActionFactory.register(PlayerMessageAction.class);
        ActionFactory.register(HttpAction.class);
        ActionFactory.register(CommandAction.class);
    }

    private void loadConfigs(){
        ConfigManager configManager = new ConfigManager(this);

        configManager.getActionsConfig().loadActions();
    }

    public boolean reloadConfigs(){
        boolean success = true;
        
        ActionRegistry.clear();
        RedstoneLinkManager.clear();

        ConfigManager configManager = new ConfigManager(this);
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
