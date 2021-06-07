package me.korbsti.mythicalraces.configmanager;

import java.io.IOException;

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
	
	public void reduceTime() {
		for (String key : plugin.dataYaml.getKeys(false)) {
			if (plugin.dataYaml.getString(key + ".time") != null) {
				if (!"0".equals(plugin.dataYaml.getString(key + ".time")))
					plugin.dataYaml.set(key + ".time", String.valueOf(Integer.valueOf(plugin.dataYaml
					        .getString(key + ".time")) - 1));
			}
		}
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void checkIfTimeNull(Player p) {
		if (dataByUUID) {
			if (plugin.dataYaml.getString(p.getUniqueId().toString() + ".time") == null)
				plugin.dataYaml.set(p.getUniqueId().toString() + ".time", "0");
			
		}
		
		if (!dataByUUID) if (plugin.dataYaml.getString(p.getName() + ".time") == null) plugin.dataYaml.set(p.getName() + ".time", "0");
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setCooldown(Player p, String num) {
		
		if (dataByUUID) plugin.dataYaml.set(p.getUniqueId().toString() + ".time", num);
		if (!dataByUUID)
			plugin.dataYaml.set(p.getName() + ".time", num);
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean hasCooldown(Player p) {
		if (dataByUUID)
			if (!plugin.dataYaml.getString(p.getUniqueId().toString() + ".time").equals("0"))
				return true;
		if (!dataByUUID)
			if (!plugin.dataYaml.getString(p.getName() + ".time").equals("0"))
				return true;
		return false;
	}
	
	public String getCooldown(Player p) {
		if (dataByUUID)
			return plugin.dataYaml.getString(p.getUniqueId().toString() + ".time");
		if (!dataByUUID)
			return plugin.dataYaml.getString(p.getName() + ".time");
		return "null";
	}
}
