package me.korbsti.mythicalraces.events.lvl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import me.korbsti.mythicalraces.MythicalRaces;
import me.korbsti.mythicalraces.race.Race;

public class BlockBreak implements Listener {
	
	MythicalRaces plugin;
	
	public BlockBreak(MythicalRaces plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		Race ras = plugin.playersRace.get(p.getName());
		if ("EXCAVATION".contains(ras.lvlType)) {
			plugin.changeXP(p, ras.xpGain);
			plugin.checkLevelUp(ras, p);
			return;
		}
		
		String matType = e.getBlock().getType().toString();
		
		if ("MINER".contains(ras.lvlType) && (matType.contains("STONE") ||matType.contains("ORE"))) {
			plugin.changeXP(p, ras.xpGain);
			plugin.checkLevelUp(ras, p);
			return;
			
		}
		if ("WOODCUTTER".contains(ras.lvlType) && (matType.contains("WOOD") || matType.contains("LOG"))) {
			plugin.changeXP(p, ras.xpGain);
			plugin.checkLevelUp(ras, p);
			return;
		}
		
	}
	
}
