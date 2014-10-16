package com.whzlong.anke;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class LoadActivity extends Activity {

	private final int LOAD_TIME = 1000;
	protected String phoneNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);
		
		Context context = this.getApplicationContext();
		
		TelephonyManager phoneManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		phoneNum = phoneManager.getLine1Number();
		
		if(isNetworkConnected(context)){
			//跳转至主界面
			new Handler().postDelayed(new Runnable(){
				public void run(){	
					
					if(phoneNum != null){
						System.out.println(phoneNum);
					}
					
					Intent intent = new Intent();
					intent.setClass(LoadActivity.this, MainActivity.class);
					startActivity(intent);
					LoadActivity.this.finish();
				}
			}, LOAD_TIME);
		}else{
			Toast.makeText(context, "网络连接不可用", Toast.LENGTH_LONG).show();
			LoadActivity.this.finish();
		}
		
	}
	
	/**
	 * 是否有网络连接
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context) {
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
	
}
