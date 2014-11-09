package com.whzlong.anke;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whzlong.anke.activity.WarningInfo;
import com.whzlong.anke.bean.Url;
import com.whzlong.anke.common.StringUtils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class AppService extends Service {
	private static final String TAG = "AnkeService";
	// 获取服务器端信息
	protected SharedPreferences preference;
	private String base_ip_port;
	private Timer mTimer;
	private int test;
	// 全局Context
	private AppContext appContext;
	// 定义一个Handler,更新一览数据
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AppConstants.OK:
				Bundle bundle = msg.getData();

				// String[] warningFactoryCodes =
				// bundle.getStringArray(AppConstants.WARNING_FACTORY_CODES);
				String warningFactoryCodes = bundle
						.getString(AppConstants.WARNING_FACTORY_CODES);

				String ns = Context.NOTIFICATION_SERVICE;

				// 通知图标
				int icon = R.drawable.loading9;
				// 状态栏显示的通知文本提示
				CharSequence tickerText = "钢厂警告信息";
				// 通知产生的时间，会在通知信息里显示
				long when = System.currentTimeMillis();
				// 用上面的属性初始化 Nofification
				Notification notification = new Notification(icon, tickerText,
						when);

				RemoteViews contentView = new RemoteViews(getPackageName(),
						R.layout.notification);

				contentView.setImageViewResource(R.id.ntImage,
						R.drawable.abc_ic_go);
				test += 1;
				CharSequence content = "钢铁" + String.valueOf(test) + "厂";
				contentView.setTextViewText(R.id.ntMsg, content);

				notification.contentView = contentView;

				Intent notificationIntent = new Intent(appContext, WarningInfo.class);
				
				PendingIntent contentIntent = PendingIntent.getActivity(
						appContext, 0, notificationIntent, 0);
				notification.contentIntent = contentIntent;
				
				notification.defaults = notification.DEFAULT_SOUND|notification.DEFAULT_VIBRATE;

				NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
				mNotificationManager.notify(0, notification);

				break;
			case AppConstants.NG:
				Toast.makeText(
						appContext,
						appContext.getString(R.string.error_select_result_zero),
						Toast.LENGTH_LONG).show();
				break;
			case AppConstants.ERROR1:
				Toast.makeText(appContext,
						appContext.getString(R.string.error_network_connected),
						Toast.LENGTH_LONG).show();
				break;
			case AppConstants.ERROR2:
				Toast.makeText(appContext,
						appContext.getString(R.string.system_error),
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}

		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "AnkeService-onCreate");
		super.onCreate();

		appContext = (AppContext) getApplication();
		// 获取服务器端信息
		preference = this.getSharedPreferences("perference", MODE_PRIVATE);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {

				String ipPort = preference.getString(AppConstants.URI_IP_PORT,
						"");

				if ("".equals(ipPort)) {
					base_ip_port = Url.HTTP + Url.DEFAULT_URI_IP_PORT;

					Editor editor = preference.edit();
					editor.putString(AppConstants.URI_IP_PORT,
							Url.DEFAULT_URI_IP_PORT);
					editor.commit();
				} else {
					base_ip_port = Url.HTTP + ipPort;
				}

				// String factoryCodes =
				// preference.getString(AppConstants.SELECTED_WARNING_FACTORY_KEY,
				// "");
				//
				// String identityUrl = base_ip_port + Url.URL_REAL_TIME_DATA;
				//
				// factoryCodes = "TestData1";
				// identityUrl = StringUtils.setParams(identityUrl,
				// factoryCodes);

				// 移动设备国际身份码
				String mImei = "356440043501517";

				String identityUrl = base_ip_port + Url.URL_VERIFY_IDENTIFY;
				identityUrl = StringUtils.setParams(identityUrl, mImei);

				getWarningInfo(identityUrl);

				// // 定时更新
				// String jsonString = getWarningInfo();
				// // 发送广播
				// Intent intent = new Intent();
				// intent.setAction( BROADCASTACTION );
				// intent.putExtra( "jsonstr", jsonString );
				// sendBroadcast( intent );
				// Message msg = handler.obtainMessage();
				// msg.what = UPDATAWEATHER;
				// handler.sendMessage( msg );

			}
		}, 0, 50 * 1000);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mTimer != null) {
			mTimer.cancel();
		}
	}
	
	private void getWarningInfo(String url) {
		// 远程获取身份验证结果
		RequestQueue mQueue = Volley.newRequestQueue(this);

		StringRequest stringRequest = new StringRequest(url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", response);
						Message msg = new Message();
						Bundle bundle = new Bundle();

						try {
							String retval = response.substring(1,
									response.length() - 1);
							retval = retval.replace("\\", "");

							JSONObject jsonObj = new JSONObject(retval);

							// retval =
							// jsonObj.getString(AppConstants.WARNING_FACTORY_CODES);

							// DELETE
							retval = jsonObj
									.getString(AppConstants.AUTHENTICATION_RESULT);

							if ("".equals(retval)) {
								msg.what = AppConstants.NG;
							} else {
								// String[] warningFactoryCodes =
								// retval.split("&");

								// bundle.putStringArray(AppConstants.WARNING_FACTORY_CODES,
								// warningFactoryCodes);

								bundle.putString(
										AppConstants.WARNING_FACTORY_CODES,
										retval);

								msg.setData(bundle);
								msg.what = AppConstants.OK;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							msg.what = AppConstants.ERROR2;
						}

						mHandler.sendMessage(msg);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						Message msg = new Message();
						msg.what = AppConstants.ERROR1;
						mHandler.sendMessage(msg);
					}
				});

		mQueue.add(stringRequest);
	}

}
