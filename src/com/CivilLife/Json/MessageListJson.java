package com.CivilLife.Json;

import java.util.ArrayList;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.MessagelistEntity;
import com.app.civillife.Util.CommonAPI;
import com.google.gson.Gson;

import android.content.Context;

/** 获取小纸条列表 **/
public class MessageListJson extends BaseJson {
	ArrayList<MessagelistEntity> Data = new ArrayList<MessagelistEntity>();

	public ArrayList<MessagelistEntity> getAl() {
		return Data;
	}

	public void setAl(ArrayList<MessagelistEntity> Data) {
		this.Data = Data;
	}

	static public MessageListJson readJsonToSendmsgObject(Context context, String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		MessageListJson data;
		try {
			data = gson.fromJson(jsonData, MessageListJson.class);
		} catch (Exception e) {
			return null;
		}
		if (data == null || data.Data == null || data.Data.size() == 0) {
			return null;
		}
		return data;
	}

}
