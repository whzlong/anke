package com.whzlong.anke;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button btnQuit = null;
	private Button btnEnergySavingData = null;
	private Button btnRealTimeData = null;
	private Button btnSystemSet = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnEnergySavingData = (Button)findViewById(R.id.btnEnergySavingData);
		btnEnergySavingData.setTextSize(20);
		
		btnEnergySavingData.setOnClickListener(new Button.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, EnergySavingDataActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
		});
		
		
		btnRealTimeData = (Button)findViewById(R.id.btnRealTimeData);
		btnRealTimeData.setTextSize(20);
		
		btnSystemSet = (Button)findViewById(R.id.btnSystemSet);
		btnSystemSet.setTextSize(20);
		
		btnSystemSet.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SystemSetActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
		});
		
		btnQuit = (Button)findViewById(R.id.btnQuit);
		btnQuit.setTextSize(20);
		
//		btnQuit.setOnClickListener(listener);
		btnQuit.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE); 
//				am.killBackgroundProcesses(getPackageName());
//				Toast.makeText(context, "quit", Toast.LENGTH_LONG).show();
				MainActivity.this.finish();
			}
		});
	}
	
//	private OnClickListener listener = new OnClickListener() {
//		public void onClick(View view) {
//			switch (view.getId()) {
//			case R.id.btnEnergySavingData:
//				break;
//			case R.id.btnRealTimeData:
//				break;
//			case R.id.btnSystemSet:
//				break;
//			case R.id.btnQuit:
////				ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE); 
////				am.killBackgroundProcesses(getPackageName());
////				System.exit(0);
//				Toast.makeText(this.getClass(), "quit", Toast.LENGTH_LONG).show();
//				break;
//			default:
//				break;
//			}
//		}
//	};

}
