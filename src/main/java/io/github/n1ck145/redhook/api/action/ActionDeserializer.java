package io.github.n1ck145.redhook.api.action;

import java.util.Map;

import io.github.n1ck145.redhook.core.action.RedstoneActionBase;

public interface ActionDeserializer {
	RedstoneActionBase deserialize(Map<?, ?> map);
}
