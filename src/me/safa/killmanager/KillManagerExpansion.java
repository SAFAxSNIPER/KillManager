package me.safa.killmanager;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.safa.killmanager.helpers.StatsHelper;
import me.safa.killmanager.utils.DataType;

public class KillManagerExpansion extends PlaceholderExpansion {

	KillManager plugin;
	StatsHelper stsHelper;

	public KillManagerExpansion() {
		this.plugin = KillManager.getInstance();
		this.stsHelper = new StatsHelper();
	}

	@Override
	public String onPlaceholderRequest(Player player, String placeholder) {
		if (player == null || placeholder == null) {
			return "";
		}

		UUID uuid = player.getUniqueId();

		if (placeholder.equals("kills")) {
			return String.valueOf(stsHelper.getData(uuid, DataType.KILLS));
		}
		if (placeholder.equals("deaths")) {
			return String.valueOf(stsHelper.getData(uuid, DataType.KILLS));
		}
		if (placeholder.equals("streaks")) {
			return String.valueOf(stsHelper.getData(uuid, DataType.STREAKS));
		}

		if (placeholder.equals("beststreaks")) {
			return String.valueOf(stsHelper.getData(uuid, DataType.KILLS));
		}

		if (placeholder.equals("kdr")) {
			return stsHelper.getKdr(uuid);
		}

		if (placeholder.equals("coins")) {
			return String.valueOf(stsHelper.getData(uuid, DataType.COINS));
		}

		if (placeholder.equals("level")) {
			return String.valueOf(stsHelper.getData(uuid, DataType.LEVEL));
		}

		return null;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public String getAuthor() {
		return "SAFAxSNIPER";
	}

	@Override
	public String getIdentifier() {
		return "killmanager";
	}

	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}

}