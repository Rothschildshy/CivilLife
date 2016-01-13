package com.CivilLife.Json;

import java.util.ArrayList;

import android.content.Context;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.IndustryEntity;
import com.CivilLife.Entity.MyInfoEntity;
import com.app.civillife.Util.CommonAPI;
import com.google.gson.Gson;

/** 获取行业职业解析格式 **/
public class GetIndustryJson extends BaseJson {
	ArrayList<IndustryEntity> Data = new ArrayList<IndustryEntity>();

	public ArrayList<IndustryEntity> getAl() {
		return Data;
	}

	public void setAl(ArrayList<IndustryEntity> Data) {
		this.Data = Data;
	}

	static public GetIndustryJson readJsonToSendmsgObject(Context context, String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		GetIndustryJson data;
		try {
			data = gson.fromJson(jsonData, GetIndustryJson.class);
		} catch (Exception e) {
			return null;
		}
		if (data == null || data.Data == null || data.Data.size() == 0) {
			return null;
		}
		return data;
	}

}
