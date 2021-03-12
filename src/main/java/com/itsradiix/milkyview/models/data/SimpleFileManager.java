package com.itsradiix.milkyview.models.data;

import com.itsradiix.milkyview.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * SimpleFileManager Class for creating custom YAML File Configurations
 * This Class has Simple functionalities
 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
 *
 * Author: ItsRadiiX (Bryan Suk)
 */

public class SimpleFileManager {

	File f;
	YamlConfiguration cnf;
	String folder;
	String filename;

	public SimpleFileManager(String folder, String filename){
		this.folder = folder;
		this.filename = filename;
		f = new File(Main.getMain().getDataFolder() + "/" + folder + "/", filename + ".yml");
	}

	public SimpleFileManager(String filename){
		this.filename = filename;
		f = new File(Main.getMain().getDataFolder(), filename + ".yml");
	}

	public boolean exists(){
		return f.exists();
	}

	public YamlConfiguration getYamlConfiguration(){
		if (cnf == null) {
			cnf = YamlConfiguration.loadConfiguration(f);
		}
		return cnf;
	}

	public void reload(){
		if (folder != null){
			f = new File(Main.getMain().getDataFolder() + "/" + folder + "/", filename + ".yml");
		} else {
			f = new File(Main.getMain().getDataFolder(), filename + ".yml");
		}
		cnf = YamlConfiguration.loadConfiguration(f);
	}

	public void save(){
		save(cnf);
	}

	public void save(FileConfiguration cnf){
		try {
			cnf.save(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
