package com.CivilLife.Json;

import java.util.ArrayList;

import android.content.Context;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.HomeEntity;
import com.app.civillife.Util.CommonAPI;
import com.google.gson.Gson;

public class HomeJson extends BaseJson {
	ArrayList<HomeEntity> Data = new ArrayList<HomeEntity>();

	public ArrayList<HomeEntity> getAl() {
		return Data;
	}

	public void setAl(ArrayList<HomeEntity> Data) {
		this.Data = Data;
	}

	static public HomeJson readJsonToSendmsgObject(Context context,
			String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		HomeJson data;
		try {
			data = gson.fromJson(jsonData, HomeJson.class);
		} catch (Exception e) {
			return null;
		}
		if (data == null || data.Data == null || data.Data.size() == 0) {
			return null;
		}
		return data;
	}

}
