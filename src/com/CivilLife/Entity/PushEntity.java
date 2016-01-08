package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

public class PushEntity extends BaseEntity {
	String ID;
	String SendID;
	String PicUrl;
	String Title;
	String Content;
	String ArticleID;
	String Type;
	
	
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ArticleID == null) ? 0 : ArticleID.hashCode());
		result = prime * result + ((Content == null) ? 0 : Content.hashCode());
		result = prime * result + ((ID == null) ? 0 : ID.hashCode());
		result = prime * result + ((PicUrl == null) ? 0 : PicUrl.hashCode());
		result = prime * result + ((SendID == null) ? 0 : SendID.hashCode());
		result = prime * result + ((Title == null) ? 0 : Title.hashCode());
		result = prime * result + ((Type == null) ? 0 : Type.hashCode());
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
		PushEntity other = (PushEntity) obj;
		if (ArticleID == null) {
			if (other.ArticleID != null)
				return false;
		} else if (!ArticleID.equals(other.ArticleID))
			return false;
		if (Content == null) {
			if (other.Content != null)
				return false;
		} else if (!Content.equals(other.Content))
			return false;
		if (ID == null) {
			if (other.ID != null)
				return false;
		} else if (!ID.equals(other.ID))
			return false;
		if (PicUrl == null) {
			if (other.PicUrl != null)
				return false;
		} else if (!PicUrl.equals(other.PicUrl))
			return false;
		if (SendID == null) {
			if (other.SendID != null)
				return false;
		} else if (!SendID.equals(other.SendID))
			return false;
		if (Title == null) {
			if (other.Title != null)
				return false;
		} else if (!Title.equals(other.Title))
			return false;
		if (Type == null) {
			if (other.Type != null)
				return false;
		} else if (!Type.equals(other.Type))
			return false;
		return true;
	}
	
	
	
}
