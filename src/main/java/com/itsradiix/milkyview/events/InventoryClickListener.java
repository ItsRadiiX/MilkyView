package com.itsradiix.milkyview.events;

import com.itsradiix.milkyview.models.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryClickListener implements Listener {

	@EventHandler
	public void onMenuClick(org.bukkit.event.inventory.InventoryClickEvent e){
		InventoryHolder holder = e.getInventory().getHolder();

		if (holder instanceof Menu) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null) {
				return;
			}
			Menu menu = (Menu) holder;
			menu.handleMenu(e);
		}

	}

	@EventHandler
	public void onMenuClose(InventoryCloseEvent e){
		InventoryHolder holder = e.getInventory().getHolder();

		if (holder instanceof Menu) {
			Menu menu = (Menu) holder;
			menu.setClosed();
		}
	}

	@EventHandler
	public void onMenuOpen(InventoryOpenEvent e){
		InventoryHolder holder = e.getInventory().getHolder();
		Player p = (Player) e.getPlayer();

		if (holder instanceof Menu) {
			Menu menu = (Menu) holder;
			if (!menu.isSilentOpening()){
				if (menu.isSwitchOpening()){
					menu.playSound(p, menu.switchSound());
				} else {
					menu.playSound(p, menu.openSound());
				}
			}
		}
	}
}
