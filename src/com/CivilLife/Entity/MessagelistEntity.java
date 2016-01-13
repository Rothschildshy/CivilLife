package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

public class MessagelistEntity extends BaseEntity {
	
	String DID;
	String ID;
	String UserID;
	String UserPicUrl;
	String ToUserID;
	String ToUserIDPicUrl;
	String Content;
	boolean Islocal=false;
	
	public boolean isIslocal() {
		return Islocal;
	}
	public void setIslocal(boolean islocal) {
		this.Islocal = islocal;
	}
	
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
	public MessagelistEntity(String dID, String iD, String userID, String userPicUrl, String toUserID,
			String toUserIDPicUrl, String content,boolean islocal) {
		super();
		DID = dID;
		ID = iD;
		UserID = userID;
		UserPicUrl = userPicUrl;
		ToUserID = toUserID;
		ToUserIDPicUrl = toUserIDPicUrl;
		Content = content;
		Islocal = islocal;
	}
	
	
	
}
