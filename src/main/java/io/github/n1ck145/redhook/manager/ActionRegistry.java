package io.github.n1ck145.redhook.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.n1ck145.redhook.redstoneactions.lib.RedstoneAction;
import io.github.n1ck145.redhook.redstoneactions.lib.ValidationResult;

public class ActionRegistry {
    private static final Map<String, RedstoneAction> actions = new HashMap<>();

    public static void register(RedstoneAction action) throws IllegalStateException {
        if (actions.containsKey(action.getId())) {
            throw new IllegalStateException("Action " + action.getId() + " is already registered");
        }

        ValidationResult validationResult = action.validate();
        if(!validationResult.isValid()) {
            throw new IllegalStateException("Action " + action.getId() + " is not configured correctly: " + validationResult.getErrorMessage()); 
        }

        actions.put(action.getId(), action);
    }

    public static RedstoneAction get(String id) {
        return actions.get(id);
    }

    public static List<RedstoneAction> getAll() {
        return actions.values().stream().toList();
    }

    public static void clear() {
        actions.clear();
    }

    public static String getTypeName(RedstoneAction action) {
        return action.getClass().getName();
    }
}
