package me.korbsti.mythicalraces.events.lvl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
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
		if(!(dmg instanceof LivingEntity)) return; 
		Entity dmger = e.getDamager();
		if(!(dmger instanceof Player || dmger instanceof Projectile)) return; 
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

			@Override
			public void run() {
				if(dmger instanceof Projectile) {
					Projectile proj = (Projectile) dmger;
					if(proj.getShooter() instanceof Player) {
						if(dmg.isDead()) {
							Player p = (Player) proj.getShooter();
							if(plugin.playersRace.get(p.getName()) == null)  return;

							Race ras = plugin.playersRace.get(p.getName());
							if(!ras.lvlType.contains("ARCHER")) return;
							plugin.changeXP(p, ras.xpGain);
							plugin.checkLevelUp(ras, p);
						}
					}
					
					return;
				} 
				
				
				if(dmg.isDead()) {
					Player p = (Player) e.getDamager();
					if(plugin.playersRace.get(p.getName()) == null)  return;

					Race ras = plugin.playersRace.get(p.getName());
					if(!ras.lvlType.contains("HUNTING")) return;
					plugin.changeXP(p, ras.xpGain);
					plugin.checkLevelUp(ras, p);
				}
				
				
			}
			
			
		}, 1);
		
		

		
	}
	
}
