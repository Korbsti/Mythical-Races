package me.korbsti.mythicalraces.configmanager;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import me.korbsti.mythicalraces.MythicalRaces;

public class ConfigCreator {
	
	MythicalRaces plugin;
	String dir = System.getProperty("user.dir");
	String directoryPathFile = dir + File.separator + "plugins" + File.separator + "MythicalRaces";
	String directoryPathFileData = directoryPathFile + File.separator + "data";

	public ConfigCreator(MythicalRaces instance) {
		this.plugin = instance;
	}
	
	public void configCreator(String fileName) {
		if (new File(directoryPathFile).mkdirs())
			Bukkit.getLogger().info("Generated MythicalRaces configuration and data folder...");
		if (new File(directoryPathFileData).mkdirs())
			Bukkit.getLogger().info("Generated MythicalRaces configuration and data folder...");
		plugin.configFile = new File(plugin.getDataFolder(), fileName);
		if (!plugin.configFile.exists())
			plugin.saveDefaultConfig();
		plugin.configYaml = YamlConfiguration.loadConfiguration(plugin.configFile);
	}
	
	public void createFile(File f) {
		try {
			f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
