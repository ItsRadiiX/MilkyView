package com.itsradiix.milkyview.modules.milkyview.menus;

import com.itsradiix.milkyview.models.data.Utils;
import com.itsradiix.milkyview.models.menus.Menu;
import com.itsradiix.milkyview.models.menus.PlayerMenuUtility;
import com.itsradiix.milkyview.modules.milkyview.Threshold;
import com.itsradiix.milkyview.modules.milkyview.ViewDistance;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ThresholdAdditionalMenu extends Menu {

	/**
	 *  ThresholdAdditionalMenu Class, this is a Menu which allows the Player to change information of a certain Threshold.
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	private final Threshold threshold;
	private final int index;
	private boolean doubleClick;

	public ThresholdAdditionalMenu(PlayerMenuUtility playerMenuUtility, Threshold threshold, int index) {
		super(playerMenuUtility);
		this.threshold = threshold;
		this.index = index;
	}

	@Override
	public String getMenuName() {
		return "Threshold editor";
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
		return 27;
	}

	@Override
	public ItemStack getFillerGlass() {
		return Utils.makeItem(Material.GRAY_STAINED_GLASS_PANE, "&r");
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
		switch (e.getSlot()){
			case 26:
				close();
				break;
			case 11:
				new AnvilGUI.Builder()
						.itemLeft(new ItemStack(Material.PLAYER_HEAD ,1))
						.title("Player Amount")
						.text(String.valueOf(threshold.getPlayerAmount()))
						.plugin(main)
						.preventClose()
						.onComplete((player, s) -> {
							Bukkit.getScheduler().runTaskLater(main, () -> {
								try {
									int i = Integer.parseInt(s);
									threshold.setPlayerAmount(i);
								} catch (Exception ignored){}
								new ThresholdAdditionalMenu(playerMenuUtility, threshold, index).silentOpen();
							}, 1);
							return AnvilGUI.Response.close();
						}).open(playerMenuUtility.getOwner());
				break;
			case 15:
				new AnvilGUI.Builder()
						.itemLeft(new ItemStack(Material.BEACON ,1))
						.title("View Distance")
						.text(String.valueOf(threshold.getViewDistance()))
						.plugin(main)
						.preventClose()
						.onComplete((player, s) -> {
							Bukkit.getScheduler().runTaskLater(main, () -> {
								try {
									int i = Integer.parseInt(s);
									threshold.setViewDistance(i);
								} catch (Exception ignored){}
								new ThresholdAdditionalMenu(playerMenuUtility, threshold, index).silentOpen();
							}, 1);
							return AnvilGUI.Response.close();
						}).open(playerMenuUtility.getOwner());
				break;
			case 18:
				new ThresholdMenu(playerMenuUtility, threshold.getViewDistanceClass().getWorld()).switchOpen();
				break;
			case 8:
				if (!doubleClick){
					doubleClick = true;
				} else {
					ViewDistance viewDistance = threshold.getViewDistanceClass();
					viewDistance.removeThreshold(threshold);
					new ThresholdMenu(playerMenuUtility, viewDistance.getWorld()).silentOpen();
				}
				break;
		}
	}

	@Override
	public void setMenuItems() {
		inventory.setItem(26, Utils.makeItem(Material.BARRIER, "&4Close"));
		inventory.setItem(18, Utils.makeItem(Material.ARROW, "&aBack to all thresholds", "&7Click to go back to all thresholds."));
		inventory.setItem(8, Utils.makeItem(Material.HOPPER, "&cDelete this threshold", "&7Double click to delete this threshold."));
		inventory.setItem(4, Utils.makeItem(Material.PAPER, "&aThreshold #" + index,
				"&7Player amount: &6" + threshold.getPlayerAmount(), "&7View distance: &6" + threshold.getViewDistance()));
		inventory.setItem(11, Utils.makeItem(Material.PLAYER_HEAD, "&aPlayer amount: &6" + threshold.getPlayerAmount(), "&8Click to set player amount."));
		inventory.setItem(15, Utils.makeItem(Material.BEACON, "&aView Distance: &6" + threshold.getViewDistance(), "&8Click to set view distance."));
		setOuterFillerGlass();
	}
}
