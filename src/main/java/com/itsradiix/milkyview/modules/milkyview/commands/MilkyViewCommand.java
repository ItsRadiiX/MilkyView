package com.itsradiix.milkyview.modules.milkyview.commands;

import com.itsradiix.milkyview.models.commands.Command;
import com.itsradiix.milkyview.models.commands.HelpCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class MilkyViewCommand extends Command {

	/**
	 * MilkyViewCommand Class, base command of this plugin
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	@Override
	public String getName() {
		return "milkyview";
	}

	@Override
	public String getDisplayName() {
		return main.getMessagesManager().getPrefix();
	}

	@Override
	public String getDescription() {
		return "Default command usage";
	}

	@Override
	public String getSyntax() {
		return "/milkyview";
	}

	@Override
	public String getPermission() {
		return "milkyview.admin";
	}

	@Override
	public boolean allowConsole() {
		return false;
	}

	@Override
	public boolean hideConsoleUsage() {
		return false;
	}

	@Override
	public void defaultUsage(CommandSender sender, String[] args) {
		HelpCommand helpCommand = new HelpCommand();
		helpCommand.setBaseCommand(this);
		helpCommand.perform(sender, args);
	}

	@Override
	public List<String> showTabListArguments() {
		return new ArrayList<>();
	}
}
