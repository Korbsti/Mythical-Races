package me.korbsti.mythicalraces.configmanager;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.korbsti.mythicalraces.MythicalRaces;

public class PlayerConfigData {
	
	MythicalRaces plugin;
	
	public YamlConfiguration dataYaml;
	public File dataFile;
	public Player p;
	
	public PlayerConfigData(MythicalRaces plugin, Player p) {
		this.plugin = plugin;
		this.p = p;
		try {
		if (plugin.dataManager.dataByUUID) {
			
			dataFile = new File(plugin.configCreator.directoryPathFileData + File.separator + p.getUniqueId()+ ".yml");
			if (!dataFile.exists())
				dataFile.createNewFile();
			
			dataYaml = YamlConfiguration.loadConfiguration(dataFile);
			
		} else {
			dataFile = new File(plugin.configCreator.directoryPathFileData + File.separator + p.getName() + ".yml");
			if (!dataFile.exists())
				dataFile.createNewFile();
			
			dataYaml = YamlConfiguration.loadConfiguration(dataFile);
			
		} } catch (Exception ignored) {};
	
	}

}
