package me.safa.killmanager.helpers;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.safa.killmanager.KillManager;
import me.safa.killmanager.utils.Actionbar;
import me.safa.killmanager.utils.DataType;
import me.safa.killmanager.utils.Title;

public class DeathMethods {

	KillManager plugin;
	MessageHelper msgHelper;
	StatsHelper stsHelper;
	Actionbar actionbar;
	Title title;

	public DeathMethods() {
		this.stsHelper = new StatsHelper();
		this.plugin = KillManager.getInstance();
		this.msgHelper = new MessageHelper();
	}

	public void updateStats(UUID victim, UUID killer) {
		// Victim Stats update!
		stsHelper.giveData(victim, DataType.DEATHS, 1);
		stsHelper.setData(victim, DataType.STREAKS, 0);

		// Killer Stats update!
		stsHelper.giveData(killer, DataType.KILLS, 1);
		stsHelper.giveData(killer, DataType.STREAKS, 1);

		if (stsHelper.getData(killer, DataType.STREAKS) > stsHelper.getData(killer, DataType.BEST_STREAK)) {
			stsHelper.setData(killer, DataType.BEST_STREAK, stsHelper.getData(killer, DataType.STREAKS));
		}
	}

	public void runAction(Player player, String actions) {
		if (actions.startsWith("[console] ")) {
			if (actions.contains("<delay=")) {
				if (actions.endsWith(">")) {
					Pattern pattern = Pattern.compile("<(.*?)>");

					Matcher matcher = pattern.matcher(actions);

					if (matcher.find()) {
						String text = matcher.group(1).replaceAll("<", "").replaceAll(">", "");

						@SuppressWarnings("unused")
						String delay = text.split("=")[0];
						String value = text.split("=")[1];
						if (msgHelper.isNumeric(value)) {
							int i = Integer.parseInt(value);

							new BukkitRunnable() {
								@Override
								public void run() {
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
											msgHelper.papiVariable(player, actions.replaceFirst("\\[console\\] ", "")
													.replaceFirst("<delay=" + i + ">", "")));
								}
							}.runTaskLater(plugin, i);
						}
					}
				}
				return;
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
					msgHelper.papiVariable(player, actions.replaceFirst("\\[console\\] ", "")));
		}
		if (actions.startsWith("[player] ")) {
			if (actions.contains("<delay=")) {
				if (actions.endsWith(">")) {
					Pattern pattern = Pattern.compile("<(.*?)>");

					Matcher matcher = pattern.matcher(actions);

					if (matcher.find()) {
						String text = matcher.group(1).replaceAll("<", "").replaceAll(">", "");

						@SuppressWarnings("unused")
						String delay = text.split("=")[0];
						String value = text.split("=")[1];
						if (msgHelper.isNumeric(value)) {
							int i = Integer.parseInt(value);

							new BukkitRunnable() {

								@Override
								public void run() {
									player.performCommand(actions.replaceFirst("\\[player\\] ", "")
											.replaceFirst("<delay=" + i + ">", ""));
								}
							}.runTaskLater(plugin, i);
						}
					}
				}
				return;
			}
			player.performCommand(msgHelper.papiVariable(player, actions.replaceFirst("\\[player\\] ", "")));
		}

