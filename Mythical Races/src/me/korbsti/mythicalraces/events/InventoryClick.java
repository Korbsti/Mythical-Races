package me.korbsti.mythicalraces.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.korbsti.mythicalraces.MythicalRaces;
import net.md_5.bungee.api.ChatColor;

public class InventoryClick implements Listener {
	MythicalRaces plugin;
	
	public InventoryClick(MythicalRaces instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		int slot = e.getSlot();
		if (plugin.configYaml.getString("other.guiName").equalsIgnoreCase(p.getOpenInventory().getTitle())) {
			e.setCancelled(true);
			String name = p.getName();
			
			
			for (String str : plugin.races) {
				if (plugin.configYaml.getInt("races." + str + ".slot") == slot && plugin.configYaml.getInt("races."
				        + str + ".guiPage") == plugin.guiNumber.get(name)) {
					if (!p.hasPermission("mythicalraces.race." + str)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
						        "noPerm")));
						p.closeInventory();
						return;
					}
					if (plugin.dataManager.hasCooldown(p) && !p.hasPermission("mythicalraces.cooldown.bypass")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString("cooldownMessage").replace("{time}", plugin.dataManager.getCooldown(p))));
						return;
					}
					plugin.setPlayersRace.changePlayersRace(p, str, false);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString("chosenRace") .replace("{race}", str)));
					p.closeInventory();
				}
			}
			if (slot == plugin.configYaml.getInt("other.forwardClick")) {
				p.closeInventory();
				plugin.guiNumber.put(name, plugin.guiNumber.get(name) + 1);
				plugin.gui.selectRaceGUI(p);
			}
			
			
			if (slot == plugin.configYaml.getInt("other.backwardClick")) {
				if(plugin.guiNumber.get(name) - 1 <= 0) {
					return;
				}
				p.closeInventory();
				plugin.guiNumber.put(name, plugin.guiNumber.get(name) - 1);
				plugin.gui.selectRaceGUI(p);
			}
			return;
		}
		if (plugin.configYaml.getString("other.treeName").equalsIgnoreCase(p.getOpenInventory().getTitle())) {
			e.setCancelled(true);
			boolean isSubRace = false;
			if (plugin.configYaml.getBoolean("races." + plugin.dataManager.getRace(p) + ".isSubRace")) {
				isSubRace = true;
			}
			if (!isSubRace) {
				if (plugin.dataManager.returnUserSubRace(p) == null) {
					
					String str = plugin.dataManager.getRace(p);
					if (slot == plugin.configYaml.getInt("races." + str + ".treeSlot")) {
						if (!p.hasPermission("mythicalraces.race." + str)) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
							        "noPerm")));
							p.closeInventory();
							return;
						}
						if (plugin.dataManager.getPlayerLevel(p) < plugin.configYaml.getInt("races." + str
						        + ".levelRequire")) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
							        "notRequiredLevel")));
							p.closeInventory();
							
							return;
						} else {
							plugin.dataManager.setPlayerRace((Player) p, str, false);
							plugin.setter.switchingRaces((Player) p, str, false);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
							        "chosenRace").replace("{race}", str)));
							p.closeInventory();
							return;
							
						}
					}
				} else {
				for (String str : plugin.dataManager.returnUserSubRace(p)) {
					if (slot == plugin.configYaml.getInt("races." + str + ".treeSlot")) {
						if (!p.hasPermission("mythicalraces.race." + str)) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
							        "noPerm")));
							p.closeInventory();
							return;
						}
						if (plugin.dataManager.getPlayerLevel(p) < plugin.configYaml.getInt("races." + str
						        + ".levelRequire")) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
							        "notRequiredLevel")));
							p.closeInventory();
							
							return;
						} else {
							plugin.dataManager.setPlayerRace((Player) p, str, false);
							plugin.setter.switchingRaces((Player) p, str, false);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
							        "chosenRace").replace("{race}", str)));
							p.closeInventory();
							return;
							
						}
						
					}
				}
				}
			} else {
				for (String str : plugin.subRaces.get(plugin.configYaml.getString("races." + plugin.dataManager.getRace(
				        p) + ".subRaceType"))) {
					String raceType = plugin.configYaml.getString("races." + plugin.dataManager.getRace(p)
					        + ".subRaceType");
					if (slot == plugin.configYaml.getInt("races." + raceType + ".treeSlot")) {
						if (!p.hasPermission("mythicalraces.race." + raceType)) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
							        "noPerm")));
							p.closeInventory();
							return;
						}
						if (plugin.dataManager.getPlayerLevel(p) < plugin.configYaml.getInt("races." + str
						        + ".levelRequire")) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
							        "notRequiredLevel")));
							p.closeInventory();
							
							return;
						} else {
							plugin.dataManager.setPlayerRace((Player) p, raceType, false);
							plugin.setter.switchingRaces((Player) p, raceType, false);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
							        "chosenRace").replace("{race}", raceType)));
							p.closeInventory();
							return;
						}
						
					}
					
					if (slot == plugin.configYaml.getInt("races." + str + ".treeSlot")) {
						if (!p.hasPermission("mythicalraces.race." + str)) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
							        "noPerm")));
							p.closeInventory();
							return;
						}
						if (plugin.dataManager.getPlayerLevel(p) < plugin.configYaml.getInt("races." + str
						        + ".levelRequire")) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
							        "notRequiredLevel")));
							p.closeInventory();
							
							return;
						} else {
							plugin.dataManager.setPlayerRace((Player) p, str, false);
							plugin.setter.switchingRaces((Player) p, str, false);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
							        "chosenRace").replace("{race}", str)));
							p.closeInventory();
							return;
						}
						
					}
				}
			}
			
		}
		
	}
	
}
