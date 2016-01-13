package com.CivilLife.Json;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.CivilLife.Base.BaseJson;
import com.CivilLife.Entity.ChatListEntity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Entity.PushEntity;
import com.app.civillife.Util.CommonAPI;
import com.google.gson.Gson;

/** 推送 **/
public class PushJson extends BaseJson {
	ArrayList<PushEntity> Data = new ArrayList<PushEntity>();

	public ArrayList<PushEntity> getAl() {
		return Data;
	}

	public void setAl(ArrayList<PushEntity> Data) {
		this.Data = Data;
	}

	static public PushJson readJsonToSendmsgObject(Context context, String jsonData) {
		if (CommonAPI.checkDataIsJson(jsonData) == false) {
			return null;
		}
		Gson gson = new Gson();
		PushJson data;
		try {
			data = gson.fromJson(jsonData, PushJson.class);
		} catch (Exception e) {
			return null;
		}
		if (data == null || data.Data == null || data.Data.size() == 0) {
			return null;
		}
		return data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Data == null) ? 0 : Data.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PushJson other = (PushJson) obj;
		if (Data == null) {
			if (other.Data != null)
				return false;
		} else if (!Data.equals(other.Data))
			return false;
		return true;
	}

	
	
}
