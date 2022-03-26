package me.korbsti.mythicalraces;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.korbsti.mythicalraces.bstats.Metrics;
import me.korbsti.mythicalraces.commands.Commands;
import me.korbsti.mythicalraces.configmanager.ConfigCreator;
import me.korbsti.mythicalraces.configmanager.PlayerDataManager;
import me.korbsti.mythicalraces.events.InventoryClick;
import me.korbsti.mythicalraces.events.InventoryClose;
import me.korbsti.mythicalraces.events.Join;
import me.korbsti.mythicalraces.events.Leave;
import me.korbsti.mythicalraces.events.lvl.BlockBreak;
import me.korbsti.mythicalraces.events.lvl.BlockPlace;
import me.korbsti.mythicalraces.events.lvl.Fishing;
import me.korbsti.mythicalraces.events.lvl.Harvest;
import me.korbsti.mythicalraces.events.lvl.Hunting;
import me.korbsti.mythicalraces.other.GUI;
import me.korbsti.mythicalraces.other.Setters;
import me.korbsti.mythicalraces.other.TreeGUI;
import me.korbsti.mythicalraces.papi.PAPI;
import me.korbsti.mythicalraces.race.Race;
import net.md_5.bungee.api.ChatColor;

public class MythicalRaces extends JavaPlugin {
	
	// Configuration File Stuff
	public YamlConfiguration configYaml;
	public File configFile;
	
	public YamlConfiguration dataYaml;
	public File dataFile;
	
	// day or night
	public int nightStart;
	public int nightEnd;
	
	// force race
	public boolean forceRace;
	
	// Races
	public ArrayList<String> races = new ArrayList<>();
	public ArrayList<String> subRaceNames = new ArrayList<>();
	public HashMap<String, ArrayList<String>> subRaces = new HashMap<String, ArrayList<String>>();
	
	// Create Class Stuff
	public ConfigCreator configCreator = new ConfigCreator(this);
	public PlayerDataManager dataManager = new PlayerDataManager(this);
	public Setters setter = new Setters(this);
	public GUI gui = new GUI(this);
	public TreeGUI treeGUI = new TreeGUI(this);
	
	// Enable level system
	public boolean enableLevelSystem;
	
	// GUI Number
	public HashMap<String, Integer> guiNumber = new HashMap<String, Integer>();
	
	// the actual RACE
	public HashMap<String, Race> race = new HashMap<String, Race>();
	public HashMap<String, Boolean> forceGUI = new HashMap<String, Boolean>();
	
	// Leveling
	public int distance;
	public int time;
	public HashMap<String, Location> playerLocation = new HashMap<String, Location>();
	
	// If the player is allowed to switch races
	public HashMap<String, Boolean> canPlayerSwitch = new HashMap<String, Boolean>();
	
	// players race thing
	public HashMap<String, Race> playersRace = new HashMap<>();
	
	public String cooldown;
	
	@Override
	public void onEnable() {
		onStartup();
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new PAPI(this).register();
		}
		Metrics metrics = new Metrics(this, 12954);
		
