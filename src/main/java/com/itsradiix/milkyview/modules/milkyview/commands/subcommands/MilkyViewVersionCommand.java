package com.itsradiix.milkyview.modules.milkyview.commands.subcommands;

import com.itsradiix.milkyview.Main;
import com.itsradiix.milkyview.models.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MilkyViewVersionCommand extends SubCommand {

	/**
	 *  MilkyViewVersionCommand Class, this is an subcommand which will show plugin version and author
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	@Override
	public String getName() {
		return "version";
	}

	@Override
	public String getDescription() {
		return "Shows version of MilkyView";
	}

	@Override
	public String getSyntax() {
		return getBaseCommand().getSyntax() + " version";
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
		sender.sendMessage(Main.chatColor(main.getMessagesManager().getPrefixChat() + "&7Currently running version: &b" + main.getDescription().getVersion()
				+ "\n" + main.getMessagesManager().getPrefixChat() + "&7This plugin is created with &c♥ &7by &6" + main.getDescription().getAuthors().get(0)));
	}

	@Override
	public ArrayList<String> getSubCommandArguments(Player player, String[] args) {
		return new ArrayList<>();
	}
}
