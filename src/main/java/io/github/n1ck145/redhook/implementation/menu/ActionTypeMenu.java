package io.github.n1ck145.redhook.implementation.menu;

import io.github.n1ck145.redhook.RedhookPlugin;
import io.github.n1ck145.redhook.annotations.ActionTypeRepresentation;
import io.github.n1ck145.redhook.core.inventory.PaginatedMenu;
import io.github.n1ck145.redhook.manager.action.ActionFactory;
import io.github.n1ck145.redhook.manager.menu.MenuManager;
import io.github.n1ck145.redhook.model.action.RedstoneActionType;
import io.github.n1ck145.redhook.util.ItemBuilder;
import io.github.n1ck145.redhook.util.common.StringUtils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;

public class ActionTypeMenu extends PaginatedMenu<RedstoneActionType> {
	private static final String TITLE = RedhookPlugin.getPrefix() + "Select Action Type";

	public ActionTypeMenu(Player player) {
		super(player, ActionFactory.getRegisteredActionTypes());
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected ItemStack getItemRepresentation(RedstoneActionType item) {
		ActionTypeRepresentation representation = item.getTypeClass().getAnnotation(ActionTypeRepresentation.class);

		if (representation == null) {
			return new ItemStack(Material.BARRIER);
		}

		return new ItemBuilder(representation.icon()).name("§7" + StringUtils.splitCamelCase(item.getName()))
				.lore(StringUtils.splitLongString(representation.description(), 60)).build();
	}

	@Override
	protected void onItemClick(RedstoneActionType item) {
		player.closeInventory();

		String newActionId = player.getName() + "_" + UUID.randomUUID();
		CreateActionMenu menu = new CreateActionMenu(player, ActionFactory.createAction(item.getTypeClass(), newActionId));
		MenuManager.openMenu(player, menu);
	}


}
