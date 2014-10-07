package com.whzlong.anke;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MultiFactoryInfoActivity extends Activity {
	private Button btnBack;
	private RelativeLayout loadingLayout;
	private RelativeLayout dataListLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_factory_info);
		
		addFactoryInfoView();
		
		//按钮事件管理
		ButtonListener bl = new ButtonListener();
		
		//加载布局
		loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
		loadingLayout.setVisibility(View.GONE);
		
		//返回按钮
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(bl);
		btnBack.setOnTouchListener(bl);
		
	}
	
	/**
	 * 显示炼钢厂信息
	 */
	private void addFactoryInfoView(){
		ScrollView sv = (ScrollView)findViewById(R.id.svFactoryInfoList);
		LinearLayout mainLayout = new LinearLayout(this);
		
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams rowLayoutParams =   
				new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		
		RelativeLayout rowLayout = null;
		RelativeLayout.LayoutParams colLayoutParams = null;
		
		TextView tv = null;
		CheckBox cb = null;
		for(int i = 0; i < 20; i++){
			rowLayout = new RelativeLayout(this);
			
		    tv = new TextView(this);
		    tv.setId(100000);
			tv.setText("上海宝钢" + String.valueOf(i));
			colLayoutParams =   
					new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
			colLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
			tv.setLayoutParams(colLayoutParams);
			
		    cb = new CheckBox(this);
			cb.setChecked(true);
			colLayoutParams =   
					new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
			colLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			cb.setLayoutParams(colLayoutParams);
			
			rowLayout.addView(tv);
			
			rowLayout.addView(cb);
			mainLayout.addView(rowLayout, rowLayoutParams);
		}
		
		sv.addView(mainLayout);
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
			
			if(R.id.btnBack == v.getId()){
				//返回按钮
			    intent = new Intent();
				intent.setClass(MultiFactoryInfoActivity.this,
						SystemSetActivity.class);
				startActivity(intent);
				MultiFactoryInfoActivity.this.finish();
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
