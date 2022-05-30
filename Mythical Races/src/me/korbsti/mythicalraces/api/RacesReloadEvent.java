package me.korbsti.mythicalraces.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.korbsti.mythicalraces.MythicalRaces;

public class RacesReloadEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();

	MythicalRaces plugin;
	private int i = 0;
	
	
	public RacesReloadEvent(MythicalRaces plugin) {
		this.plugin = plugin;
	}
	
	public HandlerList getHandlers() {
	    return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
	    return HANDLERS;
	}
	

	public int getInt() {
		return i;
	}
	

	
}
