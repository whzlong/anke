package com.whzlong.anke;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SystemSetActivity extends Activity {
	private Button btnBack;
	private RelativeLayout rlLayout;
	private TextView mWarningFactoryInfo;
	private TextView mWaringTimeAreaInfo;
	private TextView mPollingIntervalInfo;
	private TextView mServerInfo;
	private static final String SELECTED_WARNING_FACTORY_NAME = "selectedWarningFactoryName";
	private static final int WARNING_FACTORY_LENGTH = 15;
	private SharedPreferences preference;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_set);
		//初始化各种视图组件
		InitViews();
	}

	/**
	 * 初始化各种视图组件
	 */
	private void InitViews() {
		preference = SystemSetActivity.this.getSharedPreferences(
				"perference", MODE_PRIVATE);
	
		String defaultCheckedFactory = preference.getString(
				SELECTED_WARNING_FACTORY_NAME, "");
		
		String displayFactoryName = defaultCheckedFactory;
		if(defaultCheckedFactory.length() > WARNING_FACTORY_LENGTH){
			displayFactoryName = defaultCheckedFactory.substring(0,WARNING_FACTORY_LENGTH) + "...";
		}
		
		mWarningFactoryInfo = (TextView)findViewById(R.id.tvWarningFactoryInfo);
		mWarningFactoryInfo.setText(displayFactoryName);
		
		mWaringTimeAreaInfo = (TextView)findViewById(R.id.tvWaringTimeAreaInfo);
		mWaringTimeAreaInfo.setText("09:00 ~ 18:00");
		
		mPollingIntervalInfo = (TextView)findViewById(R.id.tvPollingIntervalInfo);
		mPollingIntervalInfo.setText("10分钟");
		
		mServerInfo = (TextView)findViewById(R.id.tvServerInfo);
		mServerInfo.setText("20.200.10.200:8080");
		
		//按钮事件管理
		ButtonListener bl = new ButtonListener();
		
		rlLayout = (RelativeLayout)findViewById(R.id.rlWarningFactory);
		rlLayout.setOnClickListener(bl);
				
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(bl);
		btnBack.setOnTouchListener(bl);
	}
	
	/**
	 * 处理各种按钮事件
	 *
	 */
	class ButtonListener implements OnClickListener, OnTouchListener {
		/**
		 * 单击事件
		 */
		public void onClick(View v) {
			Intent intent = null;
					
			switch (v.getId()) {
				case R.id.btnBack:
					//返回按钮
				    intent = new Intent();
					intent.setClass(SystemSetActivity.this,
							MainActivity.class);
					startActivity(intent);
					SystemSetActivity.this.finish();
					break;
				case R.id.rlWarningFactory:
					//选择需要提示警告的工厂
				    intent = new Intent();
					intent.setClass(SystemSetActivity.this,
							MultiFactoryInfoActivity.class);
					startActivity(intent);
					SystemSetActivity.this.finish();
					break;
				case R.id.btnSelect:
					//查询处理按钮
					break;
				default:
					break;
			}

		}

		/**
		 * 触摸事件
		 */
		public boolean onTouch(View v, MotionEvent event) {
			if (v.getId() == R.id.btnBack) {
				// Drawable originalColor = btnBack.getBackground();  abc_list_pressed_holo_light
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btnBack.setBackgroundResource(R.drawable.light_gray);
				}

			}

			return false;
		}
	}
	
}
