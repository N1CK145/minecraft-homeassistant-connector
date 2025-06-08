package io.github.n1ck145.redhook.api.action;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.n1ck145.redhook.model.ValidationResult;
import io.github.n1ck145.redhook.model.action.ActionFieldConfiguration;

public interface RedstoneAction {
	String getId();

	String getLabel();

	List<String> getDescription();

	ItemStack getIcon();

	Map<ActionFieldConfiguration, Field> getConfigurationItems();

	void execute(Player trigger);

	Map<String, Object> serialize();

	ValidationResult validate();
}