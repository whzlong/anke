package com.whzlong.anke.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whzlong.anke.AppConstants;
import com.whzlong.anke.R;


public class SystemSet extends Activity implements OnClickListener, OnTouchListener {
	private Button btnBack;
	private RelativeLayout lyWarningFactory;
	private RelativeLayout lyWaringTimeArea;
	private RelativeLayout lyServerInfo;
	private TextView mWarningFactoryInfo;
	private TextView mWaringTimeAreaInfo;
	private TextView mPollingIntervalInfo;
	private TextView mServerInfo;

	private static final int WARNING_FACTORY_LENGTH = 15;
	private SharedPreferences preference;
	private int mSelectedTimeAreaIndex = -1;
	// 显示在界面上的报警时间段
	private static final String[] mTimeAreaName = {"00:00 ~ 08:00","08:00 ~ 17:00","17:00 ~ 24:00"};
	// 报警时间段对应的Code，供查询使用
	private static final String[] mTimeAreaCode = {"1","2","3"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_set);
		//初始化各种视图组件
		InitViews();
	}

	/**
	 * 单击事件
	 */
	public void onClick(View v) {
		Intent intent = null;
				
		switch (v.getId()) {
			case R.id.btnBack:
				//返回按钮
			    intent = new Intent();
				intent.setClass(SystemSet.this,
						Main.class);
				startActivity(intent);
				SystemSet.this.finish();
				break;
			case R.id.rlWarningFactory:
				//选择需要提示警告的工厂
			    intent = new Intent();
				intent.setClass(SystemSet.this,
						MultiFactoryInfo.class);
				startActivity(intent);
				SystemSet.this.finish();
				break;
			case R.id.rlWaringTimeArea:
				//报警时间段设置
				AlertDialog.Builder builder = new AlertDialog.Builder(SystemSet.this); 
				
				mSelectedTimeAreaIndex = -1;
				builder.setTitle(SystemSet.this.getString(R.string.waringTimeArea));
				//设置选项
				builder.setSingleChoiceItems(mTimeAreaName, mSelectedTimeAreaIndex, new DialogInterface.OnClickListener() {  
			        public void onClick(DialogInterface dialog, int position) {  
			        	mSelectedTimeAreaIndex = position; 
			        }  
			    });
				
				//确认
			    builder.setPositiveButton(SystemSet.this.getString(R.string.confirm), new DialogInterface.OnClickListener() {  
			        public void onClick(DialogInterface dialog, int position) {  
			            if(mSelectedTimeAreaIndex >= 0) {
			            	Editor editor = preference.edit();
							editor.putString(AppConstants.SELECTED_TIME_AREA, mTimeAreaCode[mSelectedTimeAreaIndex]);
							editor.commit();
							
			            	//设置界面警告时间段的值
			            	mWaringTimeAreaInfo.setText(mTimeAreaName[mSelectedTimeAreaIndex]);
			            }  
			        }  
			    });  
			    
			    //取消
			    builder.setNegativeButton(SystemSet.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {  
			        public void onClick(DialogInterface dialog, int position) {  
			 
			        }  
			    });
			    
			   builder.create().show(); 
				
				break;
			case R.id.rlServerInfo:
				//设置服务器信息
			    intent = new Intent();
				intent.setClass(SystemSet.this,
						ServerInfo.class);
				startActivity(intent);
				SystemSet.this.finish();
			default:
				break;
		}

	}

	/**
	 * 触摸事件
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.btnBack) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				btnBack.setBackgroundResource(R.drawable.light_gray);
			}

		}

		return false;
	}
	
	
	/**
	 * 初始化各种视图组件
	 */
	private void InitViews() {
		preference = SystemSet.this.getSharedPreferences(
				"perference", MODE_PRIVATE);
		
		//报警炼钢厂
		lyWarningFactory = (RelativeLayout)findViewById(R.id.rlWarningFactory);
		lyWarningFactory.setOnClickListener(this);

		String defaultCheckedFactory = preference.getString(
				AppConstants.SELECTED_WARNING_FACTORY_NAME, "");
		
		String displayFactoryName = defaultCheckedFactory;
		if(defaultCheckedFactory.length() > WARNING_FACTORY_LENGTH){
			displayFactoryName = defaultCheckedFactory.substring(0,WARNING_FACTORY_LENGTH) + "...";
		}
		mWarningFactoryInfo = (TextView)findViewById(R.id.tvWarningFactoryInfo);
		mWarningFactoryInfo.setText(displayFactoryName);
		
		//报警时间段
		lyWaringTimeArea = (RelativeLayout)findViewById(R.id.rlWaringTimeArea);
		lyWaringTimeArea.setOnClickListener(this);
		
		mWaringTimeAreaInfo = (TextView)findViewById(R.id.tvWaringTimeAreaInfo);
		mWaringTimeAreaInfo.setText(getSelectedTimeAreaName());

		//轮询时间间隔
		mPollingIntervalInfo = (TextView)findViewById(R.id.tvPollingIntervalInfo);
		mPollingIntervalInfo.setText(String.valueOf(AppConstants.SELECTED_POLLING_INTERVAL) + AppConstants.MINUTE_UNIT);
		
		//服务器信息
		lyServerInfo = (RelativeLayout)findViewById(R.id.rlServerInfo);
		lyServerInfo.setOnClickListener(this);
		
		//服务器信息设置
		String serverInfo = preference.getString(AppConstants.URI_IP_PORT, "");
		mServerInfo = (TextView)findViewById(R.id.tvServerInfo);
		mServerInfo.setText(serverInfo);
		
		//返回按钮
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnBack.setOnTouchListener(this);
	}
	
	/**
	 * 从设置信息中获取警告时间段名称
	 * @return
	 */
	private String getSelectedTimeAreaName(){
		String selectedTimeAreaName = "";
		String selectedTimeAreaCode = preference.getString(
				AppConstants.SELECTED_TIME_AREA, "");
		
		for(int i = 0; i < mTimeAreaCode.length; i++){
			if(mTimeAreaCode[i].equals(selectedTimeAreaCode)){
				selectedTimeAreaName = mTimeAreaName[i];
				//报警时间段对话框打开时，默认选中的数据
				mSelectedTimeAreaIndex = i;
			}
		}
		
		return selectedTimeAreaName;
		
	}
	
}
