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

public class AppService extends Service {
	// 获取服务器端信息
	protected SharedPreferences preference;
	private String base_ip_port;
	private Timer mTimer;  
	private Handler mHandler = new Handler(); 
	
	
//	private Runnable mTasks = new Runnable()
//	  {
//	    public void run()
//	    {
//	      
//	    	
//	    	
//	      mHandler.postDelayed(mTasks, 5000);
//	    }
//	  };
		
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate(){
		//mHandler.postDelayed(mTasks, 5000);
		super.onCreate();
		
		// 获取服务器端信息
		preference = this.getSharedPreferences("perference",
						MODE_PRIVATE);

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
		
	}
	
	
//	@Override
//	public int  onStartCommand(Intent intent, int flags, int startId){
//		mTimer = new Timer();  
//		mTimer.schedule( new TimerTask()  
//        {  
//  
//            @Override  
//            public void run()  
//            {  
//                // 定时更新  
//                String jsonString = getWarningInfo();  
//                // 发送广播  
//                Intent intent = new Intent();  
//                intent.setAction( BROADCASTACTION );  
//                intent.putExtra( "jsonstr", jsonString );  
//                sendBroadcast( intent );  
////               Message msg = handler.obtainMessage();  
////               msg.what = UPDATAWEATHER;  
////               handler.sendMessage( msg );  
//  
//            }  
//        }, 0, 20 * 1000 ); 
//        
//		return super.onStartCommand(intent, flags, startId);  
//	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();  
        if(mTimer != null){  
        	mTimer.cancel();  
        }  
	}

//	private void getWarningInfo(){
//		String identityUrl = base_ip_port + Url.URL_ENERGY_SAVING_DATA;
//
//		identityUrl = StringUtils.setParams(identityUrl, factoryCodeList);
//
//		// 远程获取身份验证结果
//		RequestQueue mQueue = Volley.newRequestQueue(this);
//
//		StringRequest stringRequest = new StringRequest(identityUrl,
//				new Response.Listener<String>() {
//					@Override
//					public void onResponse(String response) {
//						Log.d("TAG", response);
//						Message msg = new Message();
//						Bundle bundle = new Bundle();
//
//						try {
//							String retval = response.substring(1,
//									response.length() - 1);
//							retval = retval.replace("\\", "");
//
//							if ("".equals(retval)) {
//								msg.what = AppConstants.NG;
//							} else {
//								JSONArray returnData = new JSONArray(retval);
//
//								int length = returnData.length();
//								String[] array1 = new String[length];
//								String[] array2 = new String[length];
//								String[] array3 = new String[length];
//								String[] array4 = new String[length];
//								String[] array5 = new String[length];
//
//								JSONObject jsonObj = null;
//								for (int i = 0; i < length; i++) {
//									jsonObj = returnData.getJSONObject(i);
//
//									array1[i] = jsonObj.getString(columns[0]);
//									array2[i] = jsonObj.getString(columns[1]);
//									array3[i] = jsonObj.getString(columns[2]);
//									array4[i] = jsonObj.getString(columns[3]);
//									array5[i] = jsonObj.getString(columns[4]);
//								}
//
//								bundle.putStringArray(columns[0], array1);
//								bundle.putStringArray(columns[1], array2);
//								bundle.putStringArray(columns[2], array3);
//								bundle.putStringArray(columns[3], array4);
//								bundle.putStringArray(columns[4], array5);
//
//								msg.setData(bundle);
//								msg.what = AppConstants.OK;
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//							msg.what = AppConstants.ERROR2;
//						}
//
//						mHandler.sendMessage(msg);
//					}
//				}, new Response.ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError error) {
//						Log.e("TAG", error.getMessage(), error);
//						Message msg = new Message();
//						msg.what = AppConstants.ERROR1;
//						mHandler.sendMessage(msg);
//					}
//				});
//
//		mQueue.add(stringRequest);
//	}
//	
	
}
