package com.itsradiix.milkyview.models.commands;

import com.itsradiix.milkyview.Main;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class CommandGroup {

	private final String name;
	private final List<Command> commandList = new ArrayList<>();

	public CommandGroup(String name){
		this.name = name;
	}

	public void addCommand(Command command){
		Main.getCommandManager().addCommand(command);
		commandList.add(command);
	}

	public void disableCommands(){
		for (Command c : commandList){
			Bukkit.getCommandMap().getKnownCommands().remove(c.getName());
		}
	}

	public List<Command> getCommandList() {
		return commandList;
	}

	public String getName() {
		return name;
	}
}
