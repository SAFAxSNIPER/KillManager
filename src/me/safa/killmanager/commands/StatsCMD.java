package me.safa.killmanager.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.safa.killmanager.KillManager;
import me.safa.killmanager.files.SetupConfig;
import me.safa.killmanager.files.SetupData;
import me.safa.killmanager.helpers.MessageHelper;

public class StatsCMD implements CommandExecutor {

	KillManager plugin;
	SetupData data;
	SetupConfig config;
	MessageHelper msgHelper;

	public StatsCMD() {
		this.plugin = KillManager.getInstance();
		this.data = new SetupData();
		this.config = new SetupConfig();
		this.msgHelper = new MessageHelper();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			Bukkit.getConsoleSender().sendMessage(msgHelper.NO_CONSOLE);
			return true;
		}

		Player p = (Player) sender;
		if (args.length == 0) {
			for (String messages : config.getStringList("Stats.Player")) {
				p.sendMessage(msgHelper.papiVariable(p, messages));
			}
		}
		if (args.length > 0) {
			Player t = Bukkit.getPlayer(args[0]);
			if (t == null) {
				p.sendMessage(msgHelper.PREFIX + config.getString("PlayerNotFound").replaceAll("%player%", p.getName())
						.replaceAll("%target%", args[0]));
				return true;
			}
			for (String messages : config.getStringList("Stats.Target")) {
				p.sendMessage(msgHelper.papiVariable(t, messages));
			}
		}
		return true;
	}

}
