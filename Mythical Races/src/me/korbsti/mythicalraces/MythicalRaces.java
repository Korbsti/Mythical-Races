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
import me.korbsti.mythicalraces.events.Join;
import me.korbsti.mythicalraces.other.GUI;
import me.korbsti.mythicalraces.other.Setters;
import me.korbsti.mythicalraces.other.TreeGUI;
import me.korbsti.mythicalraces.papi.PAPI;
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
	
	// races passive potion effects and attributes day time
	public HashMap<String, ArrayList<PotionEffectType>> dayRacePassivePotionEffects = new HashMap<String, ArrayList<PotionEffectType>>();
	public HashMap<String, ArrayList<Attribute>> dayRacePassiveAttributes = new HashMap<String, ArrayList<Attribute>>();
	public HashMap<String, ArrayList<Integer>> dayRacePassivePotionEffectsAmplifier = new HashMap<String, ArrayList<Integer>>();
	public HashMap<String, ArrayList<Double>> dayRacePassiveAttributesAmount = new HashMap<String, ArrayList<Double>>();
	public HashMap<String, ArrayList<Double>> dayRacePassiveAttributesLevel = new HashMap<String, ArrayList<Double>>();
	
	// races passive potion effects and attributes night time
	public HashMap<String, ArrayList<PotionEffectType>> nightRacePassivePotionEffects = new HashMap<String, ArrayList<PotionEffectType>>();
	public HashMap<String, ArrayList<Attribute>> nightRacePassiveAttributes = new HashMap<String, ArrayList<Attribute>>();
	public HashMap<String, ArrayList<Integer>> nightRacePassivePotionEffectsAmplifier = new HashMap<String, ArrayList<Integer>>();
	public HashMap<String, ArrayList<Double>> nightRacePassiveAttributesAmount = new HashMap<String, ArrayList<Double>>();
	public HashMap<String, ArrayList<Double>> nightRacePassiveAttributesLevel = new HashMap<String, ArrayList<Double>>();
	
	//command executables
	public HashMap<String, ArrayList<String>> raceCommandExecution = new HashMap<String, ArrayList<String>>();

	
	// Leveling
	public int maxLevel;
	public int xpGain;
	public int distance;
	public int time;
	public int xpPerLevel;
	public HashMap<String, Location> playerLocation = new HashMap<String, Location>();
	
	// If the player is allowed to switch races
	public HashMap<String, Boolean> canPlayerSwitch = new HashMap<String, Boolean>();
	public String cooldown;
	
	@Override
	public void onEnable() {
		configCreator.configCreator("config.yml", "data.yml");
		dataManager.dataByUUID = configYaml.getBoolean("other.dataByUUID");
		nightStart = configYaml.getInt("other.nightStart");
		nightEnd = configYaml.getInt("other.nightEnd");
		getCommand("races").setExecutor(new Commands(this));
		for (String str : getConfig().getKeys(true)) {
			String[] list = str.split("\\.");
			if (list.length == 2 && str.startsWith("races"))
				races.add(list[1]);
		}
		// subRaces.forEach((key, value) -> );
		
		for (String str : races) {
			dayRacePassivePotionEffects.put(str, new ArrayList<PotionEffectType>());
			dayRacePassiveAttributes.put(str, new ArrayList<Attribute>());
			dayRacePassivePotionEffectsAmplifier.put(str, new ArrayList<Integer>());
			dayRacePassiveAttributesAmount.put(str, new ArrayList<Double>());
			dayRacePassiveAttributesLevel.put(str, new ArrayList<Double>());
			nightRacePassivePotionEffects.put(str, new ArrayList<PotionEffectType>());
			nightRacePassiveAttributes.put(str, new ArrayList<Attribute>());
			nightRacePassivePotionEffectsAmplifier.put(str, new ArrayList<Integer>());
			nightRacePassiveAttributesAmount.put(str, new ArrayList<Double>());
			nightRacePassiveAttributesLevel.put(str, new ArrayList<Double>());
			raceCommandExecution.put(str, new ArrayList<String>());
			
			for (Object obj : configYaml.getList("races." + str + ".dayPassivePotionEffects")) {
				String potionEffect = obj.toString();
				if (!"null".equals(potionEffect)) {
					dayRacePassivePotionEffects.get(str).add(PotionEffectType.getByName(potionEffect));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".dayPassiveGenericAttributes")) {
				String attribute = obj.toString();
				if (!"null".equals(attribute)) {
					dayRacePassiveAttributes.get(str).add(Attribute.valueOf(obj.toString()));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".dayPassivePotionEffectsAmplifier")) {
				String amplifier = obj.toString();
				if (!"null".equals(amplifier)) {
					dayRacePassivePotionEffectsAmplifier.get(str).add(Integer.valueOf(amplifier));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".dayPassiveGenericAttributesAmplifier")) {
				String amount = obj.toString();
				if (!"null".equals(amount)) {
					dayRacePassiveAttributesAmount.get(str).add(Double.parseDouble(amount));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".dayRacePassiveAttributesLevel")) {
				String amount = obj.toString();
				if (!"null".equals(amount)) {
					dayRacePassiveAttributesLevel.get(str).add(Double.parseDouble(amount));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".nightPassivePotionEffects")) {
				String potionEffect = obj.toString();
				if (!"null".equals(potionEffect)) {
					nightRacePassivePotionEffects.get(str).add(PotionEffectType.getByName(potionEffect));
				}
			}
			for (Object obj : configYaml.getList("races." + str + ".nightPassiveGenericAttributes")) {
				String attribute = obj.toString();
				if (!"null".equals(attribute)) {
					nightRacePassiveAttributes.get(str).add(Attribute.valueOf(obj.toString()));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".nightPassivePotionEffectsAmplifier")) {
				String amplifier = obj.toString();
				if (!"null".equals(amplifier)) {
					nightRacePassivePotionEffectsAmplifier.get(str).add(Integer.valueOf(amplifier));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".nightPassiveGenericAttributesAmplifier")) {
				String amount = obj.toString();
				if (!"null".equals(amount))
					nightRacePassiveAttributesAmount.get(str).add(Double.parseDouble(amount));
			}
			
			for (Object obj : configYaml.getList("races." + str + ".nightRacePassiveAttributesLevel")) {
				String amount = obj.toString();
				if (!"null".equals(amount)) {
					nightRacePassiveAttributesLevel.get(str).add(Double.parseDouble(amount));
				}
			}
			for(Object obj : configYaml.getList("races." + str + ".executeCommandUponSwitching")) {
				raceCommandExecution.get(str).add(obj.toString());
			}
			
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
		maxLevel = configYaml.getInt("leveling.maxLevel");
		xpGain = configYaml.getInt("leveling.xpGain");
		distance = configYaml.getInt("leveling.distance");
		time = configYaml.getInt("leveling.time");
		xpPerLevel = configYaml.getInt("leveling.xpPerLevel");
		cooldown = configYaml.getString("other.cooldown");
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Join(this), this);
		pm.registerEvents(new InventoryClick(this), this);
		
		for (Player ¢ : Bukkit.getOnlinePlayers()) {
			dataManager.checkIfUnknown(¢);
			dataManager.checkIfTimeNull(¢);
			dataManager.checkIfLevelNull(¢);
			dataManager.checkIfXpNull(¢);
			setter.setEffects(¢);
			guiNumber.put(¢.getName(), 1);
			playerLocation.put(¢.getName(), ¢.getLocation());
		}
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			new PAPI(this).register();
		}
		
		Metrics metrics = new Metrics(this, 12954);
		
		int timerCheckingPotionEffects = configYaml.getInt("other.timerCheckingPotionEffects");
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
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
					if (playerLocation.get(p.getName()) != null) {
						if (p.getLocation().getWorld() == playerLocation.get(p.getName()).getWorld()) {
							if (p.getLocation().distance(playerLocation.get(p.getName())) > distance) {
								dataManager.setPlayerXP(p, dataManager.getPlayerXP(p) + xpGain);
								
							}
						}
					}
					playerLocation.put(p.getName(), p.getLocation());
					if (dataManager.getPlayerXP(p) >= xpPerLevel * dataManager.getPlayerLevel(p) && dataManager
					        .getPlayerLevel(p) != maxLevel) {
						dataManager.setPlayerLevel(p, dataManager.getPlayerLevel(p) + 1);
						dataManager.setPlayerXP(p, 0);
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', configYaml.getString("levelup")
						        .replace("{level}", "" + dataManager.getPlayerLevel(p))));
					}
					
				}
				
			}
			
		}, 0, time);
	}
	
	@Override
	public void onDisable() {
		
	}
}
