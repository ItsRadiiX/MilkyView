package com.itsradiix.milkyview.models.modules;

import com.itsradiix.milkyview.Main;
import com.itsradiix.milkyview.models.data.SimpleFileManager;

import java.util.ArrayList;
import java.util.List;

public class Modules {

	/**
	 * Modules Class where all instantiated module Classes should be added to enable their functionality
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	private final List<Module> moduleList = new ArrayList<>();
	private final SimpleFileManager simpleFileManager;

	public Modules(){
		simpleFileManager = new SimpleFileManager("modules");
	}

	public void addModule(Module module){
		moduleList.add(module);
		if (moduleExist(module)){
			module.setModuleEnabled(checkModule(module));
			module.setModuleLogging(isLogging(module));
		} else {
			simpleFileManager.getYamlConfiguration().set(module.getName() + ".enabled", true);
			simpleFileManager.getYamlConfiguration().set(module.getName() + ".logging", true);
			module.setModuleEnabled(true);
			module.setModuleLogging(true);
			simpleFileManager.save();
		}
	}

	public void loadModules(){
		for (Module m : moduleList){
			if (m.isModuleEnabled()){
				m.onEnable();
				if (m.isModuleLogging()){
					Main.logToConsole("&7Enabling Module &f" + m.getName());
				}
			} else {
				m.unloadCommandsIfDisabled();
				if (m.isModuleLogging()){
					Main.logToConsole("&7Module &f" + m.getName() + " &7is disabled in config");
				}
			}
		}
	}

	public void unloadModules(){
		for (Module m : moduleList){
			if (m.isModuleEnabled()){
				m.onDisable();
				if (m.isModuleLogging()) {
					Main.logToConsole("&7Disabling Module &f" + m.getName());
				}
			}
		}
	}

	public List<Module> getModuleList(){
		return moduleList;
	}

	public boolean moduleExist(Module module){
		return simpleFileManager.getYamlConfiguration().contains(module.getName());
	}

	public boolean checkModule(Module module){
		return simpleFileManager.getYamlConfiguration().getBoolean(module.getName() + ".enabled");
	}

	public boolean isLogging(Module module){
		return simpleFileManager.getYamlConfiguration().getBoolean(module.getName() + ".logging");
	}
}
