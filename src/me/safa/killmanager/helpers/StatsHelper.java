package me.safa.killmanager.helpers;

import java.util.UUID;

import me.safa.killmanager.files.SetupData;
import me.safa.killmanager.utils.DataType;

public class StatsHelper {

	SetupData data;

	public StatsHelper() {
		this.data = new SetupData();
	}

	public void setData(UUID uuid, DataType type, int value) {
		data.set("Players." + uuid + "." + type, value);
		data.load();
	}

	public void giveData(UUID uuid, DataType type, int value) {
		int oldValue = getData(uuid, type);
		int newValue = oldValue + value;
		setData(uuid, type, newValue);
	}

	public void takeData(UUID uuid, DataType type, int value) {
		int oldValue = getData(uuid, type);
		int newValue = oldValue - value;
		setData(uuid, type, newValue);
	}

	public int getData(UUID uuid, DataType type) {

		if (hasData(uuid, type)) {
			return data.getInt("Players." + uuid + "." + type);
		}
		setData(uuid, type, 0);
		return 0;
	}

	public String getKdr(UUID uuid) {
		int kills = getData(uuid, DataType.KILLS);
		int deaths = getData(uuid, DataType.DEATHS);
		double result = kills / deaths;

		String str = String.valueOf(result);

		if (str.length() > 4) {
			str.substring(0, 4);
		}

		return str;
	}

	public boolean hasData(UUID uuid, DataType type) {
		if (data.contains("Players." + uuid + "." + type)) {
			return true;
		}
		return false;
	}

}
