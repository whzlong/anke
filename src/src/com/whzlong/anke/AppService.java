package com.whzlong.anke;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whzlong.anke.bean.Url;
import com.whzlong.anke.common.StringUtils;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class AppService extends Service {
	// 获取服务器端信息
	protected SharedPreferences preference;
	private String base_ip_port;
	private Timer mTimer;  
	// 全局Context
	private AppContext appContext;
		// 定义一个Handler,更新一览数据
		private Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case AppConstants.OK:
					Bundle bundle = msg.getData();

					String[] warningFactoryCodes = bundle.getStringArray("warningFactoryCodes");
					
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
	public void onCreate(){
		super.onCreate();
		
		appContext = (AppContext) getApplication();
		// 获取服务器端信息
		preference = this.getSharedPreferences("perference",
						MODE_PRIVATE);
		
	}
	
	
	@Override
	public int  onStartCommand(Intent intent, int flags, int startId){
		mTimer = new Timer();  
		mTimer.schedule( new TimerTask()  
        {  
  
            @Override  
            public void run()  
            {  
            	
            	String ipPort = preference.getString(AppConstants.URI_IP_PORT, "");
        		
        		if("".equals(ipPort)){
        			base_ip_port = Url.HTTP
        					+ Url.DEFAULT_URI_IP_PORT;
        			
        			Editor editor = preference.edit();
        			editor.putString(AppConstants.URI_IP_PORT, Url.DEFAULT_URI_IP_PORT);
        			editor.commit();
        		}else{
        			base_ip_port = Url.HTTP
        					+ ipPort;
        		}
        		
        		String factoryCodes = preference.getString(AppConstants.SELECTED_WARNING_FACTORY_KEY, "");
        		
        		String identityUrl = base_ip_port + Url.URL_HISTORY_WARNING_REMIND;

        		identityUrl = StringUtils.setParams(identityUrl, factoryCodes);
        		
            	getWarningInfo(identityUrl);
            	
//                // 定时更新  
//                String jsonString = getWarningInfo();  
//                // 发送广播  
//                Intent intent = new Intent();  
//                intent.setAction( BROADCASTACTION );  
//                intent.putExtra( "jsonstr", jsonString );  
//                sendBroadcast( intent );  
//               Message msg = handler.obtainMessage();  
//               msg.what = UPDATAWEATHER;  
//               handler.sendMessage( msg );  
  
            }  
        }, 0, 20 * 1000 ); 
        
		return super.onStartCommand(intent, flags, startId);  
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();  
        if(mTimer != null){  
        	mTimer.cancel();  
        }  
	}

	private void getWarningInfo(String url){
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

							retval = jsonObj.getString("warningFactoryCodes");
							
							if ("".equals(retval)) {
								msg.what = AppConstants.NG;
							} else {
								String[] warningFactoryCodes = retval.split("&");

								bundle.putStringArray("warningFactoryCodes", warningFactoryCodes);
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
