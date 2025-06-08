package io.github.n1ck145.redhook.redstoneactions.lib;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import io.github.n1ck145.redhook.annotations.ActionField;
import io.github.n1ck145.redhook.lib.ActionConfigurationItem;
import io.github.n1ck145.redhook.manager.ActionRegistry;
import io.github.n1ck145.redhook.utils.ColorMapper;
import io.github.n1ck145.redhook.utils.ItemBuilder;

public abstract class AbstractRedstoneAction implements RedstoneAction {
    private final Material icon;

    @ActionField(hidden = true, required = true)
    private final String id;

    @ActionField(label = "Label", description = "The label of the action", icon = Material.NAME_TAG, required = true)
    private final String label;

    @ActionField(label = "Description", description = "The description of the action", icon = Material.PAPER, required = true)
    private final List<String> description;

    protected AbstractRedstoneAction(String id, String label, List<String> description, Material icon) {
        // Check if the class has the required annotation
        if (!getClass().isAnnotationPresent(ActionTypeRepresentation.class)) {
            throw new IllegalStateException(
                    "Class " + getClass().getName() + " must be annotated with @ActionTypeRepresentation");
        }

        this.id = id;
        this.label = label;
        this.description = description;
        this.icon = icon;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public List<String> getDescription() {
        return description;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemBuilder(icon)
                .name(ColorMapper.map(label))
                .lore(description.stream().map(ColorMapper::map).toList())
                .build();
    }

    @Override
    public Map<ActionConfigurationItem, Field> getConfigurationItems() {
        Map<ActionConfigurationItem, Field> items = new HashMap<>();

        Class<?> currentClass = getClass();
        while (currentClass != null && AbstractRedstoneAction.class.isAssignableFrom(currentClass)) {
            for (Field field : currentClass.getDeclaredFields()) {
                ActionField annotation = field.getAnnotation(ActionField.class);
                if (annotation != null) {
                    field.setAccessible(true);
                    items.put(new ActionConfigurationItem(
                            annotation,
                            field.getType()), field);
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return items;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("type", ActionRegistry.getTypeName(this));

        // Add annotated fields
        for (Field field : getClass().getDeclaredFields()) {
            ActionField annotation = field.getAnnotation(ActionField.class);
            if (annotation != null) {
                field.setAccessible(true);
                try {
                    map.put(field.getName(), field.get(this));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return map;
    }

    public static <T extends AbstractRedstoneAction> T deserialize(Map<?, ?> configurationMap, Class<T> actionClass) {
        try {
            String id = (String) configurationMap.get("id");

            String label = ColorMapper.map((String) configurationMap.get("label"));

            List<String> description = null;

            if (configurationMap.get("description") instanceof List<?> list) {
                description = list.stream().map(Object::toString).map(ColorMapper::map).toList();
            } else if (configurationMap.get("description") instanceof String string) {
                description = List.of(ColorMapper.map(string));
            }

            if (label != null && label.isEmpty()) {
                Bukkit.getLogger().info("Label is empty, setting to null");
                label = null;
            }
            if (description != null && description.isEmpty()) {
                Bukkit.getLogger().info("Description is empty, setting to null");
                description = null;
            }

            // Create instance of actionClass
            T instance = actionClass.getDeclaredConstructor(String.class, String.class, List.class).newInstance(id,
                    label, description);

            // Set any additional fields from the configuration map
            for (Field field : actionClass.getDeclaredFields()) {
                ActionField annotation = field.getAnnotation(ActionField.class);
                if (annotation != null) {
                    field.setAccessible(true);
                    Object value = configurationMap.get(field.getName());

                    // Apply color mapping for String fields
                    if (field.getType() == String.class && value instanceof String) {
                        value = ColorMapper.map((String) value);
                    }
                    // Apply color mapping for List<String> fields
                    else if (value instanceof List<?> list && field.getType() == List.class) {
                        value = list.stream()
                                .map(Object::toString)
                                .map(ColorMapper::map)
                                .toList();
                    }

                    field.set(instance, value);
                }
            }

            return instance;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize action: " + e.getMessage(), e);
        }
    }

    @Override
    /**
     * Checks if all required fields are set and if the action is not already
     * registered.
     * 
     * @return ValidationResult containing the validation status and any error
     *         message
     */
    public ValidationResult validate() {

        Bukkit.getLogger().info("Starting validation of action " + id);
        for (var keyValue : getConfigurationItems().entrySet()) {
            ActionConfigurationItem configItem = keyValue.getKey();
            Field field = keyValue.getValue();

            Bukkit.getLogger().info("Validating field: " + field.getName() + ", required: " + configItem.isRequired());

            if (configItem.isRequired()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(this);
                    if (value == null) {
                        Bukkit.getLogger().warning("Validation failed - missing required field: " + field.getName());
                        return ValidationResult.error("Action is missing required field: " + field.getName());
                    }
                    Bukkit.getLogger()
                            .info("Field " + field.getName() + " validated successfully with value: '" + value + "'");
                } catch (IllegalAccessException e) {
                    Bukkit.getLogger().severe(
                            "Validation failed - could not access field " + field.getName() + ": " + e.getMessage());
                    return ValidationResult.error("Failed to access field " + field.getName() + ": " + e.getMessage());
                }
            } else {
                Bukkit.getLogger().info("Skipping validation of optional field: " + field.getName());
            }
        }
        Bukkit.getLogger().info("All required fields validated successfully for action " + id);

        if (ActionRegistry.get(id) != null) {
            return ValidationResult.error("Action " + id + " is already registered");
        }

        return ValidationResult.success();
    }
}