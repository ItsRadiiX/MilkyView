package com.itsradiix.milkyview.modules.milkyview;

import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class ViewDistance {

	/**
	 * ViewDistance Class, This is actually a pretty messy Class with bad naming, but it basically stores a list of all thresholds which are connected to a world
	 * Please don't claim this Class as your own, a lot of love and hard work has gone into all of my Managers.
	 *
	 * Author: ItsRadiiX (Bryan Suk)
	 */

	private final List<Threshold> thresholdList = new ArrayList<>();
	private final World w;

	public ViewDistance(World w){
		this.w = w;
	}

	public void addViewDistanceStartup(int playerAmount, int viewDistance) {
		thresholdList.add(new Threshold(this, playerAmount, viewDistance));
		sortList();
	}

	public Threshold addViewDistance(int playerAmount, int viewDistance){
		Threshold threshold = new Threshold(this, playerAmount, viewDistance);
		thresholdList.add(threshold);
		sortList();
		MilkyViewModule.getMilkyViewModule().checkThreshold();
		return threshold;
	}

	public List<Threshold> getThresholdList() {
		return thresholdList;
	}

	public void removeThreshold(Threshold threshold){
		thresholdList.remove(threshold);
		MilkyViewModule.removeThreshold(threshold);
		sortList();
	}

	public World getWorld() {
		return w;
	}

	public void sortList(){
		Threshold tmp;
		for (int i = 0; i < thresholdList.size(); i++) {
			for (int j = i+1; j < thresholdList.size(); j++) {
				if (thresholdList.get(i).getPlayerAmount() > thresholdList.get(j).getPlayerAmount()){
					tmp = thresholdList.get(i);
					thresholdList.set(i, thresholdList.get(j));
					thresholdList.set(j, tmp);
				}
			}

		}
	}
}
