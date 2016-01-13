package com.CivilLife.Json;

import java.util.ArrayList;

import android.content.Context;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.MyInfoEntity;
import com.app.civillife.Util.CommonAPI;
import com.google.gson.Gson;

/** 个人资料解析格式 **/
public class GetMyIngoJson extends BaseJson {
	ArrayList<MyInfoEntity> Data = new ArrayList<MyInfoEntity>();

	public ArrayList<MyInfoEntity> getAl() {
		return Data;
	}

	public void setAl(ArrayList<MyInfoEntity> Data) {
		this.Data = Data;
	}

	static public GetMyIngoJson readJsonToSendmsgObject(Context context, String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		GetMyIngoJson data;
		try {
			data = gson.fromJson(jsonData, GetMyIngoJson.class);
		} catch (Exception e) {
			return null;
		}
		if (data == null || data.Data == null || data.Data.size() == 0) {
			return null;
		}
		return data;
	}

}
