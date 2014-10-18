package com.whzlong.anke;

import android.app.Activity;
import android.content.SharedPreferences;

public class BaseActivity extends Activity {
	
	protected String base_ip_port = "";
	
	public BaseActivity(){
		super();
		// 获取服务器端信息
		SharedPreferences preference = BaseActivity.this.getSharedPreferences(
						"perference", MODE_PRIVATE);

		base_ip_port = AppConstants.PROTOCOL
						+ preference.getString(AppConstants.URI_IP_PORT, "");
	}
}
