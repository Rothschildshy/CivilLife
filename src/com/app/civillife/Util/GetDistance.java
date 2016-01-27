package com.app.civillife.Util;

import java.util.List;
import java.util.concurrent.Executors;

import com.CivilLife.Entity.MyInfoEntity;
import com.CivilLife.Entity.PublicEntity;
import com.CivilLife.Json.GetMyIngoJson;
import com.CivilLife.Json.PublicUpJson;
import com.CivilLife.Variable.GlobalVariable;
import com.CivilLife.net.Httpurl;
import com.CivilLife.net.RequestTask;
import com.aysy_mytool.SpUtils;
import com.aysy_mytool.ToastUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;

import Requset_getORpost.RequestListener;
import android.content.Context;
import android.os.Handler;

/**
 * 利用百度定位SDK封装的定位方法
 * 
 * @author Administrator
 * 
 */
public class GetDistance {
	private static Context mContext;
	private static Handler mhandler;
	// 定位模式
	private static LocationMode tempMode = LocationMode.Hight_Accuracy; // 高精度
	// private LocationMode tempMode = LocationMode.Battery_Saving;//低功耗
	// private LocationMode tempMode = LocationMode.Device_Sensors;//仅设备
	// 坐标系
	private static String tempcoor = "gcj02";// 国测局加密经纬度
	// private String tempcoor="bd09ll";//百度经纬度标准
	// private String tempcoor="bd09";//百度墨卡托标准
	// 获取当前坐标
	public static LocationClient mLocationClient = null;
	public static BDLocationListener myListener = new MyLocationListener();

	public static LocationClient location(Context context, Handler handler) {
		mhandler = handler;
		mContext = context;
		mLocationClient = new LocationClient(mContext); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		initLocation();
		// mLocationClient.start();

		return mLocationClient;

	}

	private static void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType(tempcoor);// 可选，默认gcj02，设置返回的定位结果坐标系，
		option.setScanSpan(1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		mLocationClient.setLocOption(option);
	}

	public static class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());// 单位度
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("网络不通畅导致定位失败，请检查网络是否通畅");
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
			}
			sb.append("\nlocationdescribe : ");
			sb.append(location.getLocationDescribe());// 位置语义化信息
			List<Poi> list = location.getPoiList();// POI数据
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}
			GlobalVariable.mycoordinates_x = location.getLongitude() + "";
			SpUtils.saveString(mContext, "Longitude",
					GlobalVariable.mycoordinates_x);
			GlobalVariable.mycoordinates_y = location.getLatitude() + "";
			SpUtils.saveString(mContext, "Latitude",
					GlobalVariable.mycoordinates_y);
			GlobalVariable.address = location.getAddrStr();
			// location.getProvince();// 获得省份
			// location.getCity() ;//获得城市
			// location.getDistrict() ;//获得区/县
			GlobalVariable.City = location.getProvince() + ","
					+ location.getCity();
			if (mhandler != null) {
				mhandler.sendMessage(mhandler.obtainMessage(10,
						location.getAddrStr()));
			}
			new RequestTask(mContext, listener, false, false, "检测会员是否登录")
					.executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.IsLogin(GlobalVariable.City));
			mLocationClient.stop();

		}

		// 检测会员是否登录回调
		RequestListener listener = new RequestListener() {

			@Override
			public void responseResult(String jsonObject) {
				PublicUpJson myingojson = PublicUpJson.readJsonToSendmsgObject(
						mContext, jsonObject);
				if (myingojson == null) {
					return;
				}
				PublicEntity publicEntity = myingojson.getAl().get(0);
				String status = publicEntity.getStatus();
				if (status.equals("1")) {
					new RequestTask(mContext, listenerinfo, false, false,
							"获取昵称和头像").executeOnExecutor(Executors.newCachedThreadPool(), Httpurl.GetPicORNick());
				}
			}

			@Override
			public void responseException(String errorMessage) {
				ToastUtil.showToast(mContext, errorMessage);
			}
		};
	}

	// 获取头像 昵称回调
	static RequestListener listenerinfo = new RequestListener() {

		@Override
		public void responseResult(String jsonObject) {
			GetMyIngoJson myingojson = GetMyIngoJson.readJsonToSendmsgObject(
					mContext, jsonObject);
			if (myingojson == null) {
				return;
			}
			MyInfoEntity publicEntity = myingojson.getAl().get(0);
			String status = publicEntity.getStatus();
			if (status.equals("1")) {
				GlobalVariable.UserID = publicEntity.getID();
				SpUtils.saveString(mContext, GlobalVariable.USERID,
						GlobalVariable.UserID);
				GlobalVariable.Nickname = publicEntity.getNickname();
				SpUtils.saveString(mContext, GlobalVariable.NICKNAME,
						GlobalVariable.Nickname);
				GlobalVariable.UserImage = publicEntity.getPicUrl();
				SpUtils.saveString(mContext, GlobalVariable.USERIMAGE,
						GlobalVariable.UserImage);
				if (mhandler != null) {
					mhandler.sendMessage(mhandler.obtainMessage(11, true));
				}
			}
		}

		@Override
		public void responseException(String errorMessage) {
			ToastUtil.showToast(mContext, errorMessage);
		}
	};
}
