package com.CivilLife.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.http.message.BasicNameValuePair;

import com.CivilLife.Base.BaseActivity;
import com.CivilLife.Base.BaseUpload;
import com.CivilLife.Entity.IndustryEntity;
import com.CivilLife.Entity.MyInfoEntity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.GetIndustryJson;
import com.CivilLife.Json.GetMyIngoJson;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.Widget.CircleImageView;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.CivilLife.net.ReturnAL;
import com.app.civillife.R;
import com.app.civillife.SeleImageActivity;
import com.app.civillife.SeleImageActivity2;
import com.app.civillife.Util.GetDistance;
import com.app.civillife.Util.ImagePagerActivity;
import com.aysy_mytool.GetAgeOrConstellation;
import com.aysy_mytool.PubUtils;
import com.aysy_mytool.SpUtils;
import com.aysy_mytool.Time;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;

import Downloadimage.ImageCompression;
import Downloadimage.ImageUtils;
import Downloadimage.SelectPicActivity;
import Requset_getORpost.RequestListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 我的个人中心资料
 * 
 * @author mac
 * 
 */
@SuppressLint("SimpleDateFormat")
public class MyinfoActivity extends BaseActivity  {

	private CircleImageView mIm_Pic;
	private ImageView mIm_Background;
	private Button mBtn_Save;
	private EditText mEd_Speciality;
	private TextView mEd_YearsOfWorking;
//	private EditText mEd_Occupation;
//	private EditText mEd_Education;
	private TextView mTx_Hometown;
	private Spinner Spinner_Profession;
	private EditText mEd_Phonemodel;
	private TextView mTx_NativePlace;
	private TextView mTx_Birthday;
	private TextView mTx_Sex;
	private TextView mTx_Hint;
	private EditText mEd_Mailbox;
	private EditText mEd_Nickname;
	private EditText mEd_Affirmpw;
	private EditText mEd_PassWord;
	private EditText mEd_Name;
	private RelativeLayout mRa_Password;
	private RelativeLayout mRa_Affirmpw;
	private RelativeLayout mRa_ModificationPhoto;
	private RelativeLayout mRa_Birthday;
	private RelativeLayout mRa_NativePlace;
	private RelativeLayout mRa_Hometown;
	private RelativeLayout mRa_Background;
	private RadioGroup mRg_Sex;
	private RadioButton mRd_Man;
	private RadioButton mRd_Woman;
	private String DATEPICKER_TAG = "datepicker";
	// 本地头像设置的图片路径
	private String PIClocalFilePath = "";
	private String picUrl = "";
	// 本地大罩设置的图片路径
	private String BiglocalFilePath = "";
	private String BigPicUrl = "";
	private Bitmap bm;
	private Calendar calendar = Calendar.getInstance();
	
	private String birthday;
	private TextView mTx_Modification;
	private List<String> Id_list = new ArrayList<String>();
	private List<String> Name_list = new ArrayList<String>();
	private String ProfessionID = "";// 行业ID
	private ArrayList<String> Picimageurllist = new ArrayList<String>();// 头像图片地址容器
	private ArrayList<String> BGimageurllist = new ArrayList<String>();// 大罩图片地址容器
	private int Sex = 1;

	private boolean isUpPic = false;// 头像上传是否完成  
	private boolean isUpBig = false;// 大罩上传是否完成

