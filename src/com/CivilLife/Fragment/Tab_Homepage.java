package com.CivilLife.Fragment;

import java.util.ArrayList;
import java.util.concurrent.Executors;

import com.CivilLife.Base.BaseFragment;
import com.CivilLife.Entity.HomeonetitleEntity;
import com.CivilLife.Json.HomeoneTitleJson;
import com.CivilLife.MyAdapter.HomehorizontallvAdapter;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Variable.RequestCode;
import com.CivilLife.Widget.CustomViewPager;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.MyView.Widget.HorizontalListView;
import com.app.civillife.AuditActivity;
import com.app.civillife.PublishActivity;
import com.app.civillife.R;
import com.umeng.analytics.MobclickAgent;

import Requset_getORpost.RequestListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 首页片段
 * 
 * @author Administrator
 * 
 */
public class Tab_Homepage extends BaseFragment {
	// private View layout;
	private CustomViewPager mViewpagerR, mtwoViewpagerR0, mtwoViewpagerR1, mtwoViewpagerR2,mtwoViewpagerR3;
	private TextView mtoptitle;
	private ImageView image_back, image_logo;
	private int seleorttwo = -1; // 标记你选中的二级菜单
	ArrayList<HomeonetitleEntity> Data = new ArrayList<HomeonetitleEntity>();
	ArrayList<HomeonetitleEntity> Data1 = new ArrayList<HomeonetitleEntity>();
	// ArrayList<HomeonetitleEntity> als = new ArrayList<HomeonetitleEntity>();
	String seleorttitle1 = "";
	String seleorttitle2 = "";
	private HomehorizontallvAdapter titleadapter, twotitleadapter;
	private HorizontalListView hlv_titlename, hlv_twotitlename;
	private MyAdpter myAdpter;
	private MytwoAdpter mytwoAdpter;
	private CustomViewPager[] vps;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return FragmentCache(R.layout.tab_home, inflater, container);
	}

	@Override
	protected void initViews() {
		mtoptitle = (TextView) findViewById(R.id.tv_toptitle);
		image_back = (ImageView) findViewById(R.id.image_back);
		image_logo = (ImageView) findViewById(R.id.image_logo);
		mViewpagerR = (CustomViewPager) findViewById(R.id.viewpager);
		mtwoViewpagerR0 = (CustomViewPager) findViewById(R.id.twoviewpager0);
		mtwoViewpagerR1 = (CustomViewPager) findViewById(R.id.twoviewpager1);
		mtwoViewpagerR2 = (CustomViewPager) findViewById(R.id.twoviewpager2);
		mtwoViewpagerR3 = (CustomViewPager) findViewById(R.id.twoviewpager3);
		vps = new CustomViewPager[] { mtwoViewpagerR0, mtwoViewpagerR1, mtwoViewpagerR2,mtwoViewpagerR3 };
		mViewpagerR.setMove(false, false);
		mtwoViewpagerR0.setMove(false, false);
		mtwoViewpagerR1.setMove(false, false);
		mtwoViewpagerR2.setMove(false, false);
		mtwoViewpagerR3.setMove(false, false);
		hlv_titlename = (HorizontalListView) findViewById(R.id.hlv_titlename);// 横向listview
		hlv_twotitlename = (HorizontalListView) findViewById(R.id.hlv_twotitlename);// 横向二级listview
	}

	@Override
	protected void initEvents() {
		image_back.setOnClickListener(this);
		findViewById(R.id.image_audit).setOnClickListener(this);
		findViewById(R.id.image_add_article).setOnClickListener(this);

		titleadapter = new HomehorizontallvAdapter(mApplication, getActivity(), Data);
		hlv_titlename.setAdapter(titleadapter);
		hlv_titlename.setOnItemClickListener(new TitleOnItemClickListener());
		hlv_twotitlename.setOnItemClickListener(new TitletwoOnItemClickListener());
		// 获取片段管理器 上面一个是Activity的 下面一个是在片段中获取片段管理器的
		// FragmentManager fm = getSupportFragmentManager();
		FragmentManager fm = getChildFragmentManager();
		myAdpter = new MyAdpter(fm);
		mViewpagerR.setAdapter(myAdpter);
		mViewpagerR.setOffscreenPageLimit(1);

		twotitleadapter = new HomehorizontallvAdapter(mApplication, getActivity(), Data1);
		hlv_twotitlename.setAdapter(twotitleadapter);

		// mytwoAdpter = new MytwoAdpter(fm);
		// mtwoViewpagerR.setAdapter(mytwoAdpter);

	}

	@Override
	protected void init() {
		new RequestTask(getActivity(), hometitlelistener, true, true, "Loading")
				.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.homeonetitle);
	}

	// 获取标题列表
	RequestListener hometitlelistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			HomeoneTitleJson readJsonToSendmsgObject = HomeoneTitleJson.readJsonToSendmsgObject(getActivity(),
					jsonObject);
			if (readJsonToSendmsgObject == null) {
				return;
			}
			Data.add(new HomeonetitleEntity(0 + "", "最新"));
			Data.add(new HomeonetitleEntity(0 + "", "精华"));// 先加载固定的两个标题
			Data.addAll(1, readJsonToSendmsgObject.getAl());
			titleadapter.setmDatas(Data);
			// als.clear();
			// als.addAll(Data);
			titleadapter.notifyDataSetChanged();
			myAdpter.notifyDataSetChanged();
			GlobalVariable.Data.clear();
			GlobalVariable.Data = readJsonToSendmsgObject.getAl();
			// GlobalVariable.Data.remove(GlobalVariable.Data.size()-1);
			// GlobalVariable.Data.remove(0);
		}

		@Override
		public void responseException(String errorMessage) {
			showmsg(errorMessage);
			Data.add(new HomeonetitleEntity(0 + "", "最新"));
			Data.add(new HomeonetitleEntity(0 + "", "精华"));// 先加载固定的两个标题
			myAdpter.notifyDataSetChanged();
		}
	};

	@SuppressWarnings("static-access")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_audit:// 审核
			if (tologin()) {
				return;
			}
			startActivity(AuditActivity.class);
			break;
		case R.id.image_add_article:// 发布
			if (tologin()) {
				return;
			}
			Bundle bundle = new Bundle();
			bundle.putInt("TYPE", 1);
			bundle.putString("seleorttitle1", seleorttitle1);
			bundle.putString("seleorttitle2", seleorttitle2);
			startActivityForResult(PublishActivity.class, bundle, RequestCode.publiccode);
			break;
		case R.id.image_back:// 二级返回
			for (int i = 0; i < vps.length; i++) {
				vps[i].setVisibility(View.GONE);
			}
			mViewpagerR.setVisibility(View.VISIBLE);
			hlv_titlename.setVisibility(View.VISIBLE);// 横向listview
			hlv_twotitlename.setVisibility(View.GONE);
			image_back.setVisibility(View.GONE);
			image_logo.setVisibility(View.VISIBLE);
			mtoptitle.setVisibility(View.GONE);
			titleadapter.selseortitem = 0;

			titleadapter.notifyDataSetChanged();
			myAdpter.notifyDataSetChanged();
			mViewpagerR.setCurrentItem(0);
			// mtwoViewpagerR.re
			break;
		default:
			break;
		}
	}

	// 一级 viewpager 适配器
	class MyAdpter extends android.support.v4.app.FragmentPagerAdapter {

		public MyAdpter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int arg0) {
			HomeonetitleEntity object = (HomeonetitleEntity) Data.get(arg0);
			if (object.getArticleClassName().equals("最新")) {
				return new Homeclassify_Fragment("-1");
			} else if (object.getArticleClassName().equals("精华")) {
				return new Homeclassify_Fragment("-2");
			} else {
				return new Homeclassify_Fragment(object.getID());
			}
		}

		@Override
		public int getCount() {
			return Data.size();
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	// 一级 横向listview item点击事件
	class TitleOnItemClickListener implements OnItemClickListener {

		@SuppressWarnings("static-access")
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			HomeonetitleEntity object = (HomeonetitleEntity) titleadapter.getDatas().get(arg2);
			seleorttitle1 = object.getID();
			if (object.getID().equals("3")) {//文苑
				seleorttwo = 0;
				new RequestTask(getActivity(), hometwotitlelistener, true, true, "Loading")
						.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.hometwotitle(object.getID()));
				mtoptitle.setText(object.getArticleClassName());
			} else if (object.getID().equals("5")) {//学习
				seleorttwo = 1;
				new RequestTask(getActivity(), hometwotitlelistener, true, true, "Loading")
						.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.hometwotitle(object.getID()));
				mtoptitle.setText(object.getArticleClassName());
			} else if (object.getID().equals("6")) {//乐享
				seleorttwo = 2;
				new RequestTask(getActivity(), hometwotitlelistener, true, true, "Loading")
						.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.hometwotitle(object.getID()));
				mtoptitle.setText(object.getArticleClassName());
			} else if (object.getID().equals("4")) {//机会
				seleorttwo = 3;
				new RequestTask(getActivity(), hometwotitlelistener, true, true, "Loading")
						.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.hometwotitle(object.getID()));
				mtoptitle.setText(object.getArticleClassName());
			} else {
				mViewpagerR.setCurrentItem(arg2);
				myAdpter.notifyDataSetChanged();
			}
			titleadapter.selseortitem = arg2;
			titleadapter.notifyDataSetChanged();
		}
	}

	// 二级 横向listview item点击事件
	class TitletwoOnItemClickListener implements OnItemClickListener {

		@SuppressWarnings("static-access")
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			HomeonetitleEntity item = (HomeonetitleEntity) twotitleadapter.getItem(arg2);
			seleorttitle2 = item.getID();
			twotitleadapter.selseortitem = arg2;
			twotitleadapter.notifyDataSetChanged();
			vps[seleorttwo].setCurrentItem(arg2);
			mytwoAdpter.notifyDataSetChanged();
		}
	}

	// 二级 viewpager 适配器
	class MytwoAdpter extends android.support.v4.app.FragmentPagerAdapter {

		public MytwoAdpter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int arg0) {
			HomeonetitleEntity object = (HomeonetitleEntity) Data1.get(arg0);
			// if (object.getArticleClassName().equals("最新")) {
			// return new Homeclassify_Fragment("-1");
			// } else if (object.getArticleClassName().equals("精华")) {
			// return new Homeclassify_Fragment("-2");
			// } else {
			return new Homeclassify_Fragment(object.getID());
			// }
		}

		@Override
		public int getCount() {
			return Data1.size();
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	// 获取二级标题内容
	RequestListener hometwotitlelistener = new RequestListener() {

		@SuppressWarnings("static-access")
		@Override
		public void responseResult(String jsonObject) {
			HomeoneTitleJson readJsonToSendmsgObject = HomeoneTitleJson.readJsonToSendmsgObject(getActivity(),
					jsonObject);
			if (readJsonToSendmsgObject == null) {
				return;
			}

			hlv_titlename.setVisibility(View.GONE);// 横向listview
			hlv_twotitlename.setVisibility(View.VISIBLE);
			mtoptitle.setVisibility(View.VISIBLE);
			image_logo.setVisibility(View.GONE);
			image_back.setVisibility(View.VISIBLE);

			Data1.clear();
			Data1.addAll(readJsonToSendmsgObject.getAl());
			twotitleadapter.notifyDataSetChanged();

			twotitleadapter.selseortitem = 0;

			mytwoAdpter = new MytwoAdpter(getFragmentManager());
			mViewpagerR.setVisibility(View.GONE);
			for (int i = 0; i < vps.length; i++) {
				if (seleorttwo == i) {
					vps[i].setVisibility(View.VISIBLE);
					vps[i].setAdapter(mytwoAdpter);
					vps[i].setCurrentItem(0);
				} else {
					vps[i].setVisibility(View.GONE);
				}
			}
		}

		@Override
		public void responseException(String errorMessage) {
			showmsg(errorMessage);
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if (data != null) {
		// if (mytwoAdpter != null) {
		// mytwoAdpter.notifyDataSetChanged();
		// }
		// if (myAdpter != null) {
		// myAdpter.notifyDataSetChanged();
		// }
		// }
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("Tab_Homepage"); // 统计页面，"MainScreen"为页面名称，可自定义
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("Tab_Homepage");// 统计页面，"MainScreen"为页面名称，可自定义
	}
}
