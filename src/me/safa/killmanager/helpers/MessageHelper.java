package me.safa.killmanager.helpers;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.safa.killmanager.KillManager;
import me.safa.killmanager.files.SetupConfig;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageHelper {

	SetupConfig config;

	public MessageHelper() {
		this.config = new SetupConfig();
		loadMessage();
	}

	public String PREFIX, NO_PERMS, NO_CONSOLE, CONFIG_RELOAD;

	public void sendMessage(CommandSender sender, String str) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			player.sendMessage(papiVariable(player, str));
		} else {
			sender.sendMessage(colorFormat(str));
		}
	}

	public String colorFormat(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	public String papiVariable(Player player, String str) {
		if (KillManager.getInstance().papiFound) {
			return PlaceholderAPI.setPlaceholders(player, localVariable(player, colorFormat(str)));
		} else {
			return localVariable(player, str);
		}
	}

	public String localVariable(Player player, String str) {
		return colorFormat(str).replaceAll("%prefix%", PREFIX).replaceAll("%player%", player.getName());
	}

	public String uppercaseFirst(String str) {
		String firstLetter = str.substring(0, 1);
		String result = firstLetter + str.toLowerCase().subSequence(1, str.length()).toString();

		return result;
	}

	public void playSound(Player player, Sound sound) {
		player.playSound(player.getLocation(), sound, 1, 1);
	}

	public void sendJsonMessage(Player player, String visualCommand, ClickEvent.Action clickActions,
			String actualCommand, String hoverMessage) {

		TextComponent clickablemsg = new TextComponent(papiVariable(player, visualCommand));

		clickablemsg.setClickEvent(new ClickEvent(clickActions, actualCommand));

		clickablemsg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(papiVariable(player, hoverMessage)).create()));

		player.spigot().sendMessage(clickablemsg);
	}

	public String numberFormat(int value) {
		String str = String.format("%,d", value);
		return str;
	}

	public boolean isNumeric(String str) {
		try {
			@SuppressWarnings("unused")
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	// Loads all the public stirngs above!
	private void loadMessage() {
		this.PREFIX = config.getString("Prefix");

	}

}
