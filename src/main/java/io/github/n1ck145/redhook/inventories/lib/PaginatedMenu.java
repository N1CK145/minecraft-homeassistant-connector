package io.github.n1ck145.redhook.inventories.lib;

import io.github.n1ck145.redhook.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class PaginatedMenu<T> implements InteractiveMenu {
	protected final Player player;
	protected final List<T> entries;
	protected final Inventory inventory;
	protected final PaginatedMenuConfiguration menuConfiguration;

	protected int currentPage = 0;

	public PaginatedMenu(Player player, List<T> entries, PaginatedMenuConfiguration menuConfiguration) {
		this.player = player;
		this.entries = entries;
		this.menuConfiguration = menuConfiguration;

		inventory = Bukkit.createInventory(player, menuConfiguration.getInventorySize(), getTitle());
	}

	public PaginatedMenu(Player player, List<T> entries) {
		this(player, entries, PaginatedMenuConfiguration.builder().build());
	}

	public void open() {
		renderPage();
		player.openInventory(inventory);
	}

	protected void renderPage() {
		inventory.clear();
		addBackground();
		addBorder();
		addMenuItems();
		addNavigation();
	}

	private void addBackground() {
		Material material = menuConfiguration.getBackgroundMaterial();
		if (material == null)
			return;

		ItemStack background = new ItemBuilder(material).name(" ").build();

		for (int i = 0; i < menuConfiguration.getInventorySize(); i++) {
			inventory.setItem(i, background);
		}
	}

	private void addBorder() {
		Material material = menuConfiguration.getBorderMaterial();
		if (material == null)
			return;

		ItemStack background = new ItemBuilder(material).name(" ").build();

		// Add top border
		for (int i = 0; i < 9; i++) {
			inventory.setItem(i, background);
		}

		// Add side borders
		for (int i = 9; i < menuConfiguration.getInventorySize() - 9; i++) {
			if (i % 9 == 0 || i % 9 == 8) {
				inventory.setItem(i, background);
			}
		}

		// Add bottom border
		for (int i = menuConfiguration.getInventorySize() - 9; i < menuConfiguration.getInventorySize(); i++) {
			inventory.setItem(i, background);
		}
	}

	private void addMenuItems() {
		int offset = menuConfiguration.getItemOffset();

		int start = currentPage * menuConfiguration.getItemsPerPage();
		int end = Math.min(start + menuConfiguration.getItemsPerPage(), entries.size());

		for (int i = start; i < end; i++) {
			inventory.setItem(i - start + offset, getItemRepresentation(entries.get(i)));
		}

	}

	private void addNavigation() {
		// Navigation buttons
		ItemBuilder previousButtonBuilder =
				new ItemBuilder(menuConfiguration.getPreviousPageItemMaterial()).name("§7Previous Page");
		ItemBuilder nextButtonBuilder =
				new ItemBuilder(menuConfiguration.getPreviousPageItemMaterial()).name("§7Next Page");

		// Add page indicator
		ItemBuilder pageIndicatorBuilder = new ItemBuilder(Material.COMPASS)
				.name("§7Page " + (currentPage + 1) + " / " + (getLastPage() + 1)).addGlint();

		if (hasPreviousPage())
			inventory.setItem(menuConfiguration.getPreviousField(), previousButtonBuilder.build());

		if (hasNextPage())
			inventory.setItem(menuConfiguration.getNextPageField(), nextButtonBuilder.build());

		// Set page indicator in the middle of the bottom row
		inventory.setItem(menuConfiguration.getInventorySize() - 5, pageIndicatorBuilder.build());
	}

	public void handleClick(InventoryClickEvent event) {
		if (event.getView().getTitle() != getTitle() || event.getWhoClicked() != player)
			return;

		event.setCancelled(true);

		int slot = event.getRawSlot();

		if (slot == menuConfiguration.getPreviousField() && hasPreviousPage()) {
			currentPage--;
			open();
		}
		else if (slot == menuConfiguration.getNextPageField() && hasNextPage()) {
			currentPage++;
			open();
		}
		else if (slot < menuConfiguration.getItemsPerPage()) {
			int index = currentPage * menuConfiguration.getItemsPerPage() + slot - menuConfiguration.getItemOffset();
			if (index < entries.size()) {
				onItemClick(entries.get(index));
			}
		}
	}

	protected boolean hasPreviousPage() {
		return currentPage > 0;
	}

	protected boolean hasNextPage() {
		// currentPage is 0 when on first site
		return (currentPage + 1) * menuConfiguration.getItemsPerPage() < entries.size();
	}

	protected int getLastPage() {
		int calculatedLastPage = (int) Math.ceil((double) entries.size() / menuConfiguration.getItemsPerPage()) - 1;
		return Math.max(calculatedLastPage, 0);
	}

	protected abstract String getTitle();

	protected abstract ItemStack getItemRepresentation(T item);

	protected abstract void onItemClick(T item);

}
