package io.github.n1ck145.redhook.inventories;

import io.github.n1ck145.redhook.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class AbstractPaginatedMenu<T> implements Menu {
    protected final Player player;
    protected final List<T> entries;
    protected int currentPage = 0;
    protected final int itemsPerPage = 45;

    public AbstractPaginatedMenu(Player player, List<T> entries) {
        this.player = player;
        this.entries = entries;
    }

    public void open() {
        Inventory inventory = Bukkit.createInventory(null, 54, getTitle() + " | Page " + (currentPage + 1));
        addMenuItems(inventory);
        player.openInventory(inventory);
    }

    private void addMenuItems(Inventory inventory) {
        int start = currentPage * itemsPerPage;
        int end = Math.min(start + itemsPerPage, entries.size());

        for (int i = start; i < end; i++) {
            inventory.setItem(i - start, getItemRepresentation(entries.get(i)));
        }

        // Navigation buttons
        if (currentPage > 0) {
            inventory.setItem(45, createButton(Material.ARROW, "§aPrevious Page"));
        }
        if ((currentPage + 1) * itemsPerPage < entries.size()) {
            inventory.setItem(53, createButton(Material.ARROW, "§aNext Page"));
        }
    }

    private ItemStack createButton(Material material, String name) {
        return new ItemBuilder(material)
            .name(name)
            .build();
    }

    public void handleClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().startsWith(getTitle())) return;
        if (event.getRawSlot() < 0 || event.getRawSlot() >= event.getInventory().getSize()) return;

        event.setCancelled(true);

        int slot = event.getRawSlot();
        if (slot == 45 && currentPage > 0) {
            currentPage--;
            open();
        } else if (slot == 53 && (currentPage + 1) * itemsPerPage < entries.size()) {
            currentPage++;
            open();
        } else if (slot < itemsPerPage) {
            int index = currentPage * itemsPerPage + slot;
            if (index < entries.size()) {
                onItemClick(entries.get(index));
            }
        }
    }

    protected abstract String getTitle();
    protected abstract ItemStack getItemRepresentation(T item);
    protected abstract void onItemClick(T item);
}
