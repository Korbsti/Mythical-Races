package me.korbsti.mythicalraces.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.korbsti.mythicalraces.MythicalRaces;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {
	
	MythicalRaces plugin;
	
	public Commands(MythicalRaces instance) {
		plugin = instance;
	}
	
	public boolean noPerm(CommandSender s, String perm) {
		if (s.hasPermission(perm))
			return false;
		s.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString("noPerm")));
		return true;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if ("races".equals(label)) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Player command only");
				return false;
			}
			if (args.length == 0) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
				        "invalidArgs")));
				return true;
			}
			if ("choose".equalsIgnoreCase(args[0]) && args.length == 2) {
				for (String str : plugin.races) {
					if (str.equalsIgnoreCase(args[1])) {
						if (noPerm(sender, "mythicalraces.race." + str)) {
							return true;
						}
						plugin.dataManager.setPlayerRace((Player) sender, str);
						plugin.setter.switchingRaces((Player) sender, str);
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
						        "chosenRace").replace("{race}", str)));
						return true;
					}
				}
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
				        "invalidRace")));
				return true;
			}
			
			if ("list".equalsIgnoreCase(args[0])) {
				if (noPerm(sender, "mythicalraces.list.races")) {
					return true;
				}
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString("listRaces")
				        .replace("{races}", plugin.races.toString())));
				return true;
			}
			if ("info".equalsIgnoreCase(args[0]) && args.length == 2) {
				if (noPerm(sender, "mythicalraces.info.races")) {
					return true;
				}
				for (String str : plugin.races) {
					if (str.equalsIgnoreCase(args[1])) {
						for (Object obj : plugin.configYaml.getList("races." + str + ".lore")) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', obj.toString()));
						}
						return true;
					}
				}
				
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
				        "invalidRace")));
				return true;
			}
			if ("gui".equalsIgnoreCase(args[0])) {
				if (noPerm(sender, "mythicalraces.gui")) {
					return true;
				}
				Player p = (Player) sender;
				plugin.guiNumber.put(p.getName(), 1);
				plugin.gui.selectRaceGUI(p);
				return true;
			}
			if ("help".equalsIgnoreCase(args[0])) {
				if (noPerm(sender, "mythicalraces.help")) {
					return true;
				}
				for (Object obj : plugin.configYaml.getList("help")) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', obj.toString()));
				}
				return true;
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString("invalidArgs")));
			return false;
		}
		
		return false;
	}
	
}
