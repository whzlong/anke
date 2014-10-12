package com.whzlong.anke;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ServerInfoActivity extends Activity implements OnClickListener{
	private Button btnBack = null;
	private Button btnSave = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_info);
		
		// 保存
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);
		
		
		// 返回
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		
	}
	
	
	@Override
	public void onClick(View v){
		Intent intent = null;
		
		switch(v.getId()){
			case R.id.btnBack:
				//返回按钮
			    intent = new Intent();
				intent.setClass(ServerInfoActivity.this,
						SystemSetActivity.class);
				startActivity(intent);
				ServerInfoActivity.this.finish();
				break;
			case R.id.btnSave:
				//保存
				SharedPreferences preference = ServerInfoActivity.this.getSharedPreferences(
						"perference", MODE_PRIVATE);
				
				Editor editor = preference.edit();
				editor.putString(AppConstants.SERVER_INFO, "200.109.120.100:8080");
				editor.commit();
				
				
				break;
			default:
				break;
		}
	}
}
