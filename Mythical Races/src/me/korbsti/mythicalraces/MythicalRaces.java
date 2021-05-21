package me.korbsti.mythicalraces;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.Attributes;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.korbsti.mythicalraces.commands.Commands;
import me.korbsti.mythicalraces.configmanager.ConfigCreator;
import me.korbsti.mythicalraces.configmanager.PlayerDataManager;
import me.korbsti.mythicalraces.events.InventoryClick;
import me.korbsti.mythicalraces.events.Join;
import me.korbsti.mythicalraces.other.GUI;
import me.korbsti.mythicalraces.other.Setters;

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
	
	// Create Class Stuff
	public ConfigCreator configCreator = new ConfigCreator(this);
	public PlayerDataManager dataManager = new PlayerDataManager(this);
	public Setters setter = new Setters(this);
	public GUI gui = new GUI(this);
	
	
	
	// GUI Number
	public HashMap<String, Integer> guiNumber = new HashMap<String, Integer>();
	
	
	// races passive potion effects and attributes day time
	public HashMap<String, ArrayList<PotionEffectType>> dayRacePassivePotionEffects = new HashMap<String, ArrayList<PotionEffectType>>();
	public HashMap<String, ArrayList<Attribute>> dayRacePassiveAttributes = new HashMap<String, ArrayList<Attribute>>();
	
	public HashMap<String, ArrayList<Integer>> dayRacePassivePotionEffectsAmplifier = new HashMap<String, ArrayList<Integer>>();
	public HashMap<String, ArrayList<Double>> dayRacePassiveAttributesAmount = new HashMap<String, ArrayList<Double>>();
	
	// races passive potion effects and attributes night time
	public HashMap<String, ArrayList<PotionEffectType>> nightRacePassivePotionEffects = new HashMap<String, ArrayList<PotionEffectType>>();
	public HashMap<String, ArrayList<Attribute>> nightRacePassiveAttributes = new HashMap<String, ArrayList<Attribute>>();
	
	public HashMap<String, ArrayList<Integer>> nightRacePassivePotionEffectsAmplifier = new HashMap<String, ArrayList<Integer>>();
	public HashMap<String, ArrayList<Double>> nightRacePassiveAttributesAmount = new HashMap<String, ArrayList<Double>>();
	
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
		for (String str : races) {
			dayRacePassivePotionEffects.put(str, new ArrayList<PotionEffectType>());
			dayRacePassiveAttributes.put(str, new ArrayList<Attribute>());
			dayRacePassivePotionEffectsAmplifier.put(str, new ArrayList<Integer>());
			dayRacePassiveAttributesAmount.put(str, new ArrayList<Double>());
			nightRacePassivePotionEffects.put(str, new ArrayList<PotionEffectType>());
			nightRacePassiveAttributes.put(str, new ArrayList<Attribute>());
			nightRacePassivePotionEffectsAmplifier.put(str, new ArrayList<Integer>());
			nightRacePassiveAttributesAmount.put(str, new ArrayList<Double>());
			
			for (Object obj : configYaml.getList("races." + str + ".dayPassivePotionEffects")) {
				String potionEffect = obj.toString();
				if (!potionEffect.equals("null")) {
					dayRacePassivePotionEffects.get(str).add(PotionEffectType.getByName(potionEffect));
				}
			}
			for (Object obj : configYaml.getList("races." + str + ".dayPassiveGenericAttributes")) {
				String attribute = obj.toString();
				if (!attribute.equals("null")) {
					dayRacePassiveAttributes.get(str).add(Attribute.valueOf(obj.toString()));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".dayPassivePotionEffectsAmplifier")) {
				String amplifier = obj.toString();
				if (!amplifier.equals("null")) {
					dayRacePassivePotionEffectsAmplifier.get(str).add(Integer.valueOf(amplifier));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".dayPassiveGenericAttributesAmplifier")) {
				String amount = obj.toString();
				if (!amount.equals("null")) {
					dayRacePassiveAttributesAmount.get(str).add(Double.parseDouble(amount));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".nightPassivePotionEffects")) {
				String potionEffect = obj.toString();
				if (!potionEffect.equals("null")) {
					nightRacePassivePotionEffects.get(str).add(PotionEffectType.getByName(potionEffect));
				}
			}
			for (Object obj : configYaml.getList("races." + str + ".nightPassiveGenericAttributes")) {
				String attribute = obj.toString();
				if (!attribute.equals("null")) {
					nightRacePassiveAttributes.get(str).add(Attribute.valueOf(obj.toString()));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".nightPassivePotionEffectsAmplifier")) {
				String amplifier = obj.toString();
				if (!amplifier.equals("null")) {
					nightRacePassivePotionEffectsAmplifier.get(str).add(Integer.valueOf(amplifier));
				}
			}
			
			for (Object obj : configYaml.getList("races." + str + ".nightPassiveGenericAttributesAmplifier")) {
				String amount = obj.toString();
				if (!amount.equals("null")) {
					nightRacePassiveAttributesAmount.get(str).add(Double.parseDouble(amount));
				}
			}
			
		}
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Join(this), this);
		pm.registerEvents(new InventoryClick(this), this);
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			dataManager.checkIfUnknown(p);
			setter.setEffects(p);
		}
		int timerCheckingPotionEffects = configYaml.getInt("other.timerCheckingPotionEffects");
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					setter.setEffects(p);
				}
				  
			}
			
		}, 0, timerCheckingPotionEffects);
	}
	
	@Override
	public void onDisable() {
		
	}
}
