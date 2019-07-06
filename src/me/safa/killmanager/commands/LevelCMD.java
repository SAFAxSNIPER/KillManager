package me.safa.killmanager.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.safa.killmanager.KillManager;
import me.safa.killmanager.helpers.LevelHelpers;
import me.safa.killmanager.helpers.MessageHelper;

public class LevelCMD implements CommandExecutor {

	KillManager plugin;
	MessageHelper msgHelper;
	LevelHelpers lvlHelpers;

	public LevelCMD() {
		this.plugin = KillManager.getInstance();
		this.msgHelper = new MessageHelper();
		this.lvlHelpers = new LevelHelpers();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player))
			return true;

		@SuppressWarnings("unused")
		Player player = (Player) sender;

		return true;
	}

}
