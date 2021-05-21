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
			for(String str : plugin.races) {
				if(plugin.configYaml.getInt("races." + str + ".slot") == slot && plugin.configYaml.getInt("races." + str + ".guiPage") == plugin.guiNumber.get(p.getName())) {
					if(!p.hasPermission("mythicalraces.race." + str)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString("noPerm")));
						p.closeInventory();
						return;
					}
					plugin.dataManager.setPlayerRace((Player) p, str);
					plugin.setter.switchingRaces((Player) p, str);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString("chosenRace").replace("{race}", str)));
					p.closeInventory();
				}
			}
			if(slot == plugin.configYaml.getInt("other.forwardClick")) {
				plugin.guiNumber.put(p.getName(), plugin.guiNumber.get(p.getName()) + 1);
				plugin.gui.selectRaceGUI(p);
			}
		}
		
		
	}
	
}
