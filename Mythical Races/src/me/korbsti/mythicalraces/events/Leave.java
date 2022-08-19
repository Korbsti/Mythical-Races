package me.korbsti.mythicalraces.events;

import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.korbsti.mythicalraces.MythicalRaces;
import me.korbsti.mythicalraces.configmanager.PlayerConfigData;

public class Leave implements Listener {
	
	MythicalRaces plugin;
	
	public Leave(MythicalRaces plugin) {
		this.plugin =plugin;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
	
		plugin.forceGUI.remove(e.getPlayer().getName());
		plugin.playerData.put(e.getPlayer().getName(), new PlayerConfigData(plugin, e.getPlayer()));

		e.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
		e.getPlayer().getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);

	}
	
	
}
