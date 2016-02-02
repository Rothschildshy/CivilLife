package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 我的好友
 * 
 * @author Administrator
 * 
 */
public class MyFriendEntity extends BaseEntity {
	public String ID;
	public String UserFriendsID;
	public String UserID;
	public String AID;
	public String MasterID;
	public String PicUrl;
	public String Sex;
	public String Nickname;
	public String Birthday;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getUserFriendsID() {
		if (TextUtils.isEmpty(UserFriendsID)) {
			return "";
		}
		return UserFriendsID;
	}

	public void setUserFriendsID(String userFriendsID) {
		UserFriendsID = userFriendsID;
	}

	public String getAID() {
		if (TextUtils.isEmpty(AID)) {
			return "";
		}
		return AID;
	}

	public void setAID(String aID) {
		AID = aID;
	}

	public String getMasterID() {
		if (TextUtils.isEmpty(MasterID)) {
			return "";
		}
		return MasterID;
	}

	public void setMasterID(String masterID) {
		MasterID = masterID;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getSex() {
		if (TextUtils.isEmpty(Sex)) {
			return "";
		}
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getNickname() {
		return Nickname;
	}

	public void setNickname(String nickname) {
		Nickname = nickname;
	}

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

}
