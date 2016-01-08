package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

import android.text.TextUtils;

/**
 * 获取行业数据
 * 
 * @author Administrator
 * 
 */
public class IndustryEntity extends BaseEntity {
	public String ID;
	public String IndustryClassName;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getIndustryClassName() {
		return IndustryClassName;
	}

	public void setIndustryClassName(String industryClassName) {
		IndustryClassName = industryClassName;
	}

	public IndustryEntity(String iD, String industryClassName) {
		super();
		ID = iD;
		IndustryClassName = industryClassName;
	}

}