		int timerCheckingPotionEffects = configYaml.getInt("other.timerCheckingPotionEffects");
		
		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					setter.setEffects(p);
				}
				
			}
			
		}, 0, timerCheckingPotionEffects);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				dataManager.reduceTime();
				
			}
		}, 0, 20 * 60);
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
			
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					Race userRace = playersRace.get(p.getName());
					if(userRace.lvlType.equals("RUNNING")) {
					if (playerLocation.get(p.getName()) != null) {
						if (p.getLocation().getWorld() == playerLocation.get(p.getName()).getWorld()) {
							if (p.getLocation().distance(playerLocation.get(p.getName())) > distance) {
								changeXP(p, userRace.xpGain);
								
							}
						}
					}
					playerLocation.put(p.getName(), p.getLocation());
					
					checkLevelUp(userRace, p);
					}
					
				}
				
			}
			
		}, 0, time);
	}
	
	public void changeLevel(Player p) {
		
		Bukkit.getScheduler().runTask(this, new Runnable() {

			@Override
			public void run() {
				dataManager.setPlayerLevel(p, dataManager.getPlayerLevel(p) + 1);
				dataManager.setPlayerXP(p, 0);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', configYaml.getString("levelup")
				        .replace("{level}", "" + dataManager.getPlayerLevel(p))));
				
			}
			
		});
	}
	
	public void changeXP(Player p, int xpGain ) {
		Bukkit.getScheduler().runTask(this, new Runnable() {

			@Override
			public void run() {
				dataManager.setPlayerXP(p, dataManager.getPlayerXP(p) + xpGain);
			}
			
		});
	}
	
	public void checkLevelUp(Race userRace, Player p) {
		Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {

			@Override
			public void run() {
				if (dataManager.getPlayerXP(p) >= userRace.xpPerLevel * dataManager.getPlayerLevel(p) && dataManager.getPlayerLevel(p) != userRace.maxLevel) {
					changeLevel(p);
				}
				
			}
			
			
		});
	}
	
	
	
	
	
	
	@Override
	public void onDisable() {
		
	}
	
	public void onStartup() {
		configCreator.configCreator("config.yml", "data.yml");
		dataManager.dataByUUID = configYaml.getBoolean("other.dataByUUID");
		nightStart = configYaml.getInt("other.nightStart");
		nightEnd = configYaml.getInt("other.nightEnd");
		forceRace = configYaml.getBoolean("other.forceRace");
		
		getCommand("races").setExecutor(new Commands(this));
		for (String str : getConfig().getKeys(true)) {
			String[] list = str.split("\\.");
			if (list.length == 2 && str.startsWith("races")) {
				races.add(list[1]);
			}
		}
		
		// subRaces.forEach((key, value) -> );
		
		for (String str : races) {
			String raceName = str;
			ArrayList<PotionEffectType> dayRacePassivePotionEffects = new ArrayList<PotionEffectType>();
	        ArrayList<Attribute> dayRacePassiveAttributes = new ArrayList<Attribute>();
	        ArrayList<Integer> dayRacePassivePotionEffectsBase = new  ArrayList<Integer>();
	        ArrayList<Double> dayRacePassiveAttributesAmount = new  ArrayList<Double>();
	        ArrayList<Double> dayRacePassiveAttributesLevel = new  ArrayList<Double>();
	        ArrayList<String> dayRaceDataPotion = new ArrayList<String>();
	        ArrayList<String> dayRaceDataAttribute = new ArrayList<String>();

			ArrayList<PotionEffectType> nightRacePassivePotionEffects = new ArrayList<PotionEffectType>();
			ArrayList<Attribute> nightRacePassiveAttributes = new ArrayList<Attribute>();
	        ArrayList<Integer> nightRacePassivePotionEffectsBase = new ArrayList<Integer>();
	        ArrayList<Double> nightRacePassiveAttributesAmount = new  ArrayList<Double>();
	        ArrayList<Double> nightRacePassiveAttributesLevel = new ArrayList<Double>();
	        ArrayList<String> nightRaceDataPotion = new ArrayList<String>();
	        ArrayList<String> nightRaceDataAttribute = new ArrayList<String>();

	        ArrayList<String> raceCommandExecution = new  ArrayList<String>();
			
			
			for (Object obj : configYaml.getList("races." + str + ".dayPassivePotionEffects")) {
				String potionEffect = obj.toString();
				if (!"null".equals(potionEffect)) {
					dayRacePassivePotionEffects.add(PotionEffectType.getByName(potionEffect));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".dayPassiveGenericAttributes")) {
				String attribute = obj.toString();
				if (!"null".equals(attribute)) {
					dayRacePassiveAttributes.add(Attribute.valueOf(obj.toString()));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".dayPassivePotionEffectsBase")) {
				String Base = obj.toString();
				if (!"null".equals(Base)) {
					dayRacePassivePotionEffectsBase.add(Integer.valueOf(Base));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".dayPassiveGenericAttributesBase")) {
				String amount = obj.toString();
				if (!"null".equals(amount)) {
					dayRacePassiveAttributesAmount.add(Double.parseDouble(amount));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".dayRacePassiveAttributesLevel")) {
				String amount = obj.toString();
				if (!"null".equals(amount)) {
					dayRacePassiveAttributesLevel.add(Double.parseDouble(amount));
				}
			}
			
			
			
			for (Object obj : configYaml.getList("races." + str + ".dayRaceDataPotion")) {
				String data = obj.toString();
				if (!"null".equals(data)) {
					dayRaceDataPotion.add(data);
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".dayRaceDataAttribute")) {
				String data = obj.toString();
				if (!"null".equals(data)) {
					dayRaceDataAttribute.add(data);
				}
			}
			for (Object obj : configYaml.getList("races." + str + ".nightPassivePotionEffects")) {
				String potionEffect = obj.toString();
				if (!"null".equals(potionEffect)) {
					nightRacePassivePotionEffects.add(PotionEffectType.getByName(potionEffect));
				}
			}
			for (Object obj : configYaml.getList("races." + str + ".nightPassiveGenericAttributes")) {
				String attribute = obj.toString();
				if (!"null".equals(attribute)) {
					nightRacePassiveAttributes.add(Attribute.valueOf(obj.toString()));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".nightPassivePotionEffectsBase")) {
				String base = obj.toString();
				if (!"null".equals(base)) {
					nightRacePassivePotionEffectsBase.add(Integer.valueOf(base));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".nightPassiveGenericAttributesBase")) {
				String amount = obj.toString();
				if (!"null".equals(amount))
					nightRacePassiveAttributesAmount.add(Double.parseDouble(amount));
			}
			
			for (Object obj : configYaml.getList("races." + str + ".nightRacePassiveAttributesLevel")) {
				String amount = obj.toString();
				if (!"null".equals(amount)) {
					nightRacePassiveAttributesLevel.add(Double.parseDouble(amount));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".nightRaceDataPotion")) {
				String data = obj.toString();
				if (!"null".equals(data)) {
					nightRaceDataPotion.add(data);
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".nightRaceDataAttribute")) {
				String data = obj.toString();
				if (!"null".equals(data)) {
					nightRaceDataAttribute.add(data);
				}
			}
			
			for(Object obj : configYaml.getList("races." + str + ".executeCommandUponSwitching")) {
				raceCommandExecution.add(obj.toString());
			}
			String lvlType = configYaml.getString("races." + str + ".lvlType");
			int maxLevel = configYaml.getInt("races." + str + ".maxLevel");
			int gainXP = configYaml.getInt("races." + str + ".gainXP");
			int xpPerLevel = configYaml.getInt("races." + str + ".xpPerLevel");

			
			
			race.put(raceName, new Race(raceName, dayRacePassivePotionEffects,
			        dayRacePassiveAttributes,
			        dayRacePassivePotionEffectsBase,
			         dayRacePassiveAttributesAmount,
			        dayRacePassiveAttributesLevel,
			        dayRaceDataPotion,
			        dayRaceDataAttribute,
			       nightRacePassivePotionEffects,
			         nightRacePassiveAttributes,
			        nightRacePassivePotionEffectsBase,
			         nightRacePassiveAttributesAmount,
			        nightRacePassiveAttributesLevel,
			        nightRaceDataPotion,
			        nightRaceDataAttribute,
			        raceCommandExecution,
			        lvlType,
			        maxLevel,
			        gainXP,
			        xpPerLevel));
			
			
		}
		for (int i = 0; i != races.size(); i++) {
			if (configYaml.getBoolean("races." + races.get(i) + ".isSubRace")) {
				subRaceNames.add(races.get(i));
				subRaces.put(configYaml.getString("races." + races.get(i) + ".subRaceType"), new ArrayList<String>());
				races.remove(i);
				i = 0;
			}
		}
		for (String str : subRaceNames) {
			subRaces.get(configYaml.getString("races." + str + ".subRaceType")).add(str);
		}
		distance = configYaml.getInt("leveling.distance");
		time = configYaml.getInt("leveling.time");
		cooldown = configYaml.getString("other.cooldown");
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Join(this), this);
		pm.registerEvents(new InventoryClick(this), this);
		pm.registerEvents(new InventoryClose(this), this);
		pm.registerEvents(new Leave(this), this);
		
		pm.registerEvents(new Fishing(this), this);
		pm.registerEvents(new Hunting(this), this);
		pm.registerEvents(new BlockPlace(this), this);
		pm.registerEvents(new Harvest(this), this);
		pm.registerEvents(new BlockBreak(this), this);

		
		for (Player p : Bukkit.getOnlinePlayers()) {
			dataManager.checkIfUnknown(p);
			dataManager.checkIfTimeNull(p);
			dataManager.checkIfLevelNull(p);
			dataManager.checkIfXpNull(p);
			dataManager.checkIfChosenRace(p);
			setter.setEffects(p);
			guiNumber.put(p.getName(), 1);
			playerLocation.put(p.getName(), p.getLocation());
			forceGUI.put(p.getName(), dataManager.getChosenRace(p));
			
			playersRace.put(p.getName(), race.get(dataManager.getRace(p)));
			
			
			if (forceRace) {
				if (forceGUI.get(p.getName())) {
					guiNumber.put(p.getName(), 1);
					gui.selectRaceGUI(p);
				}
			}
		}
	}
	
	
}
