package com.CivilLife.Variable;

import java.util.ArrayList;

import com.CivilLife.Entity.HomeonetitleEntity;

/*
 * 全局变量
 */
public final class GlobalVariable {
	/** 设备唯一id **/
	public static String IMEI = "";
	/** 是否wifi下载 **/
	public static boolean WifiDown = false;
	/** 是否消息推送 **/
	public static boolean push = true;
	/** 我的坐标 x **/
	public static String mycoordinates_x = 118.144612 + "";
	/** 我的坐标 y **/
	public static String mycoordinates_y = 24.482887 + "";
	/** 我的当前城市名称 **/
	public static String City = "福建省,厦门市";
	/** 我的当前位置**/
	public static String address = "福建省,厦门市";
	/** 用户ID **/
	public static String UserID = "";
	/** 用户名 后台返回的username 请求时候用到 **/
	public static String UserName = "";
	/** 用户名  昵称 储存第三方登录的名称 如果不是第三方登录和上面的是一样的 **/
	public static String Nickname = "";
	/** 用户密码 **/
	public static String UserPassWord = "";
	/** 用户头像 **/
	public static String UserImage = "";

	/** 用于存储用户信息的 S P 的key **/
	public static String USERID="USERID";
	public static String USERNAME="USERNAME";
	public static String NICKNAME="NICKNAME";
	public static String USERPW="USERPW";
	public static String USERIMAGE="USERIMAGE";
	public static ArrayList<HomeonetitleEntity> Data=new ArrayList<HomeonetitleEntity>();//一级目录栏数据
	public static String sendID="-1";//当前用户在聊天界面时候 对方id  -1为不在聊天界面
	
	
	
	public static ArrayList<String> al_schooling=new ArrayList<String>();
	public static ArrayList<String> al_professionaltitle=new ArrayList<String>();
	public static void SetArrayList(){
		al_schooling.add("中专以下");
		al_schooling.add("　　中专");
		al_schooling.add("　　大专");
		al_schooling.add("本科学历");
		al_schooling.add("　研究生");
		al_schooling.add("博士学历");
		
		al_professionaltitle.add("初级");
		al_professionaltitle.add("中级");
		al_professionaltitle.add("高级");
		al_professionaltitle.add("其他");
		
	}
	
}
