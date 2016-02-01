package com.CivilLife.net;

import com.CivilLife.Variable.GlobalVariable;
import com.app.civillife.R;
import com.aysy_mytool.Qlog;
import com.umeng.socialize.net.v;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import kankan.wheel.R.string;

public final class Httpurl {

	// /** 服务器地址 GET**/
	// public static String URL = "http://tmssh.247.go-ip.cn/";
	public static String URL = "http://tmssh.conitm.com/";

	/** 获取 UserID UserNameMD5 UserPassWord **/
	public static String GetInfo() {
		return "UserID=" + GlobalVariable.UserID + "&UserName=" + GlobalVariable.UserName + "&PassWord="
				+ GlobalVariable.UserPassWord;
	}

	/** 获取 x y **/
	public static String GetXY() {
		return "&X=" + GlobalVariable.mycoordinates_x + "&Y=" + GlobalVariable.mycoordinates_y;
	}

	/** 检测会员是否登录 **/
	public static String IsLogin(String Address) {
		String url = URL + "?Action=LoginCheck&" + GetInfo() + GetXY() + "&CurrentAddress=" + Address;
		return url;

	}

	/** 获取会员昵称和头像 **/
	public static String GetPicORNick() {
		String url = URL + "user.html?mod=userinfo_pn&" + GetInfo();
		return url;

	}

	/** 获取首页标题第一级目录 GET **/
	public static String homeonetitle = URL + "ac.html";

	/** 获取首页标题第二级目录 GET **/
	public static String hometwotitle(String i) {
		String url = URL + "ac-" + i + ".html";
		return url;
	}

	/** 固定最新标题 内容 GET **/
	public static String newtitle(int page) {
		String url = URL + "al.html?Page=" + page + "&UserID=" + GlobalVariable.UserID + GetXY();
		return url;
	}

	/** 固定精华标题 内容 GET **/
	public static String essencetitle(int page) {
		String url = URL + "al.html?mod=r&Page=" + page + "&UserID=" + GlobalVariable.UserID + GetXY();
		return url;
	}

	/** 不固定标题 内容 GET **/
	public static String nofixedtitle(String id, int page) {
		String url = URL + "al-" + id + ".html?Page=" + page + "&UserID=" + GlobalVariable.UserID + "&DN="
				+ GlobalVariable.IMEI + GetXY();
		return url;
	}

	/** 根据文章id 获取好不好文数据 GET **/
	public static String Smiles(String id) {
		String url = URL + "a-" + id + ".html?mod=os";
		return url;
	}

	/** 根据文章id 获取评论数据 GET **/
	public static String Contenturl(String id, String page) {
		String url = URL + "a-" + id + ".html?mod=review&Page=" + page;
		return url;
	}

	/** 根据文章id 好不好文 点击 GET **/
	public static String IsSmile(String id, String Praise) {
		String url = URL + "a-" + id + ".html?mod=gb&Praise=" + Praise + "&UserID=" + GlobalVariable.UserID + "&DN="
				+ GlobalVariable.IMEI;
		return url;
	}

	/** 第三方登陆ToKen注册 登陆 **/
	public static String ToKenLogin(String token, int Type, String Nickname) {
		String Token = "";
		if (Type == 0) {
			Token = "WeixXintoken=";
		} else if (Type == 1) {
			Token = "QQtoken=";
		} else {
			Token = "Sinatoken=";
		}
		String url = URL + "?Action=Login&" + Token + token + "&Nickname=" + Nickname;
		return url;
	}

	/** 获取草稿内容 **/
	public static String GetDraft() {
		String url = URL + "ar.html?" + GetInfo();
		return url;
	}

	// /** 获取社区草稿内容 **/
	// public static String GetDraft1(String ID) {
	// String url = URL + "ar.html?" + GetInfo() + "&ArticleClassNameID=" + ID;
	// return url;
	// }

	/** 编辑我的文字 **/
	public static String GetMyDraft(String ID) {
		String url = URL + "uiam.html?mod=modify&ID=" + ID + "&" + GetInfo();
		return url;
	}

	/** 获取审核文章的内容 **/
	public static String GetAuditArticle(int page) {
		String url = URL + "al.html?mod=audit&Page=" + page + "&" + GetInfo();
		return url;
	}

	/** 获取id文章的内容 **/
	public static String GetIDArticle(String ID) {
		String url = URL + "a-" + ID + ".html?" + GetInfo();
		return url;
	}

	/** 审核 好不好文 **/
	public static String SubmitAuditArticle(String id, int audit) {
		String url = URL + "a-" + id + ".html?mod=audit&audit=" + audit + "&" + GetInfo();
		return url;
	}

	/** 举报 **/
	public static String ReportAuditArticle(String id) {
		String url = URL + "a-" + id + ".html?mod=report&" + GetInfo();
		return url;
	}

	/** 获取个人资料 **/
	public static String GetMyData() {
		String url = URL + "user.html?" + GetInfo();
		return url;
	}

	/** 获取行业数据 **/
	public static String GetIndustryData() {
		String url = URL + "ic.html";
		return url;
	}

