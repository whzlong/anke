package com.whzlong.anke;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class CheckboxListAdapter extends BaseAdapter {
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;
	private List<Map<String, String>> mData;
	public static Map<Integer, Boolean> isSelected;    
	
	public CheckboxListAdapter(Context context, List<Map<String, String>> content){
		inflater = LayoutInflater.from(context);
		mData = content;
		
		isSelected = new HashMap<Integer, Boolean>();    
        for (int i = 0; i < mData.size(); i++) {    
            isSelected.put(i, false);    
        }    
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		// TODO Auto-generated method stub
		if(view == null){
            view = inflater.inflate(R.layout.checkbox_list_item, null);  
            viewHolder = new ViewHolder();  
            viewHolder.checkBox = (CheckBox)view.findViewById(R.id.listCheckbox);  
            view.setTag(viewHolder);  
		}else{
			viewHolder = (ViewHolder)view.getTag();  
		}
		
		viewHolder.checkBox.setChecked(isSelected.get(position));
		viewHolder.checkBox.setText(mData.get(position).get("factoryName").toString());
		
		
		return view;
	}

	
	
	public final class ViewHolder{
		public CheckBox checkBox;
	}
}
