package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 个人主页
 * 
 * @author Macx
 *
 */
public class InfoHomeEntity extends BaseEntity implements Parcelable {
	public String ID;// ID
	public String PicUrl;// 头像
	public String Nickname;// 昵称
	public String Birthday;// 生日
	public String Sex;// 性别
	public String CurrentAddress;// 现在所在城市
	public String AddTime;// 添加时间？
	public String Phonemodel;// 手机型号
	public String IndustryID;// 行业ID
	public String Industry;// 行业
	public String Census;// 家乡
	public String BigPicUrl;// 大罩
	public String ArticlCount;// 文章数量
	public String ArticlePraise;// 集赞数量
	public String UserFriends;// 是否是好友

	@Override
	public int describeContents() {
		return 0;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
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

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getCurrentAddress() {
		return CurrentAddress;
	}

	public void setCurrentAddress(String currentAddress) {
		CurrentAddress = currentAddress;
	}

	public String getAddTime() {
		return AddTime;
	}

	public void setAddTime(String addTime) {
		AddTime = addTime;
	}

	public String getPhonemodel() {
		return Phonemodel;
	}

	public void setPhonemodel(String phonemodel) {
		Phonemodel = phonemodel;
	}

	public String getIndustryID() {
		return IndustryID;
	}

	public void setIndustryID(String industryID) {
		IndustryID = industryID;
	}

	public String getIndustry() {
		return Industry;
	}

	public void setIndustry(String industry) {
		Industry = industry;
	}

	public String getCensus() {
		return Census;
	}

	public void setCensus(String census) {
		Census = census;
	}

	public String getBigPicUrl() {
		return BigPicUrl;
	}

	public void setBigPicUrl(String bigPicUrl) {
		BigPicUrl = bigPicUrl;
	}

	public String getArticlCount() {
		return ArticlCount;
	}

	public void setArticlCount(String articlCount) {
		ArticlCount = articlCount;
	}

	public String getArticlePraise() {
		return ArticlePraise;
	}

	public void setArticlePraise(String articlePraise) {
		ArticlePraise = articlePraise;
	}

	public String getUserFriends() {
		return UserFriends;
	}

	public void setUserFriends(String userFriends) {
		UserFriends = userFriends;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(ID);
		arg0.writeString(PicUrl);
		arg0.writeString(Nickname);
		arg0.writeString(Birthday);
		arg0.writeString(Sex);
		arg0.writeString(CurrentAddress);
		arg0.writeString(AddTime);
		arg0.writeString(Phonemodel);
		arg0.writeString(IndustryID);
		arg0.writeString(Industry);
		arg0.writeString(Census);
		arg0.writeString(BigPicUrl);
		arg0.writeString(ArticlCount);
		arg0.writeString(ArticlePraise);
		arg0.writeString(UserFriends);
	}

	public static final Parcelable.Creator<InfoHomeEntity> CREATOR = new Parcelable.Creator<InfoHomeEntity>() {

		@Override
		public InfoHomeEntity createFromParcel(Parcel source) {

			String ID = source.readString();
			String PicUrl = source.readString();
			String Nickname = source.readString();
			String Birthday = source.readString();
			String Sex = source.readString();
			String CurrentAddress = source.readString();
			String AddTime = source.readString();
			String Phonemodel = source.readString();
			String IndustryID = source.readString();
			String Industry = source.readString();
			String Census = source.readString();
			String BigPicUrl = source.readString();
			String ArticlCount = source.readString();
			String ArticlePraise = source.readString();
			String UserFriends = source.readString();
			InfoHomeEntity hotActivity = new InfoHomeEntity(ID, PicUrl, Nickname, Birthday, Sex, CurrentAddress,
					AddTime, Phonemodel, IndustryID, Industry, Census, BigPicUrl, ArticlCount, ArticlePraise,
					UserFriends);
			return hotActivity;
		}

		@Override
		public InfoHomeEntity[] newArray(int size) {
			return new InfoHomeEntity[size];
		}
	};

	public InfoHomeEntity(String iD, String picUrl, String nickname, String birthday, String sex, String currentAddress,
			String addTime, String phonemodel, String industryID, String industry, String census, String bigPicUrl,
			String articlCount, String articlePraise, String userFriends) {
		super();
		ID = iD;
		PicUrl = picUrl;
		Nickname = nickname;
		Birthday = birthday;
		Sex = sex;
		CurrentAddress = currentAddress;
		AddTime = addTime;
		Phonemodel = phonemodel;
		IndustryID = industryID;
		Industry = industry;
		Census = census;
		BigPicUrl = bigPicUrl;
		ArticlCount = articlCount;
		ArticlePraise = articlePraise;
		UserFriends = userFriends;
	}

}
