package com.geok.langfang.pipeline.toolcase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.geok.langfang.Bean.KeypointHistoryBean;
import com.geok.langfang.pipeline.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * 
 * @author wuchangming 自然电位数据适配器
 * 
 */
public class KeypointHistoryAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<KeypointHistoryBean> list;
	static ArrayList<KeypointHistoryBean> upList = new ArrayList<KeypointHistoryBean>();
	boolean isAll = false;
	int isCheckAll = 0;//0表示未点击过全选按钮，1表示全选，2表示全不选
//	List<Integer> checkList = new ArrayList<Integer>();//选中的复选框
	Map<Integer, Boolean> isSelected  = new HashMap<Integer, Boolean>();  //选中的复选框
 
	SharedPreferences spf;// 本地文件保存选择的复选框
	Editor editor;// spf的修改工具提交更新数据
	public KeypointHistoryAdapter(Context context, ArrayList<KeypointHistoryBean> list) {
		this.context = context;
		this.list = list;
		 for (int i = 0; i < list.size(); i++) {  
		        isSelected.put(i, false);  
		   }  
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	String[] checkList = null;// 
	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final String guid =  list.get(arg0).getGuid();
		String name =  "关键点名称："+list.get(arg0).getName();
		String location = "位置：" +list.get(arg0).getLine()+list.get(arg0).getLocationdes();
		String validity = "有效期日期：" + list.get(arg0).getStart() + "至" + list.get(arg0).getEnd();
		int flag = Integer.valueOf(list.get(arg0).getIsupload());

		LayoutInflater inflater = LayoutInflater.from(context);

		
		final ViewHolder holder;
//		if(convertView == null){
			convertView = inflater.inflate(R.layout.data_history_list, null);
			holder = new ViewHolder();
			holder.keypointname = (TextView)convertView.findViewById(R.id.data_history_list_1);
			holder.keypoint_location = (TextView) convertView.findViewById(R.id.data_history_list_2);
			holder.keypoint_validity = (TextView) convertView.findViewById(R.id.data_history_list_3);
			holder.upload_text = (TextView) convertView.findViewById(R.id.data_history_list_4);
			holder.check = (CheckBox)convertView.findViewById(R.id.data_history_list_5);
			convertView.setTag(holder);
//		}else{
//			holder = (ViewHolder)convertView.getTag();
//		}
		holder.keypointname.setText(name);

		holder.keypoint_location.setText(location);

		holder.keypoint_validity.setText(validity);

		holder.check.setVisibility(View.VISIBLE);
		if (flag == 1) {
			holder.upload_text.setTextColor(Color.BLACK);
			holder.upload_text.setText("已上报");
//			holder.check.setVisibility(View.GONE);
		} else {
			holder.upload_text.setTextColor(Color.RED);
			holder.upload_text.setText("未上报");
//			holder.check.setVisibility(View.VISIBLE);
		}
//		for(int i=0;i<checkList.size();i++){
//			if(checkList.get(i)==arg0){
//				holder.check.setChecked(true);
//			}
//		}
//		if(checkList!=null){
//			for(int i=0;i<checkList.length;i++){
//				if(Integer.valueOf(checkList[i]) == arg0){
//					holder.check.setChecked(true);
//				}
//			}
//		}
		holder.check.setChecked(isSelected.get(arg0));
		if(holder.check.isChecked()){
			holder.check.setBackgroundResource(R.drawable.check_on);
		}else{
			holder.check.setBackgroundResource(R.drawable.check_off);
		}
		holder.check.setOnClickListener(new CheckBox.OnClickListener(){

			@Override
			public void onClick(View view) {
				  isCheckAll = 0;
				  if (isSelected.get(arg0)) {  
		                isSelected.put(arg0, false);  
		                if(upList.size()>0){
							upList.remove(list.get(arg0));
						}
//						if(checkList.size()>0){
//							checkList.remove(arg0);
//						}
						holder.check.setBackgroundResource(R.drawable.check_off);
		            } else {  
		                isSelected.put(arg0, true); 
		                if(upList.size()==0){  
							upList.add(list.get(arg0));
						}else{
							boolean isTwo = true;
							for(int i=0;i<upList.size();i++){
								if(list.get(arg0).getGuid().equals(upList.get(i).getGuid())){
									isTwo = false;
								}
							}
							if(isTwo){
								upList.add(list.get(arg0));
							}
						}
						
						holder.check.setBackgroundResource(R.drawable.check_on);
		            }  
		            notifyDataSetChanged();  
//				boolean isChecked = holder.check.isChecked();
//				list.get(arg0).setIsSelect(isChecked);
//				if(isChecked){
////					boolean isTwo1 = true;
////					if(checkList!=null){
////						for(int i=0;i<checkList.length;i++){
////							if(Integer.valueOf(checkList[i]) == arg0){
////								isTwo1 = true;
////							}
////						}
////					}
////					if(isTwo1){
////						String pileList = spf.getString("checklist", "");
////						editor.putString("checklist", pileList + arg0 + ";");
////						editor.commit();
////					}
//					
//					if(upList.size()==0){  
//						upList.add(list.get(arg0));
//					}else{
//						boolean isTwo = true;
//						for(int i=0;i<upList.size();i++){
//							if(list.get(arg0).getGuid().equals(upList.get(i).getGuid())){
//								isTwo = false;
//							}
//						}
//						if(isTwo){
//							upList.add(list.get(arg0));
//						}
//					}
//					
//					holder.check.setBackgroundResource(R.drawable.check_on);
//				}else{
//					if(upList.size()>0){
//						upList.remove(list.get(arg0));
//					}
////					if(checkList.size()>0){
////						checkList.remove(arg0);
////					}
//					holder.check.setBackgroundResource(R.drawable.check_off);
//				}
			}
			
		});
		if(isAll && isCheckAll==1){				
				holder.check.setChecked(true);
				upList.clear();
				for(int i=0;i<list.size();i++){
					upList.add(list.get(i));
					isSelected.put(i, true);  
				}
//				holder.check.setBackgroundResource(R.drawable.check_on);
				notifyDataSetChanged();
//			for(int i=0;i<list.size();i++){
//				int flag1 = Integer.valueOf(list.get(i).getIsupload());
//				if(flag1==0){
//					upList.add(list.get(i));
//					
//				}
//			}
		}else if((!isAll) && isCheckAll==2){

			holder.check.setChecked(false);
//			holder.check.setBackgroundResource(R.drawable.check_off);
			for(int i=0;i<list.size();i++){
				upList.add(list.get(i));
				isSelected.put(i, false);  
			}
			notifyDataSetChanged();
//			for(int i=0;i<list.size();i++){
//				int flag1 = Integer.valueOf(list.get(i).getIsupload());
//				if(flag1==0){
//					upList.add(list.get(i));
//				
//				}
//			}
			
			upList.clear();
		}
		return convertView;
	}
	
	static class ViewHolder{
		TextView keypointname;
		TextView keypoint_location;
		TextView keypoint_validity;
		TextView upload_text;
		CheckBox check;
	}
}
