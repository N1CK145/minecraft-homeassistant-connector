package io.github.n1ck145.redhook.commands;

import jdk.jshell.execution.FailOverExecutionControlProvider;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class CommandManager implements CommandExecutor {

    private final OpenWebhookInventoryCommand openWebhookInventoryCommand;

    public CommandManager() {
        this.openWebhookInventoryCommand = new OpenWebhookInventoryCommand();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            openWebhookInventoryCommand.onCommand(sender, command, label, args);
            return false;
        }

        // Handle the subcommands
        switch (args[0].toLowerCase()) {
            default:
                sender.sendMessage("Unknown subcommand. Usage: /redhook <list|add>");
                return false;
        }
    }
}