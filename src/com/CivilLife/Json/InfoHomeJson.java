package com.CivilLife.Json;

import java.util.ArrayList;

import android.content.Context;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.InfoHomeEntity;
import com.app.civillife.Util.CommonAPI;
import com.google.gson.Gson;
/**
 * 个人中心主页解析
 * @author Macx
 *
 */
public class InfoHomeJson extends BaseJson {
	ArrayList<InfoHomeEntity> Data = new ArrayList<InfoHomeEntity>();

	public ArrayList<InfoHomeEntity> getAl() {
		return Data;
	}

	public void setAl(ArrayList<InfoHomeEntity> Data) {
		this.Data = Data;
	}

	static public InfoHomeJson readJsonToSendmsgObject(Context context, String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		InfoHomeJson data;
		try {
			data = gson.fromJson(jsonData, InfoHomeJson.class);
		} catch (Exception e) {
			return null;
		}
		if (data == null || data.Data == null || data.Data.size() == 0) {
			return null;
		}
		return data;
	}

}
