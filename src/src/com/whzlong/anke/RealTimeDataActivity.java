package com.whzlong.anke;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.whzlong.anke.TableAdapter.TableCell;
import com.whzlong.anke.TableAdapter.TableRow;

/**
 * @author blowingSnow 实时状态查询界面
 * 
 */
public class RealTimeDataActivity extends Activity implements OnClickListener {
	private Button btnBack = null;
	private Button btnSelect = null;
	private ProgressBar pbDataLoad;
	private ListView lv;
	
	private String factoryCode;  //炼钢厂代码
	private String[] columns = new String[]{"position", "packageFactory", "BakingType", "BakingTemperature", "StandardTemperature"
			,"BakingTime","gasFlow","airFlow","currentAlarm"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_real_time_data);

		initViews();

	}

	private void initViews() {
		// TODO Auto-generated method stub
		// 隐藏进度条
		pbDataLoad = (ProgressBar) findViewById(R.id.pbDataLoad);
		pbDataLoad.setMax(100);
		pbDataLoad.setProgress(0);
		pbDataLoad.setVisibility(View.GONE);

		// 查询处理
		btnSelect = (Button) findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(this);

		// 返回
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(this);
	}
	
	/**
	 * 通过Web Service请求数据
	 * 
	 * @param factory
	 * 		  钢厂代码
	 * @return
	 */
	private JSONArray getListData(String factory){
		JSONArray jsonArray = new JSONArray();
		JSONObject rowData = null;
		
		try {
			rowData = new JSONObject();
			rowData.put(columns[0], "1#");
			rowData.put(columns[1], "2014/08/08 08:20");
			rowData.put(columns[2], "240.33");
			rowData.put(columns[3], "30.10%");
			rowData.put(columns[4], "92.24%");
			rowData.put(columns[5], "92.24%");
			rowData.put(columns[6], "92.24%");
			rowData.put(columns[7], "92.24%");
			rowData.put(columns[8], "92.24%");
			jsonArray.put(rowData);
			
			rowData = new JSONObject();
			rowData.put(columns[0], "1#");
			rowData.put(columns[1], "2014/08/10 08:20");
			rowData.put(columns[2], "248.32");
			rowData.put(columns[3], "28.10%");
			rowData.put(columns[4], "90.01%");
			rowData.put(columns[5], "92.24%");
			rowData.put(columns[6], "92.24%");
			rowData.put(columns[7], "92.24%");
			rowData.put(columns[8], "92.24%");
			jsonArray.put(rowData);		

			rowData = new JSONObject();
			rowData.put(columns[0], "1#");
			rowData.put(columns[1], "2014/08/15 08:20");
			rowData.put(columns[2], "242.41");
			rowData.put(columns[3], "31.40%");
			rowData.put(columns[4], "88.01%");
			rowData.put(columns[5], "92.24%");
			rowData.put(columns[6], "92.24%");
			rowData.put(columns[7], "92.24%");
			rowData.put(columns[8], "92.24%");
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
		switch (v.getId()) {
		case R.id.btnBack: // 返回
			Intent intent = new Intent();
			intent.setClass(RealTimeDataActivity.this, MainActivity.class);
			startActivity(intent);
			RealTimeDataActivity.this.finish();
			break;
		case R.id.btnSelect: // 查询处理
			pbDataLoad.setVisibility(View.VISIBLE);
			String[] titlesArray = new String[] { "烘烤位",
					"钢包号",
					"烘烤类型",
					"烘烤温度",
					"标准温度",
					"烘烤时间",
					"煤气流量",
					"空气流量",
					"当前报警" };
			setTableInfo(
					titlesArray,
					getListData(factoryCode));
			break;

		default:
			break;
		}
	}
	
	/**
	 * 显示表格数据
	 * 
	 * @param titlesArray
	 * 		表头
	 * @param jsonArray
	 * 		数据
	 */
	private void setTableInfo(String[] titlesArray, JSONArray jsonArray){
		lv = (ListView) this.findViewById(R.id.lstEnergySavingData);
		ArrayList<TableRow> table = new ArrayList<TableRow>();
		int columnLength = titlesArray.length;
		TableCell[] titles = new TableCell[columnLength];
		
		int width = this.getWindowManager().getDefaultDisplay().getWidth()/titles.length;
		
		// 定义标题
		for (int i = 0; i < titles.length; i++) {
			titles[i] = new TableCell(titlesArray[i], width + 25 * i,
					LayoutParams.FILL_PARENT, TableCell.STRING);
		}
		table.add(new TableRow(titles));
		
		// 每行的数据
		TableCell[] cells = null;
		
		try {
			
			for(int i = 0; i < jsonArray.length(); i++){
				cells = new TableCell[columnLength];
				
				JSONObject jsonObject = (JSONObject)jsonArray.getJSONObject(i);
				
				for(int j = 0; j < columns.length; j++){
					cells[j] = new TableCell(jsonObject.getString(columns[j]),
							width + 25 * j, LayoutParams.FILL_PARENT, TableCell.STRING);
				}
				
				
//				Iterator<?> it = jsonObject.keys();
//				String key = "";
//				int columnIndex = 0;
//				while(it.hasNext()){
//					key = it.next().toString();
//					cells[columnIndex] = new TableCell(jsonObject.getString(key),
//							width + 25 * columnIndex, LayoutParams.FILL_PARENT, TableCell.STRING);
//					columnIndex++;
//				}
				
				table.add(new TableRow(cells));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		for (int i = 0; i < cells.length - 1; i++) {
//			cells[i] = new TableCell("No." + String.valueOf(i),
//					titles[i].width, LayoutParams.FILL_PARENT, TableCell.STRING);
//		}
		
//		cells[cells.length - 1] = new TableCell(R.drawable.abc_ic_go,
//				titles[cells.length - 1].width, LayoutParams.WRAP_CONTENT,
//				TableCell.IMAGE);
		
		// 把表格的行添加到表格
//		for (int i = 0; i < 12; i++)
//			table.add(new TableRow(cells));
		
		TableAdapter tableAdapter = new TableAdapter(this, table);
		lv.setAdapter(tableAdapter);
		lv.setOnItemClickListener(new ItemClickEvent());
	}
	
	class ItemClickEvent implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Toast.makeText(RealTimeDataActivity.this,
					"选中第" + String.valueOf(arg2) + "行", 500).show();
		}
	}
}
