package com.itsradiix.milkyview.models.commands;

import com.itsradiix.milkyview.Main;
import org.bukkit.Sound;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command implements CommandExecutor, TabExecutor {

	/**
	 * Command Class for in-game Commands, all instances of this Class should be added to the CommandManager
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	protected Main main = Main.getMain();

	private final List<SubCommand> subCommands = new ArrayList<>();

	public abstract String getName();

	public abstract String getDisplayName();

	public abstract String getDescription();

	public abstract String getSyntax();

	public abstract String getPermission();

	public abstract boolean allowConsole();

	public abstract boolean hideConsoleUsage();

	public abstract void defaultUsage(CommandSender sender, String[] args);

	public abstract List<String> showTabListArguments();

	public void addSubCommand(SubCommand subCommand){
		subCommand.setBaseCommand(this);
		subCommands.add(subCommand);
		if (subCommand.hideFromConsole()){
			Main.getCommandManager().getFilteredCommands().add("/" + subCommand.getBaseCommand().getName().toLowerCase() + " " + subCommand.getName().toLowerCase());
			Main.getCommandManager().getFilteredCommands().add("/" + subCommand.getBaseCommand().getName().toLowerCase() + " " + subCommand.getName().toLowerCase() + " ");
		}
	}

	public List<SubCommand> getSubCommands() {
		return subCommands;
	}

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {


		if (!(sender instanceof Player)) {
			if (!allowConsole()) {
				sender.sendMessage(main.getMessagesManager().getPrefixedMessage("noConsole"));
				return true;
			}
		}

		if (sender.hasPermission(getPermission())) {
			if (!subCommands.isEmpty() && args.length > 0) {

				for (SubCommand s : subCommands) {
					if (args[0].equalsIgnoreCase(s.getName())) {
						if (!(sender instanceof Player)) {
							if (!s.allowConsole()) {
								sender.sendMessage(main.getMessagesManager().getPrefixedMessage("noConsole"));
								return true;
							}
						}
						if (sender.hasPermission(s.getPermission())) {
							s.perform(sender, sendSubArgs(args));
						} else {
							sender.sendMessage(main.getMessagesManager().getPrefixedMessage("noPermission"));
						}
						return true;
					}
				}
			}

			defaultUsage(sender, args);
		} else {
			sender.sendMessage(main.getMessagesManager().getPrefixedMessage("noPermission"));
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {

		List<String> tmp = showTabListArguments();
		List<String> subArgs = new ArrayList<>(tmp);
		if (!subCommands.isEmpty() && sender instanceof Player) {
			if (args.length < 2) {
				for (SubCommand s : subCommands) {
					if (sender.hasPermission(s.getPermission()) && !s.hideFromTabComplete()) {
						if (args[0] != null){
							if (!s.getName().toLowerCase().contains(args[0].toLowerCase())){
								continue;
							}
						}
						subArgs.add(s.getName());
					}
				}
				return subArgs;
			} else {
				for (SubCommand s : subCommands){
					if (args[0].equalsIgnoreCase(s.getName()) && sender.hasPermission(s.getPermission()) && !s.hideFromTabComplete()){
						return s.getSubCommandArguments((Player) sender, sendSubArgs(args));
					}
				}
			}
		}

		return subArgs;
	}

	public String[] sendSubArgs(String[] args){
		if (args.length > 1) {
			return Arrays.copyOfRange(args, 1, args.length);
		} else {
			return new String[0];
		}
	}

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
