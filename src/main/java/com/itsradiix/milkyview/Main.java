package com.itsradiix.milkyview;

import com.itsradiix.milkyview.events.InventoryClickListener;
import com.itsradiix.milkyview.models.commands.CommandManager;
import com.itsradiix.milkyview.models.data.FileManager;
import com.itsradiix.milkyview.models.data.MessagesManager;
import com.itsradiix.milkyview.models.menus.PlayerMenuUtility;
import com.itsradiix.milkyview.models.modules.Modules;
import com.itsradiix.milkyview.modules.milkyview.MilkyViewModule;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Main extends JavaPlugin {

	/**
	 * Main Class, this is where the Plugin starts
	 * A lot of work has been put into making MilkyView, and that really shows when looking how many lines of code have gone into it
	 * Please don't claim this Class / Plugin as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	private static Main main;

	private List<FileManager> fileManagers;

	private static CommandManager commandManager;
	private MessagesManager messagesManager;
	private static Modules modules;

	private int loadErrorsAmount = 0;
	private boolean autoReloading = false;

	private List<World> worldsList;

	private final Map<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

	@Override
	public void onEnable() {
		// Plugin startup logic
		main = this;

		// Create starting Time variable
		long start;

		// Log Starting up Plugin
		logToConsole("");
		logToConsole("&7()=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=()");
		logToConsole("&6  __  __ _ _ _       __      ___  ");
		logToConsole("&6 |  \\/  (_) | |      \\ \\    / (_)");
		logToConsole("&6 | \\  / |_| | | ___   \\ \\  / / _  _____      __");
		logToConsole("&6 | |\\/| | | | |/ / | | \\ \\/ / | |/ _ \\ \\ /\\ / /");
		logToConsole("&6 | |  | | | |   <| |_| |\\  /  | |  __/\\ V  V /");
		logToConsole("&6 |_|  |_|_|_|_|\\_\\\\__, | \\/   |_|\\___| \\_/\\_/ ");
		logToConsole("&6                   __/ |                      ");
		logToConsole("&6                  |___/                      ");
		logToConsole("");
		logToConsole("&bVersion: " + getDescription().getVersion());
		logToConsole("&aAuthor(s): " + getDescription().getAuthors().toString());
		logToConsole("&cServer version: " + getServer().getMinecraftVersion());
		logToConsole("&7()=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=()");
		logToConsole("");
		logToConsole("&6" + getName() + "&7 is starting...");

		// initialize fileManagers list
		fileManagers = new ArrayList<>();
		worldsList = Bukkit.getWorlds();

		// Loading Command Manager
		logToConsole("&7------------------------------------------|");
		logToConsole("&7Loading Command manager...                | (Task 1 activated)"); start = System.nanoTime();
		commandManager = new CommandManager();
		logToConsole("&7Command Manager has been initialized      | (Task 1 completed, took: " + determineTimePassed(start) + " &7ms)");

		// Loading Messages Manager
		logToConsole("&7                                          |");
		logToConsole("&7Loading Messages manager...               | (Task 2 activated)"); start = System.nanoTime();
		messagesManager = new MessagesManager();
		logToConsole("&7Command Messages has been initialized     | (Task 2 completed, took: " + determineTimePassed(start) + " &7ms)");

		// Loading Modules
		logToConsole("&7                                          |");
		logToConsole("&7Loading Modules...                        | (Task 3 activated)"); start = System.nanoTime();
		modules = new Modules();
		initializeModules();
		modules.loadModules();
		logToConsole("&7Modules has been initialized              | (Task 3 completed, took: " + determineTimePassed(start) + " &7ms)");

		// Loading Miscellaneous
		logToConsole("&7                                          |");
		logToConsole("&7Loading Miscellaneous...                  | (Task 4 activated)"); start = System.nanoTime();
		commandManager.initializeFilter();
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), main);
		logToConsole("&7Modules has been initialized              | (Task 4 completed, took: " + determineTimePassed(start) + " &7ms)");

		// End of starting plugin
		logToConsole("&7------------------------------------------|");
		logToConsole("");
		logToConsole("&6" + getName() + "&7 has loaded with " + loadErrorsAmount() + "&7 errors!" );
		logToConsole("&7()==========================================================()");
		logToConsole("");

	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
		modules.unloadModules();
		saveManagers();
	}

	private void initializeModules() {
		modules.addModule(new MilkyViewModule());
	}

	public double determineTimePassed(long start) {
		return ((System.nanoTime()-start)/1e6);
	}

	public void saveManagers() {
		fileManagers.forEach(FileManager::save);
	}

	private String loadErrorsAmount() {
		if (loadErrorsAmount > 0){
			return "&c" + loadErrorsAmount;
		} else {
			return "&a" + loadErrorsAmount;
		}
	}

	public void addLoadError(){
		loadErrorsAmount++;
	}

	public static Main getMain(){
		return main;
	}

	public static CommandManager getCommandManager(){
		return commandManager;
	}

	public static Modules getModules() {
		return modules;
	}

	public MessagesManager getMessagesManager() {
		return messagesManager;
	}

	public List<FileManager> getFileManagers() {
		return fileManagers;
	}

	public static void logToConsole(String message){
		Bukkit.getLogger().info(chatColor(message));
	}

	public static String chatColor(String message){
		return MessagesManager.chatColor(message);
	}

	public List<World> getWorldsList() {
		return worldsList;
	}

	public Map<Player, PlayerMenuUtility> getPlayerPlayerMenuUtilityMap() {
		return playerMenuUtilityMap;
	}

	public static PlayerMenuUtility getPlayerMenuUtility(Player p){
		if (main.getPlayerPlayerMenuUtilityMap().containsKey(p)){
			return main.getPlayerPlayerMenuUtilityMap().get(p);
		} else {
			PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility(p);
			main.getPlayerPlayerMenuUtilityMap().put(p, playerMenuUtility);
			return playerMenuUtility;
		}
	}

	public void setWorldsList(List<World> worldsList) {
		this.worldsList = worldsList;
	}
}
