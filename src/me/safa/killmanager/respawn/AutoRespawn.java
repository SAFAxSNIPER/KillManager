package me.safa.killmanager.respawn;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

import me.safa.killmanager.KillManager;
import me.safa.killmanager.files.SetupConfig;

public class AutoRespawn extends Event {

	SetupConfig config;
	KillManager plugin;

	public AutoRespawn() {
		this.plugin = KillManager.getInstance();
		this.config = new SetupConfig();
	}

	private Player player;
	private Location deathLoc;
	private Location respawnLoc;

	public AutoRespawn(Player player, Location deathLoc, Location respawnLoc) {
		this.player = player;
		this.deathLoc = deathLoc;
		this.respawnLoc = respawnLoc;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Location getDeathLocation() {
		return this.deathLoc;
	}

	public Location getRespawnLocation() {
		return this.respawnLoc;
	}

	public EntityDamageEvent.DamageCause getDeathCause() {
		return this.player.getLastDamageCause().getCause();
	}

	public boolean killedByPlayer() {
		if ((this.player.getLastDamageCause().getEntity() instanceof Player)) {
			return true;
		}
		if ((this.player.getLastDamageCause().getEntity() instanceof Projectile)) {
			Projectile a = (Projectile) this.player.getLastDamageCause().getEntity();
			if ((a.getShooter() instanceof Player)) {
				return true;
			}
			return false;
		}
		return false;
	}

	public Player getKiller() {
		return this.player.getKiller();
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
