package com.whzlong.anke.activity;

import com.whzlong.anke.AppConstants;
import com.whzlong.anke.AppManager;
import com.whzlong.anke.bean.Url;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class BaseActivity extends Activity {

	protected String base_ip_port = "";
	// 获取服务器端信息
	protected SharedPreferences preference;
	// 是否允许全屏
	private boolean allowFullScreen = true;

	// 是否允许销毁
	private boolean allowDestroy = true;

	private View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		allowFullScreen = true;
		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);

		// 获取服务器端信息
		preference = this.getSharedPreferences("perference",
				MODE_PRIVATE);

		String ipPort = preference.getString(AppConstants.URI_IP_PORT, "");
		
		if("".equals(ipPort)){
			base_ip_port = Url.HTTP
					+ Url.DEFAULT_URI_IP_PORT;
			
			Editor editor = preference.edit();
			editor.putString(AppConstants.URI_IP_PORT, Url.DEFAULT_URI_IP_PORT);
			editor.commit();
		}else{
			base_ip_port = Url.HTTP
					+ preference.getString(AppConstants.URI_IP_PORT, "");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	public boolean isAllowFullScreen() {
		return allowFullScreen;
	}

	/**
	 * 设置是否可以全屏
	 * 
	 * @param allowFullScreen
	 */
	public void setAllowFullScreen(boolean allowFullScreen) {
		this.allowFullScreen = allowFullScreen;
	}

	public void setAllowDestroy(boolean allowDestroy) {
		this.allowDestroy = allowDestroy;
	}

	public void setAllowDestroy(boolean allowDestroy, View view) {
		this.allowDestroy = allowDestroy;
		this.view = view;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && view != null) {
			view.onKeyDown(keyCode, event);
			if (!allowDestroy) {
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
