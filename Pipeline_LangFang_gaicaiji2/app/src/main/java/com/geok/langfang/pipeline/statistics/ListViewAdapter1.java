package com.geok.langfang.pipeline.statistics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.shuju.BumenBean;

import java.util.List;

/**
 * 段淇皓
 * 数据统计适配器（部门数据）
 * Created by ydb on 2018/3/26.
 */

public class ListViewAdapter1 extends BaseAdapter {

    private List<BumenBean> list = null;
    private Context context;

    public ListViewAdapter1(List<BumenBean> list, Context context) {
        this.list = list;
        this.context=context;
    }

    // 适配器中数据集中数据的个数
    @Override
    public int getCount() {
        return list.size();
    }

    // 获取数据集中与指定索引对应的数据项
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    // 获取指定行对应的ID
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 获取每一个Item显示的内容
    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = convertView.inflate(context,R.layout.item_tj_1, null);
            viewHolder.texttile = convertView.findViewById(R.id.texttile);
            viewHolder.textsum = convertView.findViewById(R.id.textsum);
            viewHolder.textyouren = convertView.findViewById(R.id.textyouren);
            viewHolder.textwuren = convertView.findViewById(R.id.textwuren);
            viewHolder.textyingxun = convertView.findViewById(R.id.textyingxun);
            viewHolder.textshixun = convertView.findViewById(R.id.textshixun);

            convertView.setTag(viewHolder);// 通过setTag将ViewHolder和convertView绑定
        }  else {
            viewHolder = (ViewHolder) convertView.getTag(); // 获取，通过ViewHolder找到相应的控件
        }
        viewHolder.texttile.setText(list.get(position).getUNITNAME());
        viewHolder.textsum.setText(list.get(position).getUSERCOUNT()+"人");
        viewHolder.textyouren.setText(list.get(position).getTASKUSERCOUNT()+"人");
        viewHolder.textwuren.setText(list.get(position).getNOTASKUSERCOUNT()+"人");
        viewHolder.textyingxun.setText(list.get(position).getALLPOINTCOUNT()+"个");
        viewHolder.textshixun.setText(list.get(position).getDOPOINTCOUNT()+"个");
        return convertView;
    }

    class ViewHolder {
        TextView texttile;
        TextView textsum;
        TextView textyouren;
        TextView textwuren;
        TextView textyingxun;
        TextView textshixun;
    }
}
