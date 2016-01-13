package com.CivilLife.Json;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.SmiledataEntity;
import com.app.civillife.Util.CommonAPI;
import com.aysy_mytool.Qlog;
import com.google.gson.Gson;

/** 点赞和评论数 **/
public class SmileDataJson extends BaseJson {
	ArrayList<SmiledataEntity> Data = new ArrayList<SmiledataEntity>();

	public ArrayList<SmiledataEntity> getData() {
		return Data;
	}

	public void setData(ArrayList<SmiledataEntity> data) {
		Data = data;
	}

	static public SmileDataJson readJsonToSendmsgObject(Context context, String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		SmileDataJson data;

		try {
			data = gson.fromJson(jsonData, SmileDataJson.class);

		} catch (Exception e) {

			return null;
		}
		if (data == null || data.Data == null || data.getData().size() == 0) {

			return null;
		}

		return data;
	}

}
