package com.itsradiix.milkyview.models.menus;

import org.bukkit.entity.Player;

public class PlayerMenuUtility {


	private final Player owner;

	private Player target;

	public PlayerMenuUtility(Player p) {
		this.owner = p;
	}

	public Player getOwner() {
		return owner;
	}

	public Player getTarget() {
		return target;
	}

	public void setTarget(Player target) {
		this.target = target;
	}
}

