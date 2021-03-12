package com.itsradiix.milkyview.models.modules;

import com.itsradiix.milkyview.Main;

public abstract class Module {

	/**
	 * Module Class for easily creating large modules to keep code in Main class clean
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	private boolean isModuleEnabled;
	private boolean moduleLogging;
	protected Main main = Main.getMain();

	public abstract String getName();

	public abstract void onEnable();

	public abstract void onDisable();

	public abstract void initializeCommands();

	public abstract void unloadCommandsIfDisabled();

	public abstract void initializeManagers();

	public boolean isModuleLogging() {
		return moduleLogging;
	}

	public void setModuleLogging(boolean moduleLogging) {
		this.moduleLogging = moduleLogging;
	}

	public boolean isModuleEnabled(){
		return isModuleEnabled;
	}

	public void setModuleEnabled(boolean enabled){
		isModuleEnabled = enabled;
	}

}
