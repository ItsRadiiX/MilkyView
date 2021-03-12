package com.itsradiix.milkyview.models.commands;

import com.itsradiix.milkyview.Main;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public abstract class SubCommand {

	/**
	 * SubCommand Class for creating subcommands of a command
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	protected Main main = Main.getMain();

	private Command baseCommand;

	public void setBaseCommand(Command command){
		baseCommand = command;
	}

	public Command getBaseCommand(){
		return baseCommand;
	}

	public abstract String getName();

	public abstract String getDescription();

	public abstract String getSyntax();

	public abstract String getPermission();

	public abstract boolean allowConsole();

	public abstract boolean hideFromTabComplete();

	public abstract boolean hideFromConsole();

	public abstract void perform(CommandSender sender, String[] args);

	public abstract ArrayList<String> getSubCommandArguments(Player player, String[] args);

	public void playSound(CommandSender sender, Sound sound){
		if (sender instanceof Player){
			Player p = (Player) sender;
			p.playSound(p.getLocation(), sound, 0.3F, 1.0F);
		}
	}
	public void playSound(Player p, Sound sound){
		p.playSound(p.getLocation(), sound, 0.3F, 1.0F);
	}

}
