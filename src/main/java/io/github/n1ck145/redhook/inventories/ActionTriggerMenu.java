package io.github.n1ck145.redhook.inventories;

import io.github.n1ck145.redhook.redstoneactions.lib.RedstoneAction;
import io.github.n1ck145.redhook.redstoneactions.lib.RedstoneActionInstance;
import io.github.n1ck145.redhook.redstoneactions.lib.TriggerCondition;
import io.github.n1ck145.redhook.utils.ResponseMessage;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.n1ck145.redhook.RedhookPlugin;
import io.github.n1ck145.redhook.manager.RedstoneLinkManager;

public class ActionTriggerMenu implements Menu {
    private final Player player;
    private final Block selectedBlock;
    private final RedstoneAction action;
    private final RedstoneActionInstance currentActionInstance;

    public ActionTriggerMenu(Player player, Block selectedBlock, RedstoneAction action) {
        this.player = player;
        this.selectedBlock = selectedBlock;
        this.action = action;
        
        ArrayList<RedstoneActionInstance> actionInstances = RedstoneLinkManager.getActionInstances(selectedBlock);
        currentActionInstance = actionInstances == null ? null : actionInstances.stream().filter(instance -> instance.getAction().equals(action)).findFirst().orElse(null);
    }

    public void open() {
        Inventory inv = Bukkit.createInventory(null, 9 * 3, "§6Select Trigger");

        setBackground(inv);
        setNavigation(inv);
        setActions(inv);
        setIndicators(inv);

        player.openInventory(inv);
    }

    private void setBackground(Inventory inv){
        for(int i = 0; i < inv.getSize(); i++){
            if(inv.getItem(i) == null){
                inv.setItem(i, createButton(Material.GRAY_STAINED_GLASS_PANE, "§7"));
            }
        }
    }

    private void setNavigation(Inventory inv){
        inv.setItem(0, createButton(Material.ARROW, "§cBack"));
        inv.setItem(8, createButton(Material.NAME_TAG, "§e" + action.getId()));
    }

    private void setActions(Inventory inv){
        inv.setItem(9 + 1, createButton(Material.REDSTONE_TORCH, "§aON"));
        inv.setItem(9 + 3, createButton(Material.LEVER, "§aOFF"));
        inv.setItem(9 + 5, createButton(Material.REDSTONE_BLOCK, "§aBOTH"));
        inv.setItem(9 + 7, createButton(Material.BARRIER, "§4DISABLED"));
    }

    private void setIndicators(Inventory inv){
        boolean[] states = new boolean[4];

        if(currentActionInstance == null){
            states[0] = false;
            states[1] = false;
            states[2] = false;
            states[3] = true;
        }else{
            TriggerCondition trigger = currentActionInstance.getTriggerCondition();
            states[0] = trigger == TriggerCondition.ON;
            states[1] = trigger == TriggerCondition.OFF;
            states[2] = trigger == TriggerCondition.BOTH;
            states[3] = false;
        }

        for(int i = 0; i < states.length; i++){
            if(states[i]){
                inv.setItem(19 + (2 * i), createButton(Material.GREEN_STAINED_GLASS_PANE, "§aON"));
            }else{
                inv.setItem(19 + (2 * i), createButton(Material.RED_STAINED_GLASS_PANE, "§cOFF"));
            }
        }
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

        if (!event.getView().getTitle().equals("§6Select Trigger")) return;
        event.setCancelled(true);

        int slot = event.getRawSlot();
        TriggerCondition trigger;

        switch (slot) {
            case 9 + 1 -> trigger = TriggerCondition.ON;
            case 9 + 3 -> trigger = TriggerCondition.OFF;
            case 9 + 5 -> trigger = TriggerCondition.BOTH;
            case 9 + 7 -> {
                if(currentActionInstance == null){
                    player.sendMessage(RedhookPlugin.getPrefix() + "§cNo action bound to this block");
                    player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.natural(0, Note.Tone.C));
                    return;
                }

                RedstoneLinkManager.unbindBlock(selectedBlock.getLocation(), action);
                player.sendMessage(RedhookPlugin.getPrefix() + "§aUnbound action §7" + action.getId());
                player.playNote(player.getLocation(), Instrument.PLING, Note.natural(1, Note.Tone.C));
                player.closeInventory();
                return;
            }
            default -> {
                return;
            }
        }

        // Bind with trigger
        ResponseMessage response = RedstoneLinkManager.bindBlock(selectedBlock, action, trigger);
        if(!response.isSuccess()){
            response.send(player);
            player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.natural(0, Note.Tone.C));
        }
        else{
            response.send(player);
            player.playNote(player.getLocation(), Instrument.PLING, Note.natural(1, Note.Tone.C));
            player.closeInventory();
        }
    }
}