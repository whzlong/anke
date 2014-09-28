package com.whzlong.anke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.ArrayList;
import com.whzlong.anke.TableAdapter.TableCell;
import com.whzlong.anke.TableAdapter.TableRow;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class EnergySavingDataActivity extends Activity {
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	private Button btnBack = null;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_energy_saving_data);

		lv = (ListView) this.findViewById(R.id.lstEnergySavingData);
		ArrayList<TableRow> table = new ArrayList<TableRow>();
		TableCell[] titles = new TableCell[5];// 每行5个单元
		int width = this.getWindowManager().getDefaultDisplay().getWidth()/titles.length;
		// 定义标题
		for (int i = 0; i < titles.length; i++) {
			titles[i] = new TableCell("标题" + String.valueOf(i), width + 8 * i,
					LayoutParams.FILL_PARENT, TableCell.STRING);
		}
		table.add(new TableRow(titles));
		
		// 每行的数据
		TableCell[] cells = new TableCell[5];// 每行5个单元
		for (int i = 0; i < cells.length - 1; i++) {
			cells[i] = new TableCell("No." + String.valueOf(i),
					titles[i].width, LayoutParams.FILL_PARENT, TableCell.STRING);
		}
		
		cells[cells.length - 1] = new TableCell(R.drawable.abc_ic_go,
				titles[cells.length - 1].width, LayoutParams.WRAP_CONTENT,
				TableCell.IMAGE);
		
		// 把表格的行添加到表格
		for (int i = 0; i < 12; i++)
			table.add(new TableRow(cells));
		
		TableAdapter tableAdapter = new TableAdapter(this, table);
		lv.setAdapter(tableAdapter);
		lv.setOnItemClickListener(new ItemClickEvent());
		
		
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
}