	private ProgressBar locationPB;
	private String NickName;  
	OnDateSetListener onDateSetListener=new OnDateSetListener() {//生日 
		
		@Override
		public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
			try {
				birthday = year + "/" + month + "/" + day;
				String Constellation = GetAgeOrConstellation
						.GetConstellation(birthday);
				String age = GetAgeOrConstellation.getAge(birthday);
				mTx_Birthday.setText(age + " • " + Constellation);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	OnDateSetListener onDateSetListener1=new OnDateSetListener() {//毕业时间
		
		@Override
		public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
			mEd_YearsOfWorking.setText(year + "/" + month + "/" + day );
			ed_years_working.setText(Time.handYeas(year + "/" + month + "/" + day));
		}
	};
	DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(//生日弹窗
			onDateSetListener, calendar.get(Calendar.YEAR),
			calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
			false);
	DatePickerDialog datePickerDialog1 = DatePickerDialog.newInstance(//毕业时间
			onDateSetListener1, calendar.get(Calendar.YEAR),
			calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
			false);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfo);  
		initViews();
		initEvents();
		init();
	}

	@Override
	protected void initViews() {
		mTx_Modification = (TextView) findViewById(R.id.tx_modification);
		mIm_Pic = (CircleImageView) findViewById(R.id.image_pic);
		mIm_Background = (ImageView) findViewById(R.id.image_background);
		mEd_Name = (EditText) findViewById(R.id.ed_name);
		ed_graduatedSchool = (EditText) findViewById(R.id.ed_graduatedSchool);
		mEd_PassWord = (EditText) findViewById(R.id.ed_password);
		mEd_Affirmpw = (EditText) findViewById(R.id.ed_affirmpw);
		mEd_Nickname = (EditText) findViewById(R.id.ed_nickname);
		mEd_Mailbox = (EditText) findViewById(R.id.ed_mailbox);
		mTx_Sex = (TextView) findViewById(R.id.tx_sex);
		ed_years_working = (TextView) findViewById(R.id.ed_years_working);//毕业时间
		mTx_Hint = (TextView) findViewById(R.id.tx_hint);
		mTx_Birthday = (TextView) findViewById(R.id.tx_birthday);
		mTx_NativePlace = (TextView) findViewById(R.id.tx_native_place);
		mEd_Phonemodel = (EditText) findViewById(R.id.ed_phonemodel);
		Spinner_Profession = (Spinner) findViewById(R.id.spinner_profession);
		mTx_Hometown = (TextView) findViewById(R.id.tx_hometown);
		sp_schooling = (Spinner) findViewById(R.id.sp_schooling);
		  
		TackType(sp_schooling, GlobalVariable.al_schooling);
		
		
		
		sp_schooling.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Education = GlobalVariable.al_schooling.get(position).toString();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		sp_schooling.setEnabled(false);
		sp_professionaltitle = (Spinner) findViewById(R.id.sp_professionaltitle);
		
		sp_professionaltitle.setEnabled(false);
		TackType(sp_professionaltitle, GlobalVariable.al_professionaltitle);
		sp_professionaltitle.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Occupation = GlobalVariable.al_professionaltitle.get(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		mEd_YearsOfWorking = (TextView) findViewById(R.id.ed_years_of_working);//毕业时间  
		mEd_Speciality = (EditText) findViewById(R.id.ed_speciality);
		mRa_Password = (RelativeLayout) findViewById(R.id.layout_password);
		mRa_Affirmpw = (RelativeLayout) findViewById(R.id.layout_affirmpw);
		mRa_ModificationPhoto = (RelativeLayout) findViewById(R.id.laout_modification_photo);
		mRa_Birthday = (RelativeLayout) findViewById(R.id.layout_birthday);
		mRa_NativePlace = (RelativeLayout) findViewById(R.id.layout_native_place);
		mRa_Hometown = (RelativeLayout) findViewById(R.id.layout_hometown);
		mRa_Background = (RelativeLayout) findViewById(R.id.layout_background);
		mBtn_Save = (Button) findViewById(R.id.btn_save);
		mRg_Sex = (RadioGroup) findViewById(R.id.radioGroup_sex);
		mRd_Man = (RadioButton) findViewById(R.id.radio_man);
		mRd_Woman = (RadioButton) findViewById(R.id.radio_woman);
		locationPB = (ProgressBar) findViewById(R.id.progressBar1);
	}

	@Override
	protected void initEvents() {
		findViewById(R.id.image_back).setOnClickListener(this);
		mEd_YearsOfWorking.setOnClickListener(this);
		mTx_Modification.setOnClickListener(this);
		mIm_Pic.setOnClickListener(this);
		mIm_Background.setOnClickListener(this);
		mBtn_Save.setOnClickListener(this);
		mRa_ModificationPhoto.setOnClickListener(this);
		mRa_Birthday.setOnClickListener(this);
		mRa_NativePlace.setOnClickListener(this);
		mRa_Hometown.setOnClickListener(this);
		mRa_Background.setOnClickListener(this);
		mRa_ModificationPhoto.setEnabled(false);
		mRa_Birthday.setEnabled(false);
		mRa_NativePlace.setEnabled(false);
		mRa_Hometown.setEnabled(false);
		mRa_Background.setEnabled(false);
	}

	@Override
	protected void init() {
		GetDistance.location(mApplication, null).start();
		mRg_Sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				if (checkedId == mRd_Man.getId()) {
					mTx_Sex.setText("男");
					Sex = 1;
				} else if (checkedId == mRd_Woman.getId()) {
					mTx_Sex.setText("女");
					Sex = 2;
				}
			}
		});
		
		String PhoneBrand = new PubUtils().GetPhoneBrand();// 获取手机品牌
		mEd_Phonemodel.setText(PhoneBrand);

		// 获取个人资料
		new RequestTask(this, listener, false, true, "加载中").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl
				.GetMyData());
		// 获取行业数据
		new RequestTask(this, Industrylistener, false, false, "加载行业中")
				.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.GetIndustryData());

		Spinner_Profession
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						ProfessionID = Id_list.get(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		Spinner_Profession.setEnabled(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image_back:// 取消
			SetResult(10002, new Bundle());
			finish();
			break;
		case R.id.tx_modification:// 修改资料
			Modification(true);
			break;
		case R.id.laout_modification_photo:// 修改上传头像 裁剪成正方形
			startActivityForResult(SeleImageActivity.class, null, 10001,
					R.anim.push_bootom_in2, R.anim.start);
			break;
		case R.id.image_pic:// 查看头像大图
			if (Picimageurllist.size() == 0) {
				showShortToast("未设置头像！");
				return;
			}
			Intent intent = new Intent(this, ImagePagerActivity.class);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
					Picimageurllist);
			intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
			startActivity(intent);
			break;
		case R.id.layout_birthday:// 修改生日
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			String date = sdf.format(new java.util.Date());
			int TthisYear = Integer.valueOf(date);// 获取当前年份
			datePickerDialog.setYearRange(TthisYear - 50, TthisYear);// 获取50年之内的日期
			datePickerDialog.setVibrate(true);// 是否震动
			datePickerDialog.setCloseOnSingleTapDay(false);// 是否选择完日期就关闭
			datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
			break;
		case R.id.ed_years_of_working:// 修改生日
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
			String date1 = sdf1.format(new java.util.Date());
			int TthisYear1 = Integer.valueOf(date1);// 获取当前年份
			datePickerDialog1.setYearRange(TthisYear1 - 50, TthisYear1);// 获取50年之内的日期
			datePickerDialog1.setVibrate(true);// 是否震动
			datePickerDialog1.setCloseOnSingleTapDay(false);// 是否选择完日期就关闭
			datePickerDialog1.show(getSupportFragmentManager(), DATEPICKER_TAG);
			break;
		case R.id.layout_native_place:// 修改出没地
			locationPB.setVisibility(View.VISIBLE);
			break;
		case R.id.ed_phonemodel:// 修改手机型号

			break;
		case R.id.layout_hometown:// 修改家乡
			startActivityForResult(CitiesActivity.class, null, 1001,
					R.anim.push_bootom_in2, R.anim.start);
			break;
		case R.id.layout_background:// 修改大罩 裁剪成长方形 1：1.8比例
			startActivityForResult(SeleImageActivity2.class, null, 10002,
					R.anim.push_bootom_in2, R.anim.start);
			break;
		case R.id.image_background:// 查看大罩
			if (BGimageurllist.size() == 0) {
				showShortToast("未设置大罩！");
				return;
			}
			Intent intent2 = new Intent(this, ImagePagerActivity.class);
			intent2.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
					BGimageurllist);
			intent2.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
			startActivity(intent2);

			break;
		case R.id.btn_save:// 保存
			SaveData();
			break;
		default:
			break;
		}
	}

	// 获取行业数据
	RequestListener Industrylistener = new RequestListener() {
		@Override
		public void responseResult(String jsonObject) {
			GetIndustryJson Industryjson = GetIndustryJson
					.readJsonToSendmsgObject(MyinfoActivity.this, jsonObject);
			if (Industryjson == null) {
				return;
			}
			ArrayList<IndustryEntity> list = Industryjson.getAl();
			// IndustryEntity entity = Industryjson.getAl().get(0);

			for (int i = 0; i < list.size(); i++) {
				Id_list.add(list.get(i).getID());
				Name_list.add(list.get(i).getIndustryClassName());
			}
			// 适配器
			ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(
					MyinfoActivity.this, android.R.layout.simple_spinner_item,
					Name_list);
			// 设置样式
			arr_adapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// 加载适配器
			Spinner_Profession.setAdapter(arr_adapter);
		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
		}
	};
	// 获取资料的回调 GetMyIngoJson
	RequestListener listener = new RequestListener() {
		@Override
		public void responseResult(String jsonObject) {
			GetMyIngoJson publicjson = GetMyIngoJson.readJsonToSendmsgObject(
					MyinfoActivity.this, jsonObject);
			if (publicjson == null) {
				return;
			}
			MyInfoEntity infoEntity = publicjson.getAl().get(0);
			InitData(infoEntity);
		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
		}
	};

	private void InitData(MyInfoEntity infoEntity) {
		// 设置头像
		if (!TextUtils.isEmpty(infoEntity.getPicUrl())) {
			// 获取后台返回的图片不为空时显示
			ImageUtils.loadImage1(MyinfoActivity.this, infoEntity.getPicUrl(),
					mIm_Pic, R.drawable.ic_my_nolog_selector,
					R.drawable.ic_my_nolog_selector, GlobalVariable.WifiDown);
			picUrl = infoEntity.getPicUrl();
			GlobalVariable.UserImage = infoEntity.getPicUrl();
		} else if (!TextUtils.isEmpty(GlobalVariable.UserImage)) {
			// 如果后台返回数据为空 缓存的图片不为空显示缓存的图片
			ImageUtils.loadImage1(MyinfoActivity.this,
					GlobalVariable.UserImage, mIm_Pic,
					R.drawable.ic_my_nolog_selector,
					R.drawable.ic_my_nolog_selector, GlobalVariable.WifiDown);
		}
		// 设置大罩
		if (!TextUtils.isEmpty(infoEntity.getBigPicUrl())) {
			// 获取后台返回的图片不为空时显示
			ImageUtils.loadImage(MyinfoActivity.this,
					infoEntity.getBigPicUrl(), mIm_Background,
					GlobalVariable.WifiDown);
			BigPicUrl = infoEntity.getBigPicUrl();
		}
		// 名称 不可修改
		mEd_Name.setText(infoEntity.getUserName());
		GlobalVariable.UserName = infoEntity.getUserName();
		if (!TextUtils.isEmpty(infoEntity.getPicUrl())) {
			Picimageurllist.add(infoEntity.getPicUrl());
		}
		if (!TextUtils.isEmpty(infoEntity.getBigPicUrl())) {
			BGimageurllist.add(infoEntity.getBigPicUrl());
		}
		// 昵称
		if (!TextUtils.isEmpty(infoEntity.getNickname())) {
			mEd_Nickname.setText(infoEntity.getNickname());
			GlobalVariable.Nickname = infoEntity.getNickname();
		} else if (!TextUtils.isEmpty(GlobalVariable.Nickname)) {
			mEd_Nickname.setText(GlobalVariable.Nickname);
		} else {
			mEd_Nickname.setHint("未设置");
		} // 性别
		if (TextUtils.isEmpty(infoEntity.getSex())) {
			mTx_Sex.setText("未设置");
		} else {
			mTx_Sex.setText(infoEntity.getSex());
			if (infoEntity.getSex().equals("男")) {
				Sex = 1;
				mRd_Man.setChecked(true);
				mRd_Woman.setChecked(false);
			} else {
				mRd_Man.setChecked(false);
				mRd_Woman.setChecked(true);
				Sex = 2;
			}
		} // 邮箱
		if (TextUtils.isEmpty(infoEntity.getEmail())) {
			mEd_Mailbox.setHint("未设置");
		} else {
			mEd_Mailbox.setText(infoEntity.getEmail());
		} // 年龄
		if (TextUtils.isEmpty(infoEntity.getBirthday())) {
			mTx_Birthday.setText("未设置");
			birthday = "";
		} else {
			try {
				birthday = infoEntity.getBirthday();
				String Constellation = GetAgeOrConstellation
						.GetConstellation(birthday);
				String age = GetAgeOrConstellation.getAge(birthday);
				mTx_Birthday.setText(age + " • " + Constellation);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} // 出没地
			// if (TextUtils.isEmpty(infoEntity.getCurrentAddress())) {
			// mTx_NativePlace.setText("未设置");
			// } else {
			// mTx_NativePlace.setText(infoEntity.getCurrentAddress());
			// } // 手机型号
			// if (TextUtils.isEmpty(infoEntity.getPhonemodel())) {
			// mEd_Phonemodel.setText("未设置");
			// } else {
			// mEd_Phonemodel.setText(infoEntity.getPhonemodel());
			// }
			// 行业
		// 行业家乡
		if (TextUtils.isEmpty(infoEntity.getCensus())) {
			mTx_Hometown.setText("未设置");
		} else {
			mTx_Hometown.setText(infoEntity.getCensus());
		}
		// 从业年限
		if (TextUtils.isEmpty(infoEntity.getGraduationDate())) {
			mEd_YearsOfWorking.setHint("未设置");
			ed_years_working.setHint("未设置");
		} else {
			mEd_YearsOfWorking.setText(infoEntity.getGraduationDate());
			ed_years_working.setText(Time.handYeas(infoEntity.getGraduationDate()));
		}
		// 毕业学校
		if (TextUtils.isEmpty(infoEntity.getGraduationDate())) {
			ed_graduatedSchool.setHint("未设置");
		} else {
			ed_graduatedSchool.setText(infoEntity.getGraduatedSchool());
		}
		// 从业年限
		if (TextUtils.isEmpty(infoEntity.getSpecialty())) {
			mEd_Speciality.setHint("未设置");
		} else {
			mEd_Speciality.setText(infoEntity.getSpecialty());
		}
		
		int indexOf = GlobalVariable.al_schooling.indexOf(infoEntity.getDegree());
		if (indexOf!=-1) {
			sp_schooling.setSelection(indexOf, true);  
		}
		int indexOf1 = GlobalVariable.al_professionaltitle.indexOf(infoEntity.getOffice());
		if (indexOf1!=-1) {
			sp_professionaltitle.setSelection(indexOf1, true);
		}
		
	}

	boolean StartUpPIC = true;// 开启上传头像
	boolean StartUpBIG = true;// 开启上传大罩
	String Education = "中专以下";//学历
	String Occupation = "初级";//职称
	// 保存资料 

	private void SaveData() {
		String PassWord = mEd_PassWord.getText().toString();
		String Affirmpw = mEd_Affirmpw.getText().toString();
		NickName = mEd_Nickname.getText().toString();
		String Mailbox = mEd_Mailbox.getText().toString();
		String Phonemodel = mEd_Phonemodel.getText().toString();
		String Hometown = mTx_Hometown.getText().toString();
		String CurrentAddress = mTx_NativePlace.getText().toString();
		String graduatedSchool = ed_graduatedSchool.getText().toString();//毕业时间

//		
		String YearsOfWorking = mEd_YearsOfWorking.getText().toString();
		String Speciality = mEd_Speciality.getText().toString();
		if (!PassWord.equals(Affirmpw)) {
			YoYo.with(Techniques.Shake).playOn(mEd_PassWord);
			YoYo.with(Techniques.Shake).playOn(mEd_Affirmpw);
			showShortToast("两次密码不一致！");
			return;
		}
		if (TextUtils.isEmpty(NickName)) {
			YoYo.with(Techniques.Shake).playOn(mEd_Nickname);
			showShortToast("昵称不能为空");
			return;
		}

		// if (TextUtils.isEmpty(Mailbox)) {
		// YoYo.with(Techniques.Shake).playOn(mEd_Mailbox);
		// showShortToast("邮箱不能为空");
		// return;
		// }

		if (TextUtils.isEmpty(birthday)) {
			YoYo.with(Techniques.Shake).playOn(mRa_Birthday);
			showShortToast("生日未设置！");
			return;
		}

		if (TextUtils.isEmpty(ProfessionID)) {
			YoYo.with(Techniques.Shake).playOn(Spinner_Profession);
			showShortToast("行业未设置！");
			return;
		}

		if (Hometown.equals("未设置")) {
			YoYo.with(Techniques.Shake).playOn(mRa_Hometown);
			showShortToast("家乡未设置！");
			return;
		}

		// if (TextUtils.isEmpty(Education)) {
		// YoYo.with(Techniques.Shake).playOn(mEd_Education);
		// showShortToast("学历未设置！");
		// return;
		// }
		// if (TextUtils.isEmpty(Occupation)) {
		// YoYo.with(Techniques.Shake).playOn(mEd_Occupation);
		// showShortToast("职称未设置！");
		// return;
		// }
		// if (TextUtils.isEmpty(YearsOfWorking)) {
		// YoYo.with(Techniques.Shake).playOn(mEd_YearsOfWorking);
		// showShortToast("从业年限未设置！");
		// return;
		// }
		// if (TextUtils.isEmpty(Speciality)) {
		// YoYo.with(Techniques.Shake).playOn(mEd_Speciality);
		// showShortToast("专长未设置！");
		// return;
		// }

		if (!TextUtils.isEmpty(PIClocalFilePath)) {// 不等于空说明设置头像回调路径保存成功
			if (StartUpPIC) {// 可以开启上传头像
				StartUpPIC = !StartUpPIC; // 已经在上传就变成上传过程
				isUpPic = false;// 开启上传显示为上传未成功
//				Log.e("", "上传头像开始");
				new BaseUpload(this).Upload(PIClocalFilePath, UpPicrl, true,
						"上传头像");
			} else {// 不用上传头像
				isUpPic = true;
			}
		} else {// 未修改头像 不用上传头像
			isUpPic = true;
		}
		if (!TextUtils.isEmpty(BiglocalFilePath)) {// 不等于空说明设置大罩回调路径保存成功
			if (StartUpBIG) {// 可以开启上传大罩
				StartUpBIG = !StartUpBIG;// 已经在上传就变成上传过程
				isUpBig = false;// 开启上传显示为上传未成功
//				Log.e("", "上传大罩开始");
				new BaseUpload(this).Upload(BiglocalFilePath, UpBigrl, true,
						"上传大罩");
			} else {// 不用上传头像
				isUpBig = true;
			}
		} else {// 未修改头像 不用上传头像
			isUpBig = true;
		}
		if (isUpBig && isUpPic) {
			ArrayList<BasicNameValuePair> infoMap = ReturnAL.ContentInfoMap(
					picUrl, PassWord, PassWord, NickName, Sex, Mailbox,
					birthday, Phonemodel, ProfessionID, Hometown, BigPicUrl,
					Education, Occupation, YearsOfWorking,graduatedSchool, Speciality);
			new RequestTask(this, infoMap, UpDatalistener, false, true, "保存数据")
					.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.URL);
		}
	}

	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				SaveData();
				break;
			case 2:
				showShortToast("大罩上传失败！");
				break;
			case 3:
				showShortToast("头像上传失败！");
				break;  
			}
			super.handleMessage(msg);
		}
	};
	// 上传头像回调
	RequestListener UpPicrl = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
