package com.itsradiix.milkyview.models.data;
import com.itsradiix.milkyview.Main;
import com.itsradiix.milkyview.events.custom.FileReloadEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

/**
 * FileManager Class for creating custom YAML File Configurations
 * This Class has multiple functionalities, and are all aimed towards maximizing the potentials.
 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
 *
 * Author: ItsRadiiX (Bryan Suk)
 */

public class FileManager{

	/**
	 Initializing all important variables
	 */
	protected final Main main = Main.getMain(); // This should be the Main Class of the Plugin
	protected File file;
	protected File folder;
	protected long fileDateModified;
	protected FileConfiguration fileConfiguration;
	protected FileConfiguration defaultFileConfiguration = null;
	protected String fileName = "";
	protected String fileNameExtension = "";
	protected BukkitTask autoReload;
	protected boolean invalidLoad = false;

	/**
	 * Default Constructor for creating FileManager Class
	 * @param fileName Filename without extension as all files managed in this Manager default to YAML files
	 */
	public FileManager(String fileName){
		this.folder = main.getDataFolder();
		this.fileName = fileName;
		this.fileNameExtension = fileName + ".yml";
		defaultFileConfiguration = loadDefaultFileConfiguration();
	}

	public FileManager(String folder, String fileName){
		this.folder = new File(main.getDataFolder() + folder);
		this.fileName = fileName;
		this.fileNameExtension = fileName + ".yml";
	}


	/**
	 * Base Constructor
	 */
	public FileManager(){}


	/**
	 * The Setup Method loads the file, and if the file hasn't been created yet it will generate a default one.
	 */
	public void setup() {

		// Try to create folder if it does not exist
		if (!folder.exists()){
			folder.mkdirs();
		}

		// Try to initialize File
		file = new File(folder, fileNameExtension);

		// If File does not exist
		if (!file.exists()){

			// Save Default resource to data folder and start using this file
			main.saveResource(fileNameExtension, true);
			file = new File(main.getDataFolder(), fileNameExtension);

			// Load YAMLConfiguration from file and log to Console that the file has been created
			fileConfiguration = YamlConfiguration.loadConfiguration(file);
			logToConsole(fileName + " file created!");

		} else {

			// File does exist
			// load YAMLConfiguration from file
			try {
				if (loadYaml(file.getPath())) {
					// // Load YAMLConfiguration from new file
					fileConfiguration = YamlConfiguration.loadConfiguration(file);
					// Old file becomes new file
					fileDateModified = file.lastModified();
					invalidLoad = false;

					// Check if there are key/value combinations that are not loaded, if so add default combinations
					addDefaults(fileConfiguration);
				}
			} catch (Exception e) {
				if (!invalidLoad) {
					// There was an error with parsing the YAML File
					logToConsole("&cAn error occurred while trying to load " + fileNameExtension);
					logToConsole("&cThis is most likely a YAML Exception, there's a syntax error somewhere.");
					logToConsole("&cLoading default configuration...");
					// Load default YAMLConfiguration to keep a working version of the file.
					fileConfiguration = defaultFileConfiguration;
					invalidLoad = true;
				}
			}
		}

		fileDateModified = file.lastModified();
	}


	/**
	 * This is used if setup is not required as the file in question is not a pre-made file from a resource
	 */
	public void initializeFiles(){

		// Try to initialize File
		file = new File(folder, fileNameExtension);

		// Load YAMLConfiguration from file
		fileConfiguration = YamlConfiguration.loadConfiguration(file);
		fileDateModified = file.lastModified();
	}


	/**
	 * Stop the autoReloading BukkitTask
	 */
	public void stopAutoReload(){
		autoReload.cancel();
	}


	/**
	 * Create a BukkitTask that checks if file has been modified every x seconds
	 * @param seconds time in seconds, this will get modified to ticks automatically.
	 */
	public void autoReload(int seconds){

		// Initialize BukkitTask with a Async Timer Task.
		// Async loading was chosen to improve performance and not throttle the main Threat if I/O of disk is overloaded.
		autoReload = Bukkit.getScheduler().runTaskTimerAsynchronously(main, () -> {

			// Create a temporary File to compare with old File initialization
			File tmpFile = new File(main.getDataFolder(), fileNameExtension);

			// If newest File initialization last modified date is not the same as the old File initialization last modified date
			// This means the file has been modified
			if (fileDateModified != tmpFile.lastModified()) {

				try {
					// If loading of YAML File returns true, meaning the file has no parse errors
					if (loadYaml(file.getPath())) {
						// // Load YAMLConfiguration from new file
						fileConfiguration = YamlConfiguration.loadConfiguration(tmpFile);
						// Old file becomes new file
						file = tmpFile;
						fileDateModified = file.lastModified();
						logToConsole(fileName + " has been modified and was reloaded!");
						invalidLoad = false;
					}
				} catch (Exception e) {
					if (!invalidLoad) {
						// There was an error with parsing the YAML File
						logToConsole("&cAn error occurred while trying to load " + fileNameExtension);
						logToConsole("&cThis is most likely a YAML Exception, there's a syntax error somewhere.");
						// Load default YAMLConfiguration to keep a working version of the file.
						fileConfiguration = defaultFileConfiguration;
						invalidLoad = true;
					}
				} finally {
					FileReloadEvent fre = new FileReloadEvent("Reloading");
					Bukkit.getScheduler().runTask(main, () -> Bukkit.getServer().getPluginManager().callEvent(fre));
				}
			}
			// First number is the delay before first run.
			// Second number is the delay between each run.
		}, 0, (seconds* 20L));
	}


