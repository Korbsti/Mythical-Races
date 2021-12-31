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
		
		for (String str : plugin.race.get(race).raceCommandExecution) {
			if (!str.equals("null")) {
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), str.replace("{player}", p.getName()));
			}
		}
		
	}
	
	// - 'ALL Y > -100 -1'
	public void setEffects(Player p) {
		long worldTime = p.getWorld().getTime();
		if (worldTime < plugin.nightEnd && worldTime > plugin.nightStart) {
			String str = plugin.dataManager.getRace(p);
			if (plugin.race.get(str).dayRacePassivePotionEffects != null) {
				for (PotionEffectType effe : plugin.race.get(str).dayRacePassivePotionEffects) {
					if (effe != null) {
						if (p.hasPotionEffect(effe)) {
							if (p.getPotionEffect(effe).getDuration() < 99999) {
								p.removePotionEffect(effe);
							}
						}
					}
				}
			}
			if (plugin.race.get(str).nightRacePassivePotionEffects != null) {
				int x = 0;
				for (PotionEffectType effe : plugin.race.get(str).nightRacePassivePotionEffects) {
					String[] data = plugin.race.get(str).nightRaceDataPotion.get(x).split(" ");
					double y = p.getLocation().getY();
					boolean signR = ">".equals(data[2]);
					boolean lowerThan = y < Double.parseDouble(data[3]) && ">".equals(data[2]);
					boolean greaterThan = y > Double.parseDouble(data[3]) && "<".equals(data[2]);
					boolean greaterThanY = y > Double.parseDouble(data[3]) && ">".equals(data[2]);
					boolean lowerThanY = y < Double.parseDouble(data[3]) && "<".equals(data[2]);
					boolean inBiome = data[0].contains(p.getLocation().getBlock().getBiome().toString());
					int elseAmp = Integer.parseInt(data[4]);
					if (effe != null) {
						if (p.hasPotionEffect(effe)) {
							if (p.getPotionEffect(effe).getDuration() > 99999) {
								int potionAmp =  p.getPotionEffect(effe).getAmplifier();
								if (data[0].contains("ALL") && ((lowerThan || greaterThan) || (greaterThanY || lowerThanY && potionAmp != elseAmp))) {
									p.removePotionEffect(effe);
								} else if (inBiome && ((lowerThan || greaterThan) || (greaterThanY || lowerThanY && potionAmp != elseAmp))) {
									p.removePotionEffect(effe);
								} else if (!inBiome && !data[0].contains("ALL")) {
									p.removePotionEffect(effe);
								}
								
							}
						}
						if (!p.hasPotionEffect(effe)) {
							if (data[0].contains("ALL") && (lowerThanY && !signR || greaterThanY && signR)) {
								p.addPotionEffect(new PotionEffect(effe, 9999999, plugin.race.get(str).nightRacePassivePotionEffectsBase.get(x)));
							} else if (inBiome && (lowerThanY && !signR || greaterThanY && signR)) {
								p.addPotionEffect(new PotionEffect(effe, 9999999, plugin.race.get(str).nightRacePassivePotionEffectsBase.get(x)));
							} else {
								if (!"-1".equals(data[4])) {
									p.addPotionEffect(new PotionEffect(effe, 9999999, elseAmp));
								}
							}
							
						}
						
						// 99999999
						
					}
					x++;
				}
				
			}
			
			if (plugin.race.get(str).nightRacePassiveAttributes != null) {
				double addedAmount = 0;
				int x = 0;
				for (Attribute att : plugin.race.get(str).nightRacePassiveAttributes) {
					String[] data = plugin.race.get(str).nightRaceDataAttribute.get(x).split(" ");
					double y = p.getLocation().getY();
					boolean signR = ">".equals(data[2]);
					boolean greaterThanY = y > Double.parseDouble(data[3]) && ">".equals(data[2]);
					boolean lowerThanY = y < Double.parseDouble(data[3]) && "<".equals(data[2]);
					boolean inBiome = data[0].contains(p.getLocation().getBlock().getBiome().toString());
					if (att != null) {
						    addedAmount = plugin.dataManager.getPlayerLevel(p) * plugin.race.get(str).nightRacePassiveAttributesLevel.get(x);
							if (data[0].contains("ALL") && (lowerThanY && !signR || greaterThanY && signR)) {
								p.getAttribute(att).setBaseValue(plugin.race.get(str).nightRacePassiveAttributesAmount.get(x) + addedAmount);
							} else if (inBiome && (lowerThanY && !signR || greaterThanY && signR)) {
								p.getAttribute(att).setBaseValue(plugin.race.get(str).nightRacePassiveAttributesAmount.get(x) + addedAmount);
							} else {
								if (!"-1".equals(data[4])) {
									p.getAttribute(att).setBaseValue(Double.parseDouble(data[4]) + addedAmount);
								}
							}
						// 99999999
					}
					addedAmount = 0;
					x++;
				}
				
			}
			return;
			
		}
		String str = plugin.dataManager.getRace(p);
		if (plugin.race.get(str).nightRacePassivePotionEffects != null) {
			if (plugin.race.get(str).nightRacePassivePotionEffects != null) {
				for (PotionEffectType effe : plugin.race.get(str).nightRacePassivePotionEffects) {
					if (effe != null) {
						if (p.hasPotionEffect(effe)) {
							if (p.getPotionEffect(effe).getDuration() > 99999) {
								p.removePotionEffect(effe);
							}
						}
					}
				}
			}
		}
			
		if (plugin.race.get(str).dayRacePassivePotionEffects != null) {
			int x = 0;
			for (PotionEffectType effe : plugin.race.get(str).dayRacePassivePotionEffects) {
				String[] data = plugin.race.get(str).dayRaceDataPotion.get(x).split(" ");
				double y = p.getLocation().getY();
				boolean signR = ">".equals(data[2]);
				boolean lowerThan = y < Double.parseDouble(data[3]) && ">".equals(data[2]);
				boolean greaterThan = y > Double.parseDouble(data[3]) && "<".equals(data[2]);
				boolean greaterThanY = y > Double.parseDouble(data[3]) && ">".equals(data[2]);
				boolean lowerThanY = y < Double.parseDouble(data[3]) && "<".equals(data[2]);
				boolean inBiome = data[0].contains(p.getLocation().getBlock().getBiome().toString());
				int elseAmp = Integer.parseInt(data[4]);
				if (effe != null) {
					if (p.hasPotionEffect(effe)) {
						if (p.getPotionEffect(effe).getDuration() < 99999) {
							int potionAmp =  p.getPotionEffect(effe).getAmplifier();
							if (data[0].contains("ALL") && ((lowerThan || greaterThan) || (greaterThanY || lowerThanY && potionAmp != elseAmp))) {
								p.removePotionEffect(effe);
							} else if (inBiome && ((lowerThan || greaterThan) || (greaterThanY || lowerThanY && potionAmp != elseAmp))) {
								p.removePotionEffect(effe);
							} else if (!inBiome && !data[0].contains("ALL")) {
								p.removePotionEffect(effe);
							}
							
						}
					}
					if (!p.hasPotionEffect(effe)) {
						if (data[0].contains("ALL") && (lowerThanY && !signR || greaterThanY && signR)) {
							p.addPotionEffect(new PotionEffect(effe, 99999, plugin.race.get(str).dayRacePassivePotionEffectsBase.get(x)));
						} else if (inBiome && (lowerThanY && !signR || greaterThanY && signR)) {
							p.addPotionEffect(new PotionEffect(effe, 99999, plugin.race.get(str).dayRacePassivePotionEffectsBase.get(x)));
						} else {
							if (!"-1".equals(data[4])) {
								p.addPotionEffect(new PotionEffect(effe, 99999, elseAmp));
							}
						}
						
					}
					
					// 99999999
					
				}
				x++;
			}
			
		}
		
		
		if (plugin.race.get(str).dayRacePassiveAttributes != null) {
			double addedAmount = 0;
			int x = 0;
			for (Attribute att : plugin.race.get(str).dayRacePassiveAttributes) {
				String[] data = plugin.race.get(str).dayRaceDataAttribute.get(x).split(" ");
				double y = p.getLocation().getY();
				boolean signR = ">".equals(data[2]);
				boolean greaterThanY = y > Double.parseDouble(data[3]) && ">".equals(data[2]);
				boolean lowerThanY = y < Double.parseDouble(data[3]) && "<".equals(data[2]);
				boolean inBiome = data[0].contains(p.getLocation().getBlock().getBiome().toString());
				if (att != null) {
					    addedAmount = plugin.dataManager.getPlayerLevel(p) * plugin.race.get(str).dayRacePassiveAttributesLevel.get(x);
						if (data[0].contains("ALL") && (lowerThanY && !signR || greaterThanY && signR)) {
							p.getAttribute(att).setBaseValue(plugin.race.get(str).dayRacePassiveAttributesAmount.get(x) + addedAmount);
						} else if (inBiome && (lowerThanY && !signR || greaterThanY && signR)) {
							p.getAttribute(att).setBaseValue(plugin.race.get(str).dayRacePassiveAttributesAmount.get(x) + addedAmount);
						} else {
								p.getAttribute(att).setBaseValue(Double.parseDouble(data[4]) + addedAmount);
							
						}
					// 99999999
				}
				addedAmount = 0;
				x++;
			}
			
		}
		
	}
}
