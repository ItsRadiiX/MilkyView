package com.itsradiix.milkyview.modules.milkyview.commands.subcommands;

import com.itsradiix.milkyview.Main;
import com.itsradiix.milkyview.models.commands.SubCommand;
import com.itsradiix.milkyview.models.data.FileManager;
import com.itsradiix.milkyview.modules.milkyview.MilkyViewModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MilkyViewReloadCommand extends SubCommand {

	/**
	 *  MilkyViewReloadCommand Class, this is an subcommand which will reload the important parts of the plugin
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	@Override
	public String getName() {
		return "reload";
	}

	@Override
	public String getDescription() {
		return "Reloads the plugin";
	}

	@Override
	public String getSyntax() {
		return getBaseCommand().getSyntax() + " reload";
	}

	@Override
	public String getPermission() {
		return getBaseCommand().getPermission();
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
		Bukkit.getScheduler().runTaskAsynchronously(main, () -> {
			MilkyViewModule.getMilkyViewModule().reload();
			for (FileManager f : main.getFileManagers()){
				f.reload();
			}
			sender.sendMessage(main.getMessagesManager().getPrefix() + " " + main.getMessagesManager().getMessage("reload"));
		});
	}

	@Override
	public ArrayList<String> getSubCommandArguments(Player player, String[] args) {
		return new ArrayList<>();
	}
}
