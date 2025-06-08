package io.github.n1ck145.redhook.model.action;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import io.github.n1ck145.redhook.annotations.ActionFieldRepresentation;

public class ActionFieldConfiguration {
	private final Material material;
	private final String label;
	private final List<String> description;
	private final Class<?> valueType;
	private final boolean hidden;
	private Object value;
	private boolean isRequired;

	public ActionFieldConfiguration(ActionFieldRepresentation annotation, Class<?> valueType) {
		this.label = annotation.label();
		this.description = Arrays.asList(annotation.description());
		this.valueType = valueType;
		this.hidden = annotation.hidden();
		this.material = annotation.icon();
		this.isRequired = annotation.required();
	}

	public boolean isRequired() {
		return isRequired;
	}

	public Material getMaterial() {
		return material;
	}

	public String getLabel() {
		return label;
	}

	public List<String> getDescription() {
		return description;
	}

	public Class<?> getValueType() {
		return valueType;
	}

	public boolean isHidden() {
		return hidden;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		if (value == null)
			return "§4Not set";

		switch (value) {
			case List l :
				if (l.isEmpty()) {
					return "[]";
				}

				return "- " + String.join("\n- ", l.stream().map(Object::toString).toList());

			default :
				break;
		}

		return value.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ActionFieldConfiguration that = (ActionFieldConfiguration) o;
		return hidden == that.hidden && isRequired == that.isRequired && material == that.material
				&& label.equals(that.label) && description.equals(that.description) && valueType.equals(that.valueType);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(material, label, description, valueType, hidden, isRequired);
	}
}