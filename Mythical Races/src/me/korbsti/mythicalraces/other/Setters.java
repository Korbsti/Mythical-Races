package me.korbsti.mythicalraces.other;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.korbsti.mythicalraces.MythicalRaces;
import me.korbsti.mythicalraces.api.RaceChangeEvent;

public class Setters {
	MythicalRaces plugin;
	
	public Setters(MythicalRaces instance) {
		plugin = instance;
		
	}
	
	public void switchingRaces(Player p, String race, boolean fromJoinEvent) {
		
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
			@Override
			public void run() {
				Bukkit.getPluginManager().callEvent(new RaceChangeEvent(plugin, race, p, fromJoinEvent));
			}
		});
		
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
		plugin.playersRace.put(p.getName(), plugin.race.get(race));
		setEffects(p);
		
		for (String str : plugin.race.get(race).raceCommandExecution) {
			if (!str.equals("null")) {
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), str.replace("{player}", p.getName()));
			}
		}
		
	}
	
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
					boolean inBiome = false;
					boolean nearProperBlock = false;
					boolean inProperWeather = false;
					boolean inProperWorld = false;
					
					if ("ALL".equals(data[7])) {
						inProperWeather = true;
					} else {
						boolean hasStorm = p.getLocation().getWorld().hasStorm();
						if ("FALSE".equals(data[7])) {
							if (!hasStorm) {
								inProperWeather = true;
							} else {
								inProperWeather = false;
							}
						} else {
							if (hasStorm) {
								inProperWeather = true;
							} else {
								inProperWeather = false;
							}
						}
					}
					
					if (!"ALL".equals(data[8])) {
						String worldName = p.getLocation().getWorld().getName();
						for (String world : data[8].split(",")) {
							if (worldName.equals(world)) {
								inProperWorld = true;
								break;
							}
						}
					} else {
						inProperWorld = true;
					}
					String pInsideBiome = p.getLocation().getBlock().getBiome().toString();
					// ALL Y > -1000 -1 ALL GRASS
					for (String biome : data[0].split(",")) {
						if (biome.equals(pInsideBiome)) {
							inBiome = true;
							if ("ALL".equals(data[6])) {
								nearProperBlock = true;
								break;
							}
							
							if ("ABOVE".equals(data[5])) {
								Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
								for (String block : data[6].split(","))
									if (above == Material.getMaterial(block)) {
										nearProperBlock = true;
										break;
										
									}
								
							} else if ("MID".equals(data[5])) {
								Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
								Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
								for (String block : data[6].split(","))
									if (mid1 == Material.getMaterial(block) || mid2 == Material.getMaterial(block)) {
										nearProperBlock = true;
										break;
									}
								
							} else if ("BELOW".equals(data[5])) {
								Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
								Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
								
								for (String block : data[6].split(","))
									if (below == Material.getMaterial(block) || below2 == Material.getMaterial(block)) {
										nearProperBlock = true;
										break;
									}
							} else if ("VAMPIRE".equals(data[5])) {
								Location playersLocation = p.getLocation();
								boolean applyEffect = true;
								for (int i = p.getLocation().getBlockY(); i < 266; i++) {
									if (playersLocation.add(0, 1, 0).getBlock().getType().isSolid()) {
										applyEffect = false;
										break;
									}
								}
								
								if (applyEffect) {
									nearProperBlock = true;
								}
								
							} else if ("AROUND".equals(data[5])) {
								Location playersLocation = p.getLocation();
								int blockX = playersLocation.getBlockX();
								int blockY = playersLocation.getBlockY();
								int blockZ = playersLocation.getBlockZ();
								outerloop: for (int pX = blockX - 1; pX != blockX + 2; pX++) {
									for (int pY = blockY - 1; pY != blockY + 4; pY++) {
										for (int pZ = blockZ - 1; pZ != blockZ + 2; pZ++) {
											for (String block : data[6].split(",")) {
												Material mat = Material.getMaterial(block);
												if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ)
												        .getBlock().getType()) {
													nearProperBlock = true;
													break outerloop;
												}
											}
											
										}
									}
								}
							}else if (data[5].startsWith("CUSTOM")) {
								Location playersLocation = p.getLocation();
								int blockX = playersLocation.getBlockX();
								int blockY = playersLocation.getBlockY();
								int blockZ = playersLocation.getBlockZ();
								
								int searchAmount = Integer.parseInt(data[5].split("M")[1]);
								
								outerloop: for (int pX = blockX - searchAmount; pX != blockX + searchAmount; pX++) {
									for (int pY = blockY - searchAmount; pY != blockY + searchAmount; pY++) {
										for (int pZ = blockZ - searchAmount; pZ != blockZ + searchAmount; pZ++) {
											for (String block : data[6].split(",")) {
												Material mat = Material.getMaterial(block);
												if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ).getBlock().getType()) {
													nearProperBlock = true;
													break outerloop;
												}
											}
											
										}
									}
								}
							} else if (data[5].startsWith("LIGHTB")) {
								int lightLevel = p.getLocation().getBlock().getLightLevel();
								int limit = Integer.parseInt(data[5].split("B")[1]);
								
								
								if(lightLevel < limit) {
									nearProperBlock = true;

								} else {
									nearProperBlock = false;

								}
								
								
								
							}else if (data[5].startsWith("LIGHTA")) {
								int lightLevel = p.getLocation().getBlock().getLightLevel();
								int limit = Integer.parseInt(data[5].split("A")[1]);
								if(lightLevel > limit) {
									nearProperBlock = true;
								
								} else {
									nearProperBlock = false;

								}
								
							
								
							}  else if ("ALL".equals(data[5])) {
								Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
								Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
								Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
								Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
								Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
								
								for (String block : data[6].split(",")) {
									Material mat = Material.getMaterial(block);
									if (below == mat || mid1 == mat || mid2 == mat || above == mat || below2 == mat) {
										nearProperBlock = true;
										break;
									}
								}
								
							}
							break;
						}
					}
					if ("ALL".equals(data[0])) {
						inBiome = true;
						if (!"ALL".equals(data[6])) {
							if ("ABOVE".equals(data[5])) {
								Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
								for (String block : data[6].split(","))
									if (above == Material.getMaterial(block)) {
										nearProperBlock = true;
										break;
									}
								
							} else if ("MID".equals(data[5])) {
								Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
								Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
								for (String block : data[6].split(","))
									if (mid1 == Material.getMaterial(block) || mid2 == Material.getMaterial(block)) {
										nearProperBlock = true;
										break;
									}
								
							} else if ("BELOW".equals(data[5])) {
								Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
								Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
								
								for (String block : data[6].split(","))
									if (below == Material.getMaterial(block) || below2 == Material.getMaterial(block)) {
										nearProperBlock = true;
										break;
									}
							} else if ("VAMPIRE".equals(data[5])) {
								Location playersLocation;
								playersLocation = p.getLocation();
								boolean applyEffect;
								applyEffect = true;
								for (int i = p.getLocation().getBlockY(); i < 266; i++) {
									if (playersLocation.add(0, 1, 0).getBlock().getType().isSolid()) {
										applyEffect = false;
										break;
									}
								}
								
								if (applyEffect) {
									nearProperBlock = true;
								}
								
							} else if ("AROUND".equals(data[5])) {
								Location playersLocation = p.getLocation();
								int blockX = playersLocation.getBlockX();
								int blockY = playersLocation.getBlockY();
								int blockZ = playersLocation.getBlockZ();
								outerloop: for (int pX = blockX - 1; pX != blockX + 2; pX++) {
									for (int pY = blockY - 1; pY != blockY + 4; pY++) {
										for (int pZ = blockZ - 1; pZ != blockZ + 2; pZ++) {
											for (String block : data[6].split(",")) {
												Material mat = Material.getMaterial(block);
												if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ)
												        .getBlock().getType()) {
													nearProperBlock = true;
													break outerloop;
												}
											}
											
										}
									}
								}
							} else if (data[5].startsWith("CUSTOM")) {
								Location playersLocation = p.getLocation();
								int blockX = playersLocation.getBlockX();
								int blockY = playersLocation.getBlockY();
								int blockZ = playersLocation.getBlockZ();
								
								int searchAmount = Integer.parseInt(data[5].split("M")[1]);
								outerloop: for (int pX = blockX - searchAmount; pX != blockX + searchAmount; pX++) {
									for (int pY = blockY - searchAmount; pY != blockY + searchAmount; pY++) {
										for (int pZ = blockZ - searchAmount; pZ != blockZ + searchAmount; pZ++) {
											for (String block : data[6].split(",")) {
												Material mat = Material.getMaterial(block);
												if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ).getBlock().getType()) {
													nearProperBlock = true;
													break outerloop;
												}
											}
											
										}
									}
								}
							}else if (data[5].startsWith("LIGHTB")) {
								int lightLevel = p.getLocation().getBlock().getLightLevel();
								int limit = Integer.parseInt(data[5].split("B")[1]);
								

								
								if(lightLevel < limit) {
									nearProperBlock = true;

								} else {
									nearProperBlock = false;

								}
								
								
								
							}else if (data[5].startsWith("LIGHTA")) {
								int lightLevel = p.getLocation().getBlock().getLightLevel();
								int limit = Integer.parseInt(data[5].split("A")[1]);
								
								if(lightLevel > limit) {
									nearProperBlock = true;
								} else {
									nearProperBlock = false;
								}
							}    else if ("ALL".equals(data[5])) {
								Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
								Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
								Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
								Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
								Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
								
								for (String block : data[6].split(",")) {
									Material mat = Material.getMaterial(block);
									if (below == mat || mid1 == mat || mid2 == mat || above == mat || below2 == mat) {
										nearProperBlock = true;
										break;
									}
								}
								
							}
						} else {
							nearProperBlock = true;
							
						}
					}

					int elseAmp = Integer.parseInt(data[4]);
					if (effe != null) {
						
						if (p.hasPotionEffect(effe)) {
							if (p.getPotionEffect(effe).getDuration() > 99999) {
								int potionAmp = p.getPotionEffect(effe).getAmplifier();
								if (data[0].equals("ALL") && !nearProperBlock && ((lowerThan || greaterThan)
								        || (greaterThanY || lowerThanY && potionAmp != elseAmp))) {
									p.removePotionEffect(effe);
								} else if (data[0].equals("ALL") && nearProperBlock && ((lowerThan || greaterThan)
								        || (greaterThanY || lowerThanY && potionAmp != elseAmp))) {
									p.removePotionEffect(effe);
								} else if (inBiome && nearProperBlock && ((lowerThan || greaterThan) || (greaterThanY
								        || lowerThanY && potionAmp != elseAmp))) {
									p.removePotionEffect(effe);
								} else if (!inBiome && !data[0].equals("ALL") || (inBiome && !nearProperBlock)
								        || (data[0].equals("ALL") && nearProperBlock && potionAmp == elseAmp)
								        || !inProperWeather || !inProperWorld) {
									p.removePotionEffect(effe);
								}
								
							}
						}
						if (!p.hasPotionEffect(effe)) {
							if (data[0].equals("ALL") && nearProperBlock && inProperWorld && inProperWeather
							        && (lowerThanY && !signR || greaterThanY && signR)) {
								p.addPotionEffect(new PotionEffect(effe, 9999999, plugin.race.get(
								        str).nightRacePassivePotionEffectsBase.get(x), false, false));
								
							} else if (inBiome && nearProperBlock && inProperWorld && inProperWeather && (lowerThanY
							        && !signR || greaterThanY && signR)) {
								p.addPotionEffect(new PotionEffect(effe, 9999999, plugin.race.get(
								        str).nightRacePassivePotionEffectsBase.get(x), false, false));
								
							} else {
								if (!"-1".equals(data[4])) {
									p.addPotionEffect(new PotionEffect(effe, 9999999, elseAmp, false, false));
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
					boolean inBiome = false;
					boolean nearProperBlock = false;
					boolean inProperWeather = false;
					boolean inProperWorld = false;
					
					if ("ALL".equals(data[7])) {
						inProperWeather = true;
					} else {
						boolean hasStorm = p.getLocation().getWorld().hasStorm();
						if ("FALSE".equals(data[7])) {
							if (!hasStorm) {
								inProperWeather = true;
							} else {
								inProperWeather = false;
							}
						} else {
							if (hasStorm) {
								inProperWeather = true;
							} else {
								inProperWeather = false;
							}
						}
					}
					
					if (!"ALL".equals(data[8])) {
						String worldName = p.getLocation().getWorld().getName();
						for (String world : data[8].split(",")) {
							if (worldName.equals(world)) {
								inProperWorld = true;
								break;
							}
						}
					} else {
						inProperWorld = true;
					}
					String pInsideBiome = p.getLocation().getBlock().getBiome().toString();
					// ALL Y > -1000 -1 ALL GRASS
					for (String biome : data[0].split(",")) {
						if (biome.equals(pInsideBiome)) {
							inBiome = true;
							if ("ALL".equals(data[6])) {
								nearProperBlock = true;
								break;
							}
							
							if ("ABOVE".equals(data[5])) {
								Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
								for (String block : data[6].split(","))
									if (above == Material.getMaterial(block)) {
										nearProperBlock = true;
										break;
									}
								
							} else if ("MID".equals(data[5])) {
								Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
								Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
								for (String block : data[6].split(","))
									if (mid1 == Material.getMaterial(block) || mid2 == Material.getMaterial(block)) {
										nearProperBlock = true;
										break;
									}
								
							} else if ("BELOW".equals(data[5])) {
								Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
								Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
								
								for (String block : data[6].split(","))
									if (below == Material.getMaterial(block) || below2 == Material.getMaterial(block)) {
										nearProperBlock = true;
										break;
									}
								
							} else if ("VAMPIRE".equals(data[5])) {
								Location playersLocation = p.getLocation();
								boolean applyEffect = true;
								for (int i = p.getLocation().getBlockY(); i < 266; i++) {
									if (playersLocation.add(0, 1, 0).getBlock().getType().isSolid()) {
										applyEffect = false;
										break;
									}
								}
								
								if (applyEffect) {
									nearProperBlock = true;
								}
								
							} else if ("AROUND".equals(data[5])) {
								Location playersLocation = p.getLocation();
								int blockX = playersLocation.getBlockX();
								int blockY = playersLocation.getBlockY();
								int blockZ = playersLocation.getBlockZ();
								outerloop: for (int pX = blockX - 1; pX != blockX + 2; pX++) {
									for (int pY = blockY - 1; pY != blockY + 4; pY++) {
										for (int pZ = blockZ - 1; pZ != blockZ + 2; pZ++) {
											for (String block : data[6].split(",")) {
												Material mat = Material.getMaterial(block);
												if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ)
												        .getBlock().getType()) {
													nearProperBlock = true;
													break outerloop;
												}
											}
											
										}
									}
								}
							} else if (data[5].startsWith("CUSTOM")) {
								Location playersLocation = p.getLocation();
								int blockX = playersLocation.getBlockX();
								int blockY = playersLocation.getBlockY();
								int blockZ = playersLocation.getBlockZ();
								
								int searchAmount = Integer.parseInt(data[5].split("M")[1]);
								outerloop: for (int pX = blockX - searchAmount; pX != blockX + searchAmount; pX++) {
									for (int pY = blockY - searchAmount; pY != blockY + searchAmount; pY++) {
										for (int pZ = blockZ - searchAmount; pZ != blockZ + searchAmount; pZ++) {
											for (String block : data[6].split(",")) {
												Material mat = Material.getMaterial(block);
												if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ).getBlock().getType()) {
													nearProperBlock = true;
													break outerloop;
												}
											}
											
										}
									}
								}
							}else if (data[5].startsWith("LIGHTB")) {
								int lightLevel = p.getLocation().getBlock().getLightLevel();
								int limit = Integer.parseInt(data[5].split("B")[1]);
								if(lightLevel < limit) {
									nearProperBlock = true;

								} else {
									nearProperBlock = false;

								}
								
								
								
							}else if (data[5].startsWith("LIGHTA")) {
								int lightLevel = p.getLocation().getBlock().getLightLevel();
								int limit = Integer.parseInt(data[5].split("A")[1]);
								if(lightLevel > limit) {
									nearProperBlock = true;

								} else {
									nearProperBlock = false;

								}
								
								
								
							}else if ("ALL".equals(data[5])) {
								Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
								Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
								Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
								Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
								Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
								
								for (String block : data[6].split(",")) {
									Material mat = Material.getMaterial(block);
									if (below == mat || mid1 == mat || mid2 == mat || above == mat || below2 == mat) {
										nearProperBlock = true;
										break;
									}
								}
								
							}
							break;
						}
					}
					if ("ALL".equals(data[0])) {
						if (!"ALL".equals(data[6])) {
							if ("ABOVE".equals(data[5])) {
								Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
								for (String block : data[6].split(","))
									if (above == Material.getMaterial(block)) {
										nearProperBlock = true;
										break;
									}
								
							} else if ("MID".equals(data[5])) {
								Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
								Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
								for (String block : data[6].split(","))
									if (mid1 == Material.getMaterial(block) || mid2 == Material.getMaterial(block)) {
										nearProperBlock = true;
										break;
									}
								
							} else if ("BELOW".equals(data[5])) {
								Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
								Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
								
								for (String block : data[6].split(","))
									if (below == Material.getMaterial(block) || below2 == Material.getMaterial(block)) {
										nearProperBlock = true;
										break;
									}
							} else if ("VAMPIRE".equals(data[5])) {
								Location playersLocation = p.getLocation();
								boolean applyEffect = true;
								for (int i = p.getLocation().getBlockY(); i < 266; i++) {
									if (playersLocation.add(0, 1, 0).getBlock().getType().isSolid()) {
										applyEffect = false;
										break;
									}
								}
								
								if (applyEffect) {
									nearProperBlock = true;
								}
								
							} else if ("AROUND".equals(data[5])) {
								Location playersLocation = p.getLocation();
								int blockX = playersLocation.getBlockX();
								int blockY = playersLocation.getBlockY();
								int blockZ = playersLocation.getBlockZ();
								outerloop: for (int pX = blockX - 1; pX != blockX + 2; pX++) {
									for (int pY = blockY - 1; pY != blockY + 4; pY++) {
										for (int pZ = blockZ - 1; pZ != blockZ + 2; pZ++) {
											for (String block : data[6].split(",")) {
												Material mat = Material.getMaterial(block);
												if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ)
												        .getBlock().getType()) {
													nearProperBlock = true;
													break outerloop;
												}
											}
											
										}
									}
								}
							} else if (data[5].startsWith("CUSTOM")) {
								Location playersLocation = p.getLocation();
								int blockX = playersLocation.getBlockX();
								int blockY = playersLocation.getBlockY();
								int blockZ = playersLocation.getBlockZ();
								
								int searchAmount = Integer.parseInt(data[5].split("M")[1]);
								outerloop: for (int pX = blockX - searchAmount; pX != blockX + searchAmount; pX++) {
									for (int pY = blockY - searchAmount; pY != blockY + searchAmount; pY++) {
										for (int pZ = blockZ - searchAmount; pZ != blockZ + searchAmount; pZ++) {
											for (String block : data[6].split(",")) {
												Material mat = Material.getMaterial(block);
												if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ).getBlock().getType()) {
													nearProperBlock = true;
													break outerloop;
												}
											}
											
										}
									}
								}
							}else if (data[5].startsWith("LIGHTB")) {
								int lightLevel = p.getLocation().getBlock().getLightLevel();
								int limit = Integer.parseInt(data[5].split("B")[1]);
								if(lightLevel < limit) {
									nearProperBlock = true;
								} else {
									nearProperBlock = false;

								}
								
								
								
							}else if (data[5].startsWith("LIGHTA")) {
								int lightLevel = p.getLocation().getBlock().getLightLevel();
								int limit = Integer.parseInt(data[5].split("A")[1]);
								if(lightLevel > limit) {
									nearProperBlock = true;

								} else {
									nearProperBlock = false;

								}
								
								
								
							}else if ("ALL".equals(data[5])) {
								Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
								Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
								Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
								Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
								Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
								
								for (String block : data[6].split(",")) {
									Material mat = Material.getMaterial(block);
									if (below == mat || mid1 == mat || mid2 == mat || above == mat || below2 == mat) {
										nearProperBlock = true;
										break;
									}
								}
								
							}
						} else {
							nearProperBlock = true;
							
						}
					}
					
					if (att != null) {
						addedAmount = plugin.dataManager.getPlayerLevel(p) * plugin.race.get(
						        str).nightRacePassiveAttributesLevel.get(x);
						if (data[0].equals("ALL") && nearProperBlock && inProperWorld && inProperWeather && (lowerThanY
						        && !signR || greaterThanY && signR)) {
							p.getAttribute(att).setBaseValue(plugin.race.get(str).nightRacePassiveAttributesAmount.get(
							        x) + addedAmount);
						} else if (inBiome && nearProperBlock && inProperWorld && inProperWeather && (lowerThanY
						        && !signR || greaterThanY && signR)) {
							p.getAttribute(att).setBaseValue(plugin.race.get(str).nightRacePassiveAttributesAmount.get(
							        x) + addedAmount);
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
				boolean inBiome = false;
				boolean nearProperBlock = false;
				boolean inProperWeather = false;
				boolean inProperWorld = false;
				
				if ("ALL".equals(data[7])) {
					inProperWeather = true;
				} else {
					boolean hasStorm = p.getLocation().getWorld().hasStorm();
					if ("FALSE".equals(data[7])) {
						if (!hasStorm) {
							inProperWeather = true;
						} else {
							inProperWeather = false;
						}
					} else {
						if (hasStorm) {
							inProperWeather = true;
						} else {
							inProperWeather = false;
						}
					}
				}
				
				if (!"ALL".equals(data[8])) {
					String worldName = p.getLocation().getWorld().getName();
					for (String world : data[8].split(",")) {
						if (worldName.equals(world)) {
							inProperWorld = true;
							break;
						}
					}
				} else {
					inProperWorld = true;
				}
				String pInsideBiome = p.getLocation().getBlock().getBiome().toString();
				// ALL Y > -1000 -1 ALL GRASS
				for (String biome : data[0].split(",")) {
					if (biome.equals(pInsideBiome)) {
						inBiome = true;
						if ("ALL".equals(data[6])) {
							nearProperBlock = true;
							break;
						}
						
						if ("ABOVE".equals(data[5])) {
							Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
							for (String block : data[6].split(","))
								if (above == Material.getMaterial(block)) {
									nearProperBlock = true;
									break;
								}
							
						} else if ("MID".equals(data[5])) {
							Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
							Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
							for (String block : data[6].split(","))
								if (mid1 == Material.getMaterial(block) || mid2 == Material.getMaterial(block)) {
									nearProperBlock = true;
									break;
								}
							
						} else if ("BELOW".equals(data[5])) {
							Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
							
							for (String block : data[6].split(","))
								if (below == Material.getMaterial(block)) {
									nearProperBlock = true;
									break;
								}
						} else if ("VAMPIRE".equals(data[5])) {
							Location playersLocation = p.getLocation();
							boolean applyEffect = true;
							for (int i = p.getLocation().getBlockY(); i < 266; i++) {
								if (playersLocation.add(0, 1, 0).getBlock().getType().isSolid()) {
									applyEffect = false;
									break;
								}
							}
							
							if (applyEffect) {
								nearProperBlock = true;
							}
							
						} else if ("AROUND".equals(data[5])) {
							Location playersLocation = p.getLocation();
							int blockX = playersLocation.getBlockX();
							int blockY = playersLocation.getBlockY();
							int blockZ = playersLocation.getBlockZ();
							outerloop: for (int pX = blockX - 1; pX != blockX + 2; pX++) {
								for (int pY = blockY - 1; pY != blockY + 4; pY++) {
									for (int pZ = blockZ - 1; pZ != blockZ + 2; pZ++) {
										for (String block : data[6].split(",")) {
											Material mat = Material.getMaterial(block);
											if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ).getBlock()
											        .getType()) {
												nearProperBlock = true;
												break outerloop;
											}
										}
										
									}
								}
							}
						} else if (data[5].startsWith("CUSTOM")) {
							Location playersLocation = p.getLocation();
							int blockX = playersLocation.getBlockX();
							int blockY = playersLocation.getBlockY();
							int blockZ = playersLocation.getBlockZ();
							
							int searchAmount = Integer.parseInt(data[5].split("M")[1]);
							outerloop: for (int pX = blockX - searchAmount; pX != blockX + searchAmount; pX++) {
								for (int pY = blockY - searchAmount; pY != blockY + searchAmount; pY++) {
									for (int pZ = blockZ - searchAmount; pZ != blockZ + searchAmount; pZ++) {
										for (String block : data[6].split(",")) {
											Material mat = Material.getMaterial(block);
											if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ).getBlock().getType()) {
												nearProperBlock = true;
												break outerloop;
											}
										}
										
									}
								}
							}
						}else if (data[5].startsWith("LIGHTB")) {
							int lightLevel = p.getLocation().getBlock().getLightLevel();
							int limit = Integer.parseInt(data[5].split("B")[1]);
							if(lightLevel < limit) {
								nearProperBlock = true;

							} else {
								nearProperBlock = false;

							}
							
							
							
						}else if (data[5].startsWith("LIGHTA")) {
							int lightLevel = p.getLocation().getBlock().getLightLevel();
							int limit = Integer.parseInt(data[5].split("A")[1]);
							if(lightLevel > limit) {
								nearProperBlock = true;

							} else {
								nearProperBlock = false;

							}
							
							
							
						} else if ("ALL".equals(data[5])) {
							Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
							Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
							Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
							Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
							Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
							
							for (String block : data[6].split(",")) {
								Material mat = Material.getMaterial(block);
								if (below == mat || mid1 == mat || mid2 == mat || above == mat || below2 == mat) {
									nearProperBlock = true;
									break;
								}
							}
							
						}
						break;
					}
				}
				if ("ALL".equals(data[0])) {
					if (!"ALL".equals(data[6])) {
						if ("ABOVE".equals(data[5])) {
							Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
							for (String block : data[6].split(","))
								if (above == Material.getMaterial(block)) {
									nearProperBlock = true;
									break;
								}
							
						} else if ("MID".equals(data[5])) {
							Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
							Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
							for (String block : data[6].split(","))
								if (mid1 == Material.getMaterial(block) || mid2 == Material.getMaterial(block)) {
									nearProperBlock = true;
									break;
								}
							
						} else if ("BELOW".equals(data[5])) {
							Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
							Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
							
							for (String block : data[6].split(","))
								if (below == Material.getMaterial(block) || below2 == Material.getMaterial(block)) {
									nearProperBlock = true;
									break;
								}
						} else if ("VAMPIRE".equals(data[5])) {
							Location playersLocation = p.getLocation();
							boolean applyEffect = true;
							for (int i = p.getLocation().getBlockY(); i < 266; i++) {
								if (playersLocation.add(0, 1, 0).getBlock().getType().isSolid()) {
									applyEffect = false;
									break;
								}
							}
							
							if (applyEffect) {
								nearProperBlock = true;
							}
							
						} else if ("AROUND".equals(data[5])) {
							Location playersLocation = p.getLocation();
							int blockX = playersLocation.getBlockX();
							int blockY = playersLocation.getBlockY();
							int blockZ = playersLocation.getBlockZ();
							outerloop: for (int pX = blockX - 1; pX != blockX + 2; pX++) {
								for (int pY = blockY - 1; pY != blockY + 4; pY++) {
									for (int pZ = blockZ - 1; pZ != blockZ + 2; pZ++) {
										for (String block : data[6].split(",")) {
											Material mat = Material.getMaterial(block);
											if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ).getBlock()
											        .getType()) {
												nearProperBlock = true;
												break outerloop;
											}
										}
										
									}
								}
							}
						} else if (data[5].startsWith("CUSTOM")) {
							Location playersLocation = p.getLocation();
							int blockX = playersLocation.getBlockX();
							int blockY = playersLocation.getBlockY();
							int blockZ = playersLocation.getBlockZ();
							
							int searchAmount = Integer.parseInt(data[5].split("M")[1]);
							outerloop: for (int pX = blockX - searchAmount; pX != blockX + searchAmount; pX++) {
								for (int pY = blockY - searchAmount; pY != blockY + searchAmount; pY++) {
									for (int pZ = blockZ - searchAmount; pZ != blockZ + searchAmount; pZ++) {
										for (String block : data[6].split(",")) {
											Material mat = Material.getMaterial(block);
											if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ).getBlock().getType()) {
												nearProperBlock = true;
												break outerloop;
											}
										}
										
									}
								}
							}
						}else if (data[5].startsWith("LIGHTB")) {
							int lightLevel = p.getLocation().getBlock().getLightLevel();
							int limit = Integer.parseInt(data[5].split("B")[1]);
							if(lightLevel < limit) {
								nearProperBlock = true;

							} else {
								nearProperBlock = false;

							}
							
							
							
						}else if (data[5].startsWith("LIGHTA")) {
							int lightLevel = p.getLocation().getBlock().getLightLevel();
							int limit = Integer.parseInt(data[5].split("A")[1]);
							if(lightLevel > limit) {
								nearProperBlock = true;

							} else {
								nearProperBlock = false;

							}
							
							
							
						}else if ("ALL".equals(data[5])) {
							Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
							Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
							Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
							Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
							Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
							
							for (String block : data[6].split(",")) {
								Material mat = Material.getMaterial(block);
								if (below == mat || mid1 == mat || mid2 == mat || above == mat || below2 == mat) {
									nearProperBlock = true;
									break;
								}
							}
							
						}
					} else {
						nearProperBlock = true;
						
					}
				}
				int elseAmp = Integer.parseInt(data[4]);
				if (effe != null) {
					if (p.hasPotionEffect(effe)) {
						if (p.getPotionEffect(effe).getDuration() < 99999) {
							int potionAmp = p.getPotionEffect(effe).getAmplifier();
							if (data[0].equals("ALL") && !nearProperBlock && ((lowerThan || greaterThan)
							        || (greaterThanY || lowerThanY
							                && potionAmp != elseAmp))) {
								p.removePotionEffect(effe);
								
							} else if (data[0].equals("ALL") && nearProperBlock && ((lowerThan || greaterThan)
							        || (greaterThanY || lowerThanY && potionAmp != elseAmp))) {
								p.removePotionEffect(effe);
							} else if (inBiome && nearProperBlock && ((lowerThan || greaterThan) || (greaterThanY
							        || lowerThanY && potionAmp != elseAmp))) {
								p.removePotionEffect(effe);
								
							} else if (!inBiome && !data[0].equals("ALL") || (inBiome && !nearProperBlock) || (data[0]
							        .equals("ALL") && nearProperBlock && potionAmp == elseAmp) || !inProperWeather
							        || !inProperWorld) {
								p.removePotionEffect(effe);
							}
							
						}
					}
					if (!p.hasPotionEffect(effe)) {
						if (data[0].equals("ALL") && nearProperBlock && inProperWorld && inProperWeather && (lowerThanY
						        && !signR || greaterThanY && signR)) {
							p.addPotionEffect(new PotionEffect(effe, 99999, plugin.race.get(
							        str).dayRacePassivePotionEffectsBase.get(x), false, false));
						} else if (inBiome && nearProperBlock && inProperWorld && inProperWeather && (lowerThanY
						        && !signR || greaterThanY && signR)) {
							p.addPotionEffect(new PotionEffect(effe, 99999, plugin.race.get(
							        str).dayRacePassivePotionEffectsBase.get(x), false, false));
						} else {
							if (!"-1".equals(data[4])) {
								p.addPotionEffect(new PotionEffect(effe, 99999, elseAmp, false, false));
								
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
				boolean inBiome = false;
				boolean nearProperBlock = false;
				boolean inProperWeather = false;
				boolean inProperWorld = false;
				
				if ("ALL".equals(data[7])) {
					inProperWeather = true;
				} else {
					boolean hasStorm = p.getLocation().getWorld().hasStorm();
					if ("FALSE".equals(data[7])) {
						if (!hasStorm) {
							inProperWeather = true;
						} else {
							inProperWeather = false;
						}
					} else {
						if (hasStorm) {
							inProperWeather = true;
						} else {
							inProperWeather = false;
						}
					}
				}
				
				if (!"ALL".equals(data[8])) {
					String worldName = p.getLocation().getWorld().getName();
					for (String world : data[8].split(",")) {
						if (worldName.equals(world)) {
							inProperWorld = true;
							break;
						}
					}
				} else {
					inProperWorld = true;
				}
				String pInsideBiome = p.getLocation().getBlock().getBiome().toString();
				for (String biome : data[0].split(",")) {
					if (biome.equals(pInsideBiome)) {
						inBiome = true;
						if ("ALL".equals(data[6])) {
							nearProperBlock = true;
							break;
						}
						
						if ("ABOVE".equals(data[5])) {
							Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
							for (String block : data[6].split(","))
								if (above == Material.getMaterial(block)) {
									nearProperBlock = true;
									break;
								}
							
						} else if ("MID".equals(data[5])) {
							Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
							Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
							for (String block : data[6].split(","))
								if (mid1 == Material.getMaterial(block) || mid2 == Material.getMaterial(block)) {
									nearProperBlock = true;
									break;
								}
							
						} else if ("BELOW".equals(data[5])) {
							Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
							
							for (String block : data[6].split(","))
								if (below == Material.getMaterial(block)) {
									nearProperBlock = true;
									break;
								}
						} else if ("VAMPIRE".equals(data[5])) {
							Location playersLocation = p.getLocation();
							boolean applyEffect = true;
							for (int i = p.getLocation().getBlockY(); i < 266; i++) {
								if (playersLocation.add(0, 1, 0).getBlock().getType().isSolid()) {
									applyEffect = false;
									break;
								}
							}
							
							if (applyEffect) {
								nearProperBlock = true;
							}
							
						} else if ("AROUND".equals(data[5])) {
							Location playersLocation = p.getLocation();
							int blockX = playersLocation.getBlockX();
							int blockY = playersLocation.getBlockY();
							int blockZ = playersLocation.getBlockZ();
							outerloop: for (int pX = blockX - 1; pX != blockX + 2; pX++) {
								for (int pY = blockY - 1; pY != blockY + 4; pY++) {
									for (int pZ = blockZ - 1; pZ != blockZ + 2; pZ++) {
										for (String block : data[6].split(",")) {
											Material mat = Material.getMaterial(block);
											if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ).getBlock()
											        .getType()) {
												nearProperBlock = true;
												break outerloop;
											}
										}
										
									}
								}
							}
						}else if (data[5].startsWith("LIGHTB")) {
							int lightLevel = p.getLocation().getBlock().getLightLevel();
							int limit = Integer.parseInt(data[5].split("B")[1]);
							if(lightLevel < limit) {
								nearProperBlock = true;
							} else {
								nearProperBlock = false;

							}
							
							
							
						}else if (data[5].startsWith("LIGHTA")) {
							int lightLevel = p.getLocation().getBlock().getLightLevel();
							int limit = Integer.parseInt(data[5].split("A")[1]);
							if(lightLevel > limit) {
								nearProperBlock = true;

							} else {
								nearProperBlock = false;

							}
							
							
							
						} else if (data[5].startsWith("CUSTOM")) {
							Location playersLocation = p.getLocation();
							int blockX = playersLocation.getBlockX();
							int blockY = playersLocation.getBlockY();
							int blockZ = playersLocation.getBlockZ();
							
							int searchAmount = Integer.parseInt(data[5].split("M")[1]);
							outerloop: for (int pX = blockX - searchAmount; pX != blockX + searchAmount; pX++) {
								for (int pY = blockY - searchAmount; pY != blockY + searchAmount; pY++) {
									for (int pZ = blockZ - searchAmount; pZ != blockZ + searchAmount; pZ++) {
										for (String block : data[6].split(",")) {
											Material mat = Material.getMaterial(block);
											if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ).getBlock().getType()) {
												nearProperBlock = true;
												break outerloop;
											}
										}
										
									}
								}
							}
						} else if ("ALL".equals(data[5])) {
							Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
							Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
							Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
							Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
							Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
							
							for (String block : data[6].split(",")) {
								Material mat = Material.getMaterial(block);
								if (below == mat || mid1 == mat || mid2 == mat || above == mat || below2 == mat) {
									nearProperBlock = true;
									break;
								}
							}
							
						}
						break;
					}
				}
				if ("ALL".equals(data[0])) {
					if (!"ALL".equals(data[6])) {
						if ("ABOVE".equals(data[5])) {
							Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
							for (String block : data[6].split(","))
								if (above == Material.getMaterial(block)) {
									nearProperBlock = true;
									break;
								}
							
						} else if ("MID".equals(data[5])) {
							Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
							Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
							for (String block : data[6].split(","))
								if (mid1 == Material.getMaterial(block) || mid2 == Material.getMaterial(block)) {
									nearProperBlock = true;
									break;
								}
							
						} else if ("BELOW".equals(data[5])) {
							Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
							Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
							
							for (String block : data[6].split(","))
								if (below == Material.getMaterial(block) || below2 == Material.getMaterial(block)) {
									nearProperBlock = true;
									break;
								}
						} else if ("VAMPIRE".equals(data[5])) {
							Location playersLocation = p.getLocation();
							boolean applyEffect = true;
							for (int i = p.getLocation().getBlockY(); i < 266; i++) {
								if (playersLocation.add(0, 1, 0).getBlock().getType().isSolid()) {
									applyEffect = false;
									break;
								}
							}
							
							if (applyEffect) {
								nearProperBlock = true;
							}
							
						} else if ("AROUND".equals(data[5])) {
							Location playersLocation = p.getLocation();
							int blockX = playersLocation.getBlockX();
							int blockY = playersLocation.getBlockY();
							int blockZ = playersLocation.getBlockZ();
							outerloop: for (int pX = blockX - 1; pX != blockX + 2; pX++) {
								for (int pY = blockY - 1; pY != blockY + 4; pY++) {
									for (int pZ = blockZ - 1; pZ != blockZ + 2; pZ++) {
										for (String block : data[6].split(",")) {
											Material mat = Material.getMaterial(block);
											if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ).getBlock()
											        .getType()) {
												nearProperBlock = true;
												break outerloop;
											}
										}
										
									}
								}
							}
						} else if (data[5].startsWith("CUSTOM")) {
							Location playersLocation = p.getLocation();
							int blockX = playersLocation.getBlockX();
							int blockY = playersLocation.getBlockY();
							int blockZ = playersLocation.getBlockZ();
							
							int searchAmount = Integer.parseInt(data[5].split("M")[1]);
							outerloop: for (int pX = blockX - searchAmount; pX != blockX + searchAmount; pX++) {
								for (int pY = blockY - searchAmount; pY != blockY + searchAmount; pY++) {
									for (int pZ = blockZ - searchAmount; pZ != blockZ + searchAmount; pZ++) {
										for (String block : data[6].split(",")) {
											Material mat = Material.getMaterial(block);
											if (mat == new Location(playersLocation.getWorld(), pX, pY, pZ).getBlock().getType()) {
												nearProperBlock = true;
												break outerloop;
											}
										}
										
									}
								}
							}
						}else if (data[5].startsWith("LIGHTB")) {
							int lightLevel = p.getLocation().getBlock().getLightLevel();
							int limit = Integer.parseInt(data[5].split("B")[1]);
							if(lightLevel < limit) {
								nearProperBlock = true;

							} else {
								nearProperBlock = false;

							}
							
							
							
						}else if (data[5].startsWith("LIGHTA")) {
							int lightLevel = p.getLocation().getBlock().getLightLevel();
							int limit = Integer.parseInt(data[5].split("A")[1]);
							if(lightLevel > limit) {
								nearProperBlock = true;
							} else {
								nearProperBlock = false;

							}
							
							
							
						} else if ("ALL".equals(data[5])) {
							Material below = p.getLocation().add(0, -0.5, 0).getBlock().getType();
							Material mid1 = p.getLocation().add(0, 0.1, 0).getBlock().getType();
							Material mid2 = p.getLocation().add(0, 1.2, 0).getBlock().getType();
							Material above = p.getLocation().add(0, 2.1, 0).getBlock().getType();
							Material below2 = p.getLocation().add(0, -1.5, 0).getBlock().getType();
							for (String block : data[6].split(",")) {
								Material mat = Material.getMaterial(block);
								if (below == mat || mid1 == mat || mid2 == mat || above == mat || below2 == mat) {
									nearProperBlock = true;
									break;
								}
							}
							
						}
					} else {
						nearProperBlock = true;
						
					}
				}
				if (att != null) {
					addedAmount = plugin.dataManager.getPlayerLevel(p) * plugin.race.get(
					        str).dayRacePassiveAttributesLevel.get(x);
					if (data[0].equals("ALL") && nearProperBlock && inProperWorld && inProperWeather && (lowerThanY
					        && !signR || greaterThanY && signR)) {
						p.getAttribute(att).setBaseValue(plugin.race.get(str).dayRacePassiveAttributesAmount.get(x)
						        + addedAmount);
					} else if (inBiome && nearProperBlock && inProperWorld && inProperWeather && (lowerThanY && !signR|| greaterThanY && signR)) {
						p.getAttribute(att).setBaseValue(plugin.race.get(str).dayRacePassiveAttributesAmount.get(x)
						        + addedAmount);
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
