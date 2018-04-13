package com.geok.langfang.pipeline.statistics;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.map.BaiduMap;
import com.geok.langfang.shuju.GjdBean;
import com.geok.langfang.shuju.SetMessage;
import com.geok.langfang.util.OkHttp3Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import android.os.Handler;

/**
 * 段淇皓
 * 数据统计适配器（关键点）
 * Created by ydb on 2018/3/26.
 */

public class ListViewAdapter3 extends BaseAdapter {

    public static ViewHolder viewHolder;
    private List<GjdBean> list = null;
    private Context context;
    private String path;
    private Handler handler;

    public ListViewAdapter3(List<GjdBean> list, Context context, String path, Handler handler) {
        this.list = list;
        this.context = context;
        this.path = path;
        this.handler = handler;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = convertView.inflate(context,R.layout.item_tj_3, null);
            viewHolder.GJDname = convertView.findViewById(R.id.GJDname);
            viewHolder.textweizhi = convertView.findViewById(R.id.textweizhi);
            viewHolder.textbumen = convertView.findViewById(R.id.textbumen);
            viewHolder.textXJname = convertView.findViewById(R.id.textXJuser);
            viewHolder.guiji = convertView.findViewById(R.id.guiji);
            viewHolder.GJDPIC = convertView.findViewById(R.id.imageView6);

            convertView.setTag(viewHolder);// 通过setTag将ViewHolder和convertView绑定
        }  else {
            viewHolder = (ViewHolder) convertView.getTag(); // 获取，通过ViewHolder找到相应的控件
        }
        viewHolder.GJDname.setText(list.get(position).getPOINTNAME());
        viewHolder.textweizhi.setText(list.get(position).getPOINTPOSITION());
        viewHolder.textbumen.setText(list.get(position).getUNITNAME());
        viewHolder.textXJname.setText(list.get(position).getINSPECTOR());
        if(list.get(position).getPOINTSTATUS().equals("1")){
            viewHolder.GJDname.setTextColor(Color.GREEN);
            viewHolder.GJDPIC.setImageDrawable(context.getResources().getDrawable(R.drawable.shi));
        }
        viewHolder.guiji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("温馨提示");
                builder.setMessage("是否修改关键点状态？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String,String> params=new HashMap<>();
                        params.put("reqType", "11192");
                        params.put("arg0",list.get(position).getEVENTID());

                        OkHttp3Utils.doPost(path, params, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String string = response.body().string();
                                SetMessage.setMessage(2,string,1,handler);
                            }
                        });
                    }
                }).setNegativeButton("取消", null).show();
            }
        });

        return convertView;
    }
    public static class ViewHolder {
        TextView GJDname;
        TextView textweizhi;
        TextView textbumen;
        TextView textXJname;
        TextView guiji;
        ImageView GJDPIC;
    }
}
