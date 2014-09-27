package com.whzlong.anke;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EnergySavingDataActivity extends Activity {
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;

	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	private Button btnBack = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_energy_saving_data);
		
//		TableLayout tablelayout = (TableLayout)findViewById(R.id.tlEnergySavingData);
//		
//		tablelayout.setStretchAllColumns(true);
//		
//		for(int row = 0; row < 30; row++){
//			TableRow tableRow = new TableRow(this);
//			
//			for(int col=0; col < 20; col++){
//				TextView tv = new TextView(this);
//				tv.setText("("+col+","+row+")");
//				tableRow.addView(tv);
//			}
//			
//			tablelayout.addView(tableRow, new TableLayout.LayoutParams(FP,WC));
//		}
		
		
		btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(EnergySavingDataActivity.this, MainActivity.class);
				startActivity(intent);
				EnergySavingDataActivity.this.finish();
			}
		});
	}
}
