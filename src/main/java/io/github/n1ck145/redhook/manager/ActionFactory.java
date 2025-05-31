package io.github.n1ck145.redhook.manager;

import io.github.n1ck145.redhook.redstoneactions.RedstoneAction;
import io.github.n1ck145.redhook.utils.ActionDeserializer;

import java.util.HashMap;
import java.util.Map;

public class ActionFactory {
    private static final Map<String, ActionDeserializer> deserializers = new HashMap<>();

    public static void register(String type, ActionDeserializer deserializer) {
        deserializers.put(type, deserializer);
    }

    public static RedstoneAction create(Map<?, ?> map) {
        String type = (String) map.get("type");
        ActionDeserializer deserializer = deserializers.get(type);

        if (deserializer != null) {
            return deserializer.deserialize(map);
        }

        return null;
    }
}
