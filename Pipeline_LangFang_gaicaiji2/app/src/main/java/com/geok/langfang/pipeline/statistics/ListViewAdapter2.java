package com.geok.langfang.pipeline.statistics;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.Request;
import com.geok.langfang.shuju.GeRenBean;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 段淇皓
 * 数据统计适配器（个人数据）
 * Created by ydb on 2018/3/26.
 */

public class ListViewAdapter2 extends BaseAdapter {

    private List<GeRenBean> list = null;
    private Context context;
    private Handler handler;
    private String date;

    public ListViewAdapter2(List<GeRenBean> list, Context context, Handler handler, String date) {
        this.list = list;
        this.context = context;
        this.handler = handler;
        this.date = date;
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
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = convertView.inflate(context,R.layout.item_tj_2, null);
            viewHolder.texttile2 = convertView.findViewById(R.id.texttile2);
            viewHolder.textlicheng = convertView.findViewById(R.id.textlicheng);
            viewHolder.textrenwu_num = convertView.findViewById(R.id.textrenwu_num);
            viewHolder.textshixun2 = convertView.findViewById(R.id.textshixun2);
            viewHolder.textweixun = convertView.findViewById(R.id.textweixun);
            viewHolder.texthege = convertView.findViewById(R.id.texthege);
            viewHolder.guiji = convertView.findViewById(R.id.guiji);
            convertView.setTag(viewHolder);// 通过setTag将ViewHolder和convertView绑定
        }  else {
            viewHolder = (ViewHolder) convertView.getTag(); // 获取，通过ViewHolder找到相应的控件
        }
        viewHolder.texttile2.setText(list.get(position).getNAME()+"("+list.get(position).getUNITNAME()+")");
        viewHolder.textlicheng.setText(list.get(position).getINSTASKLENGTH()+"公里");
        viewHolder.textrenwu_num.setText(list.get(position).getALLPOINTCOUNT()+"个");
        viewHolder.textshixun2.setText(list.get(position).getDOPOINTCOUNT()+"个");
        viewHolder.textweixun.setText(list.get(position).getNOTPOINT()+"个");

        String allpointcount = list.get(position).getALLPOINTCOUNT();
        String dopointcount = list.get(position).getDOPOINTCOUNT();
        double sum = Double.parseDouble(allpointcount);
        double ok = Double.parseDouble(dopointcount);
        double i = (ok / sum)*100;
        Log.i("unitsync",sum+"---"+ok+"---"+i+"");
        DecimalFormat df = new DecimalFormat("######0.00");
        String format = df.format(i);
        viewHolder.texthege.setText(format+"%");
        
        viewHolder.guiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemGRActivity.startProgressDialog(context);
                Request request=new Request(handler);
                request.getLocusByUser(list.get(position).getNAME(), date);
                Toast.makeText(context, "点击了轨迹按钮"+position, Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView texttile2;
        TextView textlicheng;
        TextView textrenwu_num;
        TextView textshixun2;
        TextView textweixun;
        TextView texthege;
        TextView guiji;
    }
}
