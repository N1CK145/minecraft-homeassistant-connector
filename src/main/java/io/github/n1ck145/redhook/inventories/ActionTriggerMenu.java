package io.github.n1ck145.redhook.inventories;

import io.github.n1ck145.redhook.redstoneactions.RedstoneAction;
import io.github.n1ck145.redhook.redstoneactions.TriggerCondition;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.n1ck145.redhook.manager.RedstoneLinkManager;

public class ActionTriggerMenu implements Menu {
    private final Player player;
    private final Block selectedBlock;
    private final RedstoneAction action;

    public ActionTriggerMenu(Player player, Block selectedBlock, RedstoneAction action) {
        this.player = player;
        this.selectedBlock = selectedBlock;
        this.action = action;
    }

    public void open() {
        System.out.println("Open");
        Inventory inv = Bukkit.createInventory(null, 9, "§6Select Trigger");

        inv.setItem(2, createButton(Material.REDSTONE_TORCH, "§aON"));
        inv.setItem(4, createButton(Material.LEVER, "§aOFF"));
        inv.setItem(6, createButton(Material.REDSTONE_BLOCK, "§aBOTH"));

        player.openInventory(inv);
    }

    private ItemStack createButton(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    public void handleClick(InventoryClickEvent event) {
        System.out.println("Click");

        if (!event.getView().getTitle().equals("§6Select Trigger")) return;
        event.setCancelled(true);

        int slot = event.getRawSlot();
        TriggerCondition trigger;

        switch (slot) {
            case 2 -> trigger = TriggerCondition.ON;
            case 4 -> trigger = TriggerCondition.OFF;
            case 6 -> trigger = TriggerCondition.BOTH;
            default -> {
                return;
            }
        }

        // Bind with trigger
        RedstoneLinkManager.bindBlock(selectedBlock, action, trigger);

        player.sendMessage("§aBound action §e" + action.getName() + " §awith trigger §e" + trigger + " §ato block at §e" + selectedBlock.getLocation());
        player.closeInventory();
    }
}