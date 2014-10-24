package com.whzlong.anke.activity;

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
import java.util.ArrayList;

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
import com.whzlong.anke.adapter.TableAdapter;
import com.whzlong.anke.adapter.TableAdapter.TableCell;
import com.whzlong.anke.adapter.TableAdapter.TableRow;
import com.whzlong.anke.bean.Url;
import com.whzlong.anke.common.StringUtils;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class EnergySavingData extends BaseActivity implements OnClickListener,
		OnTouchListener {
	private EditText etFactoryName;
	private Button btnBack;
	private Button btnSelect;
	private ListView lv;
	private String factoryCode = "";
	private RelativeLayout loadingLayout;
	private RelativeLayout dataListLayout;
	// 全局Context
	private AppContext appContext;
	private String[] titlesArray = new String[] { "烘烤位", "每周最后一条数据", "小时能耗",
			"节能率", "作业率" };
	private String[] columns = new String[] { "OPCGroup", "Date", "SXSHL",
			"SSYL", "SJNL" };

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

					int rowCnt = array1.length;
					int colCnt = columns.length;

					String[][] dataArray = new String[rowCnt][colCnt];

					for (int i = 0; i < rowCnt; i++) {
						dataArray[i][0] = array1[i];
						dataArray[i][1] = array2[i];
						dataArray[i][2] = array3[i];
						dataArray[i][3] = array4[i];
						dataArray[i][4] = array5[i];
					}

					setTableInfo(titlesArray, dataArray);
					dataListLayout.setVisibility(View.VISIBLE);
					
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
		setContentView(R.layout.activity_energy_saving_data);

		appContext = (AppContext) getApplication();
		// 初始化各种视图组件
		initViews();
	}

	private void initViews() {
		// //按钮事件管理
		// 设置为只读
		etFactoryName = (EditText) findViewById(R.id.etFactoryName);
		etFactoryName.setCursorVisible(false);
		etFactoryName.setFocusable(false);
		etFactoryName.setFocusableInTouchMode(false);
		etFactoryName.setOnClickListener(this);

		// 如果从选择钢厂界面返回，需要设置选择的钢厂信息
		Intent intent = this.getIntent();
		factoryCode = intent.getStringExtra("factoryCode");
		String factoryName = intent.getStringExtra("factoryName");
		etFactoryName.setText(factoryName);
		// 加载布局
		loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
		loadingLayout.setVisibility(View.GONE);
		// 一览数据布局
		dataListLayout = (RelativeLayout) findViewById(R.id.dataListLayout);
		dataListLayout.setVisibility(View.GONE);
		// 查询处理
		btnSelect = (Button) findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(this);
		// 返回
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnBack.setOnTouchListener(this);
	}

	/**
	 * 各种按钮点击事件处理
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;

		switch (v.getId()) {
		case R.id.btnBack:
			// 返回按钮
			intent = new Intent();
			intent.setClass(EnergySavingData.this, Main.class);
			startActivity(intent);
			EnergySavingData.this.finish();
			break;
		case R.id.etFactoryName:
			intent = new Intent();
			intent.setClass(EnergySavingData.this, FactoryInfo.class);
			intent.putExtra("previousActivityFlag", 1);
			intent.putExtra("factoryCode", factoryCode);
			startActivity(intent);
			EnergySavingData.this.finish();
			break;
		case R.id.btnSelect:
			// 查询处理按钮
			loadingLayout.setVisibility(View.VISIBLE);
			dataListLayout.setVisibility(View.GONE);
			btnSelect.setClickable(false);

			String selectDateFrom = ((EditText) findViewById(R.id.etDatatimeFrom))
					.getText().toString();
			String selectDateTo = ((EditText) findViewById(R.id.etDatatimeTo))
					.getText().toString();

			checkInput(selectDateFrom, selectDateTo);
			// 从服务器上获取数据
			getListData(factoryCode, selectDateFrom, selectDateTo);
			
			break;

		default:
			break;
		}
	}

	/**
	 * 验证输入数据
	 * 
	 * @param selectDateFrom
	 * @param selectDateTo
	 */
	private void checkInput(String dateFrom, String dateTo) {
		if ("".equals(dateFrom) || "".equals(dateTo)) {
			Toast.makeText(appContext,
					appContext.getString(R.string.input_prompt_select_date),
					Toast.LENGTH_LONG).show();
		}

		// TODO: 1.查询时间验证

	}

	/**
	 * 各种控件触摸事件处理
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
	 * 通过Web Service请求数据
	 * 
	 * @param factory
	 *            钢厂代码
	 * @param dateTimeFrom
	 *            查询开始时间
	 * @param dateTimeTo
	 *            查询结束时间
	 * @return
	 */
	private void getListData(String factory, String dateTimeFrom,
			String dateTimeTo) {
		String identityUrl = base_ip_port + Url.URL_ENERGY_SAVING_DATA;

		identityUrl = StringUtils.setParams(identityUrl, factory, dateTimeFrom,
				dateTimeTo);

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

								JSONObject jsonObj = null;
								for (int i = 0; i < length; i++) {
									jsonObj = returnData.getJSONObject(i);

									array1[i] = jsonObj.getString(columns[0]);
									array2[i] = jsonObj.getString(columns[1]);
									array3[i] = jsonObj.getString(columns[2]);
									array4[i] = jsonObj.getString(columns[3]);
									array5[i] = jsonObj.getString(columns[4]);
								}

								bundle.putStringArray(columns[0], array1);
								bundle.putStringArray(columns[1], array2);
								bundle.putStringArray(columns[2], array3);
								bundle.putStringArray(columns[3], array4);
								bundle.putStringArray(columns[4], array5);

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
