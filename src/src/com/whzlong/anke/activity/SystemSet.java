package com.whzlong.anke.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.whzlong.anke.AppConstants;
import com.whzlong.anke.AppContext;
import com.whzlong.anke.AppManager;
import com.whzlong.anke.AppService;
import com.whzlong.anke.R;
import com.whzlong.anke.activity.Main;

public class SystemSet extends Activity implements OnClickListener,
		OnTouchListener {
	private Button btnBack;
	private Button btnQuitApp;
	private RelativeLayout lyWarningFactory;
	private RelativeLayout lyWaringTimeArea;
	private RelativeLayout lyServerInfo;
	private TextView mWarningFactoryInfo;
	private TextView mWaringTimeAreaInfo;
	private TextView mPollingIntervalInfo;
	private TextView mServerInfo;
	// 全局Context
	private AppContext appContext;

	private static final int WARNING_FACTORY_LENGTH = 15;
	private SharedPreferences preference;
	private int mSelectedTimeAreaIndex = -1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_set);
		
		appContext = (AppContext) getApplication();
		
		// 初始化各种视图组件
		InitViews();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 返回按钮
			Intent intent = new Intent();
			intent.setClass(SystemSet.this, Main.class);
			startActivity(intent);
			SystemSet.this.finish();
		}

		return false;
	}
	
	/**
	 * 单击事件
	 */
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.btnBack:
			// 返回按钮
			intent = new Intent();
			intent.setClass(SystemSet.this, Main.class);
			startActivity(intent);
			SystemSet.this.finish();
			break;
		case R.id.rlWarningFactory:
			// 选择需要提示警告的工厂
			intent = new Intent();
			intent.setClass(SystemSet.this, MultiFactoryInfo.class);
			startActivity(intent);
			SystemSet.this.finish();
			break;
		case R.id.rlWaringTimeArea:
			// 报警时间段设置
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SystemSet.this);

			mSelectedTimeAreaIndex = -1;
			builder.setTitle(SystemSet.this.getString(R.string.waringTimeArea));
			// 设置选项
			builder.setSingleChoiceItems(AppConstants.TIME_AREA_NAME, mSelectedTimeAreaIndex,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int position) {
							mSelectedTimeAreaIndex = position;
						}
					});

			// 确认
			builder.setPositiveButton(
					SystemSet.this.getString(R.string.confirm),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int position) {
							if (mSelectedTimeAreaIndex >= 0) {
								Editor editor = preference.edit();
								editor.putInt(
										AppConstants.SELECTED_TIME_AREA,
										AppConstants.TIME_AREA_CODE[mSelectedTimeAreaIndex]);
								editor.commit();

								// 设置界面警告时间段的值
								mWaringTimeAreaInfo.setText(AppConstants.TIME_AREA_NAME[mSelectedTimeAreaIndex]);
								
								//TODO:重新启动实时获取警告信息的服务
								//退出服务
								Intent intent = new Intent(appContext, AppService.class);
								stopService(intent);
								startService(intent);
							}
							
						}
					});

			// 取消
			builder.setNegativeButton(
					SystemSet.this.getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int position) {

						}
					});

			builder.create().show();
			break;
		case R.id.rlServerInfo:
			// 设置服务器信息
			intent = new Intent();
			intent.setClass(SystemSet.this, ServerInfo.class);
			startActivity(intent);
			SystemSet.this.finish();
		case R.id.btnQuitApp:
			//退出服务
			intent = new Intent(appContext, AppService.class);
			stopService(intent);
			
			//退出界面
			AppManager.getAppManager().AppExit(appContext);
			
			break;
		default:
			break;
		}

	}

	/**
	 * 触摸事件
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.btnBack:
				btnBack.setBackgroundResource(R.drawable.light_gray);
				break;
			case R.id.rlWarningFactory:
				lyWarningFactory.setBackgroundColor(Color.LTGRAY);
				break;
			case R.id.rlWaringTimeArea:
				lyWaringTimeArea.setBackgroundColor(Color.LTGRAY);
				break;
			case R.id.rlServerInfo:
				lyServerInfo.setBackgroundColor(Color.LTGRAY);
				break;
			default:
				break;
			}
		}

		return false;
	}

	/**
	 * 初始化各种视图组件
	 */
	private void InitViews() {
		preference = SystemSet.this.getSharedPreferences("perference",
				MODE_PRIVATE);

		// 报警炼钢厂
		lyWarningFactory = (RelativeLayout) findViewById(R.id.rlWarningFactory);
		lyWarningFactory.setOnClickListener(this);
		lyWarningFactory.setOnTouchListener(this);

		String defaultCheckedFactory = preference.getString(
				AppConstants.SELECTED_WARNING_FACTORY_NAME, "");

		String displayFactoryName = defaultCheckedFactory;
		if (defaultCheckedFactory.length() > WARNING_FACTORY_LENGTH) {
			displayFactoryName = defaultCheckedFactory.substring(0,
					WARNING_FACTORY_LENGTH) + "...";
		}
		
		mWarningFactoryInfo = (TextView) findViewById(R.id.tvWarningFactoryInfo);
		mWarningFactoryInfo.setText(displayFactoryName);

		// 报警时间段
		lyWaringTimeArea = (RelativeLayout) findViewById(R.id.rlWaringTimeArea);
		lyWaringTimeArea.setOnClickListener(this);
		lyWaringTimeArea.setOnTouchListener(this);

		mWaringTimeAreaInfo = (TextView) findViewById(R.id.tvWaringTimeAreaInfo);
		mWaringTimeAreaInfo.setText(getSelectedTimeAreaName());

		// 轮询时间间隔
		mPollingIntervalInfo = (TextView) findViewById(R.id.tvPollingIntervalInfo);
		mPollingIntervalInfo.setText(String
				.valueOf(AppConstants.SELECTED_POLLING_INTERVAL)
				+ AppConstants.MINUTE_UNIT);

		// 服务器信息
		lyServerInfo = (RelativeLayout) findViewById(R.id.rlServerInfo);
		lyServerInfo.setOnClickListener(this);
		lyServerInfo.setOnTouchListener(this);

		// 服务器信息设置
		String serverInfo = preference.getString(AppConstants.URI_IP_PORT, "");
		mServerInfo = (TextView) findViewById(R.id.tvServerInfo);
		mServerInfo.setText(serverInfo);

		// 返回按钮
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnBack.setOnTouchListener(this);
		
		// 完全退出按钮
		btnQuitApp = (Button) findViewById(R.id.btnQuitApp);
		btnQuitApp.setOnClickListener(this);
	}

	/**
	 * 从设置信息中获取警告时间段名称
	 * 
	 * @return
	 */
	private String getSelectedTimeAreaName() {
		String selectedTimeAreaName = "";
		String selectedTimeArea = preference.getString(AppConstants.SELECTED_TIME_AREA, "");
		
		int selectedTimeAreaCode = (!AppConstants.EMPTY.equals(selectedTimeArea)) ? Integer.valueOf(selectedTimeArea) : 1;

		for (int i = 0; i < AppConstants.TIME_AREA_CODE.length; i++) {
			if (AppConstants.TIME_AREA_CODE[i] == selectedTimeAreaCode) {
				selectedTimeAreaName = AppConstants.TIME_AREA_NAME[i];
				// 报警时间段对话框打开时，默认选中的数据
				mSelectedTimeAreaIndex = i;
			}
		}

		return selectedTimeAreaName;

	}

}
