package com.whzlong.anke;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button btnQuit = null;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnQuit = (Button)findViewById(R.id.btnQuit);
		btnQuit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE); 
				am.killBackgroundProcesses(getPackageName());
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
//				ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE); 
//				am.killBackgroundProcesses(getPackageName());
//				System.exit(0);
//				break;
//			default:
//				break;
//			}
//		}
//	};

}
