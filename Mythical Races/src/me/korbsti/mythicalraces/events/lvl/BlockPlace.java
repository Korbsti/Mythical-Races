package me.korbsti.mythicalraces.events.lvl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.korbsti.mythicalraces.MythicalRaces;
import me.korbsti.mythicalraces.race.Race;

public class BlockPlace implements Listener {
	
	MythicalRaces plugin;
	
	public BlockPlace(MythicalRaces plugin) {
		this.plugin = plugin;
	}
	
	
	@EventHandler(ignoreCancelled=true)
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		Race ras = plugin.playersRace.get(p.getName());
		if(!"BUILDER".contains(ras.lvlType)) return;
		plugin.changeXP(p, ras.xpGain);
		plugin.checkLevelUp(ras, p);
	}
	
	
	
}
