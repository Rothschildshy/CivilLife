package com.CivilLife.Json;

import java.util.ArrayList;

import android.content.Context;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.ChatListEntity;
import com.CivilLife.Entity.PublicEntity;
import com.app.civillife.Util.CommonAPI;
import com.google.gson.Gson;

/** 聊天列表 **/
public class ChatListJson extends BaseJson {
	ArrayList<ChatListEntity> Data = new ArrayList<ChatListEntity>();

	public ArrayList<ChatListEntity> getAl() {
		return Data;
	}

	public void setAl(ArrayList<ChatListEntity> Data) {
		this.Data = Data;
	}

	static public ChatListJson readJsonToSendmsgObject(Context context, String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		ChatListJson data;
		try {
			data = gson.fromJson(jsonData, ChatListJson.class);
		} catch (Exception e) {
			return null;
		}
		if (data == null || data.Data == null || data.Data.size() == 0) {
			return null;
		}
		return data;
	}

}
