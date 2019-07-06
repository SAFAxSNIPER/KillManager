package me.safa.killmanager.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.safa.killmanager.KillManager;
import me.safa.killmanager.files.SetupConfig;
import me.safa.killmanager.helpers.DeathMethods;
import me.safa.killmanager.helpers.StatsHelper;
import me.safa.killmanager.utils.DataType;

public class PlayerDeath implements Listener {

	KillManager plugin;
	SetupConfig config;
	DeathMethods deathMethods;
	StatsHelper stsHelper;

	public PlayerDeath() {
		this.plugin = KillManager.getInstance();
		this.config = new SetupConfig();
		this.stsHelper = new StatsHelper();
		this.deathMethods = new DeathMethods();
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player victim = e.getEntity();

		if (!(victim.getKiller() instanceof Player) || victim.getKiller() == null) {
			return;
		}

		Player killer = e.getEntity().getKiller();

		UUID killerId = killer.getUniqueId();
		UUID victimId = victim.getUniqueId();

		// Updates the stats for the victim and the killer!
		deathMethods.updateStats(victimId, killerId);

		if (config.getBoolean("Kill.Exp.Enabled")) {
			e.setDroppedExp(0);
		}

		if (config.getBoolean("Kill.Message.Enabled")) {
			e.setDeathMessage(config.getString("Kill.Message.MSG").replaceAll("%victim_name%", victim.getName())
					.replaceAll("%killer_name%", killer.getName()));
		}

		// Streak methods
		for (String key : config.getConfigurationSection("KillStreaks").getKeys(false)) {
			if (stsHelper.getData(killer.getUniqueId(), DataType.STREAKS) == config
					.getInt("KillStreaks." + key + ".KillStreak")) {
				if (config.contains("KillStreaks." + key + ".Permission")) {
					if (killer.hasPermission(config.getString("KillStreaks." + key + ".Permission"))) {
						for (String actions : config.getStringList("KillStreaks." + key + ".Killer")) {
							deathMethods.runAction(killer, replaceStats(actions, victimId, killerId));
						}
						for (String actions : config.getStringList("KillStreaks." + key + ".Victim")) {
							deathMethods.runAction(victim, replaceStats(actions, victimId, killerId));
						}
					}
					continue;
				} else {
					for (String actions : config.getStringList("KillStreaks." + key + ".Killer")) {
						deathMethods.runAction(killer, replaceStats(actions, victimId, killerId));
					}
					for (String actions : config.getStringList("KillStreaks." + key + ".Victim")) {
						deathMethods.runAction(victim, replaceStats(actions, victimId, killerId));
					}
				}
			}
		}
	}

	private String replaceStats(String str, UUID victimId, UUID killerId) {
		Player victim = Bukkit.getPlayer(victimId);
		Player killer = Bukkit.getPlayer(killerId);

		// Victim Variables
		str = str.replaceAll("%victim_name%", victim.getName())
				.replaceAll("%victim_kills%", String.valueOf(stsHelper.getData(victimId, DataType.KILLS)))
				.replaceAll("%victim_deaths%", String.valueOf(stsHelper.getData(victimId, DataType.DEATHS)))
				.replaceAll("%victim_streaks%", String.valueOf(stsHelper.getData(victimId, DataType.STREAKS)))
				.replaceAll("%victim_beststreak%", String.valueOf(stsHelper.getData(victimId, DataType.BEST_STREAK)))
				.replaceAll("%victim_kdr%", String.valueOf(stsHelper.getData(victimId, DataType.KDR)))
				.replaceAll("%victim_coins%", String.valueOf(stsHelper.getData(victimId, DataType.COINS)));

		str = str.replaceAll("%killer_name%", killer.getName())
				.replaceAll("%killer_kills%", String.valueOf(stsHelper.getData(killerId, DataType.KILLS)))
				.replaceAll("%killer_deaths%", String.valueOf(stsHelper.getData(killerId, DataType.DEATHS)))
				.replaceAll("%killer_streaks%", String.valueOf(stsHelper.getData(killerId, DataType.STREAKS)))
				.replaceAll("%killer_beststreak%", String.valueOf(stsHelper.getData(killerId, DataType.BEST_STREAK)))
				.replaceAll("%killer_kdr%", String.valueOf(stsHelper.getData(killerId, DataType.KDR)))
				.replaceAll("%killer_coins%", String.valueOf(stsHelper.getData(killerId, DataType.COINS)));

		return str;
	}

}
