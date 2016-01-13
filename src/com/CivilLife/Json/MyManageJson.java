package com.CivilLife.Json;

import java.util.ArrayList;

import android.content.Context;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.MyManageEntity;
import com.app.civillife.Util.CommonAPI;
import com.google.gson.Gson;

public class MyManageJson extends BaseJson {
	ArrayList<MyManageEntity> Data = new ArrayList<MyManageEntity>();

	public ArrayList<MyManageEntity> getAl() {
		return Data;
	}

	public void setAl(ArrayList<MyManageEntity> Data) {
		this.Data = Data;
	}

	static public MyManageJson readJsonToSendmsgObject(Context context, String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		MyManageJson data;
		try {
			data = gson.fromJson(jsonData, MyManageJson.class);
		} catch (Exception e) {
			return null;
		}
		if (data == null || data.Data == null || data.Data.size() == 0) {
			return null;
		}
		return data;
	}

}
