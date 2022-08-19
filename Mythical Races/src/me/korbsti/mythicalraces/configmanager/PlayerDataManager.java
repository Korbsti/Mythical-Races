package me.korbsti.mythicalraces.configmanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.korbsti.mythicalraces.MythicalRaces;
import me.korbsti.mythicalraces.api.RaceChangeEvent;

public class PlayerDataManager {
	MythicalRaces plugin;
	
	public boolean dataByUUID;
	
	public PlayerDataManager(MythicalRaces instance) {
		plugin = instance;
	}
	
	public String getRace(Player p) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		return dataYaml.getString("race");
	}
	
	public void checkIfUnknown(Player p, boolean fromJoinEvent) {
		PlayerConfigData data = plugin.playerData.get(p.getName());
		YamlConfiguration dataYaml = data.dataYaml;
		
		if (dataYaml.getString("race") == null) {
			String defaultRace = plugin.configYaml.getString("other.defaultRace");
			
			dataYaml.set("race", defaultRace);
			
			final String innerDefault = defaultRace;
			
			Bukkit.getScheduler().runTask(plugin, new Runnable() {
				@Override
				public void run() {
					Bukkit.getPluginManager().callEvent(new RaceChangeEvent(plugin, innerDefault, p, fromJoinEvent));
				}
			});
			saveYml(p);
		}
	}
	
	public void checkIfChosenRace(Player p) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		if (dataYaml.getString("forceRace") == null) {
			dataYaml.set("forceRace", true);
			plugin.forceGUI.put(p.getName(), true);
			saveYml(p);
			
		}
		
	}
	
	public void checkIfPlayerName(Player p) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		if (dataYaml.getString("playerName") == null) {
			dataYaml.set("playerName", p.getName());
			saveYml(p);
			
		}
		
	}
	
	public void setChosenRace(Player p, boolean chosenRace) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		dataYaml.set("forceRace", chosenRace);
		plugin.forceGUI.put(p.getName(), chosenRace);
		saveYml(p);
	}
	
	public boolean getChosenRace(Player p) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		return dataYaml.getBoolean("forceRace");
	}
	
	public ArrayList<String> returnUserSubRace(Player p) {
		return (plugin.subRaces.get(getRace(p)));
	}
	
	public void setPlayerRace(Player p, String race, boolean fromJoinEvent) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		if (dataByUUID)
			dataYaml.set("race", race);
		
		String name = p.getName();
		plugin.playersRace.put(name, plugin.race.get(race));
		plugin.dataManager.setChosenRace(p, false);
		
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
			@Override
			public void run() {
				Bukkit.getPluginManager().callEvent(new RaceChangeEvent(plugin, race, p, fromJoinEvent));
			}
		});
		
		saveYml(p);
	}
	
	public void reduceTime() {
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				
				for (File file : new File(plugin.configCreator.directoryPathFileData).listFiles()) {
					YamlConfiguration dataYaml = YamlConfiguration.loadConfiguration(file);
					if (dataYaml.getString("time") != null) {
						if (!"0".equals(dataYaml.getString("time"))) {
							dataYaml.set("time", String.valueOf(Integer.valueOf(dataYaml.getString("time")) - 1));
							try {
								dataYaml.save(file);
							} catch (IOException e) {
							}
						}
					}
				}
			}
			
		});
	}
	
	public void checkIfLevelNull(Player p) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		String raceName = this.getRace(p);
		if (dataByUUID && dataYaml.getString(raceName + ".level") == null) {
			dataYaml.set(raceName + ".level", 1);
			saveYml(p);
			
		}
		
	}
	
	public void checkIfXpNull(Player p) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		String raceName = this.getRace(p);
		if (dataYaml.getString(raceName + ".xp") == null) {
			dataYaml.set(raceName + ".xp", 0);
			saveYml(p);
			
		}
	}
	
	public void checkIfTimeNull(Player p) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		if (dataYaml.getString("time") == null) {
			dataYaml.set("time", "0");
			saveYml(p);
			
		}
		
	}
	
	public void setPlayerXP(Player p, int x) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		String raceName = this.getRace(p);
		dataYaml.set(raceName + ".xp", x);
		saveYml(p);
		
	}
	
	public int getPlayerXP(Player p) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		String raceName = this.getRace(p);
		
		return dataYaml.getInt(raceName + ".xp");
	}
	
	public void setCooldown(Player p, String num) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		dataYaml.set("time", num);
		saveYml(p);
		
	}
	
	public void setPlayerLevel(Player p, int x) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		String raceName = this.getRace(p);
		dataYaml.set(raceName + ".level", x);
		saveYml(p);
		
	}
	
	public boolean hasCooldown(Player p) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		if (!dataYaml.getString("time").equals("0"))
			return true;
		return false;
	}
	
	public String getCooldown(Player p) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		return dataYaml.getString("time");
	}
	
	public int getPlayerLevel(Player p) {
		YamlConfiguration dataYaml = plugin.playerData.get(p.getName()).dataYaml;
		
		String raceName = this.getRace(p);
		return dataYaml.getInt(raceName + ".level");
	}
	
	public synchronized void saveYml(Player p) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				PlayerConfigData data = plugin.playerData.get(p.getName());
				YamlConfiguration dataYaml = data.dataYaml;
				File dataFile = data.dataFile;
				try {
					dataYaml.save(dataFile);
				} catch (IOException ignored) {
				}
			}
			
		});
		
	}
	
}
