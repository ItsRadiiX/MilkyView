package com.itsradiix.milkyview.models.commands;

import com.itsradiix.milkyview.Main;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand extends SubCommand {

	/**
	 * Default HelpCommand Class for creating easy help commands for commands.
	 * This Class extends on SubCommand as it will always be used as subArgument.
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Displays this help message";
	}

	@Override
	public String getSyntax() {
		return "/" + getBaseCommand().getName() + " help";
	}

	@Override
	public String getPermission() {
		return getBaseCommand().getName() + ".help";
	}

	@Override
	public boolean allowConsole() {
		return false;
	}

	@Override
	public boolean hideFromTabComplete() {
		return false;
	}

	@Override
	public boolean hideFromConsole() {
		return false;
	}

	@Override
	public void perform(CommandSender sender, String[] args) {
		sender.sendMessage(Main.chatColor(main.getMessagesManager().getMessage("helpHeader").replaceAll("%command_name%", getBaseCommand().getDisplayName())));

		List<SubCommand> subCommands = getBaseCommand().getSubCommands();
		sender.sendMessage(Main.chatColor("&b" + getBaseCommand().getSyntax() + " &7-&r " + getBaseCommand().getDescription()));
		for (SubCommand subCommand : subCommands) {
			if (sender.hasPermission(subCommand.getPermission())) {
				sender.sendMessage(Main.chatColor("&b" +
						subCommand.getSyntax() +
						" &7-&r " +
						subCommand.getDescription()));
			}
		}

		sender.sendMessage(Main.chatColor(main.getMessagesManager().getMessage("helpFooter") + "\n"));
		if (sender instanceof Player){
			Player player = (Player) sender;
			player.playSound(player.getLocation(), Sound.ENTITY_EGG_THROW, 0.2F, 1.0F);
		}
	}

	@Override
	public ArrayList<String> getSubCommandArguments(Player player, String[] args) {
		return null;
	}
}
