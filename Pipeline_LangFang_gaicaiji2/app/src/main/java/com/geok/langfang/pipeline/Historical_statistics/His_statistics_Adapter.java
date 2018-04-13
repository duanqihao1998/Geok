package com.geok.langfang.pipeline.Historical_statistics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.statistics.ListViewAdapter1;
import com.geok.langfang.shuju.BumenBean;

import java.util.List;

/**
 * Created by ydb on 2018/4/10.
 */

public class His_statistics_Adapter extends BaseAdapter {
    private List<HisBean> list = null;
    private Context context;

    public His_statistics_Adapter(List<HisBean> list, Context context) {
        this.list = list;
        this.context = context;
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

            convertView = convertView.inflate(context, R.layout.his_item, null);
            viewHolder.texttile=convertView.findViewById(R.id.texttile);
            viewHolder.Pipe_length=convertView.findViewById(R.id.Pipe_length);
            viewHolder.Inspection_coverage=convertView.findViewById(R.id.Inspection_coverage);
            viewHolder.Task_coverage=convertView.findViewById(R.id.Task_coverage);
            viewHolder.Inspection_is_completed=convertView.findViewById(R.id.Inspection_is_completed);
            viewHolder.look_details=convertView.findViewById(R.id.look_details);

            convertView.setTag(viewHolder);// 通过setTag将ViewHolder和convertView绑定
        }  else {
            viewHolder = (ViewHolder) convertView.getTag(); // 获取，通过ViewHolder找到相应的控件
        }
        viewHolder.texttile.setText(list.get(position).getDEPARTMENTNAME());
        viewHolder.Pipe_length.setText(list.get(position).getPLANLENGTH());
        viewHolder.Inspection_coverage.setText(list.get(position).getPLANCOVERAGE());
        viewHolder.Task_coverage.setText(list.get(position).getTASKCOVERAGE());
        viewHolder.Inspection_is_completed.setText(list.get(position).getQUALIFIED());
        viewHolder.look_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,His_statistics_details.class);
                intent.putExtra("position",position+"");
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView texttile;//标题
        TextView Pipe_length;//管道长度
        TextView Inspection_coverage;//巡检计划覆盖率
        TextView Task_coverage;//巡检任务覆盖率
        TextView Inspection_is_completed;//巡检完成率
        TextView look_details;//查看详情
    }
}
