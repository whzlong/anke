package com.whzlong.anke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class LoadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_load);
		
		Intent intent = new Intent();
		intent.setClass(LoadActivity.this, MainActivity.class);
		startActivity(intent);
		LoadActivity.this.finish();
	}
}
