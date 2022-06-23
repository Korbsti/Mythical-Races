package me.korbsti.mythicalraces.events.lvl;

import org.bukkit.Bukkit;
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
		if (ras.lvlType.contains("EXCAVATION")) {
			plugin.changeXP(p, ras.xpGain);
			plugin.checkLevelUp(ras, p);
			return;
		}
		
		String matType = e.getBlock().getType().toString();
		
		if (ras.lvlType.contains("MINER") && (matType.contains("STONE") || matType.contains("ORE") || matType.contains("DEEPSLATE"))) {
			plugin.changeXP(p, ras.xpGain);
			plugin.checkLevelUp(ras, p);
			return;
			
		}
		
		if (ras.lvlType.contains("WOODCUTTER") && (matType.contains("WOOD") || matType.contains("LOG"))) {
			plugin.changeXP(p, ras.xpGain);
			plugin.checkLevelUp(ras, p);
			return;
		}
		
		if (ras.lvlType.contains("DIGGER") && (matType.equals("GRASS_BLOCK") || matType.contains("DIRT") || matType.equals("GRAVEL"))) {
			plugin.changeXP(p, ras.xpGain);
			plugin.checkLevelUp(ras, p);
			return;
		}
		
		
	}
	
}
