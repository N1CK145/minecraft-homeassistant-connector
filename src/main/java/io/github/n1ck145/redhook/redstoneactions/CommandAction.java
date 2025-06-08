package io.github.n1ck145.redhook.redstoneactions;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import io.github.n1ck145.redhook.annotations.ActionField;
import io.github.n1ck145.redhook.redstoneactions.lib.AbstractRedstoneAction;
import io.github.n1ck145.redhook.redstoneactions.lib.ActionTypeRepresentation;

@ActionTypeRepresentation(icon = Material.COMMAND_BLOCK, description = "Executes Minecraft commands when triggered by redstone. Supports all server commands and can be used to automate various game actions.")
public class CommandAction extends AbstractRedstoneAction {
	private static final Material material = Material.COMMAND_BLOCK;

	@ActionField(label = "Command", description = "The command to execute", icon = Material.COMMAND_BLOCK, required = true)
	private String command;

	public CommandAction(String id, String label, List<String> description) {
		super(id, label, description, material);
	}

	@Override
	public void execute(Player trigger) {
		// Execute the command as console to ensure it has proper permissions
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
	}
}