//			Log.e("", "上传头像成功 "+jsonObject);
			picUrl = jsonObject;// 设置返回URL
			isUpPic = true;// 设置上传成功
			Picimageurllist.remove(0);// 清空查看大图的容器
			Picimageurllist.add(0, picUrl);// 添加新的URL到容器
			Message message = new Message();
			message.what = 1;
			myHandler.sendMessage(message);
		}  

		@Override
		public void responseException(String errorMessage) {
			 showShortToast("头像上传失败！");
			isUpPic = false;
			StartUpPIC = !StartUpPIC;

			Message message = new Message();
			message.what = 3;
			myHandler.sendMessage(message);
		}
	};

	// 上传大罩回调
	RequestListener UpBigrl = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
//			Log.e("", "上传大罩成功 "+jsonObject);
			BigPicUrl = jsonObject;
			isUpBig = true;
			BGimageurllist.remove(0);// 清空查看大图的容器
			BGimageurllist.add(0, BigPicUrl);// 添加新的URL到容器
			Message message = new Message();
			message.what = 1;
			myHandler.sendMessage(message);
		}

		@Override
		public void responseException(String errorMessage) {
			 showShortToast("大罩上传失败！");
			isUpBig = false;
			StartUpBIG = !StartUpBIG;

			Message message = new Message();
			message.what = 2;
			myHandler.sendMessage(message);
		}
	};

	// 切换可编辑状态
	private void Modification(boolean IsCompile) {

		mEd_PassWord.setEnabled(IsCompile);
		mEd_Affirmpw.setEnabled(IsCompile);
		mEd_Nickname.setEnabled(IsCompile);
		mEd_Mailbox.setEnabled(IsCompile);
		// mEd_Phonemodel.setEnabled(IsCompile);
		Spinner_Profession.setEnabled(IsCompile);
		sp_schooling.setEnabled(IsCompile);
		sp_professionaltitle.setEnabled(IsCompile);
		mEd_YearsOfWorking.setEnabled(IsCompile);
		ed_graduatedSchool.setEnabled(IsCompile);
		mEd_Speciality.setEnabled(IsCompile);
		mRa_ModificationPhoto.setEnabled(IsCompile);
		mRa_Birthday.setEnabled(IsCompile);
		mRa_NativePlace.setEnabled(IsCompile);
		mRa_Hometown.setEnabled(IsCompile);
		mRa_Background.setEnabled(IsCompile);
		// 图片不可查看
		mIm_Background.setEnabled(!IsCompile);
		mIm_Pic.setEnabled(!IsCompile);
		int visible1;
		int visible2;
		if (IsCompile) {
			visible1 = View.VISIBLE;
			visible2 = View.GONE;
		} else {
			visible2 = View.VISIBLE;
			visible1 = View.GONE;
		}
		mRg_Sex.setVisibility(visible1);
		mTx_Sex.setVisibility(visible2);
		mRa_Password.setVisibility(visible1);
		mRa_Affirmpw.setVisibility(visible1);
		mTx_Hint.setVisibility(visible1);
		mBtn_Save.setVisibility(visible1);
		mTx_Modification.setVisibility(visible2);
	}

	// 保存数据回调
	RequestListener UpDatalistener = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
