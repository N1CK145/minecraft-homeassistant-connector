package io.github.n1ck145.redhook.listeners;

import io.github.n1ck145.redhook.implementation.menu.ActionTypeMenu;
import io.github.n1ck145.redhook.implementation.menu.RedstoneActionMenu;
import io.github.n1ck145.redhook.manager.action.ActionRegistry;
import io.github.n1ck145.redhook.manager.menu.MenuManager;
import io.github.n1ck145.redhook.util.ItemUtils;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RedhookToolsInteractionListener implements Listener {

	@EventHandler
	public void onToolInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack tool = player.getInventory().getItemInMainHand();

		if (!ItemUtils.isWandItem(tool) && !ItemUtils.isDebugItem(tool))
			return;

		event.setCancelled(true);

		if (ItemUtils.isWandItem(tool))
			handleWandItemInteraction(event);

		// TODO: Refactor debug block break listener to use this
	}

	private void handleWandItemInteraction(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block clickedBlock = event.getClickedBlock();

		switch (event.getAction()) {
			case RIGHT_CLICK_AIR :
			case LEFT_CLICK_AIR :
				if (player.isSneaking())
					openActionEditor(player);
				break;

			case LEFT_CLICK_BLOCK :
				if (clickedBlock != null)
					openRedstoneActionMenu(player, clickedBlock);
				break;

			case PHYSICAL :
			case RIGHT_CLICK_BLOCK :
				break;
		}
	}

	private void openActionEditor(Player player) {
		ActionTypeMenu menu = new ActionTypeMenu(player);
		MenuManager.openMenu(player, menu);
	}

	private void openRedstoneActionMenu(Player player, Block clickedBlock) {
		RedstoneActionMenu menu = new RedstoneActionMenu(player, clickedBlock, ActionRegistry.getAll());
		MenuManager.openMenu(player, menu);
	}
}
