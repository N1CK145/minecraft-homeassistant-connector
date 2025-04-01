package io.github.n1ck145.redhook.inventories;

import io.github.n1ck145.redhook.dto.webhook.WebhookSettings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class WebhookSelectorInventory implements InventoryHolder, Listener {

    private String title;
    private String titleBase;
    private Inventory inventory;

    private static final int ITEMS_PER_PAGE = 7 * 4;  // 4 rows with 7 items
    private static final int SLOTS = 9 * 6;  // 9 rows 6 cols
    private static final Material FRAME_MATERIAL = Material.GRAY_STAINED_GLASS_PANE;
    private static final Material WEBHOOK_MATERIAL = Material.PAPER;
    private int currentPage = 0;
    private LinkedList<WebhookSettings> webhooks;

    public WebhookSelectorInventory(String title) {
        this.title = title;
        this.titleBase = title;
        this.webhooks = new LinkedList<>();
        this.inventory = Bukkit.createInventory(this, SLOTS, title);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void updateInventory() {
        loadWebhooks();

        this.title = titleBase;
        this.title += " - " + currentPage + 1 + "/" + getMaxPage();
        inventory.clear();

        buildFrame();

        addPaginationButtons();

        populateWebhooks();
    }

    private void buildFrame() {
        // Loop through the inventory slots and set the frame material on the borders
        for (int i = 0; i < SLOTS; i++) {
            // Check if the slot is on the border (edges and corners)
            if (isBorderSlot(i)) {
                ItemStack stack = new ItemStack(FRAME_MATERIAL);
                ItemMeta meta = stack.getItemMeta();
                meta.setDisplayName(" ");
                stack.setItemMeta(meta);
                inventory.setItem(i, stack);
            }
        }
    }

    private boolean isBorderSlot(int slot) {
        // Check if the slot is on the first or last row
        if (slot < 9 || slot >= SLOTS - 9) {
            return true;
        }
        // Check if the slot is on the first or last column of each row
        if (slot % 9 == 0 || slot % 9 == 8) {
            return true;
        }
        return false;
    }

    private void populateWebhooks() {
        // Placeholder: Add items for webhooks dynamically based on currentPage.
        int posIndex = 0;

        for (int i = 0; i < ITEMS_PER_PAGE; i++) {

            ItemStack item = getWebhookItem(currentPage * ITEMS_PER_PAGE + i);
            if(item == null) return;

            while(isBorderSlot(posIndex)){
                posIndex++;
            }

            inventory.setItem(posIndex, item);
            posIndex++;
        }
    }

    private ItemStack getWebhookItem(int index){
        if(this.webhooks.size() - 1 < index)
            return null;

        WebhookSettings setting = this.webhooks.get(index);

        if(setting == null)
            return null;

        ItemStack webhookItem = new ItemStack(WEBHOOK_MATERIAL);
        ItemMeta meta = webhookItem.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(setting.getName());
            webhookItem.setItemMeta(meta);
        }

        return webhookItem;
    }

    private void addPaginationButtons() {
        // Add "Next" and "Previous" buttons for pagination
        ItemStack nextButton = createPaginationButton("Next", Material.ARROW);
        ItemStack prevButton = createPaginationButton("Previous", Material.ARROW);

        inventory.setItem(50, nextButton);  // Place "Next" button in the middle-bottom slot (for example)
        inventory.setItem(48, prevButton);  // Place "Previous" button to the left of "Next"
    }

    private ItemStack createPaginationButton(String label, Material material) {
        ItemStack button = new ItemStack(material);
        ItemMeta meta = button.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(label);
            button.setItemMeta(meta);
        }
        return button;
    }

    private void setPageCounter() {

    }

    private void loadWebhooks(){
        for(int i = 1; i < 100; i++){
            this.webhooks.add(new WebhookSettings("Webhook " + i));
        }
    }

    private int getMaxPage() {
        return (int)Math.ceil(this.webhooks.size() / ITEMS_PER_PAGE);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);

            // Handle clicks for pagination buttons
            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();

                if ("Next".equals(itemName)) {
                    currentPage++;
                    updateInventory();  // Refresh the inventory with new page
                } else if ("Previous".equals(itemName)) {
                    if (currentPage > 0) {
                        currentPage--;
                        updateInventory();  // Refresh the inventory with previous page
                    }
                }
            }
        }
    }
}
