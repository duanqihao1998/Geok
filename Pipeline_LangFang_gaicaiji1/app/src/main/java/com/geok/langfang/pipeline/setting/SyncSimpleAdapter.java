//package com.geok.langfang.pipeline.setting;
//
//import java.util.List;
//import java.util.Map;
//
//import com.geok.langfang.pipeline.MainView;
//import com.geok.langfang.pipeline.R;
//import com.geok.langfang.pipeline.setting.Sync.ViewHolder;
//import com.geok.langfang.request.MyApplication;
//import com.geok.langfang.tools.Tools;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.SimpleAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class SyncSimpleAdapter extends SimpleAdapter{
//
//	private List < Map < String , Object >> mData;
////	int count = 0 ;
////
//	public SyncSimpleAdapter(Context context,
//			List<? extends Map<String, Object>> data, int resource, String[] from,
//			int[] to) {
//		super(context, data, resource, from, to);
////		// TODO Auto-generated constructor stub
////		 mItemList = (List < Map < String , Object >> ) data;
////		 mItemList = (List < Map < String , Object >> ) data;
////		              if (data == null ){
////		                 count = 0 ;
////		             } else {
////		                 count = data.size();
////		             }
////
//	}
////	@Override
////	public int getCount() {
////		// TODO Auto-generated method stub
////		return mItemList.size();
////	}
////	@Override
////	public Object getItem(int position) {
////		// TODO Auto-generated method stub
////		return position;
////	}
////	@Override
////	public long getItemId(int position) {
////		// TODO Auto-generated method stub
////		return position;
////	}
////	@Override
////	  public View getView(int position, View convertView, ViewGroup parent) {
////	   // TODO Auto-generated method stub
////	   View v= super.getView(position, convertView, parent);
////	   Button btn=(Button) v.findViewById(R.id.sync_bt);
////	   btn.setTag(position);
////	   btn.setOnClickListener(new OnClickListener() {
////	    
////	    @Override
////	    public void onClick(View v) {
////	     // TODO Auto-generated method stub
////	     Toast.makeText(null, "单击我了"+v.getTag(), 1).show();
////	    }
////	   });
////	   return v;
////	  }
////	
//	
//	final class ViewHolder {
//		public ImageView image;
//		public TextView tittle;
//		public TextView time;
//		public TextView flag;
//		public Button bt;
//	}
//
//	class MyAdapter extends BaseAdapter {
//		private LayoutInflater mInflater;
//
//		public MyAdapter(Context context) {
//			this.mInflater = LayoutInflater.from(context);
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return mData.size();
//
//		}
//
//		@Override
//		public Object getItem(int arg0) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int arg0) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		@Override
//		public View getView(final int position, View convertView,
//				ViewGroup parent) {
//			ViewHolder holder = null;
//			if (convertView == null) {
//				holder = new ViewHolder();
//				convertView = mInflater.inflate(R.layout.sync_list, null);
//				holder.image = (ImageView) convertView
//						.findViewById(R.id.sync_list_image1);
//				holder.tittle = (TextView) convertView
//						.findViewById(R.id.sync_tittle);
//				holder.time = (TextView) convertView
//						.findViewById(R.id.sync_time);
//				holder.flag = (TextView) convertView
//						.findViewById(R.id.sync_flag);
//				holder.bt = (Button) convertView.findViewById(R.id.sync_bt);
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			holder.image.setBackgroundResource((Integer) mData.get(position)
//					.get("image"));
//			holder.tittle.setText((String) mData.get(position).get("tittle"));
//			holder.time.setText((String) mData.get(position).get("time"));
//			holder.flag.setText((String) mData.get(position).get("flag"));
//
//			holder.bt.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					switch (position) {
//					case 0:
//						
//						break;
//					case 1:
//						break;
//					case 2:
//						break;
//					}
//				}
//			});
//
//			return convertView;
//		}
//	}
// }
