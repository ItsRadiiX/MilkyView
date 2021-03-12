package com.itsradiix.milkyview.models.menus;

import com.itsradiix.milkyview.Main;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public abstract class Menu implements InventoryHolder{

	/**
	 * Menu Class for easily creatable Menus
	 * If you want to use pages, please use PaginatedMenu
	 * It features Async Menu item setting & opening to maximize performance.
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	protected static final Main main = Main.getMain();

	protected PlayerMenuUtility playerMenuUtility;
	protected Inventory inventory;

	boolean silentOpening = false;
	boolean switchOpening = false;
	protected boolean close = false;

	protected int rows = getSlots()/9;
	protected int inBetweenRows = rows-2;

	public Menu(PlayerMenuUtility playerMenuUtility) {
		this.playerMenuUtility = playerMenuUtility;
		inventory = Bukkit.createInventory(this, getSlots(), Main.chatColor(getMenuName()));
	}

	public abstract String getMenuName();

	public abstract Sound openSound();

	public abstract Sound closeSound();

	public abstract Sound switchSound();

	public abstract int getSlots();

	public abstract ItemStack getFillerGlass();

	public abstract void handleMenu(InventoryClickEvent e);

	public abstract void setMenuItems();

	public void open() {
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			setMenuItems();
			Bukkit.getScheduler().runTask(main, () -> openInventory(playerMenuUtility.getOwner()));
		});
	}

	public void silentOpen(){
		silentOpening = true;
		open();
	}

	public void switchOpen(){
		switchOpening = true;
		open();
	}

	public void reload(){
		setMenuItems();
	}

	public boolean isSwitchOpening() {
		return switchOpening;
	}

	public boolean isSilentOpening() {
		return silentOpening;
	}

	public void close(){
		playSound(playerMenuUtility.getOwner(), closeSound());
		playerMenuUtility.getOwner().closeInventory();
	}

	private void openInventory(Player p){
		p.openInventory(inventory);
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	public void setFillerGlass(){
		for (int i = 0; i < getSlots(); i++) {
			if (inventory.getItem(i) == null){
				inventory.setItem(i, getFillerGlass());
			}
		}
	}

	public void setOuterFillerGlass(){

		int rows = getSlots()/9;
		int inBetweenRows = rows-2;

		if (inBetweenRows <= 0) {
			setFillerGlass();
		} else {
			for (int i = 0; i < 9; i++) {
				if (inventory.getItem(i) == null){
					inventory.setItem(i, getFillerGlass());
				}
			}

			for (int i = 0; i < inBetweenRows; i++) {

				int index = 9*(i+1);

				if (inventory.getItem(index) == null){
					inventory.setItem(index, getFillerGlass());
				}
				index = index+8;
				if (inventory.getItem(index) == null){
					inventory.setItem(index, getFillerGlass());
				}
			}

			for (int i = ((9*rows)-9); i < ((9*rows)); i++) {
				if (inventory.getItem(i) == null){
					inventory.setItem(i, getFillerGlass());
				}
			}
		}

	}

	public void playSound(Player p, Sound sound){
		if (sound != null && p != null){
			p.playSound(p.getLocation(), sound, 0.1F, 1.0F);
		}
	}

	public void setClosed(){
		close = true;
	}

}