package me.korbsti.mythicalraces.other;

import org.bukkit.entity.Player;

import me.korbsti.mythicalraces.MythicalRaces;

public class SetPlayersRace {
	
	MythicalRaces plugin;
	
	public SetPlayersRace(MythicalRaces plugin) {
		this.plugin = plugin;
	}
	
	public void changePlayersRace(Player p, String s) {
		if (plugin.resetBaseLevel) {
			plugin.dataManager.setPlayerLevel(p, 1);
		}
		plugin.dataManager.setPlayerRace(p, s);
		plugin.setter.switchingRaces(p, s);
		plugin.dataManager.setCooldown(p, plugin.cooldown);
	}
	
}
