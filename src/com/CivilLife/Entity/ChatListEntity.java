package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

public class ChatListEntity extends BaseEntity {
	String DID;
	String ID;
	String UserID;
	String UserPicUrl;
	String ToUserID;
	String ToUserIDPicUrl;
	String Content;
	public String getDID() {
		return DID;
	}
	public void setDID(String dID) {
		DID = dID;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getUserPicUrl() {
		return UserPicUrl;
	}
	public void setUserPicUrl(String userPicUrl) {
		UserPicUrl = userPicUrl;
	}
	public String getToUserID() {
		return ToUserID;
	}
	public void setToUserID(String toUserID) {
		ToUserID = toUserID;
	}
	public String getToUserIDPicUrl() {
		return ToUserIDPicUrl;
	}
	public void setToUserIDPicUrl(String toUserIDPicUrl) {
		ToUserIDPicUrl = toUserIDPicUrl;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	
	
}
