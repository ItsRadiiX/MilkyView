package com.itsradiix.milkyview.models.commands;

import com.google.common.annotations.VisibleForTesting;
import com.itsradiix.milkyview.Main;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandManager {

	/**
	 * CommandManager Class that gathers all information concerning Commands, SubCommands, CommandGroups & TabList Completion
	 * It features an console filter, tabList subArgs smart completion, easy access to all online players as subArgs and much more
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	private static final Main main = Main.getMain();
	private final List<Command> commands = new ArrayList<>();
	private final List<CommandGroup> commandGroups = new ArrayList<>();
	private final List<String> filteredCommands = new ArrayList<>();
	private static final HelpCommand helpCommand = new HelpCommand();

	public CommandManager(){}

	public void initializeFilter(){
		org.apache.logging.log4j.core.Logger logger;
		logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
		logger.addFilter(new Log4JFilter());
	}

	public void addCommandGroup(CommandGroup commandGroup){
		commandGroups.add(commandGroup);
	}

	public void addCommand(Command command) {
		commands.add(command);
		if (command.hideConsoleUsage()){
			filteredCommands.add("/" + command.getName().toLowerCase());
			filteredCommands.add("/" + command.getName().toLowerCase() + " ");
		}

		main.getCommand(command.getName()).setExecutor(command);
		main.getCommand(command.getName()).setTabCompleter(command);
	}

	public List<String> getFilteredCommands() {
		return filteredCommands;
	}

	public String[] getFilteredCommandsArray() {
		String[] tmp = new String[filteredCommands.size()];
		filteredCommands.toArray(tmp);
		return tmp;
	}

	public static ArrayList<String> getOnlinePlayersForTabIndex(String[] args, int argLength) {
		ArrayList<String> subArgs = new ArrayList<>();
		Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
		Bukkit.getServer().getOnlinePlayers().toArray(players);
		if (args.length == argLength){
			for (Player p : players) {
				subArgs.add(p.getName());
			}
		}
		return subArgs;
	}

	public static ArrayList<String> getOnlinePlayers() {
		ArrayList<String> subArgs = new ArrayList<>();
		Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
		Bukkit.getServer().getOnlinePlayers().toArray(players);
		for (Player p : players) {
			subArgs.add(p.getName());
		}
		return subArgs;
	}

	public List<Command> getCommands() {
		return commands;
	}

	public List<CommandGroup> getCommandGroups() {
		return commandGroups;
	}

	public CommandGroup getCommandGroup(String name){
		for (CommandGroup cg : commandGroups){
			if (cg.getName().equalsIgnoreCase(name)){
				return cg;
			}
		}
		return null;
	}

	public static HelpCommand getHelpCommand() {
		return helpCommand;
	}

	private class Log4JFilter extends AbstractFilter {

		/**
		 * Validates a Message instance and returns the {@link Result} value
		 * depending on whether the message contains sensitive AuthMe data.
		 *
		 * @param message The Message object to verify
		 *
		 * @return The Result value
		 */
		private Result validateMessage(Message message) {
			if (message == null) {
				return Result.NEUTRAL;
			}
			return validateMessage(message.getFormattedMessage());
		}

		/**
		 * Validates a message and returns the {@link Result} value depending
		 * on whether the message contains sensitive AuthMe data.
		 *
		 * @param message The message to verify
		 *
		 * @return The Result value
		 */
		private Result validateMessage(String message) {
			LogFilterHelper lfh = new LogFilterHelper();
			return lfh.isSensitiveCommand(message) ? Result.DENY : Result.NEUTRAL;
		}

		@Override
		public Result filter(LogEvent event) {
			Message candidate = null;
			if (event != null) {
				candidate = event.getMessage();
			}
			return validateMessage(candidate);
		}

		@Override
		public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
			return validateMessage(msg);
		}

		@Override
		public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
			return validateMessage(msg);
		}

		@Override
		public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
			String candidate = null;
			if (msg != null) {
				candidate = msg.toString();
			}
			return validateMessage(candidate);
		}
	}

	final class LogFilterHelper {
		@VisibleForTesting
		final List<String> COMMANDS_TO_SKIP = withAndWithoutPrefix(getFilteredCommandsArray());

		private static final String ISSUED_COMMAND_TEXT = "issued server command:";

		/**
		 * Validate a message and return whether the message contains a sensitive command.
		 *
		 * @param message The message to verify
		 *
		 * @return True if it is a sensitive AuthMe command, false otherwise
		 */
		public boolean isSensitiveCommand(String message) {
			if (message == null) {
				return false;
			}
			String lowerMessage = message.toLowerCase();
			return lowerMessage.contains(ISSUED_COMMAND_TEXT) && containsAny(lowerMessage, COMMANDS_TO_SKIP);
		}

		private boolean containsAny(String str, Iterable<String> pieces) {
			if (str == null) {
				return false;
			}
			for (String piece : pieces) {
				if (piece != null && str.contains(piece)) {
					return true;
				}
			}
			return false;
		}

		private List<String> withAndWithoutPrefix(String... commands) {
			List<String> commandList = new ArrayList<>(commands.length * 2);
			for (String command : commands) {
				commandList.add(command);
				commandList.add(command.substring(0, 1) + main.getName() + ":" + command.substring(1));
			}
			return Collections.unmodifiableList(commandList);
		}
	}
}
