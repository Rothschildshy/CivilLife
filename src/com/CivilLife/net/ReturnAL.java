package com.CivilLife.net;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;

import com.CivilLife.Variable.GlobalVariable;
import com.umeng.socialize.utils.Log;

import android.text.TextUtils;
import android.widget.AutoCompleteTextView.Validator;

/**
 * 请求URL的容器
 * 
 * @author Administrator
 */
public final class ReturnAL {

	static ArrayList<BasicNameValuePair> al = new ArrayList<BasicNameValuePair>();

	// 登陆
	public static ArrayList<BasicNameValuePair> LoginMap(String UserName, String PassWord) {
		al.clear();
		al.add(new BasicNameValuePair("UserName", UserName));
		al.add(new BasicNameValuePair("PassWord", PassWord));
		al.add(new BasicNameValuePair("Action", "Login"));
		return al;
	};

	// 注册
	public static ArrayList<BasicNameValuePair> RegisterMap(String UserName, String PassWord,String Nickname) {
		al.clear();
		al.add(new BasicNameValuePair("UserName", UserName));
		al.add(new BasicNameValuePair("PassWord", PassWord));
		al.add(new BasicNameValuePair("Nickname", Nickname));
		al.add(new BasicNameValuePair("ConfirmPassWord", PassWord));
		al.add(new BasicNameValuePair("Action", "UserInfo_Reg"));
		return al;
	};

	// 找回密码
	public static ArrayList<BasicNameValuePair> VerificationMap(String UserName, String PassWord, String Email) {
		al.clear();
		al.add(new BasicNameValuePair("UserName", UserName));
		al.add(new BasicNameValuePair("Email", Email));
		al.add(new BasicNameValuePair("PassWord", PassWord));
		al.add(new BasicNameValuePair("ConfirmPassWord", PassWord));
		al.add(new BasicNameValuePair("Action", "ForgetPassWord"));
		return al;
	};

	// 发表评论
	public static ArrayList<BasicNameValuePair> ContentMap(String id, String content) {
		al.clear();
		al.add(new BasicNameValuePair("ID", id));
		al.add(new BasicNameValuePair("Content", content));
		al.add(new BasicNameValuePair("Action", "Article_Comments"));
		al.add(new BasicNameValuePair("UserID", GlobalVariable.UserID));
		al.add(new BasicNameValuePair("UserName", GlobalVariable.UserName));
		al.add(new BasicNameValuePair("PassWord", GlobalVariable.UserPassWord));
		return al;
	};

	// 发表文章
	/**
	 * 匿名：Anonymous（0=不选 1=选中） 文字内容：Content 图片地址：PicUrl 视频地址：VideoUrl
	 * 发布栏目ID：ArticleClassNameID（无子栏目直接取父级栏目ID，有子栏目的取子栏目ID） 当前用户定位经纬度XY坐标：X坐标：X
	 * Y坐标：Y(这里经纬度XY是单独分开存的，用于每个栏目按附近人发布的文章排序) 隐藏Input名称：Action
	 * 值：Article_Release 发送和保存草稿（发送需要加隐藏Input名称：mod值为空，保存草稿需要加隐藏Input名称：mod
	 * 值：SaveDraft））
	 **/
	public static ArrayList<BasicNameValuePair> PublishedArticles(boolean anonymous, String content, String PicUrl,
			String VideoUrl, String ArticleClassNameID, String mod,String imageUrl,int TYPE,String id) {
		al.clear();
		al.add(new BasicNameValuePair("UserID", GlobalVariable.UserID));
		al.add(new BasicNameValuePair("UserName", GlobalVariable.UserName));
		al.add(new BasicNameValuePair("PassWord", GlobalVariable.UserPassWord));
		if (anonymous) {
			// Log.e("", "anonymous1 "+anonymous);
			al.add(new BasicNameValuePair("Anonymous", "1"));
		} else {
			// Log.e("", "anonymous2 "+anonymous);
			al.add(new BasicNameValuePair("Anonymous", "0"));
		}
		al.add(new BasicNameValuePair("Content", content));
		if (!TextUtils.isEmpty(PicUrl)) {
			al.add(new BasicNameValuePair("PicUrl", PicUrl));
		}
		if (!TextUtils.isEmpty(VideoUrl)) {
			al.add(new BasicNameValuePair("VideoUrl", VideoUrl));
			al.add(new BasicNameValuePair("VideoThumbnailsPicUrl", imageUrl));
		}
		al.add(new BasicNameValuePair("ArticleClassNameID", ArticleClassNameID));

		al.add(new BasicNameValuePair("x", GlobalVariable.mycoordinates_x));
		al.add(new BasicNameValuePair("y", GlobalVariable.mycoordinates_y));  
		if (TYPE==2) {
			al.add(new BasicNameValuePair("Action", "uiam"));
			al.add(new BasicNameValuePair("ID", id));
		}else{
			al.add(new BasicNameValuePair("Action", "Article_Release"));
		}
		al.add(new BasicNameValuePair("mod", mod));
		return al;
	};

