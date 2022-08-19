package me.korbsti.mythicalraces.events.lvl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import me.korbsti.mythicalraces.MythicalRaces;
import me.korbsti.mythicalraces.race.Race;

public class IncomingDamage implements Listener {
	
	MythicalRaces plugin;
	
	
	public IncomingDamage(MythicalRaces plugin) {
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void onIncomingDamage(EntityDamageEvent e) {
		
		if (!(e.getEntity() instanceof Player))
			return;
		Player p = (Player) e.getEntity();
		if(plugin.playersRace.get(p.getName()) == null)  return;
		
		
		Race ras = plugin.playersRace.get(p.getName());
		if (!"TANKER".contains(ras.lvlType))
			return;
		plugin.changeXP(p, ras.xpGain);
		plugin.checkLevelUp(ras, p);
		
	}
	
	
	
	
}
