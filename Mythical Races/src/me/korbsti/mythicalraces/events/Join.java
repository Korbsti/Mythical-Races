package me.korbsti.mythicalraces.events;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.korbsti.mythicalraces.MythicalRaces;

public class Join implements Listener {
	MythicalRaces plugin;
	
	public Join(MythicalRaces instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		plugin.dataManager.checkIfUnknown(p);
		plugin.setter.setEffects(p);
		plugin.guiNumber.put(p.getName(), 1);
		plugin.dataManager.checkIfTimeNull(p);
	}
}

