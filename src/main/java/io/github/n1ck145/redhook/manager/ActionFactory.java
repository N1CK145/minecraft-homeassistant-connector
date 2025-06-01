package io.github.n1ck145.redhook.manager;

import io.github.n1ck145.redhook.redstoneactions.RedstoneAction;
import io.github.n1ck145.redhook.utils.ActionDeserializer;

import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
    private static final Map<String, ActionDeserializer> deserializers = new HashMap<>();
    private static final Map<String, Class<? extends RedstoneAction>> actionClasses = new HashMap<>();

    public static String register(Class<? extends RedstoneAction> actionClass, ActionDeserializer deserializer) {
        String name = actionClass.getSimpleName();

        if (deserializers.containsKey(name)) {
            throw new IllegalArgumentException("Action class " + name + " is already registered by " + actionClasses.get(name).getName());
        }

        deserializers.put(name, deserializer);
        actionClasses.put(name, actionClass);

        return name;
    }

    public static RedstoneAction create(Map<?, ?> map) {
        String type = (String) map.get("type");
        ActionDeserializer deserializer = deserializers.get(type);

        if (deserializer != null) {
            return deserializer.deserialize(map);
        }

        return null;
    }

    public static String[] getRegisteredActions() {
        return deserializers.keySet().toArray(new String[0]);
    }
}
