package com.whzlong.anke;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	private Button btnQuit = null;
	private Button btnEnergySavingData = null;
	private Button btnRealTimeData = null;
	private Button btnWarningInfo = null;
	private Button btnSystemSet = null;
	private SharedPreferences preference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//初始化各种视图组件
		initViews();
	}
	
	/**
	 * 初始化各种视图组件
	 */
	private void initViews(){
		//节能数据查询
		btnEnergySavingData = (Button)findViewById(R.id.btnEnergySavingData);
		btnEnergySavingData.setTextSize(20);
		btnEnergySavingData.setOnClickListener(this);
		
		//实时状态查询
		btnRealTimeData = (Button)findViewById(R.id.btnRealTimeData);
		btnRealTimeData.setTextSize(20);
		btnRealTimeData.setOnClickListener(this);
		
		//警告信息查询
		btnWarningInfo = (Button)findViewById(R.id.btnWarningInfo);
		btnWarningInfo.setTextSize(20);
		btnWarningInfo.setOnClickListener(this);
		
		//系统设置
		btnSystemSet = (Button)findViewById(R.id.btnSystemSet);
		btnSystemSet.setTextSize(20);
		btnSystemSet.setOnClickListener(this);
		
		//退出
		btnQuit = (Button)findViewById(R.id.btnQuit);
		btnQuit.setTextSize(20);
		btnQuit.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		
		switch (v.getId()) {
			case R.id.btnEnergySavingData:
				//节能数据
				intent = new Intent();
				intent.setClass(MainActivity.this, EnergySavingDataActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
				break;
			case R.id.btnRealTimeData:
				//实时状态
				intent = new Intent();
				intent.setClass(MainActivity.this, RealTimeDataActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
				break;
			case R.id.btnWarningInfo:
				//报警信息
				intent = new Intent();
				intent.setClass(MainActivity.this, WarningInfoActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
				break;
			case R.id.btnSystemSet:
				//系统设置
				intent = new Intent();
				intent.setClass(MainActivity.this, SystemSetActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
				break;
			case R.id.btnQuit:
				//退出
				MainActivity.this.finish();
				break;
			default:
				break;
		}
	}
	
//	private OnClickListener listener = new OnClickListener() {
//		public void onClick(View view) {
//			switch (view.getId()) {
//			case R.id.btnEnergySavingData:
//				break;
//			case R.id.btnRealTimeData:
//				break;
//			case R.id.btnSystemSet:
//				break;
//			case R.id.btnQuit:
////				ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE); 
////				am.killBackgroundProcesses(getPackageName());
////				System.exit(0);
//				Toast.makeText(this.getClass(), "quit", Toast.LENGTH_LONG).show();
//				break;
//			default:
//				break;
//			}
//		}
//	};

}
