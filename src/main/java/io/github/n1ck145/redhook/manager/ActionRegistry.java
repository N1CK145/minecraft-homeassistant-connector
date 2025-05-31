package io.github.n1ck145.redhook.manager;

import io.github.n1ck145.redhook.redstoneactions.RedstoneAction;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionRegistry {
    private static final Map<String, RedstoneAction> actions = new HashMap<>();

    public static void register(RedstoneAction action) {
        actions.put(action.getId(), action);
    }

    public static RedstoneAction get(String id) {
        return actions.get(id);
    }

    public static List<RedstoneAction> getAll() {
        return actions.values().stream().toList();
    }
}
