package me.korbsti.mythicalraces.events.lvl;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;

import me.korbsti.mythicalraces.MythicalRaces;
import me.korbsti.mythicalraces.race.Race;

public class Harvest implements Listener {
	
	MythicalRaces plugin;
	
	public Harvest(MythicalRaces plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onHarvest(BlockBreakEvent e) {
		BlockData data = e.getBlock().getBlockData();
		if (data instanceof Ageable) {
			Ageable age = (Ageable) data;
			if (age.getAge() == age.getMaximumAge()) {
				Player p = e.getPlayer();
				
				Race ras = plugin.playersRace.get(p.getName());
				if (!"HARVESTING".contains(ras.lvlType))
					return;
				plugin.changeXP(p, ras.xpGain);
				plugin.checkLevelUp(ras, p);
			}
		}
	}
	
}
