package me.korbsti.mythicalraces.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.korbsti.mythicalraces.MythicalRaces;

public class Leave implements Listener {
	
	MythicalRaces plugin;
	
	public Leave(MythicalRaces plugin) {
		this.plugin =plugin;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
	
		plugin.forceGUI.remove(e.getPlayer().getName());
		
		
	}
	
	
}
