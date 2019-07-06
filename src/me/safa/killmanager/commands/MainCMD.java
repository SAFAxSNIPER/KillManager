package me.safa.killmanager.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.safa.killmanager.KillManager;
import me.safa.killmanager.files.SetupConfig;
import me.safa.killmanager.files.SetupData;
import me.safa.killmanager.helpers.MessageHelper;
import me.safa.killmanager.helpers.StatsHelper;
import me.safa.killmanager.utils.DataType;
import net.md_5.bungee.api.chat.ClickEvent;

public class MainCMD implements CommandExecutor, TabCompleter {

	KillManager plugin;
	SetupConfig config;
	SetupData data;
	MessageHelper msgHelper;
	StatsHelper stsHelper;

	public MainCMD() {
		this.plugin = KillManager.getInstance();
		this.config = new SetupConfig();
		this.data = new SetupData();
		this.msgHelper = new MessageHelper();
		this.stsHelper = new StatsHelper();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// km <Action> <Type> <Player> <Value>

		if (!sender.hasPermission("killmanager.admin")) {
			msgHelper.sendMessage(sender, msgHelper.PREFIX + msgHelper.NO_PERMS);
			return true;
		}

		if (args.length == 0) {
			helpMenu(sender);
			return true;
		}

		if (args.length < 4) {
			if (args[0].equalsIgnoreCase("reload")) {
				this.config.load();
				this.data.load();
				msgHelper.sendMessage(sender, msgHelper.PREFIX + msgHelper.CONFIG_RELOAD);
				return true;
			} else {
				helpMenu(sender);
				return true;
			}
		}

		String action = args[0];

		if (!action.equalsIgnoreCase("reload") && !action.equalsIgnoreCase("set") && !action.equalsIgnoreCase("add")
				&& !action.equalsIgnoreCase("take")) {
			msgHelper.sendMessage(sender, msgHelper.PREFIX + "&cValid actions are: 'set, add, take'");
			return true;
		}

		if (DataType.getByName(args[1]) == null || DataType.getByName(args[1]) == DataType.KDR
				|| DataType.getByName(args[1]) == DataType.XP) {
			String types = "";
			for (DataType dataType : DataType.values()) {
				if (dataType.equals(DataType.KDR))
					continue;
				String name = msgHelper.uppercaseFirst(dataType.toString());
				types = types + "&e" + name + "&c, ";
			}

			types = types.substring(0, types.length() - 1);

			msgHelper.sendMessage(sender, msgHelper.PREFIX + "&cValid types are " + types);
			return true;
		}

		if (Bukkit.getPlayer(args[2]) == null) {
			msgHelper.sendMessage(sender, msgHelper.PREFIX + "&cCouldn't find the player &e" + args[2]);
			return true;
		}

		if (!msgHelper.isNumeric(args[3])) {
			msgHelper.sendMessage(sender, msgHelper.PREFIX + "&cCouldn't find the value &e" + args[3]);
			return true;
		}

		String actions = args[0];
		DataType type = DataType.getByName(args[1]);
		String typeName = msgHelper.uppercaseFirst(type.toString());
		Player target = Bukkit.getPlayer(args[2]);
		UUID uuid = target.getUniqueId();
		int value = Integer.parseInt(args[3]);

		if (actions.equalsIgnoreCase("add")) {
			stsHelper.giveData(uuid, type, value);
			msgHelper.sendMessage(target,
					msgHelper.PREFIX + "&6Added &e" + value + " " + typeName + " &6to your stats!");
			msgHelper.sendMessage(sender, msgHelper.PREFIX + "&6Added &e" + value + " " + typeName + " &6to &e"
					+ target.getName() + "'s&6 stats!");
			return true;
		}

		if (actions.equalsIgnoreCase("take")) {
			stsHelper.takeData(uuid, type, value);
			msgHelper.sendMessage(target,
					msgHelper.PREFIX + "&6Took &e" + value + " " + typeName + " &6from your stats!");
			msgHelper.sendMessage(sender, msgHelper.PREFIX + "&6Took &e" + value + " " + typeName + "&6 from &e"
					+ target.getName() + "'s&6 stats!");
			return true;
		}

		if (actions.equalsIgnoreCase("set")) {
			stsHelper.setData(uuid, type, value);
			msgHelper.sendMessage(target, msgHelper.PREFIX + "&6Set your &e" + typeName + "&6 stats to &e" + value);
			msgHelper.sendMessage(sender,
					msgHelper.PREFIX + "&6Set &e" + target.getName() + "'s " + typeName + " &6to &e" + value);
			return true;
		}

		return true;
	}

	private void helpMenu(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(msgHelper.colorFormat("&8&m----------&r &6&lKillManager &8&m----------"));

			sender.sendMessage(msgHelper.colorFormat(" &e/km <Action> <Type> <Player> <Value>"));
			sender.sendMessage(msgHelper.colorFormat(" &e/km reload"));

			sender.sendMessage(msgHelper.colorFormat("&8&m-------------------------------"));
			return;
		} else {
			Player player = (Player) sender;

			player.sendMessage(msgHelper.colorFormat("&8&m----------&r &6&lKillManager &8&m----------"));

			msgHelper.sendJsonMessage(player, "&e /km <Action> <Type> <Player> <Value>",
					ClickEvent.Action.SUGGEST_COMMAND, "/km <Action> <Type> <Player> <Value>",
					"&aClick me to suggest the command into your chat!");
			msgHelper.sendJsonMessage(player, "&e /km reload", ClickEvent.Action.SUGGEST_COMMAND, "/km reload",
					"&aClick me to suggest the command into your chat!");

			player.sendMessage(msgHelper.colorFormat("&8&m-------------------------------"));
			return;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> actions = new ArrayList<>();
		actions.add("set");
		actions.add("add");
		actions.add("take");

		List<String> types = new ArrayList<>();
		types.add("kills");
		types.add("deaths");
		types.add("streaks");
		types.add("best_streak");
		types.add("coins");

		if (args.length == 1) {
			List<String> subCommands = actions;
			if (!args[0].equals("")) {
				for (String commandName : subCommands.stream().collect(Collectors.toList())) {
					if (!commandName.startsWith(args[0].toLowerCase())) {
						continue;
					}
					subCommands.clear();
					subCommands.add(commandName);
				}
			} else {
				subCommands = actions;
			}
			return subCommands;
		}

		if (args.length == 2) {
			List<String> subCommands = types;
			if (!args[1].equals("")) {
				for (String commandName : subCommands.stream().collect(Collectors.toList())) {
					if (!commandName.startsWith(args[1].toLowerCase())) {
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
