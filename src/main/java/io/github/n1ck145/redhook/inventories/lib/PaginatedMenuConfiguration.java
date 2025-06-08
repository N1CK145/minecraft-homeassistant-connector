package io.github.n1ck145.redhook.inventories.lib;

import org.bukkit.Material;

import io.github.n1ck145.redhook.actions.lib.ValidationResult;

/**
 * Configuration class for paginated menu inventories in Minecraft. This class handles the layout
 * and appearance settings for paginated menus.
 */
public final class PaginatedMenuConfiguration {
	private static final int MIN_INVENTORY_SIZE = 9;
	private static final int MAX_INVENTORY_SIZE = 54;
	private static final int ROW_SIZE = 9;
	private static final int MAX_ROWS = 5;

	private final int inventorySize;
	private final int itemsPerRow;
	private final int itemRowsPerPage;
	private final int itemRowOffset;
	private final int itemColumnOffset;
	private final Material nextPageItemMaterial;
	private final Material previousPageItemMaterial;
	private final Material backgroundMaterial;
	private final Material borderMaterial;

	// Private constructor
	private PaginatedMenuConfiguration(Builder builder) {
		this.inventorySize = builder.inventorySize;
		this.itemsPerRow = builder.itemsPerRow;
		this.itemRowsPerPage = builder.itemRowsPerPage;
		this.itemRowOffset = builder.itemRowOffset;
		this.nextPageItemMaterial = builder.nextPageItemMaterial;
		this.previousPageItemMaterial = builder.previousPageItemMaterial;
		this.backgroundMaterial = builder.backgroundMaterial;
		this.itemColumnOffset = builder.itemColumnOffset;
		this.borderMaterial = builder.borderMaterial;
	}

	/**
	 * Validates the current configuration.
	 * 
	 * @return ValidationResult indicating if the configuration is valid
	 */
	public ValidationResult validate() {
		if (inventorySize < (itemRowsPerPage + 1) * ROW_SIZE)
			return ValidationResult.error("Inventory size must be at least " + ((itemRowsPerPage + 1) * ROW_SIZE)
					+ " to fit items and navigation");

		return ValidationResult.success();
	}

	// Getters
	public int getInventorySize() {
		return inventorySize;
	}

	public int getItemsPerPage() {
		return itemsPerRow * itemRowsPerPage;
	}

	public int getItemsPerRow() {
		return itemsPerRow;
	}

	public int getItemsRowsPerPage() {
		return itemRowsPerPage;
	}

	public Material getNextPageItemMaterial() {
		return nextPageItemMaterial;
	}

	public Material getPreviousPageItemMaterial() {
		return previousPageItemMaterial;
	}

	public Material getBackgroundMaterial() {
		return backgroundMaterial;
	}

	public int getItemRowOffset() {
		return itemRowOffset;
	}

	public int getItemColumnOffset() {
		return itemColumnOffset;
	}

	public int getNextPageField() {
		return (inventorySize - ROW_SIZE) + 5; // 6th slot in last row
	}

	public int getPreviousField() {
		return (inventorySize - ROW_SIZE) + 3; // 4th slot in last row
	}

	public int getItemOffset() {
		return itemRowOffset * 9 + itemColumnOffset;
	}

	public Material getBorderMaterial() {
		return borderMaterial;
	}

	/**
	 * Static factory method to create a builder
	 * 
	 * @return A new Builder instance
	 */
	public static Builder builder() {
		return new Builder();
	}

	// Builder class for constructing immutable objects
	public static class Builder {
		private int inventorySize = 54;
		private int itemsPerRow = 7;
		private int itemRowsPerPage = 4;
		private int itemRowOffset = 1;
		private int itemColumnOffset = 1;
		private Material nextPageItemMaterial = Material.ARROW;
		private Material previousPageItemMaterial = Material.ARROW;
		private Material backgroundMaterial = Material.GRAY_STAINED_GLASS_PANE;
		private Material borderMaterial = null;

		public Builder itemColumnOffset(int offset) {
			this.itemColumnOffset = offset;
			return this;
		}

		public Builder inventorySize(int size) {
			this.inventorySize = size;
			return this;
		}

		public Builder itemsPerRow(int count) {
			this.itemsPerRow = count;
			return this;
		}

		public Builder itemRowsPerPage(int count) {
			this.itemRowsPerPage = count;
			return this;
		}

		public Builder itemRowOffset(int offset) {
			this.itemRowOffset = offset;
			return this;
		}

		public Builder nextPageItemMaterial(Material material) {
			this.nextPageItemMaterial = material;
			return this;
		}

		public Builder previousPageItemMaterial(Material material) {
			this.previousPageItemMaterial = material;
			return this;
		}

		public Builder backgroundMaterial(Material material) {
			this.backgroundMaterial = material;
			return this;
		}

		public Builder borderMaterial(Material material) {
			this.borderMaterial = material;
			return this;
		}

		public PaginatedMenuConfiguration build() {
			// Validate before building
			if (inventorySize <= MIN_INVENTORY_SIZE)
				throw new IllegalArgumentException("Inventory size must be greater than " + MIN_INVENTORY_SIZE);
			if (inventorySize % ROW_SIZE != 0)
				throw new IllegalArgumentException("Inventory size must be a multiple of " + ROW_SIZE);
			if (inventorySize > MAX_INVENTORY_SIZE)
				throw new IllegalArgumentException("Inventory size cannot be greater than " + MAX_INVENTORY_SIZE);

			return new PaginatedMenuConfiguration(this);
		}
	}
}
