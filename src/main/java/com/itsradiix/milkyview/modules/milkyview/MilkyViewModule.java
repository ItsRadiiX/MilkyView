package com.itsradiix.milkyview.modules.milkyview;

import com.itsradiix.milkyview.Main;
import com.itsradiix.milkyview.models.commands.HelpCommand;
import com.itsradiix.milkyview.models.data.SimpleFileManager;
import com.itsradiix.milkyview.models.modules.Module;
import com.itsradiix.milkyview.modules.milkyview.commands.MilkyViewCommand;
import com.itsradiix.milkyview.modules.milkyview.commands.subcommands.MilkyViewDebugCommand;
import com.itsradiix.milkyview.modules.milkyview.commands.subcommands.MilkyViewReloadCommand;
import com.itsradiix.milkyview.modules.milkyview.commands.subcommands.MilkyViewStatsCommand;
import com.itsradiix.milkyview.modules.milkyview.commands.subcommands.MilkyViewVersionCommand;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.LocalDateTime;
import java.util.*;

public class MilkyViewModule extends Module implements Listener {

	/**
	 * MilkyViewModule Class, this is the default Module on which the plugin depends, disabling this will disable the plugins functionality
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	private static MilkyViewModule milkyViewModule;

	public static SimpleFileManager fileManager = new SimpleFileManager("view-distances");
	public static Map<World, ViewDistance> worldViewDistanceHashMap = new HashMap<>();
	public static Map<World, Material> worldMaterialMap = new HashMap<>();
	public static List<Player> debugPlayerList = new ArrayList<>();

	@Override
	public String getName() {
		return "MilkyView";
	}

	@Override
	public void onEnable() {
		milkyViewModule = this;
		initializeManagers();
		initializeCommands();
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void initializeCommands() {
		MilkyViewCommand milkyViewCommand = new MilkyViewCommand();
		milkyViewCommand.addSubCommand(new HelpCommand());
		milkyViewCommand.addSubCommand(new MilkyViewDebugCommand());
		milkyViewCommand.addSubCommand(new MilkyViewStatsCommand());
		milkyViewCommand.addSubCommand(new MilkyViewVersionCommand());
		milkyViewCommand.addSubCommand(new MilkyViewReloadCommand());
		Main.getCommandManager().addCommand(milkyViewCommand);
	}

	@Override
	public void unloadCommandsIfDisabled() {
		Bukkit.getCommandMap().getKnownCommands().remove("milkyview");
	}

	@Override
	public void initializeManagers() {
		loadThresholds();
		viewDistanceTask();
		debugMessage();
		Bukkit.getServer().getPluginManager().registerEvents(this, main);
	}

	public static SimpleFileManager getFileManager() {
		return fileManager;
	}

	public static List<Player> getDebugPlayerList() {
		return debugPlayerList;
	}

	public void reload(){
		worldViewDistanceHashMap.clear();
		fileManager.reload();
		loadThresholds();
		checkThreshold();
	}

	public static void updateMaterial(World world, String material) {
		try {
			getWorldMaterialMap().put(world, Material.valueOf(material));
			fileManager.getYamlConfiguration().set(world.getName() + ".Material", material);
			fileManager.save();
		} catch (Exception ignored){}
	}

	public void setViewDistance(World world, int viewDistance){
		Bukkit.getScheduler().runTask(main, () -> world.setViewDistance(viewDistance));
		for (Player p : getDebugPlayerList()){
			p.sendMessage(Main.chatColor(main.getMessagesManager().getPrefixChat() + "&aView distance of " + world.getName() + " changed to: &6" + viewDistance));
		}
	}

	public void viewDistanceTask(){
		Bukkit.getScheduler().runTaskTimerAsynchronously(main, this::checkThreshold, 18000, 18000);
	}

	public void loadThresholds(){
		boolean save = false;
		try {
			for (World w : main.getWorldsList()){
				ConfigurationSection worldSection = fileManager.getYamlConfiguration().getConfigurationSection(w.getName());

				if (worldSection == null){
					fileManager.getYamlConfiguration().createSection(w.getName());
					worldSection = fileManager.getYamlConfiguration().getConfigurationSection(w.getName());
					if (!save){
						save = true;
					}
				}  else {
					ViewDistance viewDistance = new ViewDistance(w);
					worldViewDistanceHashMap.put(w, viewDistance);
				}

				ConfigurationSection thresholdSection = worldSection.getConfigurationSection("Thresholds");
				if ( thresholdSection != null && !thresholdSection.getKeys(false).isEmpty()){
					for (String s : thresholdSection.getKeys(false)){
						if (worldViewDistanceHashMap.containsKey(w)){
							worldViewDistanceHashMap.get(w).addViewDistanceStartup(Integer.parseInt(s), thresholdSection.getInt(s));
						} else {
							ViewDistance viewDistance = new ViewDistance(w);
							viewDistance.addViewDistanceStartup(Integer.parseInt(s), thresholdSection.getInt(s));
							worldViewDistanceHashMap.put(w, viewDistance);
						}
					}
				} else {
					ViewDistance viewDistance = new ViewDistance(w);
					worldViewDistanceHashMap.put(w, viewDistance);
					worldSection.createSection("Thresholds");
					if (!save){
						save = true;
					}
				}
				Material worldMaterial;
				if (worldSection.getString("Material") == null){
					if (w.getName().contains("nether")){
						worldSection.set("Material", Material.NETHERRACK.toString());
					} else if (w.getName().contains("the_end")){
						worldSection.set("Material", Material.END_STONE.toString());
					} else {
						worldSection.set("Material", Material.GRASS_BLOCK.toString());
					}
					if (!save){
						save = true;
					}
					worldMaterial = Material.valueOf(worldSection.getString("Material"));
				} else {
					worldMaterial = Material.valueOf(worldSection.getString("Material", Material.GRASS_BLOCK.toString()));
				}
				worldMaterialMap.put(w, worldMaterial);
			}
		} catch (Exception e){
			e.fillInStackTrace();
		}
		if (save){
			fileManager.save();
		}
	}

	public void checkThreshold(){
		for (World w : worldViewDistanceHashMap.keySet()){
			int tmp = Bukkit.getViewDistance();
			ViewDistance viewDistance = worldViewDistanceHashMap.get(w);
			List<Integer> tmpList = Threshold.getPlayerAmounts(viewDistance.getThresholdList());
			Collections.sort(tmpList);
			for (int i = 0; i < tmpList.size(); i++) {
				if (Bukkit.getOnlinePlayers().size() >= tmpList.get(i)){
					tmp = viewDistance.getThresholdList().get(i).getViewDistance();
				}
			}
			setViewDistance(w, tmp);
		}
	}

	public void debugMessage(){
		Bukkit.getScheduler().runTaskTimerAsynchronously(main, () -> {
			for (Player p : getDebugPlayerList()){
				TextComponent textComponent = new TextComponent();
				textComponent.setText(Main.chatColor(main.getMessagesManager().getPrefixChat() + "&7Currently loaded chunks amount in &b" + p.getLocation().getWorld().getName() + "&7: &a" + p.getLocation().getWorld().getLoadedChunks().length));
				textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(LocalDateTime.now().toString())));
				p.sendMessage(textComponent);
			}
		}, 60, 60);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		Bukkit.getScheduler().runTaskAsynchronously(main, this::checkThreshold);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		Bukkit.getScheduler().runTaskAsynchronously(main, this::checkThreshold);
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent e){
		Bukkit.getScheduler().runTaskAsynchronously(main, this::checkThreshold);
	}

	public static MilkyViewModule getMilkyViewModule() {
		return milkyViewModule;
	}

	public static Map<World, ViewDistance> getWorldViewDistanceHashMap() {
		return worldViewDistanceHashMap;
	}

	public static void removeThreshold(Threshold threshold){
		try {
			World w = threshold.getViewDistanceClass().getWorld();
			ConfigurationSection worldSection = fileManager.getYamlConfiguration().getConfigurationSection(w.getName());
			ConfigurationSection section = worldSection.getConfigurationSection("Thresholds");
			if (section != null){
				section.set(String.valueOf(threshold.getPlayerAmount()), null);
				fileManager.save();
			}
			fileManager.save();
		} catch (Exception ignored){}
		MilkyViewModule.getMilkyViewModule().checkThreshold();
	}

	public static void changeThresholdPlayerAmount(Threshold threshold, int playerAmount) {
		try {
			Main.logToConsole(String.valueOf(playerAmount));
			World w = threshold.getViewDistanceClass().getWorld();
			ConfigurationSection worldSection = fileManager.getYamlConfiguration().getConfigurationSection(w.getName());
			ConfigurationSection section = worldSection.getConfigurationSection("Thresholds");
			if (section != null){
				section.set(String.valueOf(playerAmount), null);
				section.set(String.valueOf(threshold.getPlayerAmount()), threshold.getViewDistance());
				fileManager.save();
			}
		} catch (Exception ignored){}
		MilkyViewModule.getMilkyViewModule().checkThreshold();
	}

	public static void addThreshold(ViewDistance viewDistance, Threshold threshold){
		ConfigurationSection section = fileManager.getYamlConfiguration().getConfigurationSection(viewDistance.getWorld().getName());
		section.getConfigurationSection("Thresholds").set(String.valueOf(threshold.getPlayerAmount()), threshold.getViewDistance());
		fileManager.save();
	}

	public static boolean doesPlayerAmountExist(World w, int playerAmount){
		try {
			return fileManager.getYamlConfiguration().getConfigurationSection(w.getName()).getConfigurationSection("Thresholds").contains(String.valueOf(playerAmount));
		} catch (Exception e){
			return false;
		}
	}

	public static void changeThresholdViewDistance(Threshold threshold) {
		try {
			World w = threshold.getViewDistanceClass().getWorld();
			ConfigurationSection worldSection = fileManager.getYamlConfiguration().getConfigurationSection(w.getName());
			ConfigurationSection section = worldSection.getConfigurationSection("Thresholds");
			if (section != null){
				section.set(String.valueOf(threshold.getPlayerAmount()), threshold.getViewDistance());
				fileManager.save();
			}
		} catch (Exception ignored){}
		MilkyViewModule.getMilkyViewModule().checkThreshold();
	}

	public static Map<World, Material> getWorldMaterialMap() {
		return worldMaterialMap;
	}
}
