package me.korbsti.mythicalraces.papi;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.korbsti.mythicalraces.MythicalRaces;
import me.korbsti.mythicalraces.race.Race;

public class PAPI extends PlaceholderExpansion {
    public MythicalRaces plugin;

    public PAPI(MythicalRaces mythicalRaces) {
		this.plugin = mythicalRaces;
	}

	@Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) {
            return "";
        }
        if (identifier.equals("race")) {
            return plugin.dataManager.getRace(p);
        }
        if (identifier.equals("cooldown_time")) {
            return plugin.dataManager.getCooldown(p);
        }
        if (identifier.equals("has_cooldown")) {
            return String.valueOf(plugin.dataManager.hasCooldown(p));
        }
        if (identifier.equals("get_xp")) {
            return String.valueOf(plugin.dataManager.getCooldown(p));
        }
        if (identifier.equals("get_level")) {
            return String.valueOf(plugin.dataManager.getPlayerLevel(p));   
        }
        if(identifier.equals("get_max_xp")) {
        	Race ras = plugin.playersRace.get(p.getName());
        	return String.valueOf(ras.xpPerLevel * plugin.dataManager.getPlayerLevel(p));
        }
        if(identifier.equals("get_xp/max_xp")) {
        	Race ras = plugin.playersRace.get(p.getName());
        	return String.valueOf(plugin.dataManager.getPlayerXP(p) + "/" + ras.xpPerLevel * plugin.dataManager.getPlayerLevel(p));
        }
        
        return null;
    }

    
    @Override
    public boolean canRegister(){
        return (plugin = (MythicalRaces) Bukkit.getPluginManager().getPlugin(getRequiredPlugin())) != null;
    }
    
	@Override
	public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
	}

	@Override
	public String getIdentifier() {
        return "MythicalRaces";
	}

	@Override
	public String getVersion() {
        return plugin.getDescription().getVersion();
	}
	
    @Override
    public String getRequiredPlugin(){
        return "MythicalRaces";
    }

}