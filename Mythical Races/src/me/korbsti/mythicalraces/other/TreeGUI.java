package me.korbsti.mythicalraces.other;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.korbsti.mythicalraces.MythicalRaces;
import net.md_5.bungee.api.ChatColor;

public class TreeGUI {
	MythicalRaces plugin;
	
	public TreeGUI(MythicalRaces plugin) {
		this.plugin = plugin;
	}
	
	public void openTree(Player p) {
		p.closeInventory();
		
		Inventory inventory = Bukkit.getServer().createInventory(p, 54,
		        plugin.configYaml.getString("other.treeName"));
		boolean isSubRace = false;
		if (plugin.configYaml.getBoolean("races." + plugin.dataManager.getRace(p) + ".isSubRace")) {
			isSubRace = true;
		}
		for (int i = 0; i != 54; i++) {
			inventory.setItem(i, (new ItemStack(Material.getMaterial(plugin.configYaml.getString("other.treeNonSlots")))));
		}
		String race = plugin.dataManager.getRace(p);
		if (isSubRace) {
			String startRace = plugin.configYaml.getString("races." + race + ".subRaceType");
			ItemStack stackk = new ItemStack(Material.getMaterial(plugin.configYaml.getString("races." + startRace
			        + ".material")), 1);
			ItemMeta metaa = stackk.getItemMeta();
			ArrayList<String> loree = new ArrayList<>();
			for (Object obj : plugin.configYaml.getList("races." + startRace + ".lore")) {
				loree.add(ChatColor.translateAlternateColorCodes('&', obj.toString()));
			}
			metaa.setLore(loree);
			metaa.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString("races."
			        + startRace + ".displayName")));
			stackk.setItemMeta(metaa);
			inventory.setItem(plugin.configYaml.getInt("races." + startRace + ".treeSlot"), stackk);
		} else {
			ItemStack stackk = new ItemStack(Material.getMaterial(plugin.configYaml.getString("races." + race
			        + ".material")), 1);
			ItemMeta metaa = stackk.getItemMeta();
			ArrayList<String> loree = new ArrayList<>();
			for (Object obj : plugin.configYaml.getList("races." + race + ".lore")) {
				loree.add(ChatColor.translateAlternateColorCodes('&', obj.toString()));
			}
			metaa.setLore(loree);
			metaa.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString("races."
			        + race + ".displayName")));
			stackk.setItemMeta(metaa);
			inventory.setItem(plugin.configYaml.getInt("races." + race + ".treeSlot"), stackk);
		}
		ArrayList<ItemStack> listStack = new ArrayList<ItemStack>();
		ArrayList<Integer> numberStack = new ArrayList<Integer>();
		if (!isSubRace && plugin.dataManager.returnUserSubRace(p) != null) {
			
			for (String str : plugin.dataManager.returnUserSubRace(p)) {
				
				ItemStack stack = new ItemStack(Material.getMaterial(plugin.configYaml.getString("races." + str
				        + ".material")), 1);
				ItemMeta meta = stack.getItemMeta();
				ArrayList<String> lore = new ArrayList<>();
				for (Object obj : plugin.configYaml.getList("races." + str + ".lore")) {
					lore.add(ChatColor.translateAlternateColorCodes('&', obj.toString()));
				}
				meta.setLore(lore);
				meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString("races."
				        + str + ".displayName")));
				stack.setItemMeta(meta);
				listStack.add(stack);
				numberStack.add(plugin.configYaml.getInt("races." + str + ".treeSlot"));
			}
			
			for (int i = 0; i != listStack.size(); i++) {
				inventory.setItem(numberStack.get(i), listStack.get(i));
			}
			
		} else {
			if (plugin.subRaces.get(plugin.configYaml.getString("races." + race + ".subRaceType")) != null) {
				for (String str : plugin.subRaces.get(plugin.configYaml.getString("races." + race + ".subRaceType"))) {
					ItemStack stack = new ItemStack(Material.getMaterial(plugin.configYaml.getString("races." + str
					        + ".material")), 1);
					ItemMeta meta = stack.getItemMeta();
					ArrayList<String> lore = new ArrayList<>();
					for (Object obj : plugin.configYaml.getList("races." + str + ".lore")) {
						lore.add(ChatColor.translateAlternateColorCodes('&', obj.toString()));
					}
					meta.setLore(lore);
					meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString("races."
					        + str + ".displayName")));
					stack.setItemMeta(meta);
					listStack.add(stack);
					numberStack.add(plugin.configYaml.getInt("races." + str + ".treeSlot"));
				}
				
				for (int i = 0; i != listStack.size(); i++) {
					inventory.setItem(numberStack.get(i), listStack.get(i));
				}
				
			}
		}	
			p.openInventory(inventory);
		
		
	}
	
}
