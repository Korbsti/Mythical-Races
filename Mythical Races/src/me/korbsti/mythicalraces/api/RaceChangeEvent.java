package me.korbsti.mythicalraces.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.korbsti.mythicalraces.MythicalRaces;

public class RaceChangeEvent extends Event {
	private static final HandlerList HANDLERS = new HandlerList();

	MythicalRaces plugin;
	String newRace;
	Player p;
	
	
	public RaceChangeEvent(MythicalRaces plugin, String newRace, Player p) {
		this.plugin = plugin;
		this.newRace = newRace;
		this.p = p;
	}

	
	public String getRace() {
		return this.newRace;
	}
	
	
	
	public Player getPlayer() {
		return this.p;
	}
	
	public HandlerList getHandlers() {
	    return HANDLERS;
	}
	
	public static HandlerList getHandlerList() {
	    return HANDLERS;
	}

	
}
