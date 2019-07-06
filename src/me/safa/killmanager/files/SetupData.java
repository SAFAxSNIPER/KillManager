package me.safa.killmanager.files;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.safa.killmanager.KillManager;
import net.md_5.bungee.api.ChatColor;

public class SetupData {

	File file;
	YamlConfiguration ymlconfig;

	public SetupData() {
		this.file = new File(KillManager.getInstance().getDataFolder(), "data.yml");
		if (!this.file.exists()) {
			KillManager.getInstance().saveResource("data.yml", false);
		}
		this.ymlconfig = YamlConfiguration.loadConfiguration(this.file);
	}

	public void load() {
		this.file = new File(KillManager.getInstance().getDataFolder(), "data.yml");
		if (!this.file.exists()) {
			KillManager.getInstance().saveResource("data.yml", false);
		}
		this.ymlconfig = YamlConfiguration.loadConfiguration(this.file);
	}

	public YamlConfiguration getConfiguration() {
		return this.ymlconfig;
	}

	public File getFile() {
		return this.file;
	}

	public void saveFile() {
		try {
			this.ymlconfig.save(this.file);
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	public String getString(String str) {
		if (this.ymlconfig.contains(str)) {
			return ChatColor.translateAlternateColorCodes('&', this.ymlconfig.getString(str));
		}
		return ChatColor.translateAlternateColorCodes('&', "&cString: " + str + " doesnt exist!");
	}

	public int getInt(String str) {
		if (this.ymlconfig.contains(str)) {
			return this.ymlconfig.getInt(str);
		}
		return 0;
	}

	public Boolean getBoolean(String str) {
		if (this.ymlconfig.contains(str)) {
			return this.ymlconfig.getBoolean(str);
		}
		return null;
	}

	public List<String> getStringList(String str) {
		if (this.ymlconfig.contains(str)) {
			return this.ymlconfig.getStringList(str);
		}
		return null;
	}

	public ConfigurationSection getConfigurationSection(String str) {
		if (this.ymlconfig.contains(str)) {
			return this.ymlconfig.getConfigurationSection(str);
		}
		return null;
	}

	public void set(String string, Object obj) {
		this.ymlconfig.set(string, obj);
		try {
			this.ymlconfig.save(this.file);
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	public Boolean contains(String str) {
		if (this.ymlconfig.contains(str)) {
			return true;
		}
		return false;
	}

}
