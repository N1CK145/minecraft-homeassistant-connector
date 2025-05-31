package io.github.n1ck145.redhook.commands;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class RedhookCommand implements CommandExecutor {
    private final ItemStack wand;

    public RedhookCommand() {
        // Prepare the wand item once
        this.wand = createWandItem();
    }

    private ItemStack createWandItem() {
        ItemStack item = new ItemStack(Material.CALIBRATED_SCULK_SENSOR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cRedstone Action Wand");
        meta.setLore(List.of("§7Use this to bind redstone actions", "§8(Right-click or break a block)"));

        NamespacedKey key = new NamespacedKey("redhook", "wand");
        meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("wand")) {
            player.getInventory().addItem(wand.clone());
            player.sendMessage("§aYou received the §dRedhook Wand§a!");
            return true;
        }

        player.sendMessage("§cUnknown subcommand. Try §e/redhook wand");
        return true;
    }
}