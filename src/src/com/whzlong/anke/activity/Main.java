package com.whzlong.anke.activity;

import com.whzlong.anke.AppContext;
import com.whzlong.anke.R;
import com.whzlong.anke.common.VersionManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends BaseActivity implements OnClickListener {
	private Button btnQuit = null;
	private Button btnEnergySavingData = null;
	private Button btnRealTimeData = null;
	private Button btnWarningInfo = null;
	private Button btnSystemSet = null;
	// 全局Context
	private AppContext appContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		appContext = (AppContext) getApplication();
		
		// 初始化各种视图组件
		initViews();

		// 检查版本
		if(appContext.isCheckUp()){
			VersionManager.getVersionManager().checkAppUpdate(this, false);
		}
		
		//启动后台服务
		//TODO:后台服务
		//startBackService();
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.btnEnergySavingData:
			// 节能数据
			intent = new Intent();
			intent.setClass(Main.this, EnergySavingData.class);
			startActivity(intent);
			Main.this.finish();
			break;
		case R.id.btnRealTimeData:
			// 实时状态
			intent = new Intent();
			intent.setClass(Main.this, RealTimeData.class);
			startActivity(intent);
			Main.this.finish();
			break;
		case R.id.btnWarningInfo:
			// 报警信息
			intent = new Intent();
			intent.setClass(Main.this, WarningInfo.class);
			startActivity(intent);
			Main.this.finish();
			break;
		case R.id.btnSystemSet:
			// 系统设置
			intent = new Intent();
			intent.setClass(Main.this, SystemSet.class);
			startActivity(intent);
			Main.this.finish();
			break;
		case R.id.btnQuit:
			// 退出
			Main.this.finish();
			break;
		default:
			break;
		}
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

	/**
	 * 启动后台服务
	 */
	private void startBackService(){
		Intent intent = new Intent("com.whzlong.anke.appService");
		startService(intent);
	}
}
