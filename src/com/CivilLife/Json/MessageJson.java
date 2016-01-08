package com.CivilLife.Json;

import java.util.ArrayList;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.MessageEntity;
import com.app.civillife.Util.CommonAPI;
import com.google.gson.Gson;

import android.content.Context;

/** 获取小纸条列表 **/
public class MessageJson extends BaseJson {
	ArrayList<MessageEntity> Data = new ArrayList<MessageEntity>();

	public ArrayList<MessageEntity> getAl() {
		return Data;
	}

	public void setAl(ArrayList<MessageEntity> Data) {
		this.Data = Data;
	}

	static public MessageJson readJsonToSendmsgObject(Context context, String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		MessageJson data;
		try {
			data = gson.fromJson(jsonData, MessageJson.class);
		} catch (Exception e) {
			return null;
		}
		if (data == null || data.Data == null || data.Data.size() == 0) {
			return null;
		}
		return data;
	}

}
