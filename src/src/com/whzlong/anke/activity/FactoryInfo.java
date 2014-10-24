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
import com.whzlong.anke.R;
import com.whzlong.anke.R.drawable;
import com.whzlong.anke.R.id;
import com.whzlong.anke.R.layout;
import com.whzlong.anke.bean.Url;
import com.whzlong.anke.common.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
	private String selectedFactoryCode;
	private String selectedFactoryName = "";
	private HashMap<String, String> factoryInfo;
	private String[] factoryNameArr;
	private RadioGroup rgFactoryInfo;
	private int countflg = 0;

	private String[] columns = new String[] { "SteelWorksCode",
			"SteelWorksName" };

	// 定义一个Handler,更新一览数据
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == AppConstants.OK) {
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

				loadingLayout.setVisibility(View.GONE);
				dataListLayout.setVisibility(View.VISIBLE);

				//Thread.currentThread().interrupt();
			} else {
				Toast.makeText(context, "无法获取数据，请检查网络连接", Toast.LENGTH_LONG)
						.show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_factory_info);

		context = this.getApplicationContext();
		// 初始化各种视图组件
		initViews();
		// 获取钢厂信息
		getFactoryInfo();
		// // 启动线程，获取数据
		// new Thread(new ObtainFactoryInfoThread()).start();

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

		selectedFactoryCode = this.getIntent().getStringExtra("factoryCode");

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
							case 1: // 节能数据界面
								intent.setClass(FactoryInfo.this,
										EnergySavingData.class);
								break;
							case RealTimeData.IS_REATIM_DATA_ACTIVITY: // 实时状态查询呢
								intent.setClass(FactoryInfo.this,
										RealTimeData.class);

								break;

							default:
								break;
							}
							
							intent.putExtra("factoryCode", factoryCode);
							intent.putExtra("factoryName", checkedRb.getText());
							startActivity(intent);
							FactoryInfo.this.finish();
						}

						countflg++;
					}

				});

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
			case 1:
				// 节能数据界面
				intent.setClass(FactoryInfo.this, EnergySavingData.class);
				break;
			case RealTimeData.IS_REATIM_DATA_ACTIVITY:
				// 实时状态查询
				intent.setClass(FactoryInfo.this, RealTimeData.class);
				break;
			default:
				break;
			}

			intent.putExtra("factoryCode", selectedFactoryCode);
			intent.putExtra("factoryName", selectedFactoryName);
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

//	/**
//	 * 获取钢厂数据的线程
//	 * 
//	 */
//	public class ObtainFactoryInfoThread implements Runnable {
//		@Override
//		public void run() {
//			JSONArray returnData = getFactoryInfo();
//
//			Message msg = new Message();
//			msg.what = STOP;
//			Bundle bundle = new Bundle();
//
//			try {
//				int length = returnData.length();
//				String[] array1 = new String[length];
//				String[] array2 = new String[length];
//
//				for (int i = 0; i < length; i++) {
//					JSONObject jsonObj = returnData.getJSONObject(i);
//
//					array1[i] = jsonObj.getString("code");
//					array2[i] = jsonObj.getString("name");
//				}
//
//				bundle.putStringArray("code", array1);
//				bundle.putStringArray("name", array2);
//
//				msg.setData(bundle);
//
//				mHandler.sendMessage(msg);
//
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				msg.what = ERROR;
//			}
//		}
//	}

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
							// TODO Auto-generated catch block
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

		// JSONArray jsonArray = new JSONArray();
		//
		// JSONObject rowData = null;
		//
		// try {
		// rowData = new JSONObject();
		// rowData.put("code", "01100");
		// rowData.put("name", "宝钢");
		// jsonArray.put(rowData);
		//
		// rowData = new JSONObject();
		// rowData.put("code", "02100");
		// rowData.put("name", "普钢");
		// jsonArray.put(rowData);
		//
		// rowData = new JSONObject();
		// rowData.put("code", "03100");
		// rowData.put("name", "美钢");
		// jsonArray.put(rowData);
		//
		// rowData = new JSONObject();
		// rowData.put("code", "03101");
		// rowData.put("name", "美钢1");
		// jsonArray.put(rowData);
		//
		// rowData = new JSONObject();
		// rowData.put("code", "03102");
		// rowData.put("name", "美钢2");
		// jsonArray.put(rowData);
		//
		// rowData = new JSONObject();
		// rowData.put("code", "03103");
		// rowData.put("name", "美钢3");
		// jsonArray.put(rowData);
		//
		// rowData = new JSONObject();
		// rowData.put("code", "03104");
		// rowData.put("name", "美钢4");
		// jsonArray.put(rowData);
		//
		// rowData = new JSONObject();
		// rowData.put("code", "03105");
		// rowData.put("name", "美钢5");
		// jsonArray.put(rowData);
		//
		// rowData = new JSONObject();
		// rowData.put("code", "03106");
		// rowData.put("name", "美钢6");
		// jsonArray.put(rowData);
		//
		// rowData = new JSONObject();
		// rowData.put("code", "03107");
		// rowData.put("name", "美钢7");
		// jsonArray.put(rowData);
		//
		// rowData = new JSONObject();
		// rowData.put("code", "03108");
		// rowData.put("name", "美钢8");
		// jsonArray.put(rowData);
		//
		// rowData = new JSONObject();
		// rowData.put("code", "03109");
		// rowData.put("name", "美钢9");
		// jsonArray.put(rowData);
		//
		// rowData = new JSONObject();
		// rowData.put("code", "03110");
		// rowData.put("name", "美钢10");
		// jsonArray.put(rowData);
		//
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		//return jsonArray;

	}

}
