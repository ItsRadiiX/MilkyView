package com.itsradiix.milkyview.modules.milkyview;

import java.util.ArrayList;
import java.util.List;

public class Threshold {

	/**
	 * Threshold Class, this Class consists all data necessary to save Threshold into memory
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	int playerAmount;
	int viewDistance;
	ViewDistance viewDistanceClass;

	public Threshold(ViewDistance  viewDistanceClass, int playerAmount, int viewDistance){
		this.playerAmount = playerAmount;
		this.viewDistance = viewDistance;
		this.viewDistanceClass = viewDistanceClass;
	}

	public int getViewDistance() {
		return viewDistance;
	}

	public int getPlayerAmount() {
		return playerAmount;
	}

	public void setPlayerAmount(int playerAmount) {
		int tmp = this.playerAmount;
		this.playerAmount = playerAmount;
		MilkyViewModule.changeThresholdPlayerAmount(this, tmp);
	}

	public void setViewDistance(int viewDistance) {
		this.viewDistance = viewDistance;
		MilkyViewModule.changeThresholdViewDistance(this);
	}

	public static List<Integer> getPlayerAmounts(List<Threshold> thresholdList){
		List<Integer> integers = new ArrayList<>();
		for (Threshold threshold : thresholdList) {
			integers.add(threshold.getPlayerAmount());
		}
		return integers;
	}

	public static List<Integer> getViewDistances(List<Threshold> thresholdList){
		List<Integer> integers = new ArrayList<>();
		for (Threshold threshold : thresholdList){
			integers.add(threshold.getViewDistance());
		}
		return integers;
	}

	public ViewDistance getViewDistanceClass() {
		return viewDistanceClass;
	}
}
