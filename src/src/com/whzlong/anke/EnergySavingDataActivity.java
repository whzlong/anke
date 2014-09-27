package com.whzlong.anke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class EnergySavingDataActivity extends Activity {
	private Button btnBack = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_energy_saving_data);
		
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(EnergySavingDataActivity.this, MainActivity.class);
				startActivity(intent);
				EnergySavingDataActivity.this.finish();
			}
		});
	}
}
