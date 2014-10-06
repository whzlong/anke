package com.whzlong.anke;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.whzlong.anke.TableAdapter.TableCell;
import com.whzlong.anke.TableAdapter.TableRow;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EnergySavingDataActivity extends Activity {
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	private EditText etFactoryName;
	private Button btnBack = null;
	private Button btnSelect;
	private ListView lv;
	private String factoryCode = "";
	private EditText etDatatimeFrom;
	private EditText etDatatimeTo;
	private RelativeLayout loadingLayout;
	private RelativeLayout dataListLayout;
	protected static final int STOP = 0x10000;
	protected static final int ERROR = 0x20000;
	protected Context context = null;
	private String[] titlesArray = new String[] { "烘烤位", "每周最后一条数据", "小时能耗",
			"节能率", "作业率" };
	private String[] columns = new String[] { "position", "lastDataPerWeek",
			"hourEnergyConsumption", "fractionalEnergySaving", "operatingRate" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_energy_saving_data);
		
		context = this.getApplicationContext();
		
		//按钮事件管理
		ButtonListener bl = new ButtonListener();
		
		//设置为只读
		etFactoryName = (EditText)findViewById(R.id.etFactoryName);	
		etFactoryName.setCursorVisible(false);      
		etFactoryName.setFocusable(false);         
		etFactoryName.setFocusableInTouchMode(false);
		etFactoryName.setOnClickListener(bl);

		Intent intent = this.getIntent();
		
		factoryCode = intent.getStringExtra("factoryCode");
		String factoryName = intent.getStringExtra("factoryName");
		etFactoryName.setText(factoryName);
		
		//加载布局
		loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
		loadingLayout.setVisibility(View.GONE);

		//一览数据布局
		dataListLayout = (RelativeLayout) findViewById(R.id.dataListLayout);
		dataListLayout.setVisibility(View.GONE);

		//查询处理
		btnSelect = (Button) findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(bl);

		//返回
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(bl);
		btnBack.setOnTouchListener(bl);
	}

	
//	class ItemClickEvent implements AdapterView.OnItemClickListener {
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//				long arg3) {
//			Toast.makeText(EnergySavingDataActivity.this,
//					"选中第" + String.valueOf(arg2) + "行", 500).show();
//		}
//	}

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
	private JSONArray getListData(String factory, String dateTimeFrom,
			String dateTimeTo) {
		JSONArray jsonArray = new JSONArray();
		JSONObject rowData = null;

		try {
			rowData = new JSONObject();
			rowData.put(columns[0], "1#");
			rowData.put(columns[1], "2014/08/08 08:20");
			rowData.put(columns[2], "240.33");
			rowData.put(columns[3], "30.10%");
			rowData.put(columns[4], "92.24%");
			jsonArray.put(rowData);

			rowData = new JSONObject();
			rowData.put(columns[0], "1#");
			rowData.put(columns[1], "2014/08/10 08:20");
			rowData.put(columns[2], "248.32");
			rowData.put(columns[3], "28.10%");
			rowData.put(columns[4], "90.01%");
			jsonArray.put(rowData);

			rowData = new JSONObject();
			rowData.put(columns[0], "1#");
			rowData.put(columns[1], "2014/08/15 08:20");
			rowData.put(columns[2], "242.41");
			rowData.put(columns[3], "31.40%");
			rowData.put(columns[4], "88.01%");
			jsonArray.put(rowData);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonArray;
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
//		lv.setOnItemClickListener(new ItemClickEvent());
	}

	// 定义一个Handler,更新一览数据
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == STOP) {
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
				Thread.currentThread().interrupt();
			}else{
				Toast.makeText(context, "无法获取数据，请检查网络连接", Toast.LENGTH_LONG).show();
			}
			
			btnSelect.setClickable(true);
			loadingLayout.setVisibility(View.GONE);
		}
	};
	
	/**
	 * 处理各种按钮事件
	 *
	 */
	class ButtonListener implements OnClickListener, OnTouchListener {
		/**
		 * 单击事件
		 */
		public void onClick(View v) {
			Intent intent = null;
					
			switch (v.getId()) {
				case R.id.btnBack:
					//返回按钮
				    intent = new Intent();
					intent.setClass(EnergySavingDataActivity.this,
							MainActivity.class);
					startActivity(intent);
					EnergySavingDataActivity.this.finish();
					break;
				case R.id.etFactoryName:
					intent = new Intent();
					intent.setClass(EnergySavingDataActivity.this, FactoryInfoActivity.class);
					intent.putExtra("previousActivityFlag", 1);
					intent.putExtra("factoryCode", factoryCode);
					startActivity(intent);
					EnergySavingDataActivity.this.finish();
					break;
				case R.id.btnSelect:
					//查询处理按钮
					loadingLayout.setVisibility(View.VISIBLE);
					dataListLayout.setVisibility(View.GONE);
					btnSelect.setClickable(false);

					etDatatimeFrom = (EditText) findViewById(R.id.etDatatimeFrom);
					etDatatimeTo = (EditText) findViewById(R.id.etDatatimeTo);
					
					new Thread(new ObtainDataThread()).start();
					break;
				
				default:
					break;
			}

		}

		/**
		 * 触摸事件
		 */
		public boolean onTouch(View v, MotionEvent event) {
			if (v.getId() == R.id.btnBack) {
				// Drawable originalColor = btnBack.getBackground();  abc_list_pressed_holo_light
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btnBack.setBackgroundResource(R.drawable.light_gray);
				}

			}

			return false;
		}
	}
	
	/**
	 * 获取一览数据的线程
	 *
	 */
	public class ObtainDataThread implements Runnable{
		@Override
		public void run(){
			JSONArray returnData = getListData(factoryCode,
					etDatatimeFrom.getText().toString(),
					etDatatimeTo.getText().toString());
			Message msg = new Message();
			msg.what = STOP;
			Bundle bundle = new Bundle();

			try {
				int length = returnData.length();
				String[] array1 = new String[length];
				String[] array2 = new String[length];
				String[] array3 = new String[length];
				String[] array4 = new String[length];
				String[] array5 = new String[length];

				for (int i = 0; i < length; i++) {
					JSONObject jsonObj = returnData
							.getJSONObject(i);

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

				mHandler.sendMessage(msg);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.what = ERROR;
			}
		}
	}
	
}
