package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class HomeonetitleEntity extends BaseEntity implements Parcelable {
	public String ID;
	public String ArticleClassName;

	public String getID() {
		if (TextUtils.isEmpty(ID)) {
			return "";
		}
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getArticleClassName() {
		if (TextUtils.isEmpty(ArticleClassName)) {
			return "";
		}
		return ArticleClassName;
	}

	public void setArticleClassName(String articleClassName) {
		ArticleClassName = articleClassName;
	}

	public HomeonetitleEntity(String iD, String articleClassName) {
		super();
		ID = iD;
		ArticleClassName = articleClassName;
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(ID);
		arg0.writeString(ArticleClassName);
	}

	public static final Parcelable.Creator<HomeonetitleEntity> CREATOR = new Parcelable.Creator<HomeonetitleEntity>() {

		@Override
		public HomeonetitleEntity createFromParcel(Parcel source) {

			String ID = source.readString();
			String ArticleClassName = source.readString();
			HomeonetitleEntity homeonetitleEntity = new HomeonetitleEntity(ID, ArticleClassName);
			return homeonetitleEntity;
		}

		@Override
		public HomeonetitleEntity[] newArray(int size) {
			return new HomeonetitleEntity[size];
		}
	};
}
