package com.whzlong.anke.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.whzlong.anke.adapter.TableAdapter;
import com.whzlong.anke.adapter.TableAdapter.TableCell;
import com.whzlong.anke.adapter.TableAdapter.TableRow;
import com.whzlong.anke.bean.Url;
import com.whzlong.anke.common.StringUtils;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 节能数据查询
 */
public class EnergySavingData extends BaseActivity implements OnClickListener,
		OnTouchListener {
	private EditText etProjectName;
	private EditText etFactoryName;
	private EditText etDatatimeFrom;
	private EditText etDatatimeTo;
	private Button btnBack;
	private Button btnSelect;
	private ListView lv;
	private String selectedFactoryCode = "";
	private String selectedFactoryName = "";
	private String selectedProjectCode = "";
	private String selectedProjectName = "";
	private RelativeLayout loadingLayout;
	private RelativeLayout dataListLayout;
	// 全局Context
	private AppContext appContext;
	
	private String[] titlesArray = new String[] { "烘烤位", "起始日", "起始累计流量",
			"起始累计时间", "当前日", "当前累计流量", "当前累计时间", "累计使用流量", "累计使用时间", "平均小时能耗", "节能率", "使用率"};
	private String[] columns = new String[] { "HKW", "DataS", "MQLJS",
			"SJLJS", "DataE", "MQLJE", "SJLJE", "MQLJC", "SJLJC", "PJXSHL", "JNL", "SYL" };

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
				String[] array5 = bundle.getStringArray(columns[4]);
				String[] array6 = bundle.getStringArray(columns[5]);
				String[] array7 = bundle.getStringArray(columns[6]);
				String[] array8 = bundle.getStringArray(columns[7]);
				String[] array9 = bundle.getStringArray(columns[8]);
				String[] array10 = bundle.getStringArray(columns[9]);
				String[] array11 = bundle.getStringArray(columns[10]);
				String[] array12 = bundle.getStringArray(columns[11]);

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
					dataArray[i][9] = array10[i];
					dataArray[i][10] = array11[i];
					dataArray[i][11] = array12[i];
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
		setContentView(R.layout.activity_energy_saving_data);

		appContext = (AppContext) getApplication();
		// 初始化各种视图组件
		initViews();
	}

	private void initViews() {
		//按钮事件管理
		// 设置为只读
		// 钢厂信息
		etFactoryName = (EditText) findViewById(R.id.etFactoryName);
		etFactoryName.setCursorVisible(false);
		etFactoryName.setFocusable(false);
		etFactoryName.setFocusableInTouchMode(false);
		etFactoryName.setOnClickListener(this);
		// 项目信息
		etProjectName = (EditText) findViewById(R.id.etProjectName);
		etProjectName.setCursorVisible(false);
		etProjectName.setFocusable(false);
		etProjectName.setFocusableInTouchMode(false);
		etProjectName.setOnClickListener(this);
		
		// 查询开始日期
		etDatatimeFrom = (EditText) findViewById(R.id.etDatatimeFrom);
		etDatatimeFrom.setCursorVisible(false);
		etDatatimeFrom.setFocusable(false);
		etDatatimeFrom.setFocusableInTouchMode(false);
		etDatatimeFrom.setOnTouchListener(this);
		// 查询结束日期
		etDatatimeTo = (EditText) findViewById(R.id.etDatatimeTo);
		etDatatimeTo.setCursorVisible(false);
		etDatatimeTo.setFocusable(false);
		etDatatimeTo.setFocusableInTouchMode(false);
		etDatatimeTo.setOnTouchListener(this);

		// 如果从选择钢厂界面返回，需要设置选择的钢厂信息
		Intent intent = this.getIntent();
		selectedFactoryCode = intent.getStringExtra(AppConstants.SELECTED_FACTORY_CODE);
		selectedFactoryName = intent.getStringExtra(AppConstants.SELECTED_FACTORY_NAME);
		etFactoryName.setText(intent.getStringExtra(AppConstants.SELECTED_FACTORY_NAME));
		
		//项目信息
		selectedProjectCode = intent.getStringExtra(AppConstants.SELECTED_PROJECT_CODE);
		selectedProjectName = intent.getStringExtra(AppConstants.SELECTED_PROJECT_NAME);
		etProjectName.setText(intent.getStringExtra(AppConstants.SELECTED_PROJECT_NAME));
		
		if(selectedFactoryCode != null && intent.getStringExtra(AppConstants.PREVIOUS_FACTORY_CODE) != null
				&& !selectedFactoryCode.equals(intent.getStringExtra(AppConstants.PREVIOUS_FACTORY_CODE))){
			selectedProjectCode = null;
			selectedProjectName = null;
			etProjectName.setText(null);
		}
		
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
	 * 验证输入数据
	 * 
	 * @param selectDateFrom
	 * @param selectDateTo
	 */
	private boolean checkInput(String strDateFrom, String strDateTo) {
		if (StringUtils.isEmpty(selectedFactoryCode)) {
			Toast.makeText(appContext,
					appContext.getString(R.string.msg_error_factory),
					Toast.LENGTH_LONG).show();
			return false;
		}

		if (StringUtils.isEmpty(selectedProjectCode)) {
			Toast.makeText(appContext,
					appContext.getString(R.string.msg_error_project),
					Toast.LENGTH_LONG).show();
			return false;
		}
		
		if ("".equals(strDateFrom) || "".equals(strDateTo)) {
			Toast.makeText(appContext,
					appContext.getString(R.string.input_prompt_select_date),
					Toast.LENGTH_LONG).show();
			return false;
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFrom = null;
		Date dateTo = null;
		long maxInterval = 30;

		try {
			dateFrom = format.parse(strDateFrom);
			dateTo = format.parse(strDateTo);

			if (dateFrom.compareTo(dateTo) > 0) {
				Toast.makeText(
						appContext,
						appContext.getString(R.string.msg_error_date_start_end),
						Toast.LENGTH_LONG).show();

				return false;
			}

			long days = (dateTo.getTime() - dateFrom.getTime())
					/ (24 * 60 * 60 * 1000);

			if (days > maxInterval) {
				Toast.makeText(appContext,
						appContext.getString(R.string.msg_error_date_interval),
						Toast.LENGTH_LONG).show();

				return false;
			}

		} catch (ParseException e) {
			Toast.makeText(appContext,
					appContext.getString(R.string.msg_error__date_format),
					Toast.LENGTH_LONG).show();
			return false;
		}

		return true;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 返回按钮
			Intent intent = new Intent();
			intent.setClass(EnergySavingData.this, Main.class);
			startActivity(intent);
			EnergySavingData.this.finish();
		}

		return false;
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
			intent.putExtra("previousActivityFlag", AppConstants.ENERGY_SAVING);
			intent.putExtra(AppConstants.SELECTED_FACTORY_CODE, selectedFactoryCode);
			intent.putExtra(AppConstants.SELECTED_PROJECT_CODE, selectedProjectCode);
			intent.putExtra(AppConstants.SELECTED_PROJECT_NAME, selectedProjectName);
			startActivity(intent);
			EnergySavingData.this.finish();
			break;
		case R.id.etProjectName:
			if(selectedFactoryCode == null || "".equals(selectedFactoryCode)){
				Toast.makeText(
						appContext,
						appContext.getString(R.string.msg_error_must_input_factory),
						Toast.LENGTH_LONG).show();
				return;
			}
			intent = new Intent();
			intent.setClass(EnergySavingData.this, Projects.class);
			intent.putExtra("previousActivityFlag", AppConstants.ENERGY_SAVING);
			intent.putExtra(AppConstants.SELECTED_PROJECT_CODE, selectedProjectCode);
			intent.putExtra(AppConstants.SELECTED_FACTORY_CODE, selectedFactoryCode);
			intent.putExtra(AppConstants.SELECTED_FACTORY_NAME, selectedFactoryName);
			startActivity(intent);
			EnergySavingData.this.finish();
			break;
		case R.id.btnSelect:
			// 查询处理按钮
			// 查询开始时间
			String selectDateFrom = ((EditText) findViewById(R.id.etDatatimeFrom))
					.getText().toString();
			// 查询结束时间
			String selectDateTo = ((EditText) findViewById(R.id.etDatatimeTo))
					.getText().toString();

			if (checkInput(selectDateFrom, selectDateTo)) {
				loadingLayout.setVisibility(View.VISIBLE);
				dataListLayout.setVisibility(View.GONE);
				btnSelect.setClickable(false);

				selectDateFrom = selectDateFrom.replace("-", "");
				selectDateTo = selectDateTo.replace("-", "");

				// 从服务器上获取数据
				getListData(selectedProjectCode, selectDateFrom, selectDateTo);
			}

			break;

		default:
			break;
		}
	}

	/**
	 * 各种控件触摸事件处理
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int inType;
		Dialog dialog = null;
		Window dialogWindow = null;
		WindowManager wm = null;
		WindowManager.LayoutParams lyParams = null;
		Display dp = null;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			View view = View.inflate(this, R.layout.dialog_date_picker, null);
			final DatePicker datePicker = (DatePicker) view
					.findViewById(R.id.date_picker);
			builder.setView(view);

			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(System.currentTimeMillis());
			datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH), null);

			switch (v.getId()) {
			case R.id.btnBack:
				btnBack.setBackgroundResource(R.drawable.light_gray);
				break;
			case R.id.etDatatimeFrom:
				inType = etDatatimeFrom.getInputType();
				etDatatimeFrom.setInputType(InputType.TYPE_NULL);
				etDatatimeFrom.onTouchEvent(event);
				etDatatimeFrom.setInputType(inType);
				etDatatimeFrom.setSelection(etDatatimeFrom.getText().length());

				builder.setTitle("选取起始日期");
				builder.setPositiveButton("确  定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								StringBuffer sb = new StringBuffer();
								sb.append(String.format("%d-%02d-%02d",
										datePicker.getYear(),
										datePicker.getMonth() + 1,
										datePicker.getDayOfMonth()));

								etDatatimeFrom.setText(sb);
								etDatatimeFrom.requestFocus();

								dialog.cancel();
							}
						});

				dialog = builder.create();

				dialogWindow = dialog.getWindow();
				wm = getWindowManager();
				dp = wm.getDefaultDisplay(); // 获取屏幕宽、高用
				lyParams = dialogWindow.getAttributes(); // 获取对话框当前的参数值
				lyParams.height = (int) (dp.getHeight() * 0.4); // 高度设置为屏幕的0.6
				lyParams.width = (int) (dp.getWidth() * 0.5); // 宽度设置为屏幕的0.65
				dialogWindow.setAttributes(lyParams);

				dialog.show();
				break;
			case R.id.etDatatimeTo:
				inType = etDatatimeTo.getInputType();
				etDatatimeTo.setInputType(InputType.TYPE_NULL);
				etDatatimeTo.onTouchEvent(event);
				etDatatimeTo.setInputType(inType);
				etDatatimeTo.setSelection(etDatatimeTo.getText().length());

				builder.setTitle("选取结束日期");
				builder.setPositiveButton("确  定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								StringBuffer sb = new StringBuffer();
								sb.append(String.format("%d-%02d-%02d",
										datePicker.getYear(),
										datePicker.getMonth() + 1,
										datePicker.getDayOfMonth()));
								etDatatimeTo.setText(sb);

								dialog.cancel();
							}
						});

				dialog = builder.create();

				dialogWindow = dialog.getWindow();
				wm = getWindowManager();
				dp = wm.getDefaultDisplay(); // 获取屏幕宽、高用
				lyParams = dialogWindow.getAttributes(); // 获取对话框当前的参数值
				lyParams.height = (int) (dp.getHeight() * 0.4); // 高度设置为屏幕的0.6
				lyParams.width = (int) (dp.getWidth() * 0.5); // 宽度设置为屏幕的0.65
				dialogWindow.setAttributes(lyParams);

				dialog.show();
				break;
			default:
				break;
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
	private void getListData(String projectCode, String dateTimeFrom,
			String dateTimeTo) {
		String identityUrl = base_ip_port + Url.URL_ENERGY_SAVING_DATA;

		identityUrl = StringUtils.setParams(identityUrl, projectCode,
				dateTimeFrom, dateTimeTo);

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

							if (AppConstants.EMPTY.equals(retval)) {
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
								String[] array10 = new String[length];
								String[] array11 = new String[length];
								String[] array12 = new String[length];
								
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
									array10[i] = jsonObj.getString(columns[9]);
									array11[i] = jsonObj.getString(columns[10]);
									array12[i] = jsonObj.getString(columns[11]);
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
								bundle.putStringArray(columns[9], array10);
								bundle.putStringArray(columns[10], array11);
								bundle.putStringArray(columns[11], array12);

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
		// 定义标题
		int width = this.getWindowManager().getDefaultDisplay().getWidth()
				/ titles.length;
		//表格每列的自定义宽度
		int[] column_width = {width + 300, width + 240, width + 240, width + 240, width + 240
				, width + 240, width + 240, width + 240, width + 240, width + 240, width + 180, width + 180};
		
		//表格行高
		int row_height = 100;
				
		// 定义标题
		for (int i = 0; i < titles.length; i++) {
			titles[i] = new TableCell(titlesArray[i], column_width[i],
					row_height, TableCell.STRING);
		}
		
		table.add(new TableRow(titles));

		// 每行的数据
		TableCell[] cells = null;

		for (int i = 0; i < dataArray.length; i++) {
			cells = new TableCell[columnLength];

			for (int j = 0; j < columns.length; j++) {
				cells[j] = new TableCell(dataArray[i][j], column_width[j],
						row_height, TableCell.STRING);
			}

			table.add(new TableRow(cells));
		}

		TableAdapter tableAdapter = new TableAdapter(this, table);
		lv.setAdapter(tableAdapter);
	}
}
