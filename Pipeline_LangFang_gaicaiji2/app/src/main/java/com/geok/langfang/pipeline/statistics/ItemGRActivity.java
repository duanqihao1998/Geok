package com.geok.langfang.pipeline.statistics;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
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

import com.geok.langfang.json.Json;
import com.geok.langfang.jsonbean.GpsHui;
import com.geok.langfang.jsonbean.GpsLocation;
import com.geok.langfang.jsonbean.GpsUser;
import com.geok.langfang.pipeline.R;
import com.geok.langfang.pipeline.map.BaiduMap;
import com.geok.langfang.request.MyApplication;
import com.geok.langfang.request.Request;
import com.geok.langfang.shuju.GeRenBean;
import com.geok.langfang.shuju.SetMessage;
import com.geok.langfang.tools.CustomProgressDialog;
import com.geok.langfang.util.OkHttp3Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 段淇皓
 * 统计页面个人信息
 * Created by ydb on 2018/4/4.
 */

public class ItemGRActivity extends Activity implements View.OnClickListener {

    private static CustomProgressDialog progressDialog = null;
    String path = "http://" + MyApplication.ip + ":" + MyApplication.port
            + "/pmi/servlet/PdaServlet";
    private ListView listview_statistics;
    private Button back;
    private List<GeRenBean> geRenBeans=new ArrayList<>();
    private TextView locus_date;
    Calendar c = null;
    private String date;
    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String uptime = sDateFormat.format(new java.util.Date());
    private String unitID;
    private ListViewAdapter2 listViewAdapter;
//    public static List<GpsHui> gpsHuis=new ArrayList<>();//轨迹回放点集合
    public static List<GpsUser> list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemgr);
        //获取控件
        isitView();
        //获取用userID
        Intent intent=getIntent();
        unitID = intent.getStringExtra("unitID");
        String data = intent.getStringExtra("data");
        c = Calendar.getInstance();
        //选择日期
        if (uptime.length() == 19) {
//            uptime.substring(0, 10)
            locus_date.setText(data);
        }
        //请求数据
        getdata(unitID);

    }

    public void isitView() {
        locus_date = findViewById(R.id.locus_date);
        listview_statistics = findViewById(R.id.listview_statistics);
        back = findViewById(R.id.back);
        locus_date.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public void getdata(String unitID) {
        date = locus_date.getText().toString();
        Log.i("geren", date);
        Map<String,String> params=new HashMap<>();
        params.put("reqType", "11003");
        params.put("arg0",unitID);
        params.put("arg1",date);
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
		@SuppressLint("WrongConstant")
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
                            String allpointcount = jsonObject.getString("ALLPOINTCOUNT");
                            String dopointcount = jsonObject.getString("DOPOINTCOUNT");
                            String eventid = jsonObject.getString("EVENTID");
                            String inspectorid = jsonObject.getString("INSPECTORID");
                            String instasklength = jsonObject.getString("INSTASKLENGTH");
                            String name = jsonObject.getString("NAME");
                            String notpoint = jsonObject.getString("NOTPOINT");
                            String taskid = jsonObject.getString("TASKID");
                            String unitid = jsonObject.getString("UNITID");
                            String unitname = jsonObject.getString("UNITNAME");

                            GeRenBean geRenBean=new GeRenBean(allpointcount,dopointcount,eventid,inspectorid,instasklength,name,notpoint,taskid,unitid,unitname);
                            geRenBeans.add(geRenBean);
                        }
                        listViewAdapter = new ListViewAdapter2(geRenBeans,ItemGRActivity.this,handler,date);
                        listview_statistics.setAdapter(listViewAdapter);
                        listview_statistics.setDividerHeight(0);
                        listview_statistics.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent=new Intent(ItemGRActivity.this,ItemGJDActivity.class);
                                intent.putExtra("INSPECTORID",geRenBeans.get(i).getINSPECTORID());
                                intent.putExtra("data",date);
                                startActivity(intent);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
					break;
                case 55:
                    if (msg.getData().getInt("flag") == 1) {
                        stopProgressDialog();
                        String data = msg.getData().getString("result");
                        Log.i("geren",data);
                        if (data.trim().equals("-1")) {
                            Toast.makeText(ItemGRActivity.this, "暂时没有这个人员的巡检轨迹", 2000).show();
                        } else {
//                            //换成本地数据
//                            List<GpsUser> list = Json.getLocusUser(data);
//                            for (int i = 0; i < list.size(); i++) {
//                                List<GpsLocation> gpsList = list.get(i).getGpsList();
//                                for (int j = 0; j < gpsList.size(); j++) {
//                                    String lon = gpsList.get(j).getLON();
//                                    String lat = gpsList.get(j).getLAT();
//                                    Log.i("hahaha",lon+"-----"+lat);
//                                    GpsHui gpsHui=new GpsHui(lon,lat);
//                                    gpsHuis.add(gpsHui);
//                                    Log.i("geren",gpsHuis.size()+"-----"+gpsList.size());
////                                    if (gpsHuis.size() == gpsList.size()) {
////                                        Intent intent=new Intent(ItemGRActivity.this,BaiduMap.class);
////                                        startActivity(intent);
////                                    }
////                                    else {
////                                        Toast.makeText(ItemGRActivity.this, "暂时没有这个人员的巡检轨迹", 2000).show();
////                                    }
//                                }
//                            }
                            //换成本地数据
                            List<GpsUser> list = Json.getLocusUser1(data);
//                            list.get(0).getGpsList().size() > 0
//                            Json.gpsHuis.size()
                            if (list.get(0).getGpsList().size() > 0) {
                                Intent intent=new Intent(ItemGRActivity.this,BaiduMap.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ItemGRActivity.this, "暂时没有这个人员的巡检轨迹", 2000).show();
                            }
                        }
                    } else {
                        stopProgressDialog();
                        Toast.makeText(ItemGRActivity.this, "请检查网络", 2000).show();
                    }
                    break;
			}
		}
	};

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.locus_date:
                new DatePickerDialog(ItemGRActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        getdata(unitID);
                        listViewAdapter.notifyDataSetChanged();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    public static void startProgressDialog(Context context) {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(context).setMessage("请求数据中...");
        }
        progressDialog.show();
    }
    private void stopProgressDialog() {
        if (progressDialog != null) {

            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}