package me.korbsti.mythicalraces.other;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import me.korbsti.mythicalraces.MythicalRaces;
import net.md_5.bungee.api.ChatColor;

public class GUI {
	MythicalRaces plugin;
	
	public GUI(MythicalRaces instance) {
		plugin = instance;
	}
	
	public void selectRaceGUI(Player p) {
		if(p == null || p.getOpenInventory().getTitle().equals(plugin.configYaml.getString("other.guiName"))) return;
		Inventory playerList = Bukkit.getServer().createInventory(p, plugin.configYaml.getInt("other.guiSlots"), plugin.configYaml.getString("other.guiName"));
		//////////////////////////////////////////////
		ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta playerHeadMeta = (SkullMeta) playerHead.getItemMeta();
		playerHeadMeta.setOwningPlayer(p);
		ArrayList<String> lore1 = new ArrayList<>();
		for (Object obj : plugin.configYaml.getList("other.guiPlayerLore")) {
			lore1.add(ChatColor.translateAlternateColorCodes('&', obj.toString().replace("{race}", plugin.dataManager
			        .getRace(p))));
		}
		playerHeadMeta.setLore(lore1);
		playerHeadMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
		        "other.guiPlayerDisplay")));
		playerHead.setItemMeta(playerHeadMeta);
		/////////////////////////////////////////
		ArrayList<ItemStack> listStack = new ArrayList<ItemStack>();
		ArrayList<Integer> numberStack = new ArrayList<Integer>();
		
		for (String str : plugin.races) {
			if (plugin.configYaml.getInt("races." + str + ".guiPage") == plugin.guiNumber.get(p.getName())) {
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
				numberStack.add(plugin.configYaml.getInt("races." + str + ".slot"));
			}
		}
		
		int x = 0;
		for (ItemStack s : listStack) {
			playerList.setItem(numberStack.get(x), s);
			++x;
		}
		/// 
		
		ItemStack forward = new ItemStack(Material.getMaterial(plugin.configYaml.getString("other.forwardClickItem")), 1);
		ItemMeta me = forward.getItemMeta();
		me.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.configYaml.getString(
		        "other.forwardClickDisplay")));
		forward.setItemMeta(me);
		playerList.setItem(plugin.configYaml.getInt("other.forwardClick"), forward);
		playerList.setItem(plugin.configYaml.getInt("other.guiPlayerDisplayNumber"), playerHead);
		p.openInventory(playerList);
	
	}
	
}
