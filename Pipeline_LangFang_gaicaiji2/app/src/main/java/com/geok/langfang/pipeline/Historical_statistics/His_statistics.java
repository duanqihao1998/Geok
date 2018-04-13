package com.geok.langfang.pipeline.Historical_statistics;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.statistics.ItemGRActivity;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.shuju.SetMessage;
import com.geok.langfang.util.OkHttp3Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by ydb on 2018/4/10.
 */

public class His_statistics extends Activity implements View.OnClickListener {
    String path = "http://" + MyApplication.ip + ":" + MyApplication.port
            + "/pmi/servlet/PdaServlet";
    private Button back;
    private ListView completion_listview;
    public static List<HisBean> hisBeanList=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case 1:
                    String unitsync = msg.getData().getString("result");
                    Log.i("unitsync",unitsync);
                    if(unitsync.equals("-1")){
                        Toast.makeText(His_statistics.this, "对不起，没有权限访问数据！", Toast.LENGTH_SHORT).show();
                    }else {
                        try {
                            JSONArray jsonArray=new JSONArray(unitsync);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                String ckeypoint = jsonObject.getString("CKEYPOINT");
                                String departmentid = jsonObject.getString("DEPARTMENTID");
                                String departmentname = jsonObject.getString("DEPARTMENTNAME");
                                String instasklength = jsonObject.getString("INSTASKLENGTH");
                                String linelogpercount = jsonObject.getString("LINELOGPERCOUNT");
                                String nlinelogpercount = jsonObject.getString("NLINELOGPERCOUNT");
                                String percount = jsonObject.getString("PERCOUNT");
                                String plancoverage = jsonObject.getString("PLANCOVERAGE");
                                String planlength = jsonObject.getString("PLANLENGTH");
                                String qualified = jsonObject.getString("QUALIFIED");
                                String quantity = jsonObject.getString("QUANTITY");
                                String rkeypoint = jsonObject.getString("RKEYPOINT");
                                String rkeypointcount = jsonObject.getString("RKEYPOINTCOUNT");
                                String rlength = jsonObject.getString("RLENGTH");
                                String skeypoint = jsonObject.getString("SKEYPOINT");
                                String skeypointcount = jsonObject.getString("SKEYPOINTCOUNT");
                                String slength = jsonObject.getString("SLENGTH");
                                String taskcoverage = jsonObject.getString("TASKCOVERAGE");
                                String unitlive = jsonObject.getString("UNITLIVE");

                                HisBean hisBean=new HisBean(ckeypoint,departmentid,departmentname,instasklength,linelogpercount,nlinelogpercount,percount,plancoverage,planlength,qualified,quantity,rkeypoint,rkeypointcount,rlength,skeypoint,skeypointcount,slength,taskcoverage,unitlive);
                                hisBeanList.add(hisBean);
                                his_statistics_adapter = new His_statistics_Adapter(hisBeanList,His_statistics.this);
                                completion_listview.setAdapter(his_statistics_adapter);
                                completion_listview.setDividerHeight(0);
                                completion_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String unitlive1 = hisBeanList.get(i).getUNITLIVE();
                                        if(unitlive1.equals("2")){
                                            getdata();
                                            his_statistics_adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };
    private His_statistics_Adapter his_statistics_adapter;
    private TextView locus_date;
    Calendar c = null;
    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String uptime = sDateFormat.format(new java.util.Date());
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.his_statistics);
        //获取控件
        isitview();
        c = Calendar.getInstance();
        //选择日期
        if (uptime.length() == 19) {
            Date DangData = new Date();
//            String DangData = uptime.substring(0, 10);
            c.setTime(DangData);
            c.add(Calendar.DAY_OF_MONTH, -1);
            Date time = c.getTime();
            String format = sDateFormat.format(time);
            locus_date.setText(format.substring(0, 10));
//            locus_date.setText(uptime.substring(0, 10));
        }
        //获取数据
        getdata();

    }
    private void getdata() {
        hisBeanList.clear();
        date = locus_date.getText().toString();
        Map<String,String> params=new HashMap<>();
        params.put("reqType", "11005");
        params.put("arg0","");
//        024cfcc0-a072-11df-b1f4-842b2b048340  未知
//        3a998501-7b4c-11e7-9dd3-e41f13e34d4c  金德荣
        params.put("arg1",MyApplication.depterid);
        params.put("arg2",date);
        params.put("arg3","");
        OkHttp3Utils.doPost(path, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                SetMessage.setMessage(1,string,1,handler);
            }
        });
    }

    public void isitview() {
        locus_date = findViewById(R.id.locus_date);
        completion_listview = findViewById(R.id.Inspection_completion_listview);
        back = findViewById(R.id.back);
        back.setOnClickListener(this);
        locus_date.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.locus_date:
                new DatePickerDialog(His_statistics.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int n = monthOfYear + 1;
                        String month = "";
                        if (n > 9) {
                            month = n + "";
                        } else {
                            month = "0" + n;
                        }
                        String day = "";
                        if (dayOfMonth > 9) {
                            day = dayOfMonth + "";
                        } else {
                            day = "0" + dayOfMonth;
                        }
                        locus_date.setText(year + "-" + month + "-" + day);
                        getdata();
                        his_statistics_adapter.notifyDataSetChanged();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }
}