	/**
	 * Manual reloading of the file
	 * @return return if file could be reloaded
	 */
	public boolean reload(){
		// Create a temporary File
		File tmpFile = new File(main.getDataFolder(), fileNameExtension);
		try {
			// If loading of YAML File returns true, meaning the file has no parse errors
			if (loadYaml(file.getPath())) {
				// // Load YAMLConfiguration from new file
				fileConfiguration = YamlConfiguration.loadConfiguration(tmpFile);
				// Old file becomes new file and return true
				file = tmpFile;
				fileDateModified = file.lastModified();
				return true;
			}
		} catch (Exception e) {
			// There was an error with parsing the YAML File
			logToConsole("&cAn error occurred while trying to load " + fileNameExtension);
			logToConsole("&cThis is most likely a YAML Exception, there's a syntax error somewhere.");
			// Load old YAMLConfiguration to keep a working version of the file.
			fileConfiguration = defaultFileConfiguration;
		} finally {
			FileReloadEvent fre = new FileReloadEvent("Reloading");
			Bukkit.getScheduler().runTask(main, () -> Bukkit.getServer().getPluginManager().callEvent(fre));
		}
		// Return false, there was an error with reloading the file
		return false;
	}


	/**
	 * Add Default parameters if they dont exist in the currently loaded file.
	 * @param fileConfiguration this it the configuration where the default are added to.
	 */
	protected void addDefaults(FileConfiguration fileConfiguration){
		// Compare both YamlConfigs, set defaults and save
		fileConfiguration.setDefaults(defaultFileConfiguration);
		fileConfiguration.options().copyDefaults(true);
		save();
	}


	/**
	 * Save the file to disk
	 */
	public void save() {
		if (!invalidLoad) {
			try {
				fileConfiguration.save(file);
			} catch(IOException e){
				e.fillInStackTrace();
			}
		}
	}


	/**
	 * Try to parse / load the YAML file to check if there was an error
	 * @param Path path to the file that needs to be parsed
	 * @return returns true if no error was detected
	 * @throws Exception if there was an error with parsing the YAML
	 */
	protected static boolean loadYaml(String Path) throws Exception {
		Map map;
		Yaml yaml = new Yaml();
		InputStream stream = new FileInputStream(Path);
		map = yaml.load(stream);
		if (map == null) {
			throw new Exception("Failed to read yaml file");
		}
		return true;
	}

	// return the default File Configuration
	protected FileConfiguration loadDefaultFileConfiguration() {
		// Create a InputStreamReader to read all information from the file
		Reader defConfigStream = new InputStreamReader(main.getResource(fileNameExtension));
		// Load YamlConfig from the InputStreamReader
		return YamlConfiguration.loadConfiguration(defConfigStream);
	}


	// Simplified version of the getString from fileConfiguration
	public String getString(String path){
		return chatColor(fileConfiguration.getString(path));
	}

	public String getPlayerString(Player p, String path){
		return chatColor(PlaceholderAPI.setPlaceholders(p, fileConfiguration.getString(path)));
	}

	// Simplified version of the getBoolean from fileConfiguration
	public boolean getBoolean(String path){
		return fileConfiguration.getBoolean(path);
	}

	// Simplified version of the getInt from fileConfiguration
	public int getInt(String path){
		return fileConfiguration.getInt(path);
	}

	// Simplified version of the getDouble from fileConfiguration
	public double GetDouble(String path){
		return fileConfiguration.getDouble(path);
	}

	// Simplified version of the getLong from fileConfiguration
	public long getLong(String path){
		return fileConfiguration.getLong(path);
	}

	// Return fileName
	public String getFileName() {
		return fileName;
	}

	public File getFile() {
		return file;
	}

	// Return fileName with extension
	public String getFileNameExtension() {
		return fileNameExtension;
	}

	// Return FileConfiguration
	public FileConfiguration getFileConfiguration() {
		return fileConfiguration;
	}

	// Easier to read way to log to console
	protected void logToConsole(String string){
		Bukkit.getLogger().info(chatColor(string));
	}

	// Create the ability to use Color Codes in chat
	protected String chatColor(String string) {return ChatColor.translateAlternateColorCodes('&', string);}
}
