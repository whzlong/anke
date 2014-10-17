package com.whzlong.anke;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class LoadActivity extends Activity {

	private final int LOAD_TIME = 1000;
	// 终端唯一识别码
	private String mImsi;
	private boolean mVerifyIdentity = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);

		Context context = this.getApplicationContext();

		if (isNetworkConnected(context)) {
			// 跳转至主界面
			new Handler().postDelayed(new Runnable() {
				public void run() {

					if (verifyIdentity()) {
						System.out.println(mImsi);
					}

					Intent intent = new Intent();
					intent.setClass(LoadActivity.this, MainActivity.class);
					startActivity(intent);
					LoadActivity.this.finish();
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

	protected boolean verifyIdentity() {
		//获取手机唯一识别码
		TelephonyManager phoneManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		mImsi = phoneManager.getSubscriberId();

		//
		RequestQueue mQueue = Volley.newRequestQueue(this);
		StringRequest stringRequest = new StringRequest(
				"http://201.231.219.254:8082/restservice.svc/ChkTelCode/13524485769",
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", response);
						
						mVerifyIdentity = true;
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
					}
				});

		mQueue.add(stringRequest);

		return mVerifyIdentity;
	}
	
}
