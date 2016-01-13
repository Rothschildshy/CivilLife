package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class SmiledataEntity extends BaseEntity implements Parcelable {
	public String Praises;
	public String Reviews;

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

	public SmiledataEntity(String praises, String reviews) {
		super();
		this.Praises = praises;
		this.Reviews = reviews;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(Praises);
		arg0.writeString(Reviews);
	}

	public static final Parcelable.Creator<SmiledataEntity> CREATOR = new Parcelable.Creator<SmiledataEntity>() {

		@Override
		public SmiledataEntity createFromParcel(Parcel source) {

			String Praises = source.readString();
			String Reviews = source.readString();
			SmiledataEntity hotActivity = new SmiledataEntity(Praises, Reviews);
			return hotActivity;
		}

		@Override
		public SmiledataEntity[] newArray(int size) {
			return new SmiledataEntity[size];
		}
	};

}
