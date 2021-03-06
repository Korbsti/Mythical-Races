package me.korbsti.mythicalraces.events.lvl;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;

import me.korbsti.mythicalraces.MythicalRaces;
import me.korbsti.mythicalraces.race.Race;

public class Fishing implements Listener {
	
	MythicalRaces plugin;
	
	public Fishing(MythicalRaces plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onFish(PlayerFishEvent e) {
		Player p = e.getPlayer();
		Race ras = plugin.playersRace.get(p.getName());
		if (!ras.lvlType.contains("FISHING")) return;
		State state = e.getState();
		if (state == State.CAUGHT_FISH) {
			plugin.changeXP(p, ras.xpGain);
			plugin.checkLevelUp(ras, p);
		}
	}
	
}
