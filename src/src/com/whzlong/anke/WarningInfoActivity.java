package com.whzlong.anke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class WarningInfoActivity extends Activity {

	private Button btnBack = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warning_info);
		
		//返回
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(WarningInfoActivity.this,
						MainActivity.class);
				startActivity(intent);
				WarningInfoActivity.this.finish();
			}
		});		
	}
}
