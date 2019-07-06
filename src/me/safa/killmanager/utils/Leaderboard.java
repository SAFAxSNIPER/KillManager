package me.safa.killmanager.utils;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;

import me.safa.killmanager.KillManager;
import me.safa.killmanager.files.SetupData;
import me.safa.killmanager.helpers.MessageHelper;

public class Leaderboard {

	// Needs to be recoded!

	KillManager plugin;
	SetupData data;
	MessageHelper msgHelper;

	public Leaderboard() {
		this.plugin = KillManager.getInstance();
		this.data = new SetupData();
		this.msgHelper = new MessageHelper();
	}

	public String[] getLeaderboard(DataType type, int limit) {

		String[] leaderBoard = new String[limit];
		AtomicInteger counter = new AtomicInteger(0);

		data.getConfigurationSection("Players").getValues(false).entrySet().stream().sorted((a1, a2) -> {
			int kills1 = ((MemorySection) a1.getValue()).getInt(type.toString());
			int kills2 = ((MemorySection) a2.getValue()).getInt(type.toString());
			return kills2 - kills1;
		}).limit(limit) // Limit the number of 'results'
				.forEach(f -> {
					int kills = ((MemorySection) f.getValue()).getInt(type.toString());
					leaderBoard[counter.get()] = f.getKey() + " " + kills;
					counter.set(counter.get() + 1);
				});

		return leaderBoard;
	}

	public ArrayList<String> getMessage(DataType type, String[] top) {

		String name = msgHelper.uppercaseFirst(type.toString());

		ArrayList<String> players = new ArrayList<>();
		for (String topList : top) {
			String playerName = Bukkit.getOfflinePlayer(UUID.fromString(topList.split(" ")[0])).getName();
			if (playerName != null) {
				players.add(playerName);
			}
		}

		ArrayList<Integer> values = new ArrayList<>();
		for (String topList : top) {
			values.add(Integer.parseInt(topList.split(" ")[1]));
		}

		ArrayList<String> list = new ArrayList<>();

		list.add(msgHelper.colorFormat("&8&m-----&r &6&l" + "Top 5 " + name + " Leaderboard" + " &8&m-----"));

		list.add(msgHelper.colorFormat("&71. &e" + players.get(0) + " &7- &e" + values.get(0) + " &7" + name));

		list.add(msgHelper.colorFormat("&72. &e" + players.get(1) + " &7- &e" + values.get(1) + " &7" + name));

		list.add(msgHelper.colorFormat("&73. &e" + players.get(2) + " &7- &e" + values.get(2) + " &7" + name));

		list.add(msgHelper.colorFormat("&74. &e" + players.get(3) + " &7- &e" + values.get(3) + " &7" + name));

		list.add(msgHelper.colorFormat("&75. &e" + players.get(4) + " &7- &e" + values.get(4) + " &7" + name));

		list.add(msgHelper.colorFormat("&8&m---------------------------------"));

		return list;
	}

}
