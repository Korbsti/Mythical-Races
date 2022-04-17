package me.korbsti.mythicalraces.events.lvl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.korbsti.mythicalraces.MythicalRaces;
import me.korbsti.mythicalraces.race.Race;

public class Hunting implements Listener {
	
	MythicalRaces plugin;
	
	public Hunting(MythicalRaces plugin) {
		this.plugin =plugin;
	}
	
	
	@EventHandler(ignoreCancelled=true)
	public void onDamage(EntityDamageByEntityEvent e) {
		Entity dmg = e.getEntity();
		if(!(dmg instanceof LivingEntity) || !(e.getDamager() instanceof Player)) return; 
		if (e.getFinalDamage() - ((LivingEntity) dmg).getHealth() < 0) return;
		Player p = (Player) e.getDamager();
		Race ras = plugin.playersRace.get(p.getName());
		if(!"HUNTING".contains(ras.lvlType)) return;
		plugin.changeXP(p, ras.xpGain);
		plugin.checkLevelUp(ras, p);
		
	}
	
}
