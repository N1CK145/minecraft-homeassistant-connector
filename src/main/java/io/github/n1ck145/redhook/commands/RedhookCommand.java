package io.github.n1ck145.redhook.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.n1ck145.redhook.RedhookPlugin;
import io.github.n1ck145.redhook.lib.StaticItems;

public class RedhookCommand implements CommandExecutor {
	private final ItemStack wand;
	private final ItemStack debug;

	public RedhookCommand() {
		this.wand = StaticItems.WAND.clone();
		this.debug = StaticItems.DEBUG.clone();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player player)) {
			sender.sendMessage("§cOnly players can use this command.");
			return true;
		}

		if (args.length == 0 || args[0].equalsIgnoreCase("wand")) {
			if (player.hasPermission("redhook.wand")) {
				player.getInventory().addItem(wand.clone());
				player.sendMessage(RedhookPlugin.getPrefix() + "§aYou received the §dRedhook Wand§a!");
			}
			else {
				player.sendMessage(RedhookPlugin.getPrefix() + "§cYou don't have permission to use this command.");
			}
			return true;
		}

		if (args.length == 0 || args[0].equalsIgnoreCase("debug")) {
			if (player.hasPermission("redhook.debug")) {
				player.getInventory().addItem(debug.clone());
				player.sendMessage(RedhookPlugin.getPrefix() + "§aYou received the §dRedhook Debug Stick§a!");
			}
			else {
				player.sendMessage(RedhookPlugin.getPrefix() + "§cYou don't have permission to use this command.");
			}
			return true;
		}

		if (args.length == 0 || args[0].equalsIgnoreCase("reload")) {
			if (player.hasPermission("redhook.reload")) {
				boolean reloadSuccess = RedhookPlugin.getInstance().reloadConfigs();

				if (reloadSuccess) {
					player.sendMessage(RedhookPlugin.getPrefix() + "§aPlugin reloaded!");
				}
				else {
					player.sendMessage(RedhookPlugin.getPrefix() + "§cFailed to reload plugin. Check console for more information.");
				}
			}
			else {
				player.sendMessage(RedhookPlugin.getPrefix() + "§cYou don't have permission to use this command.");
			}
			return true;
		}

		player.sendMessage(RedhookPlugin.getPrefix() + "§cUnknown subcommand. Try §e/redhook wand");
		return true;
	}
}