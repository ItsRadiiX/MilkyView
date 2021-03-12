package com.itsradiix.milkyview.modules.milkyview.commands.subcommands;

import com.itsradiix.milkyview.Main;
import com.itsradiix.milkyview.models.commands.SubCommand;
import com.itsradiix.milkyview.modules.milkyview.MilkyViewModule;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MilkyViewDebugCommand extends SubCommand {

	/**
	 *  MilkyViewVersionCommand Class, this is an subcommand which will toggle debug mode for the player
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	@Override
	public String getName() {
		return "debug";
	}

	@Override
	public String getDescription() {
		return "Shows debug information as chat messages";
	}

	@Override
	public String getSyntax() {
		return getBaseCommand().getSyntax() + " debug";
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
		Player p = (Player) sender;
		if (MilkyViewModule.getDebugPlayerList().contains(p)){
			MilkyViewModule.getDebugPlayerList().remove(p);
			sender.sendMessage(Main.chatColor(main.getMessagesManager().getMessage("toggleDebugMode").replaceAll("%status%", "&cFalse")));
			playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS);
		} else {
			MilkyViewModule.getDebugPlayerList().add(p);
			sender.sendMessage(Main.chatColor(main.getMessagesManager().getMessage("toggleDebugMode").replaceAll("%status%", "&aTrue")));
			playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING);
		}
	}

	@Override
	public ArrayList<String> getSubCommandArguments(Player player, String[] args) {
		return new ArrayList<>();
	}
}
