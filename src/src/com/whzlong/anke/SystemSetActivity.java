package com.whzlong.anke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class SystemSetActivity extends Activity {
	private Button btnBack;
	private RelativeLayout rlLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_set);
		
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
					//返回按钮
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
