package io.github.n1ck145.redhook;

import org.bukkit.plugin.java.JavaPlugin;

public final class RedhookPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("Enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("Disabled!");
    }
}
