package io.github.n1ck145.redhook.redstoneactions;

import io.github.n1ck145.redhook.annotations.ActionField;
import io.github.n1ck145.redhook.redstoneactions.lib.AbstractRedstoneAction;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerMessageAction extends AbstractRedstoneAction {

    private static final Material material = Material.PAPER;

    @ActionField(label = "Message", description = "The message to send to the player", icon = Material.PAPER, required = true)
    private String message;

    @ActionField(label = "Target Player", description = "The player to send the message to", icon = Material.PAPER, required = false)
    private String targetPlayerName;

    public PlayerMessageAction(String id, String label, List<String> description) {
        super(id, label, description, material);
    }

    @Override
    public void execute(Player trigger) {
        if (targetPlayerName != null) {
            Player target = Bukkit.getPlayer(targetPlayerName);
            if (target != null && target.isOnline()) {
                target.sendMessage("§e[Action] §r" + message);
            } else {
                if (trigger != null) trigger.sendMessage("§cTarget player is not online.");
            }
        } else {
            Bukkit.broadcastMessage("§e[Broadcast] §r" + message);
        }
    }
}

