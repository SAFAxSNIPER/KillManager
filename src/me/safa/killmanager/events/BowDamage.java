package me.safa.killmanager.events;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.safa.killmanager.KillManager;
import me.safa.killmanager.files.SetupConfig;
import me.safa.killmanager.helpers.DeathMethods;

public class BowDamage implements Listener {

	KillManager plugin;
	SetupConfig config;
	DeathMethods deathMethods;

	public BowDamage() {
		this.plugin = KillManager.getInstance();
		this.config = new SetupConfig();
		this.deathMethods = new DeathMethods();
	}

	@EventHandler
	public void DamageEvent(EntityDamageByEntityEvent e) {
		if (config.getBoolean("BowHit.Enabled")) {
			if (e.getDamager() instanceof Arrow) {
				Arrow a = (Arrow) e.getDamager();
				if (a.getShooter() instanceof Player && e.getEntity() instanceof Player) {

					Player k = (Player) a.getShooter();
					Player v = (Player) e.getEntity();
					double damage = e.getFinalDamage();
					if (!v.isDead() && k.hasPermission(config.getString("BowHit.Permission"))) {
						int vh = Integer.valueOf((int) (v.getHealth() - damage));
						int kh = Integer.valueOf((int) (k.getHealth() - damage));
						if (vh > 0) {
							if (k.getName() != v.getName()) {
								for (String actions : config.getStringList("BowHit.BowShooter")) {
									deathMethods.runAction(k,
											actions.replaceAll("%victim_name%", v.getName())
													.replaceAll("%killer_name%", k.getName())
													.replaceAll("%victim_health%", String.valueOf(vh / 2.0D))
													.replaceAll("%killer_health%", String.valueOf(kh / 2.0D)));

								}
								return;
							}
						}
					}
				}
			}
		}
	}

}
