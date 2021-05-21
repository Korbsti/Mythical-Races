package me.korbsti.mythicalraces.other;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.korbsti.mythicalraces.MythicalRaces;

public class Setters {
	MythicalRaces plugin;
	
	public Setters(MythicalRaces instance) {
		plugin = instance;
		
	}
	
	public void switchingRaces(Player p, String race) {
		for (Attribute att : Attribute.values()) {
			if (p.getAttribute(att) != null) {
				p.getAttribute(att).setBaseValue(p.getAttribute(att).getDefaultValue());
			}
		}
		for (PotionEffectType effect : PotionEffectType.values()) {
			if (p.hasPotionEffect(effect)) {
				p.removePotionEffect(effect);
			}
		}
		setEffects(p);
	}
	
	public void setEffects(Player p) {
		long worldTime = p.getWorld().getTime();
		if (worldTime < plugin.nightEnd && worldTime > plugin.nightStart) {
			String str = plugin.dataManager.getRace(p);
			if (plugin.dayRacePassiveAttributes.get(str) != null) {
				for (PotionEffectType effe : plugin.dayRacePassivePotionEffects.get(str)) {
					if (effe != null) {
						if (p.hasPotionEffect(effe)) {
							if (p.getPotionEffect(effe).getDuration() < 99999) {
								p.removePotionEffect(effe);
							}
						}
					}
				}
			}
			if (plugin.nightRacePassivePotionEffects.get(str) != null) {
				int x = 0;
				for (PotionEffectType effect : plugin.nightRacePassivePotionEffects.get(str)) {
					if (effect != null) {
						if (!p.hasPotionEffect(effect)) {
							p.addPotionEffect(new PotionEffect(effect, 9999999,
							        plugin.nightRacePassivePotionEffectsAmplifier
							                .get(str).get(x)));
						}
					}
					x++;
					// 99999999
				}
			}
			if (plugin.nightRacePassiveAttributes.get(str) != null) {
				int x = 0;
				for (Attribute att : plugin.nightRacePassiveAttributes.get(str)) {
					p.getAttribute(att).setBaseValue(plugin.nightRacePassiveAttributesAmount.get(str).get(x));
					x++;
					
				}
			}
			return;
			
		}
		
		String str = plugin.dataManager.getRace(p);
		if (plugin.dayRacePassivePotionEffects.get(str) != null) {
			int x = 0;
			if (plugin.nightRacePassivePotionEffects.get(str) != null) {
				for (PotionEffectType effe : plugin.nightRacePassivePotionEffects.get(str)) {
					if (effe != null) {
						if (p.hasPotionEffect(effe)) {
							if (p.getPotionEffect(effe).getDuration() > 99999) {
								p.removePotionEffect(effe);
							}
						}
					}
				}
			}
			for (PotionEffectType effect : plugin.dayRacePassivePotionEffects.get(str)) {
				if (effect != null) {
					if (!p.hasPotionEffect(effect)) {
						p.addPotionEffect(new PotionEffect(effect, 99999, plugin.dayRacePassivePotionEffectsAmplifier
						        .get(str).get(x)));
					}
				}
				x++;
				
			}
		}
		if (plugin.dayRacePassiveAttributes.get(str) != null) {
			int x = 0;
			for (Attribute att : plugin.dayRacePassiveAttributes.get(str)) {
				p.getAttribute(att).setBaseValue(plugin.dayRacePassiveAttributesAmount.get(str).get(x));
				x++;
				
			}
		}
		
	}
}
