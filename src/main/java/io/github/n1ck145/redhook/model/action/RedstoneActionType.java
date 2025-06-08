package io.github.n1ck145.redhook.model.action;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;

import io.github.n1ck145.redhook.annotations.ActionFieldRepresentation;
import io.github.n1ck145.redhook.api.action.ActionDeserializer;
import io.github.n1ck145.redhook.api.action.RedstoneAction;

public class RedstoneActionType {
	private final String name;
	private final Class<? extends RedstoneAction> typeClass;
	private final ActionDeserializer deserializer;

	public RedstoneActionType(Class<? extends RedstoneAction> cls, ActionDeserializer deserializer) {
		this.typeClass = cls;
		this.deserializer = deserializer;
		this.name = cls.getSimpleName();
	}

	public String getName() {
		return name;
	}

	public Class<? extends RedstoneAction> getTypeClass() {
		return typeClass;
	}

	public ActionDeserializer getDeserializer() {
		return deserializer;
	}

	public List<ActionFieldConfiguration> getActionFields() {
		List<ActionFieldConfiguration> fields = new ArrayList<>();
		for (Field field : typeClass.getDeclaredFields()) {
			ActionFieldRepresentation annotation = field.getAnnotation(ActionFieldRepresentation.class);
			if (annotation != null) {
				fields.add(new ActionFieldConfiguration(annotation, field.getType()));
			}
		}
		return fields;
	}
}
