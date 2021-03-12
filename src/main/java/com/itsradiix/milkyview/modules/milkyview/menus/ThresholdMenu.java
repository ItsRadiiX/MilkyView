package com.itsradiix.milkyview.modules.milkyview.menus;

import com.itsradiix.milkyview.Main;
import com.itsradiix.milkyview.models.data.Utils;
import com.itsradiix.milkyview.models.menus.PaginatedMenu;
import com.itsradiix.milkyview.models.menus.PlayerMenuUtility;
import com.itsradiix.milkyview.modules.milkyview.MilkyViewModule;
import com.itsradiix.milkyview.modules.milkyview.Threshold;
import com.itsradiix.milkyview.modules.milkyview.ViewDistance;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class ThresholdMenu extends PaginatedMenu {

	/**
	 * ThresholdMenu Class, this is a PaginatedMenu showing all thresholds of a certain world and allows for creation / deletion
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	World world;
	Material material;
	List<Threshold> thresholdList;

	public ThresholdMenu(PlayerMenuUtility playerMenuUtility, World world) {
		super(playerMenuUtility);
		this.world = world;
		material = MilkyViewModule.getWorldMaterialMap().get(world);
		thresholdList = MilkyViewModule.getWorldViewDistanceHashMap().get(world).getThresholdList();
		maxItemsPerPage = 21;
	}

	@Override
	public String getMenuName() {
		return "Thresholds overview";
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
		handleMenuButtons(e, thresholdList.size(), 40, 39, 41);
		if (e.getSlot() == 36){
			new StatsMenu(playerMenuUtility).switchOpen();
		} else if (e.getSlot() == 0){
			new AnvilGUI.Builder()
					.itemLeft(new ItemStack(Material.PLAYER_HEAD ,1))
					.title("Player Amount")
					.text("[number]")
					.plugin(main)
					.preventClose()
					.onComplete((player, playerAmount) -> {
						Bukkit.getScheduler().runTaskLater(main, () -> {
							try {
								int pa = Integer.parseInt(playerAmount);
								if (MilkyViewModule.doesPlayerAmountExist(world, pa)){
									new ThresholdMenu(playerMenuUtility, world).silentOpen();
									playerMenuUtility.getOwner().sendMessage(Main.chatColor("&cThreshold for that player amount already exists!"));
								} else {
									new AnvilGUI.Builder()
											.itemLeft(new ItemStack(Material.PLAYER_HEAD ,1))
											.title("View Distance")
											.text("[number 2-32]")
											.plugin(main)
											.preventClose()
											.onComplete((p, viewDistance) -> {
												try {
													int vd = Integer.parseInt(viewDistance);
													if (vd > 1 && vd < 33){
														ViewDistance tmp = MilkyViewModule.getWorldViewDistanceHashMap().get(world);
														MilkyViewModule.addThreshold(tmp, MilkyViewModule.getWorldViewDistanceHashMap().get(world).addViewDistance(pa, vd));
													}
													new ThresholdMenu(playerMenuUtility, world).silentOpen();
												} catch (Exception ignored){
													new ThresholdMenu(playerMenuUtility, world).silentOpen();
												}
												return AnvilGUI.Response.close();
											}).open(playerMenuUtility.getOwner());
								}
							} catch (Exception ignored){
								new ThresholdMenu(playerMenuUtility, world).silentOpen();
							}
						}, 1);
						return AnvilGUI.Response.close();
					}).open(playerMenuUtility.getOwner());
		} else if (e.getSlot() == 4){
			new AnvilGUI.Builder()
					.itemLeft(new ItemStack(material ,1))
					.title(world.getName() + " icon...")
					.text(material.name())
					.plugin(main)
					.preventClose()
					.onComplete((player, material) -> {
							MilkyViewModule.updateMaterial(world, material.toUpperCase());
							new ThresholdMenu(playerMenuUtility, world).silentOpen();
						return AnvilGUI.Response.close();
					}).open(playerMenuUtility.getOwner());
		}

		if (clickInItemSection(e)){
			int index = getClickInItemSectionIndex(e);
			Threshold threshold = thresholdList.get(index);
			new ThresholdAdditionalMenu(playerMenuUtility, threshold, (index + 1)).switchOpen();
		}
	}

	@Override
	public void setMenuItems() {
		updateItems();
		setItems();
		setOuterFillerGlass();
	}

	public void setItems(){
		addMenuBorder(39, 40, 41);
		inventory.setItem(36, Utils.makeItem(Material.ARROW, "&aBack to statistics", "&7Click to go back to statistics"));
		inventory.setItem(0, Utils.makeItem(Material.WRITABLE_BOOK, "&aCreate new threshold", "&7Click to create."));
		inventory.setItem(4, Utils.makeItem(material, "&b" + world.getName(),
				"&7Loaded Chunks: &a" + world.getLoadedChunks().length,
				"&7Player Amount: &a" + world.getPlayerCount(),
				"&7Entity Amount: &a" + world.getEntityCount(),
				"&7Tile Amount: &a" + world.getTileEntityCount(),
				"",
				"&a&lCurrent view distance: &6" + world.getViewDistance(),
				"&8Click to change world icon!"));
		for (int i = 0; i < thresholdList.size(); i++) {
			inventory.setItem(setCorrectIndex(i), Utils.makeItem(Material.PAPER, "&aThreshold #" + (i+1),
					"&7Player amount: &6" + thresholdList.get(i).getPlayerAmount(), "&7View distance: &6" + thresholdList.get(i).getViewDistance()));
		}
	}

	public void updateItems() {
		new BukkitRunnable() {
			@Override
			public void run() {
				inventory.setItem(4, Utils.makeItem(material, "&b" + world.getName(),
						"&7Loaded Chunks: &a" + world.getLoadedChunks().length,
						"&7Player Amount: &a" + world.getPlayerCount(),
						"&7Player Amount: &a" + world.getPlayerCount(),
						"&7Entity Amount: &a" + world.getEntityCount(),
						"&7Tile Amount: &a" + world.getTileEntityCount(),
						"",
						"&a&lCurrent view distance: &6" + world.getViewDistance()));
				if (close){
					cancel();
				}
			}
		}.runTaskTimerAsynchronously(main, 0, 20);
	}
}
