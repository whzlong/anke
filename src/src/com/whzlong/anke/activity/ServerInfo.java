package com.whzlong.anke.activity;

import com.whzlong.anke.AppConstants;
import com.whzlong.anke.AppContext;
import com.whzlong.anke.R;
import com.whzlong.anke.common.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ServerInfo extends BaseActivity implements OnTouchListener,
		OnClickListener {
	private Button btnBack = null;
	private Button btnSave = null;
	// 全局Context
	private AppContext appContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_info);

		appContext = (AppContext) getApplication();
		// 初始化各种视图组件
		InitViews();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 返回按钮
			Intent intent = new Intent();
			intent.setClass(ServerInfo.this, SystemSet.class);
			startActivity(intent);
			ServerInfo.this.finish();
		}

		return false;
	}

	/**
	 * 处理各种触摸事件
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.btnBack) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				btnBack.setBackgroundResource(R.drawable.light_gray);
			}
		}

		return false;
	}

	/**
	 * 处理各种按钮点击事件
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.btnBack:
			// 返回按钮
			intent = new Intent();
			intent.setClass(ServerInfo.this, SystemSet.class);
			startActivity(intent);
			ServerInfo.this.finish();
			break;
		case R.id.btnSave:
			// 保存
			// IP地址
			String strServerIp = ((EditText) findViewById(R.id.etServerIp))
					.getText().toString();
			String strServerPort = ((EditText) findViewById(R.id.etServerPort))
					.getText().toString();

			// 输入验证处理
			if (StringUtils.checkInput(strServerIp, strServerPort)) {
				String ip_port = strServerIp + ":" + strServerPort;

				SharedPreferences preference = ServerInfo.this
						.getSharedPreferences("perference", MODE_PRIVATE);
				Editor editor = preference.edit();
				editor.putString(AppConstants.URI_IP_PORT, ip_port);
				editor.commit();

				// 返回
				intent = new Intent();
				intent.setClass(ServerInfo.this, SystemSet.class);
				startActivity(intent);
				ServerInfo.this.finish();
			} else {
				Toast.makeText(
						ServerInfo.this,
						appContext.getString(R.string.input_prompt_server_info),
						Toast.LENGTH_LONG).show();
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 初始化各种视图组件
	 */
	private void InitViews() {
		// 保存
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);

		// 返回
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnTouchListener(this);
		btnBack.setOnClickListener(this);
	}

}
