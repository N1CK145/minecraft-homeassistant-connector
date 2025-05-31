package io.github.n1ck145.redhook.manager;

import io.github.n1ck145.redhook.redstoneactions.RedstoneAction;
import io.github.n1ck145.redhook.redstoneactions.RedstoneActionInstance;
import io.github.n1ck145.redhook.redstoneactions.TriggerCondition;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class RedstoneLinkManager {
    private static final Map<Location, RedstoneActionInstance> bindings = new HashMap<>();

    public static void bindBlock(Block block, RedstoneActionInstance actionInstance) {
        bindings.put(block.getLocation(), actionInstance);
    }

    public static void bindBlock(Block block, RedstoneAction action, TriggerCondition triggerCondition) {
        bindBlock(block, new RedstoneActionInstance(action, triggerCondition));
    }

    public static RedstoneActionInstance getActionInstance(Block block) {
        return bindings.get(block.getLocation());
    }

    public static boolean hasBinding(Location location) {
        return bindings.containsKey(location);
    }

    public static void unbindBlock(Location location) {
        bindings.remove(location);
        // TODO: Update persistent storage if needed
    }

    public static void triggerOn(Block block, Player triggerPlayer) {
        handleTrigger(block, triggerPlayer, true);
    }

    public static void triggerOff(Block block, Player triggerPlayer) {
        handleTrigger(block, triggerPlayer, false);
    }

    private static void handleTrigger(Block block, Player triggerPlayer, boolean isRedstoneOn){
        RedstoneActionInstance instance = getActionInstance(block);
        if(instance == null)
            return;

        RedstoneAction action = ActionRegistry.get(instance.getAction().getId());

        if (action != null && instance.getTriggerCondition().matches(isRedstoneOn)) {
            action.execute(triggerPlayer);
        }
    }
}