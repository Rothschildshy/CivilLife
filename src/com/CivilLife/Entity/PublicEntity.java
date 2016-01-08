package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

import android.text.TextUtils;

public class PublicEntity extends BaseEntity {
	public String Status;
	public String Message;
	public String UserID;
	public String UserName;
	public String UserPassWord;
	public String PassWord;


	public String getPassWord() {
		if (TextUtils.isEmpty(PassWord)) {
			return "";
		}
		return PassWord;
	}

	public void setPassWord(String passWord) {
		PassWord = passWord;
	}

	public String getUserId() {
		if (TextUtils.isEmpty(UserID)) {
			return "";
		}
		return UserID;
	}

	public void setUserId(String userId) {
		UserID = userId;
	}

	public String getUserName() {
		if (TextUtils.isEmpty(UserName)) {
			return "";
		}
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getUserPassWord() {
		if (TextUtils.isEmpty(UserPassWord)) {
			return "";
		}
		return UserPassWord;
	}

	public void setUserPassWord(String userPassWord) {
		UserPassWord = userPassWord;
	}

	public String getStatus() {
		if (TextUtils.isEmpty(Status)) {
			return "";
		}
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getMessage() {
		if (TextUtils.isEmpty(Message)) {
			return "";
		}
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

}
