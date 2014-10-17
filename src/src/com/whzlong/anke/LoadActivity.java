package com.whzlong.anke;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class LoadActivity extends Activity {

	private final int LOAD_TIME = 1000;
	// 终端唯一识别码
	private String mImei;
	private Context context;
	protected static final int OK = 0x10000;
	protected static final int NG = 0x10001;
	protected static final int ERROR1 = 0x20000;
	protected static final int ERROR2 = 0x20001;
	protected static final int ERROR3 = 0x20002;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);

		context = this.getApplicationContext();

		if (isNetworkConnected(context)) {
			// 跳转至主界面
			new Handler().postDelayed(new Runnable() {
				public void run() {
					// 身份验证
					verifyIdentity();
				}
			}, LOAD_TIME);
		} else {
			Toast.makeText(context, "网络连接不可用", Toast.LENGTH_LONG).show();
			LoadActivity.this.finish();
		}

	}

	/**
	 * 是否有网络连接
	 * 
	 * @param context
	 * @return
	 */
	private boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	protected void verifyIdentity() {
		// 移动设备国际身份码
		TelephonyManager phoneManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		mImei = phoneManager.getDeviceId();

		// 远程获取身份验证结果
		RequestQueue mQueue = Volley.newRequestQueue(this);
		StringRequest stringRequest = new StringRequest(
				"http://101.231.219.254:8082/restservice.svc/ChkTelCode/13524485769",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", response);
						Message msg = new Message();

						try {
							String retval = response.substring(1,
									response.length() - 1);
							JSONObject jsonObj = new JSONObject(retval);

							retval = jsonObj.getString("authenticationResult");

							if ("1".equals(retval)) {
								msg.what = OK;
							} else {
								msg.what = NG;
							}

							mHandler.sendMessage(msg);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							msg.what = ERROR2;
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						Message msg = new Message();
						msg.what = ERROR1;
						mHandler.sendMessage(msg);
					}
				});

		mQueue.add(stringRequest);
	}

	// 定义一个Handler,更新一览数据
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case OK:
					Intent intent = new Intent();
					intent.setClass(LoadActivity.this, MainActivity.class);
					startActivity(intent);
					LoadActivity.this.finish();
					break;
				case NG:
					EditText et = new EditText(LoadActivity.this);
					et.setText(mImei);

					AlertDialog.Builder builder = new Builder(LoadActivity.this);
					builder.setTitle("IMEI:");
					builder.setView(et);
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int item) {
									LoadActivity.this.finish();
								}
							});
					builder.show();
					break;
				case ERROR1:
					
					
					
					break;
				case ERROR2:
					LoadActivity.this.finish();
					break;
				default:
					break;
			}
		}
	};
}
