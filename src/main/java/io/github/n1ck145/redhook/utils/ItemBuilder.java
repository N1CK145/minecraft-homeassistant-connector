package io.github.n1ck145.redhook.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private final ItemStack item;

    public ItemBuilder(final Material material) {
        this.item = new ItemStack(material);
    }

    public ItemBuilder amount(final int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder name(final String name) {
        withMeta(meta -> meta.setDisplayName(name));
        return this;
    }

    public ItemBuilder lore(final String... lores) {
        return lore(Arrays.asList(lores));
    }

    public ItemBuilder lore(final List<String> lore) {
        withMeta(meta -> meta.setLore(lore));
        return this;
    }

    public ItemBuilder skullOwner(final OfflinePlayer player) {
        if (item.getItemMeta() instanceof SkullMeta meta) {
            meta.setOwningPlayer(player);
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder damage(final int damage) {
        if (item.getItemMeta() instanceof Damageable damageable) {
            damageable.setDamage(damage);
            item.setItemMeta((ItemMeta) damageable);
        }
        return this;
    }

    public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(final Enchantment enchantment) {
        item.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder type(final Material material) {
        item.setType(material);
        return this;
    }

    public ItemBuilder clearLore() {
        withMeta(meta -> meta.setLore(new ArrayList<>()));
        return this;
    }

    public ItemBuilder unbreakable() {
        withMeta(meta -> meta.setUnbreakable(true));
        return this;
    }

    public ItemBuilder addItemFlag(final ItemFlag... flags) {
        withMeta(meta -> meta.addItemFlags(flags));
        return this;
    }

    public ItemBuilder addAllItemFlags() {
        withMeta(meta -> meta.addItemFlags(ItemFlag.values()));
        return this;
    }

    public ItemBuilder clearEnchantments() {
        item.getEnchantments().keySet().forEach(item::removeEnchantment);
        return this;
    }

    public ItemBuilder addGlint() {
        withMeta(meta -> meta.setEnchantmentGlintOverride(true));
        return this;
    }

    public ItemBuilder removeGlint() {
        withMeta(meta -> meta.setEnchantmentGlintOverride(false));
        return this;
    }

    public ItemBuilder color(final Color color) {
        if (item.getItemMeta() instanceof LeatherArmorMeta meta) {
            meta.setColor(color);
            item.setItemMeta(meta);
        } else {
            throw new IllegalArgumentException("Color can only be applied to leather armor.");
        }
        return this;
    }

    public <T, Z> ItemBuilder persistentData(String namespace, String key, PersistentDataType<T, Z> type, Z value) {
        return persistentData(new NamespacedKey(namespace, key), type, value);
    }

    public <T, Z> ItemBuilder persistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        withMeta(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            container.set(key, type, value);
        });
        return this;
    }

    public ItemBuilder withMeta(java.util.function.Consumer<ItemMeta> consumer) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            consumer.accept(meta);
            item.setItemMeta(meta);
        }
        return this;
    }

    public ItemStack build() {
        return item;
    }
}
