package me.korbsti.mythicalraces.events.lvl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;

import me.korbsti.mythicalraces.MythicalRaces;
import me.korbsti.mythicalraces.race.Race;

public class Harvest implements Listener {
	
	MythicalRaces plugin;
	
	public Harvest(MythicalRaces plugin) {
		this.plugin = plugin;
	}
	
	
	@EventHandler(ignoreCancelled=true)
	public void onHarvest(PlayerHarvestBlockEvent e) {
		Player p = e.getPlayer();
		Race ras = plugin.playersRace.get(p.getName());
		if(!"HARVESTING".equals(ras.lvlType)) return;
		plugin.changeXP(p, ras.xpGain);
		plugin.checkLevelUp(ras, p);
	}
	
	
	
}