		if (actions.startsWith("[msg] ")) {
			if (actions.contains("<delay=")) {
				if (actions.endsWith(">")) {
					Pattern pattern = Pattern.compile("<(.*?)>");

					Matcher matcher = pattern.matcher(actions);

					if (matcher.find()) {
						String text = matcher.group(1).replaceAll("<", "").replaceAll(">", "");

						@SuppressWarnings("unused")
						String delay = text.split("=")[0];
						String value = text.split("=")[1];
						if (msgHelper.isNumeric(value)) {
							int i = Integer.parseInt(value);

							new BukkitRunnable() {
								@Override
								public void run() {
									player.sendMessage(msgHelper.papiVariable(player, actions
											.replaceFirst("\\[msg\\] ", "").replaceFirst("<delay=" + i + ">", "")));
								}
							}.runTaskLater(plugin, i);
						}
					}
				}
				return;
			}
			player.sendMessage(msgHelper.papiVariable(player, actions.replaceFirst("\\[msg\\] ", "")));
		}
		if (actions.startsWith("[title] ")) {
			if (actions.contains("<delay=")) {
				if (actions.endsWith(">")) {
					Pattern pattern = Pattern.compile("<(.*?)>");

					Matcher matcher = pattern.matcher(actions);

					if (matcher.find()) {
						String text = matcher.group(1).replaceAll("<", "").replaceAll(">", "");

						@SuppressWarnings("unused")
						String delay = text.split("=")[0];
						String value = text.split("=")[1];
						if (msgHelper.isNumeric(value)) {
							int i = Integer.parseInt(value);

							new BukkitRunnable() {
								@Override
								public void run() {
									Title t = new Title();
									String[] title = actions.replaceFirst("\\[title\\] ", "")
											.replaceFirst("<delay=" + i + ">", "").split("%newline%");
									t.sendTitle(player, 20, 20, 50, msgHelper.papiVariable(player, title[0]),
											msgHelper.papiVariable(player, title[1]));
								}
							}.runTaskLater(plugin, i);
						}
					}
				}
				return;
			}
			Title t = new Title();
			String[] title = actions.replaceFirst("\\[title\\] ", "").split("%newline%");
			t.sendTitle(player, 20, 20, 50, msgHelper.papiVariable(player, title[0]),
					msgHelper.papiVariable(player, title[1]));

		}
		if (actions.startsWith("[actionbar] ")) {
			if (actions.contains("<delay=")) {
				if (actions.endsWith(">")) {
					Pattern pattern = Pattern.compile("<(.*?)>");

					Matcher matcher = pattern.matcher(actions);

					if (matcher.find()) {
						String text = matcher.group(1).replaceAll("<", "").replaceAll(">", "");

						@SuppressWarnings("unused")
						String delay = text.split("=")[0];
						String value = text.split("=")[1];
						if (msgHelper.isNumeric(value)) {
							int i = Integer.parseInt(value);

							new BukkitRunnable() {
								@Override
								public void run() {
									Actionbar b = new Actionbar();
									b.sendActionBar(player,
											msgHelper.papiVariable(player, actions.replaceFirst("\\[actionbar\\] ", "")
													.replaceFirst("<delay=" + i + ">", "")),
											60);
								}
							}.runTaskLater(plugin, i);
						}
					}
				}
				return;
			}
			Actionbar b = new Actionbar();
			b.sendActionBar(player, msgHelper.papiVariable(player, actions.replaceFirst("\\[actionbar\\] ", "")), 60);
		}
		if (actions.startsWith("[sound] ")) {
			if (actions.contains("<delay=")) {
				if (actions.endsWith(">")) {
					Pattern pattern = Pattern.compile("<(.*?)>");

					Matcher matcher = pattern.matcher(actions);

					if (matcher.find()) {
						String text = matcher.group(1).replaceAll("<", "").replaceAll(">", "");

						@SuppressWarnings("unused")
						String delay = text.split("=")[0];
						String value = text.split("=")[1];
						if (msgHelper.isNumeric(value)) {
							int i = Integer.parseInt(value);

							new BukkitRunnable() {
								@Override
								public void run() {
									msgHelper.playSound(player, Sound.valueOf(actions.replaceFirst("\\[sound\\] ", "")
											.replaceFirst("<delay=" + i + ">", "")));
								}
							}.runTaskLater(plugin, i);
						}
					}
				}
				return;
			}
			msgHelper.playSound(player, Sound.valueOf(actions.replaceFirst("\\[sound\\] ", "")));
		}
	}

}
