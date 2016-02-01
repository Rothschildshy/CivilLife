package com.CivilLife.Fragment;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseFragment;
import com.CivilLife.Entity.InfoHomeEntity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.CircleImageView;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.app.civillife.ArticleListActivity;
import com.app.civillife.ManageActivity;
import com.app.civillife.MessageActivity;
import com.app.civillife.R;
import com.app.civillife.Util.ImagePagerActivity;
import com.app.civillife.Util.ViewLoadManager2;
import com.app.civillife.Util.ViewLoadManager2.IMAGE_LOAD_TYPE;
import com.aysy_mytool.GetAgeOrConstellation;
import com.aysy_mytool.Time;

import Downloadimage.ImageUtils;
import Requset_getORpost.RequestListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/***
 * 个人中心主页片段
 * 
 * @author Administrator
 * 
 */
@SuppressLint({ "ResourceAsColor", "ValidFragment" })
public class MyInFo_Fragment extends BaseFragment {
	private InfoHomeEntity info;
	private TextView mTx_Name;
	private TextView mTx_Age;
	private TextView mTx_Constellation;
	private TextView mTx_EssayNum;
	private TextView mTx_PraiseNum;
	private RelativeLayout mRa_Age;
	private RelativeLayout mRa_BG;
	private RelativeLayout mRa_Constellation;
	private CircleImageView mImage_Poto;
	private ImageView mImage_Add;
	private ImageView mImage_Messgae;
	private Button mTx_Industry;
	private Button mTx_Hometown;
	private Button mTx_Location;
	private Button mTx_Num;
	private Button mTx_Phone;
	private boolean IsFriends = false;// 判断是否好友
	private boolean Master_Apprentice = false;// 判断是否是师徒关系
	private ArrayList<String> Picimageurllist = new ArrayList<String>();// 头像图片地址容器

	public MyInFo_Fragment(InfoHomeEntity info, boolean Master_Apprentice) {

		this.info = info;
		this.Master_Apprentice = Master_Apprentice;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return NoFragmentCache(R.layout.layout_info_viewpage_item, inflater, container);
	}

	@Override
	protected void initViews() {
		mTx_Name = (TextView) findViewById(R.id.tx_name);
		mTx_Age = (TextView) findViewById(R.id.tx_age);
		mTx_Constellation = (TextView) findViewById(R.id.tx_constellation);
		mTx_EssayNum = (TextView) findViewById(R.id.tx_essay_num);
		mTx_PraiseNum = (TextView) findViewById(R.id.tx_praise_num);
		mRa_BG = (RelativeLayout) findViewById(R.id.layout_bg);
		mRa_Age = (RelativeLayout) findViewById(R.id.layout_age);
		mRa_Constellation = (RelativeLayout) findViewById(R.id.layout_constellation);
		mImage_Poto = (CircleImageView) findViewById(R.id.image_poto);
		mImage_Add = (ImageView) findViewById(R.id.image_add);
		mImage_Messgae = (ImageView) findViewById(R.id.image_message);
		mTx_Industry = (Button) findViewById(R.id.btn_money);
		mTx_Hometown = (Button) findViewById(R.id.btn_hometown);
		mTx_Location = (Button) findViewById(R.id.btn_location);
		mTx_Num = (Button) findViewById(R.id.btn_num);
		mTx_Phone = (Button) findViewById(R.id.btn_phone);
	}

	@Override
	protected void initEvents() {
		mImage_Poto.setOnClickListener(this);
		mImage_Add.setOnClickListener(this);
		mImage_Messgae.setOnClickListener(this);
		findViewById(R.id.layout_statistics).setOnClickListener(this);
	}

