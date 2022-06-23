package me.korbsti.mythicalraces.other;

import org.bukkit.entity.Player;

import me.korbsti.mythicalraces.MythicalRaces;

public class SetPlayersRace {
	
	MythicalRaces plugin;
	
	public SetPlayersRace(MythicalRaces plugin) {
		this.plugin = plugin;
	}
	
	public void changePlayersRace(Player p, String s) {
		plugin.dataManager.setPlayerRace(p, s);
		plugin.setter.switchingRaces(p, s);
		plugin.dataManager.setCooldown(p, plugin.cooldown);
		plugin.dataManager.checkIfLevelNull(p);
		plugin.dataManager.checkIfXpNull(p);
	}
	
}
