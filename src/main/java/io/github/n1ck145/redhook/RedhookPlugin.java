package io.github.n1ck145.redhook;

import io.github.n1ck145.redhook.commands.CommandManager;
import io.github.n1ck145.redhook.inventories.WebhookSelectorInventory;
import io.github.n1ck145.redhook.inventories.WebhookSelectorPoweredInventory;
import io.github.n1ck145.redhook.inventories.WebhookSelectorUnpoweredInventory;
import io.github.n1ck145.redhook.inventories.WebhookSelectorViewerInventory;
import io.github.n1ck145.redhook.listeners.BindBlockToWebhookListener;
import io.github.n1ck145.redhook.listeners.InventoryClickListener;
import io.github.n1ck145.redhook.listeners.RedstonePowerChangeListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedhookPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        RegisterEvents();

        this.getCommand("redhook").setExecutor(new CommandManager());

        System.out.println("Enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Disabled!");
    }

    private void RegisterEvents() {
        getServer().getPluginManager().registerEvents(WebhookSelectorPoweredInventory.GetInstance(), this);
        getServer().getPluginManager().registerEvents(WebhookSelectorUnpoweredInventory.GetInstance(), this);
        getServer().getPluginManager().registerEvents(WebhookSelectorViewerInventory.GetInstance(), this);

        getServer().getPluginManager().registerEvents(new RedstonePowerChangeListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new BindBlockToWebhookListener(), this);
    }
}
