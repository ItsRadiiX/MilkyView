package com.itsradiix.milkyview.models.data;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * MessagesManager Class for easy usage of messages across the plugin
 * This Class has multiple functionalities, and are all aimed towards maximizing the potentials.
 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
 *
 * Author: ItsRadiiX (Bryan Suk)
 */

public class MessagesManager {

	private final FileManager messages = new FileManager("messages");

	public MessagesManager(){
		messages.setup();
	}

	public FileManager getFileManager() {
		return messages;
	}

	public FileConfiguration getConfiguration() {
		return messages.getFileConfiguration();
	}

	public static String chatColor(String string) {return ChatColor.translateAlternateColorCodes('&', string);}

	public static String[] chatColor(List<String> string) {
		String[] stringArray = new String[string.size()];
		for (int i = 0; i < string.size(); i++) {
			stringArray[i] = ChatColor.translateAlternateColorCodes('&', string.get(i));
		}
		return stringArray;
	}

	public static String removeChatColors(String string){
		return ChatColor.stripColor(chatColor(string));
	}

	public String getMessage(String message){
		return messages.getString(message);
	}

	public List<String> getMessages(String message){
		return messages.getFileConfiguration().getStringList(message);
	}

	public String getPAPIMessage(Player p, String message){
		return messages.getPlayerString(p, message);
	}

	public String getPrefix(){
		return getMessage("prefix");
	}

	public String getPrefixChatSymbol(){return getMessage("prefixChatSymbol");}

	public String getPrefixChat(){
		return getPrefix() + getPrefixChatSymbol();
	}

	public String getPrefixedMessage(String message){
		return getPrefixChat() + getMessage(message);
	}

	public String getPrefixedPAPIMessage(Player p, String message){
		return getPrefixChat() + getPAPIMessage(p, message);
	}

}

