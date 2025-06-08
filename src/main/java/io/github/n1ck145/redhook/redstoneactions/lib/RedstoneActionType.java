package io.github.n1ck145.redhook.redstoneactions.lib;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;

import io.github.n1ck145.redhook.annotations.ActionField;
import io.github.n1ck145.redhook.lib.ActionConfigurationItem;
import io.github.n1ck145.redhook.utils.ActionDeserializer;

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

	public List<ActionConfigurationItem> getActionFields() {
		List<ActionConfigurationItem> fields = new ArrayList<>();
		for (Field field : typeClass.getDeclaredFields()) {
			ActionField annotation = field.getAnnotation(ActionField.class);
			if (annotation != null) {
				fields.add(new ActionConfigurationItem(annotation, field.getType()));
			}
		}
		return fields;
	}
}
