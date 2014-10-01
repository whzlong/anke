package com.whzlong.anke;

import android.app.Activity;

import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.os.Bundle;
import android.view.View;
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
import android.widget.TextView;
import android.widget.Toast;

public class EnergySavingDataActivity extends Activity {
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	private Button btnBack = null;
	private Button btnSelect;
	private ListView lv;
	private ProgressBar pbDataLoad;
	private String factoryCode;
	private TextView tvFactory;
	private EditText etDatatimeFrom;
	private EditText etDatatimeTo;
	private String[] columns = new String[]{"position", "lastDataPerWeek", "hourEnergyConsumption", "fractionalEnergySaving", "operatingRate"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_energy_saving_data);
		
		//隐藏进度条
		pbDataLoad = (ProgressBar)findViewById(R.id.pbDataLoad);
		pbDataLoad.setMax(100);
		pbDataLoad.setProgress(0);
		pbDataLoad.setVisibility(View.GONE);
		
		//查询处理
		btnSelect = (Button)findViewById(R.id.btnSelect);
		btnSelect.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v){
				pbDataLoad.setVisibility(View.VISIBLE);
				
				etDatatimeFrom = (EditText)findViewById(R.id.etDatatimeFrom);
				etDatatimeTo = (EditText)findViewById(R.id.etDatatimeTo);
				
				String[] titlesArray = new String[]{"烘烤位", "每周最后一条数据", "小时能耗", "节能率", "作业率"};
				setTableInfo(titlesArray, getListData(factoryCode, etDatatimeFrom.getText().toString(), etDatatimeTo.getText().toString()));
			}
		});
		
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(EnergySavingDataActivity.this,
						MainActivity.class);
				startActivity(intent);
				EnergySavingDataActivity.this.finish();
			}
		});		
	}

	class ItemClickEvent implements AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Toast.makeText(EnergySavingDataActivity.this,
					"选中第" + String.valueOf(arg2) + "行", 500).show();
		}
	}
	
	/**
	 * 通过Web Service请求数据
	 * 
	 * @param factory
	 * 		  钢厂代码
	 * @param dateTimeFrom
	 * 		  查询开始时间
	 * @param dateTimeTo
	 * 		  查询结束时间
	 * @return
	 */
	private JSONArray getListData(String factory, String dateTimeFrom, String dateTimeTo){
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
}
