package me.korbsti.mythicalraces.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import me.korbsti.mythicalraces.MythicalRaces;

public class InventoryClose implements Listener {
	
	MythicalRaces plugin;
	
	public InventoryClose(MythicalRaces plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (plugin.forceRace) {
			String name = ((Player) e.getPlayer()).getName();
			
			if (plugin.forceGUI.get(name) != null) {
				if (plugin.forceGUI.get(name)) {
					Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

						@Override
						public void run() {
							plugin.guiNumber.put(name, 1);
							plugin.gui.selectRaceGUI((Player) e.getPlayer());
							
						}
						
						
					}, 10);
				}
			}
		}
	}
}
