package io.github.n1ck145.redhook.inventories;

import io.github.n1ck145.redhook.dto.webhook.WebhookSettings;
import io.github.n1ck145.redhook.utils.webclient.HttpMethod;
import io.github.n1ck145.redhook.utils.webclient.WebClient;
import io.github.n1ck145.redhook.utils.webclient.WebhookSettingRepository;
import it.unimi.dsi.fastutil.Hash;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;


import java.net.URI;
import java.net.http.HttpResponse;
import java.util.*;

public class WebhookSelectorInventory implements InventoryHolder, Listener {
    protected String title;
    protected String titleBase;
    protected Inventory inventory;

    protected static final int ITEMS_PER_PAGE = 7 * 4;  // 4 rows with 7 items
    protected static final int SLOTS = 9 * 6;  // 9 rows 6 cols
    protected static final Material FRAME_MATERIAL = Material.GRAY_STAINED_GLASS_PANE;
    protected static final Material WEBHOOK_MATERIAL = Material.PAPER;
    protected int currentPage = 0;
    protected LinkedList<WebhookSettings> webhooks;
    protected Hashtable<WebhookSettings, ItemStack> webhookToItems;
    protected Hashtable<ItemStack, WebhookSettings> itemsToWebhook;

    public WebhookSelectorInventory(String title) {
        this.webhooks = new LinkedList<>();
        this.webhookToItems = new Hashtable<>();
        this.itemsToWebhook = new Hashtable<>();

        this.title = title;
        this.titleBase = title;
        this.inventory = Bukkit.createInventory(this, SLOTS, title);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public void updateInventory() {
        loadWebhooks();

        this.title = titleBase;
        inventory.clear();

        buildFrame();

        addPaginationButtons();

        populateWebhooks();
    }

    protected void buildFrame() {
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

    protected boolean isBorderSlot(int slot) {
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

    protected void populateWebhooks() {
        int invSlot = 0;
        int startIndex = currentPage * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, webhooks.size());

        for (int webhookIndex = startIndex; webhookIndex < endIndex; webhookIndex++) {
            WebhookSettings webhook = webhooks.get(webhookIndex);
            ItemStack item = webhookToItems.get(webhook);

            if (item == null) return;

            while (isBorderSlot(invSlot)) {
                invSlot++;
            }

            inventory.setItem(invSlot, item);
            invSlot++;
        }
    }

    protected ItemStack getWebhookItemStack(WebhookSettings settings){
        ItemStack webhookItem = new ItemStack(WEBHOOK_MATERIAL);
        ItemMeta meta = webhookItem.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + settings.getName()); // Name visible always

            // Default lore (ID hidden)
            List<String> lore = new ArrayList<>();
            lore.add(settings.getDescription());
            meta.setLore(lore);

            webhookItem.setItemMeta(meta);
        }

        return webhookItem;
    }

    protected void addPaginationButtons() {
        // Add "Next" and "Previous" buttons for pagination
        ItemStack nextButton = createPaginationButton("Next", Material.ARROW);
        ItemStack prevButton = createPaginationButton("Previous", Material.ARROW);

        if(currentPage > 0)
            inventory.setItem(48, prevButton);  // Place "Previous" button to the left of "Next"

        if(currentPage < getMaxPage())
            inventory.setItem(50, nextButton);  // Place "Next" button in the middle-bottom slot (for example)
    }

    protected ItemStack createPaginationButton(String label, Material material) {
        ItemStack button = new ItemStack(material);
        ItemMeta meta = button.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(label);
            button.setItemMeta(meta);
        }
        return button;
    }

    protected void loadWebhooks(){
        ArrayList<WebhookSettings> settings = WebhookSettingRepository.getInstance().getWebhookSettings();

        for(int i = 0; i < settings.size(); i++){
            WebhookSettings webhook = settings.get(i);
            webhooks.add(webhook);
            ItemStack webhookStack = getWebhookItemStack(webhook);

            webhookToItems.put(webhook, webhookStack);
            itemsToWebhook.put(webhookStack, webhook);
        }
    }

    protected int getMaxPage() {
        return (int)Math.ceil((double)this.webhooks.size() / ITEMS_PER_PAGE);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory().equals(inventory)) {
            event.setCancelled(true);

            if (event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()) {
                String itemName = event.getCurrentItem().getItemMeta().getDisplayName();


                // Handle clicks for pagination buttons
                if ("Next".equals(itemName)) {
                    if(currentPage < getMaxPage()){
                        currentPage++;
                        updateInventory();  // Refresh the inventory with new page
                        player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
                    }
                    else{
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    }
                } else if ("Previous".equals(itemName)) {
                    if (currentPage > 0) {
                        currentPage--;
                        updateInventory();  // Refresh the inventory with previous page
                        player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
                    }
                    else{
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
                    }
                }
            }
        }
    }
}
