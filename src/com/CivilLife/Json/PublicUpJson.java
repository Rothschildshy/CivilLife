package com.CivilLife.Json;

import java.util.ArrayList;

import android.content.Context;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.PublicEntity;
import com.app.civillife.Util.CommonAPI;
import com.google.gson.Gson;

/** 公用的解析格式 **/
public class PublicUpJson extends BaseJson {
	ArrayList<PublicEntity> Data = new ArrayList<PublicEntity>();

	public ArrayList<PublicEntity> getAl() {
		return Data;
	}

	public void setAl(ArrayList<PublicEntity> Data) {
		this.Data = Data;
	}

	static public PublicUpJson readJsonToSendmsgObject(Context context, String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		PublicUpJson data;
		try {
			data = gson.fromJson(jsonData, PublicUpJson.class);
		} catch (Exception e) {
			return null;
		}
		if (data == null || data.Data == null || data.Data.size() == 0) {
			return null;
		}
		return data;
	}

}
