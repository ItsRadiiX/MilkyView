package com.itsradiix.milkyview.modules.milkyview.commands.subcommands;

import com.itsradiix.milkyview.Main;
import com.itsradiix.milkyview.models.commands.SubCommand;
import com.itsradiix.milkyview.modules.milkyview.menus.StatsMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MilkyViewStatsCommand extends SubCommand {

	/**
	 * MilkyViewStatsCommand Class, this is an subcommand which will open a menu which display important statistics
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	@Override
	public String getName() {
		return "stats";
	}

	@Override
	public String getDescription() {
		return "View important statistics";
	}

	@Override
	public String getSyntax() {
		return getBaseCommand().getSyntax() + " stats";
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
		new StatsMenu(Main.getPlayerMenuUtility((Player) sender)).open();
	}

	@Override
	public ArrayList<String> getSubCommandArguments(Player player, String[] args) {
		return new ArrayList<>();
	}
}
