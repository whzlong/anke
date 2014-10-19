package com.whzlong.anke;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AppService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onStart(Intent intent, int startId){
		super.onStart(intent, startId);
		
	}

}
