package io.github.n1ck145.redhook.manager;

import io.github.n1ck145.redhook.redstoneactions.lib.AbstractRedstoneAction;
import io.github.n1ck145.redhook.redstoneactions.lib.RedstoneAction;
import io.github.n1ck145.redhook.redstoneactions.lib.RedstoneActionType;
import io.github.n1ck145.redhook.utils.ActionDeserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ActionFactory {
    private static final Map<String, RedstoneActionType> registeredTypes = new HashMap<>();

    public static RedstoneActionType register(Class<? extends AbstractRedstoneAction> actionClass) {
        return register(actionClass, (map) -> AbstractRedstoneAction.deserialize(map, actionClass));
    }

    public static RedstoneActionType register(Class<? extends RedstoneAction> actionClass,
            ActionDeserializer deserializer) {
        RedstoneActionType actionType = new RedstoneActionType(actionClass, deserializer);

        if (registeredTypes.containsKey(actionType.getName())) {
            throw new IllegalArgumentException(
                    "Action class " + actionType.getName() + " is already registered by "
                            + actionType.getClass().getName());
        }

        registeredTypes.put(actionType.getName(), actionType);
        return actionType;
    }

    public static RedstoneAction create(Map<?, ?> map) {
        String typeName = (String) map.get("type");
        String id = (String) map.get("id");
        RedstoneActionType type = registeredTypes.get(typeName);

        if (type == null)
            throw new IllegalArgumentException("Unknown action type: " + typeName);

        if (id == null)
            throw new IllegalArgumentException("Action id must be set");

        return type.getDeserializer().deserialize(map);
    }

    public static RedstoneAction create(Class<? extends RedstoneAction> actionClass, String id,
            String label, List<String> description) {

        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("type", actionClass.getSimpleName());
        map.put("label", label);
        map.put("description", description);

        return create(map);
    }

    public static RedstoneAction create(Class<? extends RedstoneAction> actionClass) {
        return create(actionClass, UUID.randomUUID().toString(), null, null);
    }

    public static RedstoneAction create(Class<? extends RedstoneAction> actionClass, String id) {
        return create(actionClass, id, null, null);
    }

    public static List<RedstoneActionType> getRegisteredActionTypes() {
        return new ArrayList<>(registeredTypes.values());
    }
}
