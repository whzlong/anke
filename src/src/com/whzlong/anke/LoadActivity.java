package com.whzlong.anke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadActivity extends Activity {

	private final int LOAD_TIME = 6000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);
		
		new Handler().postDelayed(new Runnable(){
			public void run(){
				Intent intent = new Intent();
				intent.setClass(LoadActivity.this, MainActivity.class);
				startActivity(intent);
				LoadActivity.this.finish();
			}
		}, LOAD_TIME);
		
		
		
	}
}