//			Qlog.e("", "保存成功？");
			PublicUpJson publicjson = PublicUpJson.readJsonToSendmsgObject(
					MyinfoActivity.this, jsonObject);
			if (publicjson == null) {
				return;
			}
			PublicEntity publicEntity = publicjson.getAl().get(0);
			String status = publicEntity.getStatus();
			if (status.equals("1")) {
				Modification(false);
			}
			isUpBig = false;
			isUpPic = false;
			BiglocalFilePath = null;
			PIClocalFilePath = null;
			showShortToast(publicEntity.getMessage());
			// 保存昵称
			GlobalVariable.Nickname = NickName;
			SpUtils.saveString(MyinfoActivity.this, GlobalVariable.NICKNAME,
					GlobalVariable.Nickname);
			// 保存头像
			GlobalVariable.UserImage = picUrl;
			SpUtils.saveString(MyinfoActivity.this, GlobalVariable.USERIMAGE,
					GlobalVariable.UserImage);
			// 保存密码
			GlobalVariable.UserPassWord = publicEntity.getPassWord();
			SpUtils.saveString(MyinfoActivity.this, GlobalVariable.USERPW,
					GlobalVariable.UserPassWord);
		}

		@Override
		public void responseException(String errorMessage) {
			showShortToast(errorMessage);
		}
	};
	/**学历**/
	private Spinner sp_schooling;
	/**职称**/
	private Spinner sp_professionaltitle;
	private String name;
	private EditText ed_graduatedSchool;
	private TextView ed_years_working;


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (resultCode == Activity.RESULT_OK) {
				String localFilePath = data
						.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
				bm = ImageCompression.getSmallBitmap(localFilePath);// 图片压缩
				if (requestCode == 10001) {
					mIm_Pic.setImageBitmap(bm);
					PIClocalFilePath = localFilePath;
				} else {
					mIm_Background.setImageBitmap(bm);
					BiglocalFilePath = localFilePath;
				}
			}
			if (requestCode == 1001 && resultCode == CitiesActivity.resultCode) {
				mTx_Hometown.setText(data.getStringExtra("name"));
			}
		}
	}

	/**
	 * 按back相当于回调
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SetResult(10002, new Bundle());
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//下拉框
	@SuppressLint("NewApi")
	private void TackType(Spinner spinner,final ArrayList<String>  al) {
		
		ArrayAdapter<String> arr_adapter2 = new ArrayAdapter<String>(MyinfoActivity.this,
				android.R.layout.simple_spinner_item, al);
		// 设置样式
		arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 加载适配器
		spinner.setAdapter(arr_adapter2);
		
	}
}
