package com.whzlong.anke.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whzlong.anke.AppConstants;
import com.whzlong.anke.AppContext;
import com.whzlong.anke.R;
import com.whzlong.anke.adapter.TableAdapter;
import com.whzlong.anke.adapter.TableAdapter.TableCell;
import com.whzlong.anke.adapter.TableAdapter.TableRow;
import com.whzlong.anke.bean.Url;
import com.whzlong.anke.common.StringUtils;

/**
 * @author blowingSnow 实时状态查询界面
 * 
 */
public class RealTimeData extends BaseActivity implements OnClickListener {
	private Button btnBack = null;
	private Button btnSelect = null;
	private ListView lv;

	private EditText mFactoryName = null;

	private RelativeLayout loadingLayout;
	private RelativeLayout dataListLayout;

	// 全局Context
	private AppContext appContext;
	public static final int IS_REATIM_DATA_ACTIVITY = 2;

	private String factoryCode; // 炼钢厂代码
	private String[] columns = new String[] { "HkwName", "GBH",
			"HKLX", "HKWD", "BJWD",
			"HKSJ", "MQLL", "KQLL", "BJLX" };
	String[] titlesArray = new String[] { "烘烤位", "钢包号", "烘烤类型", "烘烤温度", "标准温度",
			"烘烤时间", "煤气流量", "空气流量", "当前报警" };
	
