package me.korbsti.mythicalraces.race;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.attribute.Attribute;
import org.bukkit.potion.PotionEffectType;

public class Race {
	
	public String raceName;
	
	// races passive potion effects and attributes day time
	public ArrayList<PotionEffectType> dayRacePassivePotionEffects = new ArrayList<PotionEffectType>();
	public ArrayList<Attribute> dayRacePassiveAttributes = new ArrayList<Attribute>();
	public ArrayList<Integer> dayRacePassivePotionEffectsBase = new ArrayList<Integer>();
	public ArrayList<Double> dayRacePassiveAttributesAmount = new ArrayList<Double>();
	public ArrayList<Double> dayRacePassiveAttributesLevel = new ArrayList<Double>();
	public ArrayList<String> dayRaceDataPotion = new ArrayList<String>();
	public ArrayList<String> dayRaceDataAttribute = new ArrayList<String>();

	// races passive potion effects and attributes night time
	public  ArrayList<PotionEffectType> nightRacePassivePotionEffects = new  ArrayList<PotionEffectType>();
	public  ArrayList<Attribute> nightRacePassiveAttributes = new  ArrayList<Attribute>();
	public  ArrayList<Integer> nightRacePassivePotionEffectsBase = new  ArrayList<Integer>();
	public  ArrayList<Double> nightRacePassiveAttributesAmount = new  ArrayList<Double>();
	public  ArrayList<Double> nightRacePassiveAttributesLevel = new  ArrayList<Double>();
	public  ArrayList<String> nightRaceDataPotion = new ArrayList<String>();
	public  ArrayList<String> nightRaceDataAttribute = new ArrayList<String>();

	// command executables
	public  ArrayList<String> raceCommandExecution = new ArrayList<String>();
	
	public Race(String raceName, ArrayList<PotionEffectType> dayRacePassivePotionEffects,
	        ArrayList<Attribute> dayRacePassiveAttributes,
	        ArrayList<Integer> dayRacePassivePotionEffectsBase,
	        ArrayList<Double> dayRacePassiveAttributesAmount,
	        ArrayList<Double> dayRacePassiveAttributesLevel,
	        ArrayList<String> dayRaceDataPotion,
	        ArrayList<String> dayRaceDataAttribute,
	        ArrayList<PotionEffectType> nightRacePassivePotionEffects,
	        ArrayList<Attribute> nightRacePassiveAttributes,
	        ArrayList<Integer> nightRacePassivePotionEffectsBase,
	        ArrayList<Double> nightRacePassiveAttributesAmount,
	        ArrayList<Double> nightRacePassiveAttributesLevel,
	        ArrayList<String> nightRaceDataPotion,
	        ArrayList<String> nightRaceDataAttribute,
	        ArrayList<String> raceCommandExecution
	
	) {
		
		this.raceName=raceName;
		this.dayRacePassivePotionEffects=dayRacePassivePotionEffects;
		this.dayRacePassiveAttributes=dayRacePassiveAttributes;
		this.dayRacePassivePotionEffectsBase=dayRacePassivePotionEffectsBase;
		this.dayRacePassiveAttributesAmount=dayRacePassiveAttributesAmount;
		this.dayRacePassiveAttributesLevel=dayRacePassiveAttributesLevel;
		this.dayRaceDataPotion=dayRaceDataPotion;
		this.dayRaceDataAttribute=dayRaceDataAttribute;		
		this.nightRacePassivePotionEffects=nightRacePassivePotionEffects;
		this.nightRacePassiveAttributes=nightRacePassiveAttributes;
		this.nightRacePassivePotionEffectsBase=nightRacePassivePotionEffectsBase;
		this.nightRacePassiveAttributesAmount=nightRacePassiveAttributesAmount;
		this.nightRacePassiveAttributesLevel=nightRacePassiveAttributesLevel;
		this.nightRaceDataPotion = nightRaceDataPotion;
		this.nightRaceDataAttribute = nightRaceDataAttribute;
		this.raceCommandExecution =raceCommandExecution;
		
		
		
	}
	
}
