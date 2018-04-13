package com.geok.langfang.tools;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.LineSyncBean;
import com.geok.langfang.pipeline.R;

import java.util.ArrayList;
import java.util.List;

public class TreeView extends ListActivity {
	private ArrayList<TreeElement> mPdfOutlinesCount;
	private TreeViewAdapter treeViewAdapter = null;
	List<LineSyncBean> list;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initialData();
		treeViewAdapter = new TreeViewAdapter(this, R.layout.outline, mPdfOutlinesCount);
		setListAdapter(treeViewAdapter);
		this.getListView().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.circle_cornor_line_black));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("TreeView.onDestroy()");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("TreeView.onPause()");

	}

	private void initialData() {

		list = Json.getPipelineSyncList(getSharedPreferences("sync", MODE_PRIVATE).getString(
				"line", "-1"));
		Log.i("woaini",getSharedPreferences("sync", MODE_PRIVATE).getString(
				"line", "-1"));

		mPdfOutlinesCount = new ArrayList<TreeElement>();
		for (int i = 0; i < list.size(); i++) {
			LineSyncBean bean = list.get(i);
			TreeElement elenElement = new TreeElement(bean.getLineloopeventid(),
					bean.getLineloopname());
			Log.i("woaini",bean.getLineloopeventid()+"-------"+bean.getLineloopname());
			if (bean.getChildrenList().length > 0) {
				SaxTree(bean.getChildrenList(), elenElement);
			}
			mPdfOutlinesCount.add(elenElement);
		}

		System.out.println(list);
		list = null;
	}

	private void SaxTree(LineSyncBean[] list, TreeElement parent) {

		for (int i = 0; i< list.length; i++) {
			LineSyncBean bean = list[i];
			TreeElement element = new TreeElement(bean.getLineloopeventid(), bean.getLineloopname());
			Log.i("woaini1","woaini1:"+bean.getLineloopeventid()+"-------"+bean.getLineloopname());

			if (bean.getChildrenList() != null) {
				this.SaxTree(bean.getChildrenList(), element);
			}
			parent.addChild(element);

		}

	}

	@SuppressWarnings("unchecked")
	private class TreeViewAdapter extends ArrayAdapter {

		public TreeViewAdapter(Context context, int textViewResourceId, List objects) {
			super(context, textViewResourceId, objects);
			mInflater = LayoutInflater.from(context);
			mfilelist = objects;
			mIconCollapse = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.outline_list_collapse);
			mIconExpand = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.outline_list_expand);

		}

		private LayoutInflater mInflater;
		private List<TreeElement> mfilelist;
		private Bitmap mIconCollapse;
		private Bitmap mIconExpand;

		@Override
		public int getCount() {
			return mfilelist.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final ViewHolder holder;
			/* if (convertView == null) { */
			convertView = mInflater.inflate(R.layout.outline, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
			/*
			 * } else { holder = (ViewHolder) convertView.getTag(); }
			 */

			final TreeElement obj = mfilelist.get(position);

			holder.text.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// System.out.println("holder.text:"+holder.text.getText());
					final View convertView = mInflater.inflate(R.layout.outline, null);
					TreeView.this.onListItemClick(TreeView.this.getListView(), convertView,
							position, position);

				}
			});
			

			int level = obj.getLevel();
			holder.icon.setPadding(25 * (level + 1), holder.icon.getPaddingTop(), 0,
					holder.icon.getPaddingBottom());
			holder.text.setText(obj.getOutlineTitle());
			holder.icon.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (mPdfOutlinesCount.get(position) != null) {
						//
						if (!mPdfOutlinesCount.get(position).isMhasChild()) {
							System.out.println(mPdfOutlinesCount.get(position).getOutlineTitle() + ":子信息");

							Intent intent = new Intent();
							intent.putExtra("line", mPdfOutlinesCount.get(position).getOutlineTitle());
							intent.putExtra("lineId", mPdfOutlinesCount.get(position).getId());
							setResult(Activity.RESULT_OK, intent);
							finish();
						}
					} else {
						System.out.println("mPdfOutlinesCount.get(position)为空");
					}
					// if (mPdfOutlinesCount.get(position).isMhasChild()) {
					// Toast.makeText(this, mPdfOutlinesCount.get(position)
					// .getOutlineTitle(), 2000);
					// }

					if (mPdfOutlinesCount.get(position).isExpanded()) {
						mPdfOutlinesCount.get(position).setExpanded(false);
						TreeElement element = mPdfOutlinesCount.get(position);
						ArrayList<TreeElement> temp = new ArrayList<TreeElement>();

						for (int i = position + 1; i < mPdfOutlinesCount.size(); i++) {
							if (element.getLevel() >= mPdfOutlinesCount.get(i).getLevel()) {
								break;
							}
							temp.add(mPdfOutlinesCount.get(i));
						}

						mPdfOutlinesCount.removeAll(temp);

						treeViewAdapter.notifyDataSetChanged();

					} else {
						TreeElement obj = mPdfOutlinesCount.get(position);
						obj.setExpanded(true);
						int level = obj.getLevel();
						int nextLevel = level + 1;

						for (TreeElement element : obj.getChildList()) {
							element.setLevel(nextLevel);
							element.setExpanded(false);
							mPdfOutlinesCount.add(position + 1, element);

						}
						treeViewAdapter.notifyDataSetChanged();

					}
					
				}
			});
			
			if (obj.isMhasChild() && (obj.isExpanded() == false)) {
				holder.icon.setImageBitmap(mIconCollapse);
			} else if (obj.isMhasChild() && (obj.isExpanded() == true)) {
				holder.icon.setImageBitmap(mIconExpand);
			} else if (!obj.isMhasChild()) {
				holder.icon.setImageBitmap(mIconCollapse);
				holder.icon.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}

		class ViewHolder {
			TextView text;
			ImageView icon;

		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// System.out.println("position:" + position);

		if (mPdfOutlinesCount.get(position) != null) {
			//
			if (!mPdfOutlinesCount.get(position).isMhasChild()) {
				System.out.println(mPdfOutlinesCount.get(position).getOutlineTitle() + ":子信息");

				Intent intent = new Intent();
				intent.putExtra("line", mPdfOutlinesCount.get(position).getOutlineTitle());
				intent.putExtra("lineId", mPdfOutlinesCount.get(position).getId());
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		} else {
			System.out.println("mPdfOutlinesCount.get(position)为空");
		}
		// if (mPdfOutlinesCount.get(position).isMhasChild()) {
		// Toast.makeText(this, mPdfOutlinesCount.get(position)
		// .getOutlineTitle(), 2000);
		// }

		if (mPdfOutlinesCount.get(position).isExpanded()) {
			mPdfOutlinesCount.get(position).setExpanded(false);
			TreeElement element = mPdfOutlinesCount.get(position);
			ArrayList<TreeElement> temp = new ArrayList<TreeElement>();

			for (int i = position + 1; i < mPdfOutlinesCount.size(); i++) {
				if (element.getLevel() >= mPdfOutlinesCount.get(i).getLevel()) {
					break;
				}
				temp.add(mPdfOutlinesCount.get(i));
			}

			mPdfOutlinesCount.removeAll(temp);

			treeViewAdapter.notifyDataSetChanged();

		} else {
			TreeElement obj = mPdfOutlinesCount.get(position);
			obj.setExpanded(true);
			int level = obj.getLevel();
			int nextLevel = level + 1;

			for (TreeElement element : obj.getChildList()) {
				element.setLevel(nextLevel);
				element.setExpanded(false);
				mPdfOutlinesCount.add(position + 1, element);

			}
			treeViewAdapter.notifyDataSetChanged();

		}
	}

}