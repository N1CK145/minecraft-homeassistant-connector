package io.github.n1ck145.redhook;

import io.github.n1ck145.redhook.commands.RedhookCommand;
import io.github.n1ck145.redhook.config.ConfigManager;
import io.github.n1ck145.redhook.listeners.BlockBreakListener;
import io.github.n1ck145.redhook.listeners.InventoryClickListener;
import io.github.n1ck145.redhook.listeners.RedstoneBindListener;
import io.github.n1ck145.redhook.listeners.RedstonePowerChangeListener;
import io.github.n1ck145.redhook.manager.ActionFactory;
import io.github.n1ck145.redhook.manager.ActionRegistry;
import io.github.n1ck145.redhook.manager.RedstoneLinkManager;
import io.github.n1ck145.redhook.redstoneactions.PlayerMessageAction;
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
        registerActions();
        loadConfigs();
        
        RedstoneLinkManager.initialize(this);

        this.getCommand("redhook").setExecutor(new RedhookCommand());

        this.getLogger().info("Enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("Disabled!");
    }

    public static String getPrefix(){
        return "§8[§4Red§cHook§8] §r";
    }

    public static RedhookPlugin getInstance(){
        return instance;
    }

    private void registerEvents() {
        this.getLogger().info("Register events...");

        getServer().getPluginManager().registerEvents(new RedstonePowerChangeListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new RedstoneBindListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
    }

    private void registerActions() {
        this.getLogger().info("Register actions...");
        
        ActionFactory.register("PlayerMessageAction", PlayerMessageAction::deserialize);
    }

    private void loadConfigs(){
        ConfigManager configManager = new ConfigManager(this);

        configManager.getActionsConfig().loadActions();
    }

    public void reloadConfigs(){
        ActionRegistry.clear();
        RedstoneLinkManager.clear();

        ConfigManager configManager = new ConfigManager(this);
        configManager.getActionsConfig().loadActions();
        configManager.getBindingsConfig().loadBindings();

        RedstoneLinkManager.initialize(this);
    }
}
