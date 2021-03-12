package com.itsradiix.milkyview.models.menus;

import com.itsradiix.milkyview.models.data.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class PaginatedMenu extends Menu {

	/**
	 * Paginated Menu Class for easily creating multiple page Menus
	 * To create a normal Menu, please extend your class on Menu
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	protected int page = 0;
	protected int maxItemsPerPage = 28;
	protected int index = 0;
	protected int[] itemIntList = getItemIntList();

	public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
		super(playerMenuUtility);
	}

	public void addMenuBorder(){
		inventory.setItem(48, Utils.makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"));
		inventory.setItem(49, Utils.makeItem(Material.BARRIER, ChatColor.DARK_RED + "Close"));
		inventory.setItem(50, Utils.makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Right"));
	}

	public void addMenuBorder(int left, int close, int right){
		inventory.setItem(left, Utils.makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"));
		inventory.setItem(close, Utils.makeItem(Material.BARRIER, ChatColor.DARK_RED + "Close"));
		inventory.setItem(right, Utils.makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Right"));
	}

	public void clearItemSection(){
		for (int i : itemIntList){
			if (inventory.getItem(i) != null){
				inventory.setItem(i, null);
			}
		}
	}

	protected int setCorrectIndex(int index){
		if (index >= 0 && index < 7){
			index = index + 10;
		} else if (index >= 7 && index < 14){
			index = index + 12;
		} else if (index >= 14 && index < 21){
			index = index + 14;
		} else if (index >= 21 && index < 28){
			index = index + 16;
		}
		return index;
	}

	private int[] getItemIntList(){
		int[] tmp = new int[(7 * inBetweenRows)];
		int x = 0;
		for (int i = 0; i < inBetweenRows; i++) {
			int index = 9*(i+1);
			for (int j = 0; j < 7; j++) {
				tmp[x] = (index+j+1);
				x++;
			}
		}
		return tmp;
	}

	public boolean clickInItemSection(InventoryClickEvent e){
		for (int i : itemIntList){
			if (e.getSlot() == i){
				return true;
			}
		}
		return false;
	}

	public int getClickInItemSectionIndex(InventoryClickEvent e){
		if (e.getSlot() >= 10 && e.getSlot() < 17){
			return (e.getSlot()-10);
		} else if (e.getSlot() >= 19 && e.getSlot() < 26){
			return (e.getSlot()-12);
		} else if (e.getSlot() >= 28 && e.getSlot() < 35){
			return (e.getSlot()-14);
		} else {
			return (e.getSlot()-16);
		}
	}

	public int getMaxItemsPerPage() {
		return maxItemsPerPage;
	}

	public void handleMenuButtons(Player p, InventoryClickEvent e, int arrayListSize) {
		Material material = e.getCurrentItem().getType();
		if (material.equals(Material.GRAY_STAINED_GLASS_PANE)){return;}
		if (material.equals(Material.BARRIER) && e.getSlot() == 49) {
			close();
		} else if (material.equals(Material.DARK_OAK_BUTTON)) {
			if (arrayListSize > maxItemsPerPage) {
				if (e.getSlot() == 48) {
					if (page != 0) {
						page = page - 1;
						super.silentOpen();
					}
				} else if (e.getSlot() == 50) {
					if (!((index + 1) >= arrayListSize)) {
						page = page + 1;
						super.silentOpen();
					}
				}
			}
		}
	}

	public void handleMenuButtons(InventoryClickEvent e, int arrayListSize, int closeSlot, int leftButton, int rightButton) {
		Material material = e.getCurrentItem().getType();
		if (material.equals(Material.GRAY_STAINED_GLASS_PANE)){return;}
		if (material.equals(Material.BARRIER) && e.getSlot() == closeSlot) {
			close();
		} else if (material.equals(Material.DARK_OAK_BUTTON)) {
			if (arrayListSize > maxItemsPerPage){
				if (e.getSlot() == leftButton) {
					if (page != 0) {
						page = page - 1;
						super.silentOpen();
					}
				} else if (e.getSlot() == rightButton) {
					if (!((index + 1) >= arrayListSize)) {
						page = page + 1;
						super.silentOpen();
					}
				}
			}
		}
	}

	public void handleMenuButtons(InventoryClickEvent e, int closeSlot) {
		Material material = e.getCurrentItem().getType();
		if (material.equals(Material.GRAY_STAINED_GLASS_PANE)){return;}
		if (material.equals(Material.BARRIER) && e.getSlot() == closeSlot) {
			close();
		}
	}
}
