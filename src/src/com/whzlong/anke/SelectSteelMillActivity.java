package com.whzlong.anke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @author blowingSnow 选择炼钢厂界面
 * 
 */
public class SelectSteelMillActivity extends Activity implements OnClickListener {
	private Button btnBack = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_steelmill_activity);
		initViews();

	}

	private void initViews() {
		// 返回
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
	}
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnBack: // 返回
			Intent intent = new Intent();
			intent.setClass(SelectSteelMillActivity.this, RealTimeDataActivity.class);
			startActivity(intent);
			SelectSteelMillActivity.this.finish();
			break;

		default:
			break;
		}
	}
	
}
