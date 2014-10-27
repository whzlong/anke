package com.whzlong.anke;

import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class AppService extends Service {

//	final Handler handler = new Handler(){  
//		      public void handleMessage(Message msg) {  
//		          switch (msg.what) {      
//		             case 1:      
//		                 break;      
//		              }      
//		              super.handleMessage(msg);  
//		        }    
//		     };  
		
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onStart(Intent intent, int startId){
		super.onStart(intent, startId);
		
//		TimerTask task = new TimerTask(){  
//			public void run() {  
//				       Message message = new Message();      
//				       message.what = 1;      
//				       handler.sendMessage(message);    
//				    }  
//		}
//		
//		timer = new Timer(true);
//		timer.schedule(task,1000, 1000);
		
	}

}
