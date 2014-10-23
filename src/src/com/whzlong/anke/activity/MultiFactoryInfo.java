package com.whzlong.anke.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.whzlong.anke.R.drawable;
import com.whzlong.anke.R.id;
import com.whzlong.anke.R.layout;
import com.whzlong.anke.adapter.CheckboxListAdapter;
import com.whzlong.anke.adapter.CheckboxListAdapter.ViewHolder;
import com.whzlong.anke.AppConstants;
import com.whzlong.anke.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MultiFactoryInfo extends Activity {
	private Button btnBack;
	private Button btnSave;
	private RelativeLayout loadingLayout;
	private RelativeLayout dataListLayout;
	private ListView lvCheckbox;
	private Context context;
	private Map<String, String> mpGetCodeByName = new HashMap<String, String>();
	private Map<String, String> mpGetNameByCode = new HashMap<String, String>();
	private List<String> lsSelectedFactoryCode = new ArrayList<String>();
	private SharedPreferences preference;
	private  String[] columns = new String[] { "factoryCode", "factoryName" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_factory_info);
		context = this.getApplicationContext();
		
		//初始化各种视图组件
		InitViews();
	}

	/**
	 * 初始化各种视图组件
	 */
	private void InitViews() {
		// 按钮事件管理
		ButtonListener bl = new ButtonListener();

		// 加载布局
		loadingLayout = (RelativeLayout) findViewById(R.id.loadingLayout);
		loadingLayout.setVisibility(View.VISIBLE);
		// 数据显示布局
		dataListLayout = (RelativeLayout) findViewById(R.id.dataListLayout);
		dataListLayout.setVisibility(View.GONE);

		// 保存
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(bl);

		// 返回按钮
		btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(bl);
		btnBack.setOnTouchListener(bl);

		new Thread(new ObtainDataThread()).start();
	}

	/**
	 * 显示钢厂信息
	 * 
	 * @param lsMap
	 */
	private void addFactoryInfoView(List<Map<String, String>> lsMap) {
		// 获取原来的设置信息
		preference = MultiFactoryInfo.this.getSharedPreferences(
				"perference", MODE_PRIVATE);
		String defaultCheckedFactory = preference.getString(
				AppConstants.SELECTED_WARNING_FACTORY_KEY, "");
		
		List<String> lsCheckedData = null;
		
		if(defaultCheckedFactory != ""){
			String[] defaultCheckedFactoryArray = defaultCheckedFactory.split("&");
			
			lsCheckedData = new ArrayList<String>();
			for (String code : defaultCheckedFactoryArray) {
				//保存选中的钢厂Code
				lsSelectedFactoryCode.add(code);
				//保存选中的钢厂名称
				lsCheckedData.add(mpGetNameByCode.get(code));
			}
		}
		

		CheckboxListAdapter cblistAdapter = new CheckboxListAdapter(this,
				lsMap, lsCheckedData);

		lvCheckbox = (ListView) findViewById(R.id.lvDataList);
		lvCheckbox.setAdapter(cblistAdapter);
		lvCheckbox.setItemsCanFocus(false);
		lvCheckbox.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		lvCheckbox
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ViewHolder vHollder = (ViewHolder) view.getTag();
						// 在每次获取点击的item时将对于的checkbox状态改变，同时修改map的值。
						vHollder.checkBox.toggle();
						// 钢厂代码
						String code = mpGetCodeByName.get(vHollder.checkBox
								.getText());
						if (vHollder.checkBox.isChecked()) {
							lsSelectedFactoryCode.add(code);
						} else {
							lsSelectedFactoryCode.remove(code);
						}

						CheckboxListAdapter.isSelected.put(position,
								vHollder.checkBox.isChecked());
					}
				});
	}

	/**
	 * 
	 * @return
	 */
	private JSONArray getDataFromServer() {
		JSONArray jsonArray = new JSONArray();
		JSONObject rowData = null;

		try {

			for (int i = 0; i < 20; i++) {
				rowData = new JSONObject();
				rowData.put(columns[0], String.valueOf(i));
				rowData.put(columns[1], "中国宝钢第" + (i + 1) + "厂");
				jsonArray.put(rowData);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonArray;
	}

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
				// 返回按钮
				intent = new Intent();
				intent.setClass(MultiFactoryInfo.this,
						SystemSet.class);
				startActivity(intent);
				MultiFactoryInfo.this.finish();
				break;
			case R.id.btnSave:
				String selectedFactoryCode = "";
				String selectedFactoryName = "";

				// 保存按钮
				StringBuffer sbCode = new StringBuffer();
				StringBuffer sbName = new StringBuffer();

				for (String code : lsSelectedFactoryCode) {
					sbCode.append("&" + code);
					sbName.append("&" + mpGetNameByCode.get(code));
				}

				if (lsSelectedFactoryCode.size() != 0) {
					selectedFactoryCode = sbCode.toString().substring(1);
					selectedFactoryName = sbName.toString().substring(1);
				}

				Editor editor = preference.edit();

				editor.putString(AppConstants.SELECTED_WARNING_FACTORY_KEY, selectedFactoryCode);
				editor.putString(AppConstants.SELECTED_WARNING_FACTORY_NAME, selectedFactoryName);
				
				editor.commit();

				intent = new Intent();
				intent.setClass(MultiFactoryInfo.this,
						SystemSet.class);
				startActivity(intent);
				MultiFactoryInfo.this.finish();
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
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					btnBack.setBackgroundResource(R.drawable.light_gray);
				}

			}

			return false;
		}
	}

	// 定义一个Handler,更新一览数据
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == AppConstants.STOP) {
				loadingLayout.setVisibility(View.GONE);
				Bundle bundle = msg.getData();

				String[] array1 = bundle.getStringArray(columns[0]);
				String[] array2 = bundle.getStringArray(columns[1]);

				int rowCnt = array1.length;

				Map<String, String> map = new HashMap<String, String>();
				List<Map<String, String>> lsMapFactoryNm = new ArrayList<Map<String, String>>();

				for (int i = 0; i < rowCnt; i++) {
					mpGetCodeByName.put(array2[i], array1[i]);
					mpGetNameByCode.put(array1[i], array2[i]);

					// 显示钢厂信息一览使用的数据
					map = new HashMap<String, String>();
					map.put(columns[1], array2[i]);
					lsMapFactoryNm.add(map);
				}

				addFactoryInfoView(lsMapFactoryNm);

				loadingLayout.setVisibility(View.GONE);
				dataListLayout.setVisibility(View.VISIBLE);
				Thread.currentThread().interrupt();
			} else {
				Toast.makeText(context, "无法获取数据，请检查网络连接", Toast.LENGTH_LONG)
						.show();
			}

			loadingLayout.setVisibility(View.GONE);
		}
	};

	/**
	 * 获取一览数据的线程
	 * 
	 */
	public class ObtainDataThread implements Runnable {
		@Override
		public void run() {
			JSONArray returnData = getDataFromServer();

			Message msg = new Message();
			msg.what = AppConstants.STOP;
			Bundle bundle = new Bundle();

			try {
				int length = returnData.length();
				String[] array1 = new String[length];
				String[] array2 = new String[length];

				for (int i = 0; i < length; i++) {
					JSONObject jsonObj = returnData.getJSONObject(i);

					array1[i] = jsonObj.getString(columns[0]);
					array2[i] = jsonObj.getString(columns[1]);
				}

				bundle.putStringArray(columns[0], array1);
				bundle.putStringArray(columns[1], array2);

				msg.setData(bundle);

				mHandler.sendMessage(msg);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg.what = AppConstants.ERROR;
			}
		}
	}
}
