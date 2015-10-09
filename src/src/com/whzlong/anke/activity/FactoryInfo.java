package com.whzlong.anke.activity;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whzlong.anke.AppConstants;
import com.whzlong.anke.AppContext;
import com.whzlong.anke.R;
import com.whzlong.anke.bean.Url;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FactoryInfo extends BaseActivity implements OnClickListener,
		OnTouchListener {

	private Button btnBack;
	protected int previousActivityFlag;
	private RelativeLayout loadingLayout;
	private RelativeLayout dataListLayout;
	protected Context context = null;
	private String[] factoryCodeArr;
	private String selectedFactoryCode = "";
	private String selectedFactoryName = "";
	private String previousFactoryCode;
	private String selectedProCode = "";
	private String selectedProName = "";
	private HashMap<String, String> factoryInfo;
	private String[] factoryNameArr;
	private RadioGroup rgFactoryInfo;
	private int countflg = 0;
	// 全局Context
	private AppContext appContext;

	private String[] columns = new String[] { "SteelCode",
			"SteelName" };

	// 定义一个Handler,更新一览数据
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AppConstants.OK:
				Bundle bundle = msg.getData();

				factoryCodeArr = bundle.getStringArray(columns[0]);
				factoryNameArr = bundle.getStringArray(columns[1]);
				factoryInfo = new HashMap<String, String>();

				rgFactoryInfo = (RadioGroup) findViewById(R.id.rgFactoryInfo);

				RadioButton rb = null;

				for (int i = 0; i < factoryNameArr.length; i++) {
					rb = new RadioButton(rgFactoryInfo.getContext());

					rb.setText(factoryNameArr[i]);
					rb.setTextSize(20);
					// 选中其次选中的数据
					if (factoryCodeArr[i].equals(selectedFactoryCode)) {
						rb.setChecked(true);
						// 直接点击返回按钮后，将其次选择的数据返回
						selectedFactoryName = factoryNameArr[i];
					}

					rgFactoryInfo.addView(rb);

					factoryInfo.put(factoryNameArr[i], factoryCodeArr[i]);
				}

				dataListLayout.setVisibility(View.VISIBLE);
				break;
			case AppConstants.NG:
				Toast.makeText(
						appContext,
						appContext.getString(R.string.error_select_result_zero),
						Toast.LENGTH_LONG).show();
				break;
			case AppConstants.ERROR1:
				Toast.makeText(appContext,
						appContext.getString(R.string.error_network_connected),
						Toast.LENGTH_LONG).show();
				break;
			case AppConstants.ERROR2:
				Toast.makeText(appContext,
						appContext.getString(R.string.system_error),
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}

			loadingLayout.setVisibility(View.GONE);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_factory_info);

		appContext = (AppContext) getApplication();
		// 初始化各种视图组件
		initViews();
		// 获取钢厂信息
		getFactoryInfo();
	}

	private void initViews() {
		Intent it = this.getIntent();
		previousActivityFlag = it.getIntExtra("previousActivityFlag", 0);
		// 加载布局
		loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
		loadingLayout.setVisibility(View.VISIBLE);

		// 一览数据布局
		dataListLayout = (RelativeLayout) findViewById(R.id.dataListLayout);
		dataListLayout.setVisibility(View.GONE);

		// 返回处理
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnBack.setOnTouchListener(this);

		selectedFactoryCode = this.getIntent().getStringExtra(AppConstants.SELECTED_FACTORY_CODE);
		
		previousFactoryCode = this.getIntent().getStringExtra(AppConstants.SELECTED_FACTORY_CODE);
		
		selectedProCode = this.getIntent().getStringExtra(AppConstants.SELECTED_PROJECT_CODE);
		selectedProName = this.getIntent().getStringExtra(AppConstants.SELECTED_PROJECT_NAME);

		// 钢厂信息RadioGroup
		rgFactoryInfo = (RadioGroup) findViewById(R.id.rgFactoryInfo);
		rgFactoryInfo
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// 不是第一次执行此事件时，运行下面的代码（设置默认值时会激发此事件）
						if (countflg != 0 || selectedFactoryCode == null) {
							RadioButton checkedRb = (RadioButton) findViewById(checkedId);
							String factoryCode = factoryInfo.get(checkedRb
									.getText());

							// 返回按钮
							Intent intent = new Intent();
							switch (previousActivityFlag) {
							case AppConstants.ENERGY_SAVING:
								// 节能数据界面
								intent.setClass(FactoryInfo.this,
										EnergySavingData.class);
								break;
							case AppConstants.REAL_TIME:
								// 实时状态查询
								intent.setClass(FactoryInfo.this,
										RealTimeData.class);
								break;
							case AppConstants.WARNING_INFO:
								// 警告信息
								intent.setClass(FactoryInfo.this,
										WarningInfo.class);
							default:
								break;
							}

							intent.putExtra(AppConstants.SELECTED_FACTORY_CODE, factoryCode);
							intent.putExtra(AppConstants.SELECTED_FACTORY_NAME, checkedRb.getText());
							intent.putExtra(AppConstants.SELECTED_PROJECT_CODE, selectedProCode);
							intent.putExtra(AppConstants.SELECTED_PROJECT_NAME, selectedProName);
							intent.putExtra(AppConstants.PREVIOUS_FACTORY_CODE, previousFactoryCode);
							startActivity(intent);
							FactoryInfo.this.finish();
						}

						countflg++;
					}

				});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 返回按钮
			Intent intent = new Intent();

			switch (previousActivityFlag) {
			case AppConstants.ENERGY_SAVING:
				// 节能数据界面
				intent.setClass(FactoryInfo.this, EnergySavingData.class);
				break;
			case AppConstants.REAL_TIME:
				// 实时状态查询
				intent.setClass(FactoryInfo.this, RealTimeData.class);
				break;
			case AppConstants.WARNING_INFO:
				// 实时状态查询
				intent.setClass(FactoryInfo.this, WarningInfo.class);
				break;
			default:
				break;
			}

			intent.putExtra(AppConstants.SELECTED_FACTORY_CODE, selectedFactoryCode);
			intent.putExtra(AppConstants.SELECTED_FACTORY_NAME, selectedFactoryName);
			intent.putExtra(AppConstants.PREVIOUS_FACTORY_CODE, previousFactoryCode);
			intent.putExtra(AppConstants.SELECTED_PROJECT_CODE, selectedProCode);
			intent.putExtra(AppConstants.SELECTED_PROJECT_NAME, selectedProName);
			startActivity(intent);
			FactoryInfo.this.finish();
		}

		return false;
	}

	/**
	 * 单击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack:
			// 返回按钮
			Intent intent = new Intent();

			switch (previousActivityFlag) {
			case AppConstants.ENERGY_SAVING:
				// 节能数据界面
				intent.setClass(FactoryInfo.this, EnergySavingData.class);
				break;
			case AppConstants.REAL_TIME:
				// 实时状态查询
				intent.setClass(FactoryInfo.this, RealTimeData.class);
				break;
			case AppConstants.WARNING_INFO:
				// 实时状态查询
				intent.setClass(FactoryInfo.this, WarningInfo.class);
				break;
			default:
				break;
			}

			intent.putExtra(AppConstants.SELECTED_FACTORY_CODE, selectedFactoryCode);
			intent.putExtra(AppConstants.SELECTED_FACTORY_NAME, selectedFactoryName);
			intent.putExtra(AppConstants.PREVIOUS_FACTORY_CODE, previousFactoryCode);
			intent.putExtra(AppConstants.SELECTED_PROJECT_CODE, selectedProCode);
			intent.putExtra(AppConstants.SELECTED_PROJECT_NAME, selectedProName);
			startActivity(intent);
			FactoryInfo.this.finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 触摸事件
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
	 * 获取工厂信息
	 * 
	 * @return
	 */
	private void getFactoryInfo() {
		String identityUrl = base_ip_port + Url.URL_FACTORY_INFO;
		// 远程获取身份验证结果
		RequestQueue mQueue = Volley.newRequestQueue(this);

		StringRequest stringRequest = new StringRequest(identityUrl,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Log.d("TAG", response);
						Message msg = new Message();
						Bundle bundle = new Bundle();

						try {
							String retval = response.substring(1,
									response.length() - 1);
							retval = retval.replace("\\", "");

							if ("".equals(retval)) {
								msg.what = AppConstants.NG;
							} else {
								JSONArray returnData = new JSONArray(retval);

								int length = returnData.length();
								String[] array1 = new String[length];
								String[] array2 = new String[length];

								JSONObject jsonObj = null;
								for (int i = 0; i < length; i++) {
									jsonObj = returnData.getJSONObject(i);

									array1[i] = jsonObj.getString(columns[0]);
									array2[i] = jsonObj.getString(columns[1]);
								}

								bundle.putStringArray(columns[0], array1);
								bundle.putStringArray(columns[1], array2);

								msg.setData(bundle);
								msg.what = AppConstants.OK;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							msg.what = AppConstants.ERROR2;
						}

						mHandler.sendMessage(msg);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("TAG", error.getMessage(), error);
						Message msg = new Message();
						msg.what = AppConstants.ERROR1;
						mHandler.sendMessage(msg);
					}
				});

		mQueue.add(stringRequest);
	}

}
