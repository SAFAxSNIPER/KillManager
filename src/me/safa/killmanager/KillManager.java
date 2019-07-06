package me.safa.killmanager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.safa.killmanager.commands.LeaderboardCMD;
import me.safa.killmanager.commands.LevelCMD;
import me.safa.killmanager.commands.MainCMD;
import me.safa.killmanager.events.BowDamage;
import me.safa.killmanager.files.SetupConfig;
import me.safa.killmanager.files.SetupData;
import me.safa.killmanager.helpers.MessageHelper;
import net.milkbowl.vault.economy.Economy;

public class KillManager extends JavaPlugin {

	private static KillManager instance;

	public static KillManager getInstance() {
		return instance;
	}

	public boolean papiFound;
	public boolean vaultFound;
	public boolean combatLogFound;

	private MessageHelper msgHelper;

	private Economy econ;

	public void onEnable() {
		long oldMs = System.currentTimeMillis();

		instance = this;

		loadFiles();

		this.msgHelper = new MessageHelper();

		this.papiFound = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
		this.vaultFound = Bukkit.getPluginManager().isPluginEnabled("Vault");
		this.combatLogFound = Bukkit.getPluginManager().isPluginEnabled("CombatLogX");

		registerEvents();

		Bukkit.getConsoleSender().sendMessage(msgHelper.colorFormat("&8&m-----&r &6&lKillManager &8&m-----"));
		Bukkit.getConsoleSender().sendMessage(msgHelper.colorFormat("&6Autor: SAFAxSNIPER"));

		if (vaultFound && setupEconomy() != false) {
			Bukkit.getConsoleSender().sendMessage(
					msgHelper.colorFormat("&6Hooked into &eVault&6, Registered to &e" + econ.getName() + "&6!"));
		} else {
			getLogger().warning("Vault or an economy plugin not found, Disabling...");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		if (papiFound) {
			Bukkit.getConsoleSender().sendMessage(msgHelper.colorFormat("&6Hooked into &ePlaceholderAPI"));
		}

		if (combatLogFound) {
			Bukkit.getConsoleSender().sendMessage(msgHelper.colorFormat("&6Hooked into &eCombatLogX"));
		}

		long newMs = System.currentTimeMillis() - oldMs;

		Bukkit.getConsoleSender().sendMessage(msgHelper.colorFormat("&6Enabled in " + newMs + "ms"));

		Bukkit.getConsoleSender().sendMessage(msgHelper.colorFormat("&6Version: " + getDescription().getVersion()));

		Bukkit.getConsoleSender().sendMessage(msgHelper.colorFormat("&8&m-------------------------"));

		new KillManagerExpansion().register();
	}

	@Override
	public void onDisable() {
		long oldMs = System.currentTimeMillis();

		instance = null;

		Bukkit.getConsoleSender().sendMessage(msgHelper.colorFormat("&8&m-----&r &6&lKillManager &8&m-----"));
		Bukkit.getConsoleSender().sendMessage(msgHelper.colorFormat("&6Autor: SAFAxSNIPER"));

		long newMs = System.currentTimeMillis() - oldMs;

		Bukkit.getConsoleSender().sendMessage(msgHelper.colorFormat("&6Disabled in " + newMs + "ms"));

		Bukkit.getConsoleSender().sendMessage(msgHelper.colorFormat("&6Version: " + getDescription().getVersion()));

		Bukkit.getConsoleSender().sendMessage(msgHelper.colorFormat("&8&m-------------------------"));
	}

	private void registerEvents() {
		getServer().getPluginManager().registerEvents(new BowDamage(), this);

		getCommand("lvl").setExecutor(new LevelCMD());

		getCommand("leaderboard").setExecutor(new LeaderboardCMD());

		getCommand("leaderboard").setExecutor(new LeaderboardCMD());
		getCommand("killmanager").setExecutor(new MainCMD());
		getCommand("killmanager").setTabCompleter(new MainCMD());
	}

	private void loadFiles() {
		new SetupConfig();
		new SetupData();
	}

	private boolean setupEconomy() {
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
			return false;
		}

		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public Economy getEcononomy() {
		return econ;
	}

}
