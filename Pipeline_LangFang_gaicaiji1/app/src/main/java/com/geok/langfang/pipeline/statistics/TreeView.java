package com.geok.langfang.pipeline.statistics;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.pipeline.R;

public class TreeView extends ListView {
	public Context context;
	TreeViewAdapter treeViewAdapter;
	public ArrayList<TreeElement> mPdfOutlinesCount = new ArrayList<TreeElement>();

	public static List<String> userCheckList = new ArrayList<String>();// 选择的人员id集合
	public static List<String> userNameCheckList = new ArrayList<String>();// 选择的人员名字集合
	public int selectedChildNum = 0;

	String type = "";
	
	public TreeView(Context context) {
		super(context);
		this.context = context;
		userCheckList.clear();
		userNameCheckList.clear();
	}

	public TreeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		userCheckList.clear();
		userNameCheckList.clear();
	}

	// public void traversalNode(Element node,TreeElement treeNode) {
	// ArrayList<Element> elements = new ArrayList<Element>();
	// ArrayList<TreeElement> treeElements = new ArrayList<TreeElement>();
	// for (Iterator it = node.elementIterator(); it.hasNext();) {
	// Element element = (Element) it.next();
	// TreeElement treeElement = new TreeElement(
	// element.attributeValue(treeIDName),
	// element.attributeValue(treeContentName));
	// if (treeNode==null){
	// mPdfOutlinesCount.add(treeElement);
	// }
	// elements.add(element);
	// treeElements.add(treeElement);
	// if(treeNode!=null){
	// treeNode.addChild(treeElement);
	// }
	// }
	// if (elements.size() != 0)
	// for (int j = 0; j < elements.size(); j++) {
	// traversalNode(elements.get(j),treeElements.get(j));
	// }
	// }

	public void initialData() {

		// try {
		// initialData();
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		treeViewAdapter = new TreeViewAdapter(this.context, R.layout.outline_user,
				mPdfOutlinesCount);
		this.setAdapter(treeViewAdapter);
		type = "";
		// SAXReader reader = new SAXReader();
		// InputStream inputStream = LoadData.class.getClassLoader()
		// .getResourceAsStream("adtree.xml");
		// Document document = reader.read(inputStream);
		// Element rootElement = document.getRootElement();
		// traversalNode(rootElement,null);
	}
	
	public void initialData(String type) {

		treeViewAdapter = new TreeViewAdapter(this.context, R.layout.outline_user,
				mPdfOutlinesCount);
		this.setAdapter(treeViewAdapter);
		this.type = type;
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
//			if(convertView == null){
				convertView = mInflater.inflate(R.layout.outline_user, null);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.text);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.choose = (CheckBox) convertView.findViewById(R.id.choose);
				convertView.setTag(holder);
//			}else{
//				 holder = (ViewHolder) convertView.getTag();
//			}
			
			
			final TreeElement obj = mfilelist.get(position);
			holder.choose.setChecked(obj.isChoose());
			
			if(type.equals("locus")){
				holder.choose.setVisibility(View.GONE);
				holder.text.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(!(obj.hasChild) && obj.getNotetype().equals("0")){
							userCheckList.add(obj.getId());
							userNameCheckList.add(obj.getOutlineTitle());
							context.sendBroadcast(new Intent("dialogdismiss"));
						}
						
					}
				});
			}

			
			holder.icon.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!mPdfOutlinesCount.get(position).hasChild) {
						// Toast.makeText(context,
						// mPdfOutlinesCount.get(position)
						// .getOutlineTitle(), 2000);
						/*
						 * int pageNumber; Intent i = getIntent(); element
						 * element = mPdfOutlinesCount .get(position);
						 * pageNumber = element.getOutlineElement().pageNumber;
						 * if (pageNumber <= 0) { String name =
						 * element.getOutlineElement().destName; pageNumber =
						 * idocviewer.getPageNumberForNameForOutline(name);
						 * element.getOutlineElement().pageNumber = pageNumber;
						 * element.getOutlineElement().destName = null; }
						 * i.putExtra("PageNumber", pageNumber);
						 * setResult(RESULT_OK, i); finish();
						 */

						return;
					}

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
						/*
						 * fileExploreAdapter = new TreeViewAdapter(this,
						 * R.layout.outline, mPdfOutlinesCount);
						 */

						// setListAdapter(fileExploreAdapter);

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
//			holder.text.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					System.out.println(obj.getId());
//				}
//			});

			holder.choose.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// holder.choose.setChecked(isChecked);
					
					obj.setChoose(isChecked);
					if(type.equals("locus")&&obj.hasChild){
						obj.setChoose(false);
						holder.choose.setChecked(false);
					}
					
					System.out.println(obj.getOutlineTitle());
					if (isChecked == true) {
						
						if (!(obj.hasChild) && obj.getNotetype().equals("0")) {
							if(type.equals("locus")){
								if(userCheckList.size()>0){
									holder.choose.setChecked(false);
									obj.setChoose(false);
									Toast.makeText(context, "单选，请删除已经选择的人员再选", 2000).show();
								}else{
									obj.setChoose(isChecked);
									userCheckList.add(obj.getId());
									userNameCheckList.add(obj.getOutlineTitle());
									selectedChildNum++;
								}
							}else{
								userCheckList.add(obj.getId());
								userNameCheckList.add(obj.getOutlineTitle());
								selectedChildNum++;
							}
							
						}

					} else if (isChecked == false) {
						
						if (!(obj.hasChild)) {
							userCheckList.remove(obj.getId());
							userNameCheckList.remove(obj.getOutlineTitle());
							selectedChildNum--;
						}

					}
					if(!(type.equals("locus"))){
						if (obj.hasChild) {
							obj.onClickIsChoose(isChecked);
							if (isChecked == true)
								selectedChildNum += obj.getTotalChildList().size();
							else if (isChecked == false)
								selectedChildNum -= obj.getTotalChildList().size();
						}
					}
					
					treeViewAdapter.notifyDataSetChanged();
				}

			});

			int level = obj.getLevel();
			holder.icon.setPadding(25 * (level + 1), holder.icon.getPaddingTop(), 0,
					holder.icon.getPaddingBottom());
			holder.text.setText(obj.getOutlineTitle());
			if (obj.hasChild && (obj.isExpanded() == false)) {
				holder.icon.setImageBitmap(mIconCollapse);
			} else if (obj.hasChild && (obj.isExpanded() == true)) {
				holder.icon.setImageBitmap(mIconExpand);
			} else if (!obj.hasChild) {
				holder.icon.setImageBitmap(mIconCollapse);
				holder.icon.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}
	}

	/**
	 * 
	 * @功能描述 将选择的用户id转换为String
	 * @author 张龙飞Email:longfeiz@geo-k.cn Tel:13671277587
	 * @return
	 * @createDate 2013-11-26 下午2:41:16
	 * @update:[日期YYYY-MM-DD][更改人姓名][变更描述]
	 */
	public static String getUserIdList() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < userCheckList.size(); i++) {
			sb.append(userCheckList.get(i) + ";");
		}
		return sb.toString();
	}

	public static String getUserNameList() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < userNameCheckList.size(); i++) {
			sb.append(userNameCheckList.get(i) + ";");
		}
		return sb.toString();
	}
}
