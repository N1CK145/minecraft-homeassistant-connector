package io.github.n1ck145.redhook.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtil {
    private static final NamespacedKey DEBUG_KEY = new NamespacedKey("redhook", "debug");
    private static final NamespacedKey WAND_KEY = new NamespacedKey("redhook", "wand");

    public static boolean isDebugItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        Byte value = meta.getPersistentDataContainer().get(DEBUG_KEY, PersistentDataType.BYTE);
        return value != null && value == 1;
    }

    public static boolean isWandItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        Byte value = meta.getPersistentDataContainer().get(WAND_KEY, PersistentDataType.BYTE);
        return value != null && value == 1;
    }
}