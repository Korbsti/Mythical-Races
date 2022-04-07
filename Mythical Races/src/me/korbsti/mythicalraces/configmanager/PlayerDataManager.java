package me.korbsti.mythicalraces.configmanager;

import java.io.IOException;
import java.util.ArrayList;

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
		if (dataByUUID && plugin.dataYaml.getString(p.getUniqueId().toString()) == null) {
			plugin.dataYaml.set(p.getUniqueId().toString() + ".race", plugin.configYaml.getString("other.defaultRace"));
		}
		if (!dataByUUID && plugin.dataYaml.getString(p.getName()) == null) {
		plugin.dataYaml.set(p.getName() + ".race", plugin.configYaml.getString("other.defaultRace"));
		}
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void checkIfChosenRace(Player p) {
		if (dataByUUID && plugin.dataYaml.getString(p.getUniqueId().toString() + ".forceRace") == null) {
			plugin.dataYaml.set(p.getUniqueId().toString() + ".forceRace", true);
			plugin.forceGUI.put(p.getName(), true);

		}
		if (!dataByUUID && plugin.dataYaml.getString(p.getName() + ".forceRace") == null) {
			plugin.dataYaml.set(p.getName() + ".forceRace", true);
			plugin.forceGUI.put(p.getName(), true);

		}
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setChosenRace(Player p, boolean chosenRace) {
		if (dataByUUID)
			plugin.dataYaml.set(p.getUniqueId().toString() + ".forceRace", chosenRace);
		if (!dataByUUID)
			plugin.dataYaml.set(p.getName() + ".forceRace", chosenRace);
		plugin.forceGUI.put(p.getName(), chosenRace);
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean getChosenRace(Player p) {
		if (dataByUUID)
			return plugin.dataYaml.getBoolean(p.getUniqueId().toString() + ".forceRace");
		if (!dataByUUID)
			return plugin.dataYaml.getBoolean(p.getName() + ".forceRace");
		return false;
	}
	
	public ArrayList<String> returnUserSubRace(Player p) {
		return (plugin.subRaces.get(getRace(p)));
	}
	
	public void setPlayerRace(Player p, String race) {
		if (dataByUUID)
			plugin.dataYaml.set(p.getUniqueId().toString() + ".race", race);
		if (!dataByUUID)
			plugin.dataYaml.set(p.getName() + ".race", race);
		plugin.playersRace.put(p.getName(), plugin.race.get(race));
		plugin.dataManager.setChosenRace(p, false);
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
		}
	}
	
	public void checkIfLevelNull(Player p) {
		if (dataByUUID && plugin.dataYaml.getString(p.getUniqueId().toString() + ".level") == null)
			plugin.dataYaml.set(p.getUniqueId().toString() + ".level", 1);
		if (!dataByUUID && plugin.dataYaml.getString(p.getName() + ".level") == null)
			plugin.dataYaml.set(p.getName() + ".level", 1);
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void checkIfXpNull(Player p) {
		if (dataByUUID && plugin.dataYaml.getString(p.getUniqueId().toString() + ".xp") == null)
			plugin.dataYaml.set(p.getUniqueId().toString() + ".xp", 0);
		if (!dataByUUID && plugin.dataYaml.getString(p.getName() + ".xp") == null)
			plugin.dataYaml.set(p.getName() + ".xp", 0);
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void checkIfTimeNull(Player p) {
		if (dataByUUID) {
			if (plugin.dataYaml.getString(p.getUniqueId().toString() + ".time") == null)
				plugin.dataYaml.set(p.getUniqueId().toString() + ".time", "0");
			
		}
		
		if (!dataByUUID)
			if (plugin.dataYaml.getString(p.getName() + ".time") == null)
				plugin.dataYaml.set(p.getName() + ".time", "0");
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setPlayerXP(Player p, int x) {
		
		if (dataByUUID)
			plugin.dataYaml.set(p.getUniqueId().toString() + ".xp", x);
		if (!dataByUUID)
			plugin.dataYaml.set(p.getName() + ".xp", x);
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (IOException e) {
		}
		
	}
	
	public int getPlayerXP(Player p) {
		if (dataByUUID)
			return plugin.dataYaml.getInt(p.getUniqueId().toString() + ".xp");
		if (!dataByUUID)
			return plugin.dataYaml.getInt(p.getName() + ".xp");
		return 0;
	}
	
	public void setCooldown(Player p, String num) {
		
		if (dataByUUID)
			plugin.dataYaml.set(p.getUniqueId().toString() + ".time", num);
		if (!dataByUUID)
			plugin.dataYaml.set(p.getName() + ".time", num);
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setPlayerLevel(Player p, int x) {
		if (dataByUUID)
			plugin.dataYaml.set(p.getUniqueId().toString() + ".level", x);
		if (!dataByUUID)
			plugin.dataYaml.set(p.getName() + ".level", x);
		try {
			plugin.dataYaml.save(plugin.dataFile);
		} catch (IOException e) {
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
	
	public int getPlayerLevel(Player p) {
		if (dataByUUID)
			return plugin.dataYaml.getInt(p.getUniqueId().toString() + ".level");
		if (!dataByUUID)
			return plugin.dataYaml.getInt(p.getName() + ".level");
		return 0;
	}
}
