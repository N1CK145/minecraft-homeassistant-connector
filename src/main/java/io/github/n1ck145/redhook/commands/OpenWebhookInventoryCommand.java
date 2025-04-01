package io.github.n1ck145.redhook.commands;

import io.github.n1ck145.redhook.inventories.WebhookSelectorInventory;
import io.github.n1ck145.redhook.inventories.WebhookSelectorPoweredInventory;
import io.github.n1ck145.redhook.inventories.WebhookSelectorViewerInventory;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenWebhookInventoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage("This command can only run as a player!");
            return false;
        }

        openWebhookInventory((Player) commandSender, WebhookSelectorViewerInventory.GetInstance());

        return true;
    }

    private void openWebhookInventory(Player player, WebhookSelectorInventory inventory) {
        inventory.updateInventory();
        player.openInventory(inventory.getInventory());
    }
}
