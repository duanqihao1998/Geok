package com.geok.langfang.pipeline.Historical_statistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.pipeline.R;

import java.util.List;

/**
 * Created by ydb on 2018/4/11.
 */

public class His_statistics_details extends Activity {

    public static List<HisBean> hisBeanList = His_statistics.hisBeanList;
    private int id;
    private TextView details_title;
    private TextView pipe_length;
    private TextView plan_length;
    private TextView play_coverage;
    private TextView task_length;
    private TextView task_coverage;
    private TextView shi_length;
    private TextView shi_coverage;
    private Button back;
    private TextView number_of_inspection;
    private TextView key_points;
    private TextView ykey_points;
    private TextView skey_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.his_statistics_details);
        getData();
        isitView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        details_title.setText(hisBeanList.get(id).getDEPARTMENTNAME());
        pipe_length.setText(hisBeanList.get(id).getPLANLENGTH()+"公里");
        plan_length.setText(hisBeanList.get(id).getSLENGTH()+"公里");
        play_coverage.setText(hisBeanList.get(id).getPLANCOVERAGE());
        task_length.setText(hisBeanList.get(id).getRLENGTH()+"公里");
        task_coverage.setText(hisBeanList.get(id).getTASKCOVERAGE());
        shi_length.setText(hisBeanList.get(id).getRLENGTH()+"公里");
        shi_coverage.setText(hisBeanList.get(id).getQUALIFIED());
        number_of_inspection.setText(hisBeanList.get(id).getPERCOUNT()+"个");
        key_points.setText(hisBeanList.get(id).getCKEYPOINT()+"个");
        ykey_points.setText(hisBeanList.get(id).getSKEYPOINT()+"个");
        skey_points.setText(hisBeanList.get(id).getRKEYPOINT()+"个");
    }
    //获取控件
    public void isitView() {
        back = findViewById(R.id.back);//返回
        details_title = findViewById(R.id.details_title);//标题
        pipe_length = findViewById(R.id.pipe_length);//应巡管道长度
        plan_length = findViewById(R.id.plan_length);//计划巡检长度
        play_coverage = findViewById(R.id.play_coverage);//计划巡检覆盖率
        task_length = findViewById(R.id.task_length);//巡检任务长度
        task_coverage = findViewById(R.id.task_coverage);//巡检任务覆盖率
        shi_length = findViewById(R.id.shi_length);//实训管道长度
        shi_coverage = findViewById(R.id.shi_coverage);//巡检完成率
        number_of_inspection = findViewById(R.id.number_of_inspection);//巡检人数
        key_points = findViewById(R.id.key_points);//采集关键点
        ykey_points = findViewById(R.id.Ykey_points);//应巡关键点
        skey_points = findViewById(R.id.skey_points);//实训关键点数
    }

    //获取数据
    public void getData() {
        Intent intent = getIntent();
        String position = intent.getStringExtra("position");
        //String转int
        id = Integer.parseInt(position);
    }
}
