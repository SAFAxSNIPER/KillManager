package me.safa.killmanager.helpers;

import java.util.UUID;

import org.bukkit.ChatColor;

import me.safa.killmanager.utils.DataType;

public class LevelHelpers {

	StatsHelper stsHelper;
	private int amount = 128;

	public LevelHelpers() {
		this.stsHelper = new StatsHelper();
	}

	public String getProgressBar(UUID uuid) {
		int current = stsHelper.getData(uuid, DataType.XP);
		int max = stsHelper.getData(uuid, DataType.LEVEL) * amount;
		int totalBars = 10;

		char symbol = '\u25CF';
		ChatColor completedColor = ChatColor.GREEN;
		ChatColor notCompletedColor = ChatColor.GRAY;

		float percent = (float) current / max;

		int progressBars = (int) ((int) totalBars * percent);

		int leftOver = (totalBars - progressBars);

		StringBuilder sb = new StringBuilder();
		sb.append(completedColor);
		for (int i = 0; i < progressBars; i++) {
			sb.append(symbol);
		}
		sb.append(notCompletedColor);
		for (int i = 0; i < leftOver; i++) {
			sb.append(symbol);
		}
		return ChatColor.DARK_GRAY + "[" + sb.toString() + ChatColor.DARK_GRAY + "]";
	}

	public int getRequiredXP(UUID uuid) {
		int required = (stsHelper.getData(uuid, DataType.LEVEL) * amount) - stsHelper.getData(uuid, DataType.XP);

		return required;
	}

	public int getNextLevelXP(UUID uuid) {
		return stsHelper.getData(uuid, DataType.LEVEL) * amount;
	}

	public int getAmount(UUID uuid) {
		return amount;
	}

}
