package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

import android.text.TextUtils;

/**
 * 获取行业数据
 * 
 * @author Administrator
 * 
 */
public class MessageEntity extends BaseEntity {
	public String ID;
	public String SendID;// 发送者ID
	public String PicUrl;// 发送者头像
	public String Title;// 消息标题
	public String Content;// 消息内容
	public String ArticleID;// 文章ID
	public String Type;// 消息类型 Type=1是聊天信息 Type=2是筑友发送的文章

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getSendID() {
		return SendID;
	}

	public void setSendID(String sendID) {
		SendID = sendID;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getArticleID() {
		return ArticleID;
	}

	public void setArticleID(String articleID) {
		ArticleID = articleID;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

}
