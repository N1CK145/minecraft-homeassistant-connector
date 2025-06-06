package io.github.n1ck145.redhook.manager;

import io.github.n1ck145.redhook.redstoneactions.lib.AbstractRedstoneAction;
import io.github.n1ck145.redhook.utils.ActionDeserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionFactory {
    private static final Map<String, ActionDeserializer> deserializers = new HashMap<>();
    private static final Map<String, Class<? extends AbstractRedstoneAction>> actionClasses = new HashMap<>();

    public static String register(Class<? extends AbstractRedstoneAction> actionClass) {
        String name = actionClass.getSimpleName();

        if (deserializers.containsKey(name)) {
            throw new IllegalArgumentException("Action class " + name + " is already registered by " + actionClasses.get(name).getName());
        }

        deserializers.put(name, (map) -> AbstractRedstoneAction.deserialize(map, actionClass));
        actionClasses.put(name, actionClass);

        return name;
    }

    public static AbstractRedstoneAction create(Map<?, ?> map) {
        String type = (String) map.get("type");
        
        ActionDeserializer deserializer = deserializers.get(type);

        if (deserializer != null) {
            return deserializer.deserialize(map);
        }

        return null;
    }

    public static List<String> getRegisteredActions() {
        return new ArrayList<>(deserializers.keySet());
    }
}
