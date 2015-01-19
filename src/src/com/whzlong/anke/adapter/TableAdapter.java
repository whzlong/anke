package com.whzlong.anke.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

import com.whzlong.anke.R;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TableAdapter extends BaseAdapter {

	private Context context;
	private List<TableRow> table;

	public TableAdapter(Context context, List<TableRow> table) {
		this.context = context;
		this.table = table;
	}

	@Override
	public int getCount() {
		return table.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public TableRow getItem(int position) {
		return table.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TableRow tableRow = table.get(position);
		return new TableRowView(this.context, tableRow);
	}

	/**
	 * TableRowView 实现表格行的样式
	 * 
	 * @author hellogv
	 */
	class TableRowView extends LinearLayout {
		
		public TableRowView(Context context, TableRow tableRow) {
			super(context);

			this.setOrientation(LinearLayout.HORIZONTAL);
			
			for (int i = 0; i < tableRow.getSize(); i++) {
				// 逐个格单元添加到行
				TableCell tableCell = tableRow.getCellValue(i);
				
				// 按照格单元指定的大小设置空间
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
						tableCell.width, tableCell.height);
				
				// 预留空隙制造边框
				layoutParams.setMargins(0, 0, 1, 1);
				
				if (tableCell.type == TableCell.STRING) {
					// 如果格单元是文本内容
					TextView textCell = new TextView(context);
					
					textCell.setLines(1);
					textCell.setGravity(Gravity.CENTER);
					textCell.setBackgroundColor(R.drawable.light_green);
					
					// 背景黑色
					textCell.setText(String.valueOf(tableCell.value));
					textCell.setTextColor(Color.BLACK);
					addView(textCell, layoutParams);
					
				} else if (tableCell.type == TableCell.IMAGE) {
					// 如果格单元是图像内容
					ImageView imgCell = new ImageView(context);
					imgCell.setBackgroundColor(R.drawable.light_green);// 背景黑色
					imgCell.setImageResource((Integer) tableCell.value);
					addView(imgCell, layoutParams);
				}
			}
			
			this.setBackgroundColor(Color.WHITE);// 背景白色，利用空隙来实现边框
		}
	}

	/**
	 * TableRow 实现表格的行
	 * 
	 * @author hellogv
	 */
	public static class TableRow {
		private TableCell[] cell;

		public TableRow(TableCell[] cell) {
			this.cell = cell;
		}

		public int getSize() {
			return cell.length;
		}

		public TableCell getCellValue(int index) {
			if (index >= cell.length)
				return null;
			return cell[index];
		}
	}

	/**
	 * TableCell 实现表格的格单元
	 * 
	 * @author hellogv
	 */
	public static class TableCell {
		public static  final int STRING = 0;
		public static  final int IMAGE = 1;
		public Object value;
		public int width;
		public int height;
		private int type;

		public TableCell(Object value, int width, int height, int type) {
			this.value = value;
			this.width = width;
			this.height = height;
			this.type = type;
		}
	}

}
