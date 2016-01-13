package com.CivilLife.Entity;

import com.CivilLife.Base.BaseEntity;

import android.text.TextUtils;

/**
 * 获取个人资料
 * 
 * @author Administrator
 * 
 */
public class MyInfoEntity extends BaseEntity {
	public String Status;
	public String ID;
	public String PicUrl;
	public String UserName;
	public String Nickname;
	public String Sex;
	public String Email;
	public String Birthday;
	public String CurrentAddress;
	public String Phonemodel;
	public String IndustryID;
	public String Census;
	public String BigPicUrl;
	public String Degree;//学历
	public String Office;//职称
	public String GraduationDate;//毕业时间
	public String Specialty;
	public String GraduatedSchool;//毕业学校
	
	
	

	public String getGraduatedSchool() {
		return GraduatedSchool;
	}

	public void setGraduatedSchool(String graduatedSchool) {
		GraduatedSchool = graduatedSchool;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
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

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getNickname() {
		return Nickname;
	}

	public void setNickname(String nickname) {
		Nickname = nickname;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	public String getCurrentAddress() {
		return CurrentAddress;
	}

	public void setCurrentAddress(String currentAddress) {
		CurrentAddress = currentAddress;
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

	public String getDegree() {
		return Degree;
	}

	public void setDegree(String degree) {
		Degree = degree;
	}

	public String getOffice() {
		return Office;
	}

	public void setOffice(String office) {
		Office = office;
	}

	public String getGraduationDate() {
		return GraduationDate;
	}

	public void setGraduationDate(String GraduationDate) {
		GraduationDate = GraduationDate;
	}

	public String getSpecialty() {
		return Specialty;
	}

	public void setSpecialty(String specialty) {
		Specialty = specialty;
	}

}
