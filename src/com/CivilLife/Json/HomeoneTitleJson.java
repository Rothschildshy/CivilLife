package com.CivilLife.Json;

import java.util.ArrayList;

import android.content.Context;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.HomeonetitleEntity;
import com.app.civillife.Util.CommonAPI;
import com.aysy_mytool.ToastUtil;
import com.google.gson.Gson;

public class HomeoneTitleJson extends BaseJson {
	ArrayList<HomeonetitleEntity> Data = new ArrayList<HomeonetitleEntity>();

	public ArrayList<HomeonetitleEntity> getAl() {
		return Data;
	}

	public void setAl(ArrayList<HomeonetitleEntity> Data) {
		this.Data = Data;
	}

	static public HomeoneTitleJson readJsonToSendmsgObject(Context context, String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		HomeoneTitleJson data;
		try {
			data = gson.fromJson(jsonData, HomeoneTitleJson.class);
		} catch (Exception e) {
			return null;
		}
		if (data == null || data.Data == null || data.Data.size() == 0) {
			return null;
		}
		return data;
	}

}
