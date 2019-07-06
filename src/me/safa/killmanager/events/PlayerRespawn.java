package me.safa.killmanager.events;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.safa.killmanager.KillManager;
import me.safa.killmanager.files.SetupConfig;
import me.safa.killmanager.helpers.DeathMethods;
import me.safa.killmanager.respawn.AutoRespawn;

public class PlayerRespawn implements Listener {

	KillManager plugin;
	SetupConfig config;
	DeathMethods deathMethods;

	public PlayerRespawn() {
		this.plugin = KillManager.getInstance();
		this.config = new SetupConfig();
		this.deathMethods = new DeathMethods();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		final Location deathLoc = p.getLocation();
		if (config.getBoolean("AutoRespawn.Enabled") && p.hasPermission(config.getString("AutoRespawn.Permission"))) {
			List<String> worlds = config.getStringList("AutoRespawn.EnabledWorlds");
			for (String key : worlds) {
				if (key.equals(p.getWorld().getName())) {
					new BukkitRunnable() {
						@Override
						public void run() {
							p.spigot().respawn();
							Location respawnLoc = e.getEntity().getLocation();
							Bukkit.getPluginManager().callEvent(new AutoRespawn(e.getEntity(), deathLoc, respawnLoc));
						}
					}.runTaskLater(plugin, 0);
					for (String actions : config.getStringList("AutoRespawn.Actions")) {
						if (e.getEntity().getKiller() instanceof Player) {
							actions.replaceAll("%killer_name%", e.getEntity().getKiller().getName());
						}
						deathMethods.runAction(p, actions.replaceAll("%victim_name%", p.getName()));
					}
				}
			}

		}
	}
}