	// 修改资料
	public static ArrayList<BasicNameValuePair> ContentInfoMap(String PicUrl, String NewPassWord,
			String ConfirmPassWord, String Nickname, int Sex, String Email, String Birthday, String Phonemodel,
			String IndustryID, String Census, String BigPicUrl, String Degree, String Office, String Workingyears,
			String GraduatedSchool,String Specialty) {
		al.clear();
		al.add(new BasicNameValuePair("UserID", GlobalVariable.UserID));
		al.add(new BasicNameValuePair("UserName", GlobalVariable.UserName));
		al.add(new BasicNameValuePair("PassWord", GlobalVariable.UserPassWord));

		al.add(new BasicNameValuePair("PicUrl", PicUrl));
		al.add(new BasicNameValuePair("NewPassWord", NewPassWord));
		al.add(new BasicNameValuePair("ConfirmPassWord", ConfirmPassWord));
		al.add(new BasicNameValuePair("Nickname", Nickname));
		al.add(new BasicNameValuePair("Sex", "" + Sex));
		if (!TextUtils.isEmpty(Email)) {
			al.add(new BasicNameValuePair("Email", Email));
		}
		al.add(new BasicNameValuePair("Birthday", Birthday));
		// al.add(new BasicNameValuePair("CurrentAddress", CurrentAddress));
		al.add(new BasicNameValuePair("Phonemodel", Phonemodel));
		al.add(new BasicNameValuePair("IndustryID", IndustryID));
		al.add(new BasicNameValuePair("Census", Census));
		al.add(new BasicNameValuePair("BigPicUrl", BigPicUrl));
		if (!TextUtils.isEmpty(Degree)) {
			al.add(new BasicNameValuePair("Degree", Degree));
		}
		if (!TextUtils.isEmpty(Office)) {
			al.add(new BasicNameValuePair("Office", Office));
		}
		if (!TextUtils.isEmpty(Workingyears)) {
			al.add(new BasicNameValuePair("GraduationDate", Workingyears));
		}
		if (!TextUtils.isEmpty(GraduatedSchool)) {
			al.add(new BasicNameValuePair("GraduatedSchool", GraduatedSchool));
		}
		if (!TextUtils.isEmpty(Specialty)) {
			al.add(new BasicNameValuePair("Specialty", Specialty));
		}
		al.add(new BasicNameValuePair("Action", "UserInfo_Mod"));
		return al;
	};

	// 发布聊天
	public static ArrayList<BasicNameValuePair> ToMessageList(String Content, String ToUserID) {
		al.clear();
		al.add(new BasicNameValuePair("UserID", GlobalVariable.UserID));
		al.add(new BasicNameValuePair("UserName", GlobalVariable.UserName));
		al.add(new BasicNameValuePair("PassWord", GlobalVariable.UserPassWord));

		al.add(new BasicNameValuePair("Action", "SPP"));
		al.add(new BasicNameValuePair("Content", Content));
		al.add(new BasicNameValuePair("ToUserID", ToUserID));
		return al;
	};

	// 意见反馈
	public static ArrayList<BasicNameValuePair> FeedBack(String Note, String Contact) {
		al.clear();
		al.add(new BasicNameValuePair("Note", Note));
		al.add(new BasicNameValuePair("Contact", Contact));
		al.add(new BasicNameValuePair("Action", "FeedBack"));
		return al;
	};

}