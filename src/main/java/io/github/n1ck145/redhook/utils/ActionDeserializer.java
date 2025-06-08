package io.github.n1ck145.redhook.utils;

import java.util.Map;

import io.github.n1ck145.redhook.actions.lib.RedstoneActionBase;

public interface ActionDeserializer {
	RedstoneActionBase deserialize(Map<?, ?> map);
}
