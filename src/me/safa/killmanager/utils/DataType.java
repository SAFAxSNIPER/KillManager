package me.safa.killmanager.utils;

public enum DataType {

	KILLS, DEATHS, STREAKS, BEST_STREAK, KDR, COINS, LEVEL, XP;

	public static DataType getByName(String str) {

		if (str.equalsIgnoreCase("kills") || str.equalsIgnoreCase("kill")) {
			return DataType.KILLS;
		}
		if (str.equalsIgnoreCase("deaths") || str.equalsIgnoreCase("death")) {
			return DataType.DEATHS;
		}
		if (str.equalsIgnoreCase("streak") || str.equalsIgnoreCase("streaks")) {
			return DataType.STREAKS;
		}
		if (str.equalsIgnoreCase("best_streak") || str.equalsIgnoreCase("best_streaks")) {
			return DataType.BEST_STREAK;
		}
		if (str.equalsIgnoreCase("kdr") || str.equalsIgnoreCase("kd")) {
			return DataType.KDR;
		}
		if (str.equalsIgnoreCase("coin") || str.equalsIgnoreCase("coins")) {
			return DataType.COINS;
		}

		if (str.equalsIgnoreCase("lvl") || str.equalsIgnoreCase("level")) {
			return DataType.LEVEL;
		}

		if (str.equalsIgnoreCase("exp") || str.equalsIgnoreCase("xp")) {
			return DataType.XP;
		}

		return null;
	}

}
