package io.github.n1ck145.redhook.inventories;

import io.github.n1ck145.redhook.inventories.lib.AbstractPaginatedMenu;
import io.github.n1ck145.redhook.manager.MenuManager;
import io.github.n1ck145.redhook.redstoneactions.lib.RedstoneAction;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class RedstoneActionMenu extends AbstractPaginatedMenu<RedstoneAction> {
	private final Block selectedBlock;

	public RedstoneActionMenu(Player player, Block selectedBlock, List<RedstoneAction> actions) {
		super(player, actions);
		this.selectedBlock = selectedBlock;
	}

	@Override
	protected String getTitle() {
		return "§6Bind Action";
	}

	@Override
	protected ItemStack getItemRepresentation(RedstoneAction action) {
		return action.getIcon();
	}

	@Override
	protected void onItemClick(RedstoneAction action) {
		player.closeInventory();
		MenuManager.openMenu(player, new ActionTriggerMenu(player, selectedBlock, action));
	}
}