	/** 拜师、附近、同城同行、同城老乡 **/
	public static String Hometown(int type, int page, String id) {
		String url = null;
		if (type == 1) {// 拜师傅
			url = URL + "ui.html?mod=master&page=" + page + "&" + GetInfo() + GetXY();
		} else if (type == 2) {// 附近筑友
			url = URL + "ui.html?mod=friend&page=" + page + "&" + GetInfo() + GetXY();
		} else if (type == 3) {// 寻同城老乡
			url = URL + "ui.html?mod=hometown&page=" + page + "&" + GetInfo() + GetXY();
			// +"&Census=" +GlobalVariable.City;//记得是不用传
		} else if (type == 4) {// 寻同城同行
			url = URL + "ui.html?mod=industry&page=" + page + "&" + GetInfo() + GetXY();
		} else if (type == 5) {// 个人主页
			url = URL + "ui.html?id=" + id + "&" + GetInfo() + GetXY();
		} else if (type == 6) {// 搜索好友结果
			url = URL + "ui.html?mod=nickname&nickname=" + id + "&page=" + page + "&" + GetInfo() + GetXY();
		} else if (type == 7) {// 搜索师傅结果
			url = URL + "ui.html?mod=masternickname&nickname=" + id + "&page=" + page + "&" + GetInfo() + GetXY();
		}
		return url;
	}

	/** 各社区 **/
	public static String Hometown1(int type, int page) {
		String url = null;
		if (type == 1) {// 师徒社区
			url = URL + "al.html?mod=usermaster&page=" + page + "&" + GetInfo() + GetXY();
		} else if (type == 3) {// 同城社区
			url = URL + "al.html?mod=industry&Page=" + page + "&" + GetInfo() + GetXY() + "&CurrentAddress="
					+ GlobalVariable.City;
		} else if (type == 2) {// 老乡社区
			url = URL + "al.html?mod=hometown&Page=" + page + "&" + GetInfo() + GetXY();
		}
		return url;
	}

	/** 用户数统计 **/
	public static String OnOnlineUsers(int type) {
		String url = null;
		/** 同城用户数统计 **/
		if (type == 3) {
			url = URL + "al.html?mod=industry&" + GetInfo() + "&Action=OnlineUsers&CurrentAddress="
					+ GlobalVariable.City;
			/** 老乡用户数统计 **/
		} else if (type == 2) {
			url = URL + "al.html?mod=hometown&" + GetInfo() + "&Action=OnlineUsers";
		}
		return url;
	}

	/** 个人发布的文章 **/
	public static String GetPersonalarticles(int Page, String duserid) {
		String url = URL + "al.html?Page=" + Page + "&duserid=" + duserid + "&" + GetInfo();
		return url;
	}

	// /** 获取我发表的文字信息 **/
	// public static String GetMyManage(int page) {
	// String url = URL + "uiam.html?" + "page=" + page + "&" + GetInfo();
	// return url;
	// }

	/** 删除我发表的文字信息 **/
	public static String DelMyManage(String id) {
		String url = URL + "uiam.html?mod=del&ID=" + id + "&" + GetInfo();
		return url;
	}

	/** 添加好友\师傅 **/
	public static String AddFriend(boolean ismaster, String id) {
		String type = "";
		if (ismaster) {
			type = "addmaster";
		} else {
			type = "addfriend";
		}
		String url = URL + "ui.html?mod=" + type + "&ID=" + id + "&" + GetInfo();
		return url;
	}

	/** 删除列表好友\师徒关系 **/
	public static String DelFriend(int type, String id) {
		String url = "";
		if (type == 1) {
			url = URL + "mf.html?mod=del&ID=" + id + "&" + GetInfo();
		} else {
			url = URL + "ui.html?mod=delmaster&ID=" + id + "&" + GetInfo();
		}
		return url;
	}

	/** 我的好友 徒弟 师傅 列表 **/
	public static String FriendList(int type, int page) {
		String Str = "";
		if (type == 1) {// 好友
			Str = "mf.html?Page=";
		} else if (type == 2) { // 徒弟
			Str = "ma.html?mod=apprentice&Page=";
		} else if (type == 3) {// 师傅
			Str = "ma.html?mod=master&Page=";
		}
		String url = URL + Str + page + "&" + GetInfo();
		return url;
	}

	/** 聊天列表 **/
	public static String ChatMessageList(String Toid, int page) {
		String url = URL + "spp.html?Page=" + page + "&ToUserID=" + Toid + "&" + GetInfo();
		return url;
	}

	/** 消息列表 **/
	public static String MessageList(int page) {
		String url = URL + "message.html?" + GetInfo() + "&Page=" + page;
		return url;
	}

	/** 删除消息 **/
	public static String DelMessage(String id) {
		String url = URL + "message.html?mod=del&ID=" + id + "&" + GetInfo();
		return url;
	}

	/** 获取未读消息数量 **/
	public static String GriNoMessageNum(String id) {
		String url = URL + "message.html?mod=count&" + GetInfo();
		return url;
	}

	/** 消息的推送 **/
	public static String Push() {
		String url = URL + "message.html?mod=push&" + GetInfo();
		return url;
	}
}
