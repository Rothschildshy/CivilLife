package com.CivilLife.Json;

import java.util.ArrayList;

import android.content.Context;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.MyFriendEntity;
import com.app.civillife.Util.CommonAPI;
import com.google.gson.Gson;

public class MyFriendJson extends BaseJson {
	ArrayList<MyFriendEntity> Data = new ArrayList<MyFriendEntity>();

	public ArrayList<MyFriendEntity> getAl() {
		return Data;
	}

	public void setAl(ArrayList<MyFriendEntity> Data) {
		this.Data = Data;
	}

	static public MyFriendJson readJsonToSendmsgObject(Context context, String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		MyFriendJson data;
		try {
			data = gson.fromJson(jsonData, MyFriendJson.class);
		} catch (Exception e) {
			return null;
		}
		if (data == null || data.Data == null || data.Data.size() == 0) {
			return null;
		}
		return data;
	}

}
