package io.github.n1ck145.redhook.utils;

import java.util.Map;

import io.github.n1ck145.redhook.redstoneactions.lib.AbstractRedstoneAction;

public interface ActionDeserializer {
    AbstractRedstoneAction deserialize(Map<?, ?> map);
}
