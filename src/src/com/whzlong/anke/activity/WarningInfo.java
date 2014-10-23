package com.whzlong.anke.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.whzlong.anke.R;
import com.whzlong.anke.R.drawable;
import com.whzlong.anke.R.id;
import com.whzlong.anke.R.layout;
import com.whzlong.anke.adapter.TableAdapter;
import com.whzlong.anke.adapter.TableAdapter.TableCell;
import com.whzlong.anke.adapter.TableAdapter.TableRow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

public class WarningInfo extends Activity {

	private Button btnBack;
	private Button btnSelect;
	private ListView lv;
	private EditText etDatatimeFrom;
	private EditText etDatatimeTo;
	private RelativeLayout loadingLayout;
	private RelativeLayout dataListLayout;
	protected static final int STOP = 0x10000;
	protected static final int ERROR = 0x20000;
	protected Context context = null;
	private String[] titlesArray = new String[] { "炼钢厂", "烘烤位", "报警时间",
			"报警内容"};
	private String[] columns = new String[] { "factory", "positon",
			"waringTime", "waringContent"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_warning_info);
		
		context = this.getApplicationContext();
		//按钮事件管理
		ButtonListener bl = new ButtonListener();
		
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
	
		
	/**
	 * 通过Web Service请求数据
	 * 
	 * @param dateTimeFrom
	 *            查询开始时间
	 * @param dateTimeTo
	 *            查询结束时间
	 * @return
	 */
	private JSONArray getListData(String dateTimeFrom,
			String dateTimeTo) {
		JSONArray jsonArray = new JSONArray();
		JSONObject rowData = null;

		try {
			rowData = new JSONObject();
			rowData.put(columns[0], "宝钢一厂");
			rowData.put(columns[1], "1＃");
			rowData.put(columns[2], "2014/08/10 08:20");
			rowData.put(columns[3], "小时能耗过高");
			jsonArray.put(rowData);

			rowData = new JSONObject();
			rowData.put(columns[0], "宝钢二厂");
			rowData.put(columns[1], "1＃");
			rowData.put(columns[2], "2014/08/13 08:20");
			rowData.put(columns[3], "空气流量不足");
			jsonArray.put(rowData);

			rowData = new JSONObject();
			rowData.put(columns[0], "宝钢三厂");
			rowData.put(columns[1], "2＃");
			rowData.put(columns[2], "2014/08/15 08:20");
			rowData.put(columns[3], "烘烤时间过长");
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
					intent.setClass(WarningInfo.this,
							Main.class);
					startActivity(intent);
					WarningInfo.this.finish();
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
			JSONArray returnData = getListData(etDatatimeFrom.getText().toString(),
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

				for (int i = 0; i < length; i++) {
					JSONObject jsonObj = returnData
							.getJSONObject(i);

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

				mHandler.sendMessage(msg);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.what = ERROR;
			}
		}
	}
}
