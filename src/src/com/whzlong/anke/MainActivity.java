package com.whzlong.anke;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnClickListener {
	private Button btnQuit = null;
	private Button btnEnergySavingData = null;
	private Button btnRealTimeData = null;
	private Button btnWarningInfo = null;
	private Button btnSystemSet = null;
	//查询动画
    private ProgressDialog mProDialog;
    //下载对话框
  	private Dialog downloadDialog;
  	//进度条
    private ProgressBar mProgress;
    //显示下载数值
    private TextView mProgressText;
    //下载线程
    private Thread downLoadThread;

	// 定义一个Handler,更新一览数据
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			String latestVersion = bundle.getString(AppConstants.APP_VERSION);
			
			String currentVersionNo = preference.getString(AppConstants.APP_VERSION, "");
			
			if(AppConstants.EMPTY.equals(currentVersionNo)){
				Editor editor = preference.edit();
				editor.putString(AppConstants.APP_VERSION, latestVersion);
				editor.commit();
			}else if(!AppConstants.EMPTY.equals(latestVersion) && latestVersion.equals(currentVersionNo)){
				//TODO: 最新app下载
				
				
				
				
				
				
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 初始化各种视图组件
		initViews();

		// 检查版本
		checkAppVersion();
	}

	/**
	 * 初始化各种视图组件
	 */
	private void initViews() {
		// 节能数据查询
		btnEnergySavingData = (Button) findViewById(R.id.btnEnergySavingData);
		btnEnergySavingData.setTextSize(20);
		btnEnergySavingData.setOnClickListener(this);

		// 实时状态查询
		btnRealTimeData = (Button) findViewById(R.id.btnRealTimeData);
		btnRealTimeData.setTextSize(20);
		btnRealTimeData.setOnClickListener(this);

		// 警告信息查询
		btnWarningInfo = (Button) findViewById(R.id.btnWarningInfo);
		btnWarningInfo.setTextSize(20);
		btnWarningInfo.setOnClickListener(this);

		// 系统设置
		btnSystemSet = (Button) findViewById(R.id.btnSystemSet);
		btnSystemSet.setTextSize(20);
		btnSystemSet.setOnClickListener(this);

		// 退出
		btnQuit = (Button) findViewById(R.id.btnQuit);
		btnQuit.setTextSize(20);
		btnQuit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.btnEnergySavingData:
			// 节能数据
			intent = new Intent();
			intent.setClass(MainActivity.this, EnergySavingDataActivity.class);
			startActivity(intent);
			MainActivity.this.finish();
			break;
		case R.id.btnRealTimeData:
			// 实时状态
			intent = new Intent();
			intent.setClass(MainActivity.this, RealTimeDataActivity.class);
			startActivity(intent);
			MainActivity.this.finish();
			break;
		case R.id.btnWarningInfo:
			// 报警信息
			intent = new Intent();
			intent.setClass(MainActivity.this, WarningInfoActivity.class);
			startActivity(intent);
			MainActivity.this.finish();
			break;
		case R.id.btnSystemSet:
			// 系统设置
			intent = new Intent();
			intent.setClass(MainActivity.this, SystemSetActivity.class);
			startActivity(intent);
			MainActivity.this.finish();
			break;
		case R.id.btnQuit:
			// 退出
			MainActivity.this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 验证应用版本
	 */
	private void checkAppVersion() {
		String identityUrl = base_ip_port + AppConstants.APP_VERSION;

		// 远程获取身份验证结果
		RequestQueue mQueue = Volley.newRequestQueue(this);

		StringRequest stringRequest = new StringRequest(identityUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", response);
						Message msg = new Message();
						Bundle bundle = new Bundle();

						try {
							String versionNo = response.substring(1,
									response.length() - 1);
							JSONObject jsonObj = new JSONObject(versionNo);

							versionNo = jsonObj.getString(AppConstants.APP_VERSION);
							
							bundle.putString(AppConstants.APP_VERSION, versionNo);
							
							msg.what = AppConstants.OK;
							msg.setData(bundle);
							
							mHandler.sendMessage(msg);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							msg.what = AppConstants.ERROR2;
						}

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
