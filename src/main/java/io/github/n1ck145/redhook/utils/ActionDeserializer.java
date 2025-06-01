package io.github.n1ck145.redhook.utils;

import io.github.n1ck145.redhook.redstoneactions.RedstoneAction;

import java.util.Map;

public interface ActionDeserializer {
    RedstoneAction deserialize(Map<?, ?> map);
}
