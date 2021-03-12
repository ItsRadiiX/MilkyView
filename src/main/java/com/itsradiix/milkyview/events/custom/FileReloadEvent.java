package com.itsradiix.milkyview.events.custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class FileReloadEvent extends Event {

	/**
	 * FileReloadEvent for detecting when a file has been reloaded
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	private static final HandlerList handlers = new HandlerList();
	private final String message;

	public FileReloadEvent(String example) {
		message = example;
	}

	public String getMessage() {
		return message;
	}

	public @NotNull
	HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
