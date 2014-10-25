package com.whzlong.anke.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import com.whzlong.anke.R.drawable;
import com.whzlong.anke.R.id;
import com.whzlong.anke.R.layout;
import com.whzlong.anke.adapter.TableAdapter;
import com.whzlong.anke.adapter.TableAdapter.TableCell;
import com.whzlong.anke.adapter.TableAdapter.TableRow;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class WarningInfo extends BaseActivity implements OnClickListener,
OnTouchListener{

	private Button btnBack;
	private Button btnSelect;
	private ListView lv;
	private EditText etDatatimeFrom;
	private EditText etDatatimeTo;
	private RelativeLayout loadingLayout;
	private RelativeLayout dataListLayout;
	protected static final int STOP = 0x10000;
	protected static final int ERROR = 0x20000;
	// 全局Context
	private AppContext appContext;
	private String[] titlesArray = new String[] { "炼钢厂", "烘烤位", "报警时间",
			"报警内容"};
	private String[] columns = new String[] { "factory", "positon",
			"waringTime", "waringContent"};
	
	// 定义一个Handler,更新一览数据
	private Handler mHandler = new Handler() {
				public void handleMessage(Message msg) {
					switch (msg.what) {
						case AppConstants.OK:
							loadingLayout.setVisibility(View.GONE);
							Bundle bundle = msg.getData();

							String[] array1 = bundle.getStringArray(columns[0]);
							String[] array2 = bundle.getStringArray(columns[1]);
							String[] array3 = bundle.getStringArray(columns[2]);
							String[] array4 = bundle.getStringArray(columns[3]);

							int rowCnt = array1.length;
							int colCnt = columns.length;

							String[][] dataArray = new String[rowCnt][colCnt];

							for (int i = 0; i < rowCnt; i++) {
								dataArray[i][0] = array1[i];
								dataArray[i][1] = array2[i];
								dataArray[i][2] = array3[i];
								dataArray[i][3] = array4[i];
							}

							setTableInfo(titlesArray, dataArray);
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
					
					btnSelect.setClickable(true);
					loadingLayout.setVisibility(View.GONE);
				}
			};
			
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warning_info);
		
		appContext = (AppContext) getApplication();
		
		// 初始化各种视图组件
		initViews();
	}
	

	private void initViews(){
		//加载布局
		loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
		loadingLayout.setVisibility(View.GONE);

		//一览数据布局
		dataListLayout = (RelativeLayout) findViewById(R.id.dataListLayout);
		dataListLayout.setVisibility(View.GONE);

		//查询处理
		btnSelect = (Button) findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(this);
				
		//返回
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
		btnBack.setOnTouchListener(this);
	}
	
	/**
	 * 单击事件
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;
				
		switch (v.getId()) {
			case R.id.btnBack:
				//返回按钮
			    intent = new Intent();
				intent.setClass(WarningInfo.this,
						Main.class);
				startActivity(intent);
				WarningInfo.this.finish();
				break;
			case R.id.btnSelect:
				//查询处理按钮
				String strDatatimeFrom = ((EditText) findViewById(R.id.etDatatimeFrom)).getText().toString();
				String strDatatimeTo = ((EditText) findViewById(R.id.etDatatimeTo)).getText().toString();
				
				if(checkInput(strDatatimeFrom, strDatatimeTo)){
					loadingLayout.setVisibility(View.VISIBLE);
					dataListLayout.setVisibility(View.GONE);
					btnSelect.setClickable(false);
					
					getListData(strDatatimeFrom, strDatatimeTo);
				}
				
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
	 * 验证输入
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	private boolean checkInput(String strDateFrom, String strDateTo){
		if("".equals(strDateFrom) || "".equals(strDateTo)){
			Toast.makeText(appContext,
					appContext.getString(R.string.input_prompt_select_date),
					Toast.LENGTH_LONG).show();
			return false;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date dateFrom = null;
		Date dateTo = null;
		long maxInterval = 30;

		try {
			dateFrom = format.parse(strDateFrom);
			dateTo = format.parse(strDateTo);
			
			if(dateFrom.compareTo(dateTo) > 0){
				Toast.makeText(appContext,
						appContext.getString(R.string.error_info_date_start_end),
						Toast.LENGTH_LONG).show();
				
				return false;
			}
			
			long days = (dateTo.getTime() - dateFrom.getTime()) / (24*60*60*1000);  
			
			if(days > maxInterval){
				Toast.makeText(appContext,
						appContext.getString(R.string.error_info_date_interval),
						Toast.LENGTH_LONG).show();
				
				return false;
			}

		} catch (ParseException e) {
			Toast.makeText(appContext,
					appContext.getString(R.string.error_info_date_format),
					Toast.LENGTH_LONG).show();
			return false;
		}
		
		return true;
	}
	/**
	 * 通过Web Service请求数据
	 * 
	 * @param dateTimeFrom
	 *            查询开始时间
	 * @param dateTimeTo
	 *            查询结束时间
	 * @return
	 */
	private void getListData(String dateTimeFrom,
			String dateTimeTo) {
		
		String identityUrl = base_ip_port + Url.URL_HISTORY_WARNING_INFO;

		identityUrl = StringUtils.setParams(identityUrl, dateTimeFrom,
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

								JSONObject jsonObj = null;
								for (int i = 0; i < length; i++) {
									jsonObj = returnData.getJSONObject(i);

									array1[i] = jsonObj.getString(columns[0]);
									array2[i] = jsonObj.getString(columns[1]);
									array3[i] = jsonObj.getString(columns[2]);
									array4[i] = jsonObj.getString(columns[3]);
								}

								bundle.putStringArray(columns[0], array1);
								bundle.putStringArray(columns[1], array2);
								bundle.putStringArray(columns[2], array3);
								bundle.putStringArray(columns[3], array4);

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
		//获取ListView
		lv = (ListView) this.findViewById(R.id.lstWarningInfo);
		
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