	@Override
	protected void init() {
		mTx_Name.setText(info.getNickname());// 设置昵称
		mTx_EssayNum.setText(info.getArticlCount());// 设置文章数量
		mTx_PraiseNum.setText(info.getArticlePraise());// 设置点赞数量
		// 判断是否是自己
		if (info.getID().equals(GlobalVariable.UserID)) {
			mImage_Add.setEnabled(false);
			mImage_Messgae.setEnabled(false);
		} else if (info.getUserFriends().equals("0")) {// 不是好友
			IsFriends = false;
			mImage_Add.setImageResource(R.drawable.btn_add_sele);
			mImage_Messgae.setEnabled(false);
		} else {// 是好友
			IsFriends = true;
			mImage_Add.setImageResource(R.drawable.btn_del_sele);
			mImage_Messgae.setEnabled(true);
		}
		// 设置性别
		if (info.getSex().equals("1")) {
			mRa_Age.setBackgroundResource(R.drawable.nearby_gender_male);
			mRa_Constellation.setBackgroundResource(R.drawable.constellation_man);
		} else {
			mRa_Age.setBackgroundResource(R.drawable.nearby_gender_female);
			mRa_Constellation.setBackgroundResource(R.drawable.constellation_woman);
		}
		// 设置生日星座
		try {
			String birthday = info.getBirthday();
			String Constellation = GetAgeOrConstellation.GetConstellation(birthday);
			String age = GetAgeOrConstellation.getAge(birthday);

			mTx_Age.setText(age);
			mTx_Constellation.setText(Constellation);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 设置头像
		String picUrl = info.getPicUrl();
		Picimageurllist.add(picUrl);
		if (!TextUtils.isEmpty(picUrl)) {
			ImageUtils.loadImage1(getActivity(), info.getPicUrl(), mImage_Poto, R.drawable.ic_my_nolog_selector,
					R.drawable.ic_my_nolog_selector, GlobalVariable.WifiDown);
		}
		// 设置背景大罩
		String bigPicUrl = info.getBigPicUrl();
		if (!TextUtils.isEmpty(bigPicUrl)) {
			new ViewLoadManager2(getActivity()).setViewBackground(IMAGE_LOAD_TYPE.FILE_URL, bigPicUrl, mRa_BG);
		}
		mTx_Industry.setText(info.getIndustry());
		mTx_Hometown.setText(info.getCensus());
		mTx_Location.setText(info.getCurrentAddress());
		mTx_Phone.setText(info.getPhonemodel());
		String addTime = Time.AddTime(info.getAddTime());
		mTx_Num.setText(addTime);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_statistics:// 跳转到文章列表
			Bundle bundle = new Bundle();
			// 如果是自己的话就调整到看自己的文字列表
			if (info.getID().equals(GlobalVariable.UserID)) {
				startActivity(ManageActivity.class, bundle);
			} else {
				bundle.putInt("type", 4);
				bundle.putString("UserName", info.getNickname());
				bundle.putString("Userid", info.getID());
				startActivity(ArticleListActivity.class, bundle);
			}
			break;
		case R.id.image_add:// 添加好友
			AddFriends();
			break;
		case R.id.image_message:// 发送消息
			Bundle bundle2 = new Bundle();
			bundle2.putString("ToUserId", info.getID());
			bundle2.putString("ToUserName", info.getNickname());
			startActivity(MessageActivity.class, bundle2);
			break;
		case R.id.image_poto:
			Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, Picimageurllist);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	// 添加好友
	private void AddFriends() {
		if (IsFriends) {// 是好友解除好友关系
			Delfriend(info.getID());
		} else {// 不是进行添加
			if (Master_Apprentice) {// 添加师傅
				new RequestTask(getActivity(), listener, false, true, "发送拜师请求").executeOnExecutor(
						Executors.newCachedThreadPool(), Httpurl.AddFriend(Master_Apprentice, info.getID()));
			} else {// 添加好友
				new RequestTask(getActivity(), listener, false, true, "发送好友请求").executeOnExecutor(
						Executors.newCachedThreadPool(), Httpurl.AddFriend(Master_Apprentice, info.getID()));
			}

		}
	}

	RequestListener listener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			PublicUpJson publicjson = PublicUpJson.readJsonToSendmsgObject(getActivity(), jsonObject);
			if (publicjson == null) {
				showShortToast("请求已发送，请等待对方确认！");
				return;
			}
			PublicEntity publicEntity = publicjson.getAl().get(0);
			String status = publicEntity.getStatus();
			if (status.equals("1")) {
				IsFriends = true;
				if (!Master_Apprentice) {// 添加好友才可以删除好友 师徒不行解除关系
					mImage_Add.setImageResource(R.drawable.btn_del_sele);
				} else {
					mImage_Add.setEnabled(false);
				}
				showShortToast(publicEntity.getMessage());
			} else {
				IsFriends = false;
				mImage_Add.setImageResource(R.drawable.btn_add_sele);
				showShortToast(publicEntity.getMessage());
			}

		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
		}
	};

	// 解除好友关系
	public void Delfriend(final String id) {
		PromptDialogs("确定解除好友关系", new OnClickListener() {

			@Override
			public void onClick(View v) {
				new RequestTask(getActivity(), DELlistener, false, true, "解除好友关系")
						.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.DelFriend(1, id));
			}
		});
	}

	RequestListener DELlistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			PublicUpJson publicjson = PublicUpJson.readJsonToSendmsgObject(getActivity(), jsonObject);
			if (publicjson == null) {
				IsFriends = true;
				mImage_Add.setImageResource(R.drawable.btn_del_sele);
				return;
			}
			PublicEntity publicEntity = publicjson.getAl().get(0);
			String status = publicEntity.getStatus();
			if (status.equals("1")) {
				IsFriends = false;
				mImage_Add.setImageResource(R.drawable.btn_add_sele);
			} else {
				showShortToast(publicEntity.getMessage());
			}

		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
		}
	};

}
