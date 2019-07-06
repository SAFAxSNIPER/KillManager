package me.safa.killmanager.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.scheduler.BukkitRunnable;

import me.safa.killmanager.KillManager;
import me.safa.killmanager.files.SetupConfig;
import me.safa.killmanager.helpers.MessageHelper;
import me.safa.killmanager.utils.DataType;
import me.safa.killmanager.utils.Leaderboard;

public class LeaderboardCMD implements CommandExecutor, TabCompleter {

	KillManager plugin;
	SetupConfig config;
	MessageHelper msgHelper;
	Leaderboard leaderBoard;

	public LeaderboardCMD() {
		this.plugin = KillManager.getInstance();
		this.config = new SetupConfig();
		this.msgHelper = new MessageHelper();
		this.leaderBoard = new Leaderboard();
	}

	private HashSet<String> cooldown = new HashSet<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cooldown.contains(sender.getName())) {
			msgHelper.sendMessage(sender, msgHelper.PREFIX + "&cPlease wait a few seconds!");
			return true;
		}

		if (args.length == 0) {
			msgHelper.sendMessage(sender, msgHelper.PREFIX + "&cPlease type the stats name!\n" + getTypes());
			return true;
		}

		if (DataType.getByName(args[0]) == null || DataType.getByName(args[0]) == DataType.KDR
				|| DataType.getByName(args[0]) == DataType.XP) {
			msgHelper.sendMessage(sender, msgHelper.PREFIX + "&cValid types are " + getTypes());
			return true;
		}

		cooldown.add(sender.getName());

		new BukkitRunnable() {

			@Override
			public void run() {
				if (cooldown.contains(sender.getName())) {
					cooldown.remove(sender.getName());
				}
			}
		}.runTaskLater(plugin, 80L);

		DataType type = DataType.getByName(args[0]);
		String[] top = leaderBoard.getLeaderboard(type, 5);

		ArrayList<String> list = leaderBoard.getMessage(type, top);

		for (String msg : list) {
			sender.sendMessage(msg);
		}

		return true;
	}

	private String getTypes() {
		String types = "";
		for (DataType dataType : DataType.values()) {
			if (dataType.equals(DataType.KDR))
				continue;
			String name = msgHelper.uppercaseFirst(dataType.toString());
			types = types + "&e" + name + "&c, ";
		}

		types = types.substring(0, types.length() - 2);

		return types;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		List<String> types = new ArrayList<>();
		types.add("kills");
		types.add("deaths");
		types.add("streaks");
		types.add("best_streak");
		types.add("coins");

		if (args.length == 1) {
			List<String> subCommands = types;
			if (!args[0].equals("")) {
				for (String commandName : subCommands.stream().collect(Collectors.toList())) {
					if (!commandName.startsWith(args[0].toLowerCase())) {
						continue;
					}
					subCommands.clear();
					subCommands.add(commandName);
				}
			} else {
				subCommands = types;
			}
			return subCommands;
		}
		return null;
	}

}
