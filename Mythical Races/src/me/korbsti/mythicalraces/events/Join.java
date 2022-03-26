package me.korbsti.mythicalraces.events;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.korbsti.mythicalraces.MythicalRaces;
import me.korbsti.mythicalraces.race.Race;

public class Join implements Listener {
	MythicalRaces plugin;
	
	public Join(MythicalRaces instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		plugin.dataManager.checkIfUnknown(p);
		plugin.dataManager.checkIfTimeNull(p);
		plugin.dataManager.checkIfLevelNull(p);
		plugin.dataManager.checkIfXpNull(p);
		plugin.dataManager.checkIfChosenRace(p);
		plugin.setter.setEffects(p);
		plugin.guiNumber.put(p.getName(), 1);
		plugin.playerLocation.put(p.getName(), p.getLocation());
		plugin.forceGUI.put(p.getName(), plugin.dataManager.getChosenRace(p));
		
		if (plugin.forceRace) {
			if (plugin.forceGUI.get(p.getName())) {
				plugin.guiNumber.put(p.getName(), 1);
				plugin.gui.selectRaceGUI(e.getPlayer());
			}
		}
		
		plugin.playersRace.put(p.getName(), plugin.race.get(plugin.dataManager.getRace(p)));
		
		
	}
}
