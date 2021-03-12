package com.itsradiix.milkyview.modules.milkyview.menus;

import com.itsradiix.milkyview.Main;
import com.itsradiix.milkyview.models.data.Utils;
import com.itsradiix.milkyview.models.menus.PaginatedMenu;
import com.itsradiix.milkyview.models.menus.PlayerMenuUtility;
import com.itsradiix.milkyview.modules.milkyview.MilkyViewModule;
import org.bukkit.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class StatsMenu extends PaginatedMenu {

	/**
	 * StatsMenu Class, this is a PaginatedMenu which shows information about all worlds
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	public StatsMenu(PlayerMenuUtility playerMenuUtility) {
		super(playerMenuUtility);
		maxItemsPerPage = 21;
	}

	@Override
	public String getMenuName() {
		return "MilkyView statistics";
	}

	@Override
	public Sound openSound() {
		return Sound.BLOCK_CHEST_OPEN;
	}

	@Override
	public Sound closeSound() {
		return Sound.BLOCK_CHEST_CLOSE;
	}

	@Override
	public Sound switchSound() {
		return Sound.UI_BUTTON_CLICK;
	}

	@Override
	public int getSlots() {
		return 45;
	}

	@Override
	public ItemStack getFillerGlass() {
		return Utils.makeItem(Material.GRAY_STAINED_GLASS_PANE, "&r");
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
	    handleMenuButtons(e, main.getWorldsList().size(),  40, 39, 41);
	    if (clickInItemSection(e)){
	    	int index = getClickInItemSectionIndex(e);
	    	World w = main.getWorldsList().get(index);
			new ThresholdMenu(playerMenuUtility, w).switchOpen();
	    }
	}

	@Override
	public void setMenuItems() {
		addMenuBorder(39, 40, 41);
		inventory.setItem(4, Utils.makeItem(Material.MILK_BUCKET, "&bMilkyView Statistics",
				"&7This menu shows you some information",
				"&7about each world that has been loaded.",
				"&7You will also be able to set your thresholds."));
		setOuterFillerGlass();
		updateItems();
	}

	public void updateItems(){
		new BukkitRunnable() {
			@Override
			public void run() {
				if (!main.getWorldsList().isEmpty()) {
					for (int i = 0; i < getMaxItemsPerPage(); i++) {
						index = getMaxItemsPerPage() * page + i;
						if (index >= main.getWorldsList().size()) break;
						World w = main.getWorldsList().get(index);
						if (w != null) {
							inventory.setItem(setCorrectIndex(i), Utils.makeItem(MilkyViewModule.getWorldMaterialMap().get(w), "&b" + w.getName(),
									"&7Loaded Chunks: &a" + w.getLoadedChunks().length,
									"&7Player Amount: &a" + w.getPlayerCount(),
									"&7Entity Amount: &a" + w.getEntityCount(),
									"&7Tile Amount: &a" + w.getTileEntityCount(),
									"",
									"&a&lCurrent view distance: &6" + w.getViewDistance()));
						}
					}
				}
				if (close){
					cancel();
				}
			}
		}.runTaskTimerAsynchronously(main, 0, 20);
	}
}