	// 定义一个Handler,更新一览数据
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what){
			case AppConstants.OK:
				loadingLayout.setVisibility(View.GONE);
				Bundle bundle = msg.getData();

				String[] array1 = bundle.getStringArray(columns[0]);
				String[] array2 = bundle.getStringArray(columns[1]);
				String[] array3 = bundle.getStringArray(columns[2]);
				String[] array4 = bundle.getStringArray(columns[3]);
				String[] array5 = bundle.getStringArray(columns[4]);
				String[] array6 = bundle.getStringArray(columns[5]);
				String[] array7 = bundle.getStringArray(columns[6]);
				String[] array8 = bundle.getStringArray(columns[7]);
				String[] array9 = bundle.getStringArray(columns[8]);

				int rowCnt = array1.length;
				int colCnt = columns.length;

				String[][] dataArray = new String[rowCnt][colCnt];

				for (int i = 0; i < rowCnt; i++) {
					dataArray[i][0] = array1[i];
					dataArray[i][1] = array2[i];
					dataArray[i][2] = array3[i];
					dataArray[i][3] = array4[i];
					dataArray[i][4] = array5[i];
					dataArray[i][5] = array6[i];
					dataArray[i][6] = array7[i];
					dataArray[i][7] = array8[i];
					dataArray[i][8] = array9[i];
				}

				setTableInfo(titlesArray, dataArray);
				break;
			case AppConstants.NG:
				Toast.makeText(appContext, appContext.getString(R.string.error_select_result_zero), Toast.LENGTH_LONG).show();
				break;
			case AppConstants.ERROR1:
				Toast.makeText(appContext, appContext.getString(R.string.error_network_connected), Toast.LENGTH_LONG).show();
				break;
			case AppConstants.ERROR2:
				Toast.makeText(appContext,
						appContext.getString(R.string.system_error),
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}

			btnSelect.setClickable(true);
			loadingLayout.setVisibility(View.GONE);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_real_time_data);

		appContext = (AppContext) getApplication();
		// 初始化各种视图组件
		initViews();
	}

	private void initViews() {
		// 查询处理
		btnSelect = (Button) findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(this);

		// 选择钢铁厂,设置为只读
		mFactoryName = (EditText) findViewById(R.id.etFactoryName);
		mFactoryName.setCursorVisible(false);
		mFactoryName.setFocusable(false);
		mFactoryName.setFocusableInTouchMode(false);
		mFactoryName.setOnClickListener(this);

		Intent intent = this.getIntent();
		factoryCode = intent.getStringExtra("factoryCode");
		String factoryName = intent.getStringExtra("factoryName");
		mFactoryName.setText(factoryName);

		// 加载布局
		loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
		loadingLayout.setVisibility(View.GONE);

		// 一览数据布局
		dataListLayout = (RelativeLayout) findViewById(R.id.dataListLayout);
		dataListLayout.setVisibility(View.GONE);

		// 返回
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
	}

	/**
	 * 通过Web Service请求数据
	 * 
	 * @param factory
	 *            钢厂代码
	 * @return
	 */
	private void getListData(String factory) {
		String identityUrl = base_ip_port + Url.URL_REAL_TIME_DATA;

		identityUrl = StringUtils.setParams(identityUrl, factory);

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
								String[] array3 = new String[length];
								String[] array4 = new String[length];
								String[] array5 = new String[length];
								String[] array6 = new String[length];
								String[] array7 = new String[length];
								String[] array8 = new String[length];
								String[] array9 = new String[length];

								JSONObject jsonObj = null;
								for (int i = 0; i < length; i++) {
									jsonObj = returnData.getJSONObject(i);

									array1[i] = jsonObj.getString(columns[0]);
									array2[i] = jsonObj.getString(columns[1]);
									array3[i] = jsonObj.getString(columns[2]);
									array4[i] = jsonObj.getString(columns[3]);
									array5[i] = jsonObj.getString(columns[4]);
									array6[i] = jsonObj.getString(columns[5]);
									array7[i] = jsonObj.getString(columns[6]);
									array8[i] = jsonObj.getString(columns[7]);
									array9[i] = jsonObj.getString(columns[8]);
								}

								bundle.putStringArray(columns[0], array1);
								bundle.putStringArray(columns[1], array2);
								bundle.putStringArray(columns[2], array3);
								bundle.putStringArray(columns[3], array4);
								bundle.putStringArray(columns[4], array5);
								bundle.putStringArray(columns[5], array6);
								bundle.putStringArray(columns[6], array7);
								bundle.putStringArray(columns[7], array8);
								bundle.putStringArray(columns[8], array9);

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

	@Override
	public void onClick(View v) {
		Intent intent = null;
		
		switch (v.getId()) {
			case R.id.btnBack: // 返回
				intent = new Intent();
				intent.setClass(RealTimeData.this, Main.class);
				startActivity(intent);
				RealTimeData.this.finish();
				
				break;
			case R.id.etFactoryName: // 选择炼钢厂
				intent = new Intent();
				intent.setClass(RealTimeData.this,
						FactoryInfo.class);
				intent.putExtra("previousActivityFlag", IS_REATIM_DATA_ACTIVITY);
				intent.putExtra("factoryCode", factoryCode);
				startActivity(intent);
				RealTimeData.this.finish();
	
				break;
			case R.id.btnSelect: // 查询处理
				// 查询处理按钮
				loadingLayout.setVisibility(View.VISIBLE);
				dataListLayout.setVisibility(View.GONE);
				btnSelect.setClickable(false);
	
				getListData(factoryCode);
				break;
	
			default:
				break;
		}
	}

	/**
	 * 显示表格数据
	 * 
	 * @param titlesArray
	 *            表头
	 * @param jsonArray
	 *            数据
	 */
	private void setTableInfo(String[] titlesArray, String[][] dataArray) {
		lv = (ListView) this.findViewById(R.id.lstEnergySavingData);
		ArrayList<TableRow> table = new ArrayList<TableRow>();
		int columnLength = titlesArray.length;
		TableCell[] titles = new TableCell[columnLength];

		int width = this.getWindowManager().getDefaultDisplay().getWidth()
				/ titles.length;

		// 定义标题
		for (int i = 0; i < titles.length; i++) {
			titles[i] = new TableCell(titlesArray[i], width + 25 * i,
					LayoutParams.FILL_PARENT, TableCell.STRING);
		}
		table.add(new TableRow(titles));

		// 每行的数据
		TableCell[] cells = null;

		for (int i = 0; i < dataArray.length; i++) {
			cells = new TableCell[columnLength];

			for (int j = 0; j < columns.length; j++) {
				cells[j] = new TableCell(dataArray[i][j], width + 25 * j,
						LayoutParams.FILL_PARENT, TableCell.STRING);
			}

			table.add(new TableRow(cells));
		}

		TableAdapter tableAdapter = new TableAdapter(this, table);
		lv.setAdapter(tableAdapter);
	}
}
