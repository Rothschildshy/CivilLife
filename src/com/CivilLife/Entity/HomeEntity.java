package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class HomeEntity extends BaseEntity implements Parcelable {
	public String ID;
	public String Content;
	public String PicUrl;
	public String VideoUrl;
	public String ArticleClassNameID;
	public String X;
	public String Y;
	public String UserID;
	public String UserInfoPicUrl;
	public String Nickname;
	public String Praise;
	public String Praises;
	public String Reviews;
	public String  Anonymous;
	public String Parent_ArticleClassNameID;
	public String Message;
	public String AddTime;
	public String VideoThumbnailsPicUrl;  
	
	
	
	public String getVideoThumbnailsPicUrl() {
		return VideoThumbnailsPicUrl;
	}

	public void setVideoThumbnailsPicUrl(String videoThumbnailsPicUrl) {
		VideoThumbnailsPicUrl = videoThumbnailsPicUrl;
	}

	public String getAddTime() {
		return AddTime;
	}

	public void setAddTime(String addTime) {
		AddTime = addTime;
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

	public String getParent_ArticleClassNameID() {
		if (TextUtils.isEmpty(Parent_ArticleClassNameID)) {
			return "0";
		}
		return Parent_ArticleClassNameID;
	}

	public void setParent_ArticleClassNameID(String parent_ArticleClassNameID) {
		Parent_ArticleClassNameID = parent_ArticleClassNameID;
	}

	public String getAnonymous() {
		if (TextUtils.isEmpty(Anonymous)) {
			return "0";
		}
		return Anonymous;
	}

	public void setAnonymous(String anonymous) {
		Anonymous = anonymous;
	}

	public String getPraises() {
		if (TextUtils.isEmpty(Praises)) {
			return "";
		}
		return Praises;
	}

	public void setPraises(String praises) {
		Praises = praises;
	}

	public String getReviews() {
		if (TextUtils.isEmpty(Reviews)) {
			return "";
		}
		return Reviews;
	}

	public void setReviews(String reviews) {
		Reviews = reviews;
	}

	public String getID() {
		if (TextUtils.isEmpty(ID)) {
			return "";
		}
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getContent() {
		if (TextUtils.isEmpty(Content)) {
			return "";
		}
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getPicUrl() {
		if (TextUtils.isEmpty(PicUrl)) {
			return "";
		}
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getVideoUrl() {
		if (TextUtils.isEmpty(VideoUrl)) {
			return "";
		}
		return VideoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		VideoUrl = videoUrl;
	}

	public String getArticleClassNameID() {
		if (TextUtils.isEmpty(ArticleClassNameID)) {
			return "";
		}
		return ArticleClassNameID;
	}

	public void setArticleClassNameID(String articleClassNameID) {
		ArticleClassNameID = articleClassNameID;
	}

	public String getX() {
		if (TextUtils.isEmpty(X)) {
			return "";
		}
		return X;
	}

	public void setX(String x) {
		X = x;
	}

	public String getY() {
		if (TextUtils.isEmpty(Y)) {
			return "";
		}
		return Y;
	}

	public void setY(String y) {
		Y = y;
	}

	public String getUserID() {
		if (TextUtils.isEmpty(UserID)) {
			return "";
		}
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUserInfoPicUrl() {
		if (TextUtils.isEmpty(UserInfoPicUrl)) {
			return "";
		}
		return UserInfoPicUrl;
	}

	public void setUserInfoPicUrl(String userInfoPicUrl) {
		UserInfoPicUrl = userInfoPicUrl;
	}

	public String getNickname() {
		if (TextUtils.isEmpty(Nickname)) {
			return "";
		}
		return Nickname;
	}

	public void setNickname(String nickname) {
		Nickname = nickname;
	}

	public String getPraise() {
		if (TextUtils.isEmpty(Praise)) {
			return "";
		}
		return Praise;
	}

	public void setPraise(String praise) {
		Praise = praise;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public HomeEntity(String iD, String content, String picUrl, String videoUrl, String articleClassNameID, String x,
			String y, String userID, String userInfoPicUrl, String nickname, String praise, String praises,
			String reviews,String Anonymous,String Parent_ArticleClassNameID,String Message,String AddTime,String VideoThumbnailsPicUrl) {
		super();
		this.ID = iD;
		this.Parent_ArticleClassNameID = Parent_ArticleClassNameID;
		this.Content = content;
		this.PicUrl = picUrl;
		this.VideoUrl = videoUrl;
		this.ArticleClassNameID = articleClassNameID;
		this.X = x;
		this.Y = y;
		this.UserID = userID;
		this.UserInfoPicUrl = userInfoPicUrl;
		this.Nickname = nickname;
		this.Praise = praise;
		this.Praises = praises;
		this.Reviews = reviews;
		this.Anonymous=Anonymous;
		this.Message=Message;
		this.AddTime = AddTime;
		this.VideoThumbnailsPicUrl = VideoThumbnailsPicUrl;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(ID);
		arg0.writeString(Content);
		arg0.writeString(PicUrl);
		arg0.writeString(VideoUrl);
		arg0.writeString(ArticleClassNameID);
		arg0.writeString(X);
		arg0.writeString(Y);
		arg0.writeString(UserID);
		arg0.writeString(UserInfoPicUrl);
		arg0.writeString(Nickname);
		arg0.writeString(Praise);
		arg0.writeString(Praises);
		arg0.writeString(Reviews);
		arg0.writeString(Anonymous);
		arg0.writeString(Parent_ArticleClassNameID);
		arg0.writeString(Message);
		arg0.writeString(AddTime);
		arg0.writeString(VideoThumbnailsPicUrl);
	}

	public static final Parcelable.Creator<HomeEntity> CREATOR = new Parcelable.Creator<HomeEntity>() {

		@Override
		public HomeEntity createFromParcel(Parcel source) {

			String ID = source.readString();
			String Content = source.readString();
			String PicUrl = source.readString();
			String VideoUrl = source.readString();
			String ArticleClassNameID = source.readString();
			String X = source.readString();
			String Y = source.readString();
			String UserID = source.readString();
			String UserInfoPicUrl = source.readString();
			String Nickname = source.readString();
			String Praise = source.readString();
			String Praises = source.readString();
			String Reviews = source.readString();
			String Anonymous = source.readString();
			String Parent_ArticleClassNameID = source.readString();
			String Message = source.readString();
			String AddTime = source.readString();
			String VideoThumbnailsPicUrl = source.readString();
			HomeEntity hotActivity = new HomeEntity(ID, Content, PicUrl, VideoUrl, ArticleClassNameID, X, Y, UserID,
					UserInfoPicUrl, Nickname, Praise, Praises, Reviews,Anonymous,Parent_ArticleClassNameID,Message,AddTime,VideoThumbnailsPicUrl);
			return hotActivity;
		}

		@Override
		public HomeEntity[] newArray(int size) {
			return new HomeEntity[size];
		}
	};

}
