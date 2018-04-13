package com.geok.langfang.pipeline.statistics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.geok.langfang.pipeline.Login;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.shuju.GeRenBean;
import com.geok.langfang.shuju.GjdBean;
import com.geok.langfang.shuju.SetMessage;
import com.geok.langfang.util.OkHttp3Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 段淇皓
 * 统计页面关键点
 * Created by ydb on 2018/4/4.
 */

public class ItemGJDActivity extends Activity {

    private Button back;
    String path = "http://" + MyApplication.ip + ":" + MyApplication.port
            + "/pmi/servlet/PdaServlet";
    private List<GjdBean> gjdList=new ArrayList<>();
    private ListView listview_statistics;
    private ListViewAdapter3 listViewAdapter;
    private String inspectorid;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemgjd);
        isitView();//获取控件
        Intent intent = getIntent();
        inspectorid = intent.getStringExtra("INSPECTORID");
        data = intent.getStringExtra("data");
        //获取数据
        getdata(inspectorid, data);
    }

    public void isitView() {
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listview_statistics = findViewById(R.id.listview_statistics);
    }

    public void getdata(String INSPECTORID,String data) {
        Map<String,String> params=new HashMap<>();
        params.put("reqType", "11004");
        params.put("arg0",INSPECTORID);
        params.put("arg1",data);
        params.put("arg2","");

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
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1){
                case 1:
                    String unitsync = msg.getData().getString("result");
                    Log.i("unitsync",unitsync);
                    try {
                        JSONArray jsonArray=new JSONArray(unitsync);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String chararrivaltime = jsonObject.getString("CHARARRIVALTIME");
                            String departmentid = jsonObject.getString("DEPARTMENTID");
                            String eventid = jsonObject.getString("EVENTID");
                            String freqindex = jsonObject.getString("FREQINDEX");
                            String lat = jsonObject.getString("LAT");
                            String lineloopeventid = jsonObject.getString("LINELOOPEVENTID");
                            String linename = jsonObject.getString("LINENAME");
                            String lon = jsonObject.getString("LON");
                            String pointid = jsonObject.getString("POINTID");
                            String pointname = jsonObject.getString("POINTNAME");
                            String pointposition = jsonObject.getString("POINTPOSITION");
                            String pointstatus = jsonObject.getString("POINTSTATUS");
                            String station = jsonObject.getString("STATION");
                            String unitname = jsonObject.getString("UNITNAME");
                            String inspector = jsonObject.getString("INSPECTOR");

                            GjdBean gjdBean=new GjdBean(chararrivaltime,departmentid,eventid,freqindex,lat,lineloopeventid,linename,lon,pointid,pointname,pointposition,pointstatus,station,unitname,inspector);
                            gjdList.add(gjdBean);
                        }
                        listViewAdapter = new ListViewAdapter3(gjdList,ItemGJDActivity.this,path,handler);
                        listview_statistics.setAdapter(listViewAdapter);
                        listview_statistics.setDividerHeight(0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    String result = msg.getData().getString("result");
                    Log.i("unitsync",result);
                    if (result.contains("|")) {
                        String[] temp = result.split("\\|");

                        if (temp[0].contains("err") || temp[0].contains("ERR")) {
                            Toast.makeText(ItemGJDActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        } else if(temp[0].contains("ok") || temp[0].contains("OK")){
                            gjdList.clear();
                            getdata(inspectorid,data);
                            listViewAdapter.notifyDataSetChanged();
                            Toast.makeText(ItemGJDActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    };
}
