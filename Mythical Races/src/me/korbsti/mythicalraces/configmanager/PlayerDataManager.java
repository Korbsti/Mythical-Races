package me.korbsti.mythicalraces.configmanager;

import org.bukkit.entity.Player;

import me.korbsti.mythicalraces.MythicalRaces;

public class PlayerDataManager {
	MythicalRaces plugin;
	
	public boolean dataByUUID;
	
	public PlayerDataManager(MythicalRaces instance) {
		plugin = instance;
	}
	
	public String getRace(Player p) {
		if (dataByUUID)
			return plugin.dataYaml.getString(p.getUniqueId().toString() + ".race");
		if (!dataByUUID)
			return plugin.dataYaml.getString(p.getName() + ".race");
		return "null";
	}
	
	public void checkIfUnknown(Player p) {
		if (dataByUUID && plugin.dataYaml.getString(p.getUniqueId().toString()) == null)
			plugin.dataYaml.set(p.getUniqueId().toString() + ".race", plugin.configYaml.getString("other.defaultRace"));
		if (!dataByUUID && plugin.dataYaml.getString(p.getName()) == null)
			plugin.dataYaml.set(p.getName() + ".race", plugin.configYaml.getString("other.defaultRace"));
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPlayerRace(Player p, String race) {
		if (dataByUUID)
			plugin.dataYaml.set(p.getUniqueId().toString() + ".race", race);
		if (!dataByUUID)
			plugin.dataYaml.set(p.getName() + ".race", race);
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
