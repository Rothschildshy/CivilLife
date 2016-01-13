package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class MyManageEntity extends BaseEntity {
	public String ID;
	public String Content;
	public String AddTime;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getAddTime() {
		return AddTime;
	}

	public void setAddTime(String addTime) {
		AddTime = addTime;
	}

}
