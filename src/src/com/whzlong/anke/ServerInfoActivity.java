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

public class ServerInfoActivity extends Activity implements OnTouchListener,OnClickListener{
	private Button btnBack = null;
	private Button btnSave = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_info);
		
		//初始化各种视图组件
		InitViews();
	}
	
	/**
	 * 初始化各种视图组件
	 */
	private void InitViews() {
		// 保存
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		
		// 返回
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
	}
	
	/**
	 * 处理各种触摸事件
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event){
		if (v.getId() == R.id.btnBack) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				btnBack.setBackgroundResource(R.drawable.light_gray);
			}
		}
		
		return false;
	}
	
	/**
	 * 处理各种按钮点击事件
	 */
	@Override
	public void onClick(View v){
		Intent intent = null;
		
		switch(v.getId()){
			case R.id.btnBack:
				//返回按钮
			    intent = new Intent();
				intent.setClass(ServerInfoActivity.this,
						SystemSetActivity.class);
				startActivity(intent);
				ServerInfoActivity.this.finish();
				break;
			case R.id.btnSave:
				//保存
				SharedPreferences preference = ServerInfoActivity.this.getSharedPreferences(
						"perference", MODE_PRIVATE);
				
				Editor editor = preference.edit();
				editor.putString(AppConstants.SERVER_INFO, "200.109.120.100:8080");
				editor.commit();
				
				
				break;
			default:
				break;
		}
	}
}
