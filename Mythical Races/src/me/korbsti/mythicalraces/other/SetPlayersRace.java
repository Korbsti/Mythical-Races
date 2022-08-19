package me.korbsti.mythicalraces.other;

import org.bukkit.entity.Player;

import me.korbsti.mythicalraces.MythicalRaces;

public class SetPlayersRace {
	
	MythicalRaces plugin;
	
	public SetPlayersRace(MythicalRaces plugin) {
		this.plugin = plugin;
	}
	
	public void changePlayersRace(Player p, String s, boolean fromJoinEvent) {
		plugin.dataManager.setPlayerRace(p, s, fromJoinEvent);
		plugin.setter.switchingRaces(p, s, fromJoinEvent);
		plugin.dataManager.setCooldown(p, plugin.cooldown);
		plugin.dataManager.checkIfLevelNull(p);
		plugin.dataManager.checkIfXpNull(p);
	}
	
}
