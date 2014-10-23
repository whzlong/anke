package com.whzlong.anke.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.whzlong.anke.R;
import com.whzlong.anke.R.id;
import com.whzlong.anke.R.layout;
import com.whzlong.anke.activity.WarningInfo.ObtainDataThread;
import com.whzlong.anke.adapter.TableAdapter;
import com.whzlong.anke.adapter.TableAdapter.TableCell;
import com.whzlong.anke.adapter.TableAdapter.TableRow;

/**
 * @author blowingSnow 实时状态查询界面
 * 
 */
public class RealTimeData extends Activity implements OnClickListener {
	private Button btnBack = null;
	private Button btnSelect = null;
	private ListView lv;

	private EditText mFactoryName = null;

	private RelativeLayout loadingLayout;
	private RelativeLayout dataListLayout;

	public static final int IS_REATIM_DATA_ACTIVITY = 2;

	private String factoryCode; // 炼钢厂代码
	private String[] columns = new String[] { "position", "packageFactory",
			"BakingType", "BakingTemperature", "StandardTemperature",
			"BakingTime", "gasFlow", "airFlow", "currentAlarm" };
	String[] titlesArray = new String[] { "烘烤位", "钢包号", "烘烤类型", "烘烤温度", "标准温度",
			"烘烤时间", "煤气流量", "空气流量", "当前报警" };

	protected static final int STOP = 100;
	protected static final int ERROR = 403;

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
				dataListLayout.setVisibility(View.VISIBLE);
				Thread.currentThread().interrupt();
			} else {
				Toast.makeText(RealTimeData.this, "无法获取数据，请检查网络连接",
						Toast.LENGTH_LONG).show();
			}

			btnSelect.setClickable(true);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_real_time_data);

		initViews();

	}

	private void initViews() {
		// TODO Auto-generated method stub

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
	private JSONArray getListData(String factory) {
		JSONArray jsonArray = new JSONArray();
		JSONObject rowData = null;

		try {
			rowData = new JSONObject();
			rowData.put(columns[0], "1#");
			rowData.put(columns[1], "80");
			rowData.put(columns[2], "大修");
			rowData.put(columns[3], "938");
			rowData.put(columns[4], "900");
			rowData.put(columns[5], "60");
			rowData.put(columns[6], "164");
			rowData.put(columns[7], "206");
			rowData.put(columns[8], "无");
			jsonArray.put(rowData);

			rowData = new JSONObject();
			rowData.put(columns[0], "1#");
			rowData.put(columns[1], "11");
			rowData.put(columns[2], "预备");
			rowData.put(columns[3], "877");
			rowData.put(columns[4], "800");
			rowData.put(columns[5], "70");
			rowData.put(columns[6], "227");
			rowData.put(columns[7], "320");
			rowData.put(columns[8], "无");
			jsonArray.put(rowData);

			rowData = new JSONObject();
			rowData.put(columns[0], "1#");
			rowData.put(columns[1], "0");
			rowData.put(columns[2], "永久层");
			rowData.put(columns[3], "5");
			rowData.put(columns[4], "5");
			rowData.put(columns[5], "50");
			rowData.put(columns[6], "0");
			rowData.put(columns[7], "0");
			rowData.put(columns[8], "无");
			jsonArray.put(rowData);
			
			rowData = new JSONObject();
			rowData.put(columns[0], "1#");
			rowData.put(columns[1], "76");
			rowData.put(columns[2], "预备");
			rowData.put(columns[3], "917");
			rowData.put(columns[4], "910");
			rowData.put(columns[5], "20");
			rowData.put(columns[6], "186");
			rowData.put(columns[7], "214");
			rowData.put(columns[8], "无");
			jsonArray.put(rowData);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonArray;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
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
	
				new Thread(new ObtainDataThread()).start();
				break;
	
			default:
				break;
		}
	}

	/**
	 * 获取一览数据的线程
	 * 
	 */
	public class ObtainDataThread implements Runnable {
		@Override
		public void run() {
			JSONArray returnData = getListData(factoryCode);
			String s = returnData.toString();
			Log.d(RealTimeData.class.getSimpleName(), "s===" + s);

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
				String[] array6 = new String[length];
				String[] array7 = new String[length];
				String[] array8 = new String[length];
				String[] array9 = new String[length];

				for (int i = 0; i < length; i++) {
					JSONObject jsonObj = returnData.getJSONObject(i);

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

				mHandler.sendMessage(msg);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.what = ERROR;
			}
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
		// lv.setOnItemClickListener(new ItemClickEvent());
	}

	class ItemClickEvent implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Toast.makeText(RealTimeData.this,
					"选中第" + String.valueOf(arg2) + "行", 500).show();
		}
	}
}
