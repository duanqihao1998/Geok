package com.geok.langfang.tools;

import com.geok.langfang.pipeline.R;
import com.geok.langfang.tools.TypeQuest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author Administrator 弹出对话框编辑工具类
 * 
 */
public class DialogEditActivity extends Activity {
	TextView text, text_promprt;
	EditText edittext;
	Button button;
	private int key;
	String str0, str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialogactivity_edit);
		/*
		 * init
		 */
		text = (TextView) findViewById(R.id.dialogactivity_tittle);
		edittext = (EditText) findViewById(R.id.dialogactivity_edit);
		button = (Button) findViewById(R.id.dialogactivity_button);
		text_promprt = (TextView) findViewById(R.id.dialogactivity_promprt);
		str0 = getIntent().getStringExtra("text");
		str = getIntent().getStringExtra("text");
		key = getIntent().getIntExtra("flag", 4);
		if ("未填写".equals(str0)) {
			str0 = "";
			str = "";
		} else {

		}
		switch (key) {

		case TypeQuest.PROBLEM_COMMENT:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			if (str == null) {
				edittext.setText("");
			} else if ((getIntent().getStringExtra("Tittle") + "：未填写").equals(str)) {
				edittext.setText("");
			} else {
				edittext.setText(str.substring(5, str.length()));
			}
			break;
		case TypeQuest.PROBLEM_DISTANCE:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;

		case TypeQuest.PROBLEM_LOCATION:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			if (str == null) {
				edittext.setText("");
			} else if ((getIntent().getStringExtra("Tittle") + "：未填写").equals(str)) {
				edittext.setText("");
			} else {
				edittext.setText(str.substring(7, str.length()));
			}
			break;
		case TypeQuest.PROBLEM_PLAN:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			if (str == null) {
				edittext.setText("");
			} else if ((getIntent().getStringExtra("Tittle") + "：未填写").equals(str)) {
				edittext.setText("");
			} else {
				edittext.setText(str.substring(7, str.length()));
			}
			break;
		case TypeQuest.PROBLEM_RESULT:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			if (str == null) {
				edittext.setText("");
			} else if ((getIntent().getStringExtra("Tittle") + "：未填写").equals(str)) {
				edittext.setText("");
			} else {
				edittext.setText(str.substring(7, str.length()));
			}
			break;
		case TypeQuest.PROTECT_TESTER:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			break;
		case TypeQuest.PIPELINE_RECORD_WERTHER:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.PIPELINE_RECORD_ROAD:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.PIPELINE_RECORD_RECORD:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.PIPELINE_RECORD_PROBLEM:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.PIPELINE_RECORD_RESULT:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.PIPELINE_RECORD_SENDER:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.PIPELINE_RECORD_RECEIVER:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.PIPELINE_RECORD_TIME:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(getIntent().getStringExtra("text"));
			break;
		case TypeQuest.PIPELINE_RECORD_STATION:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.PIPELINE_RECORD_CAR:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.PIPELINE_RECORD_OTHER:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.SETTING_PORT:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
			edittext.setText(str0);
			break;
		case TypeQuest.SETTING_M:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			edittext.setText(str0);
			break;
		case TypeQuest.SETTING_SERVER_NAME:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			break;

		case TypeQuest.PROTECT_VALUE:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.PROTECT_TESTTIME:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			break;
		case TypeQuest.PROTECT_TEMPERATURE:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.PROTECT_GROUND:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.PROTECT_VOLTAGE:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.PROTECT_REMARKS:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			if (str == null) {
				edittext.setText("");
			} else if ((getIntent().getStringExtra("Tittle") + "：未填写").equals(str)) {
				edittext.setText("");
			} else {
				edittext.setText(str.substring(3, str.length()));
			}
			break;
		case TypeQuest.PROTECT_NATURAL:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.PROTECT_IR:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.NATURAL_TEMPERATURE:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.NATURAL_WEATHER:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.NATURAL_VALUE:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.NATURAL_TESTTIME:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			break;
		case TypeQuest.NATURAL_TESTTIME2:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			break;
		case TypeQuest.NATURAL_VOLTAGE:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.NATURAL_REMARKS:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			if ((str == null) || (getIntent().getStringExtra("Tittle") + "：未填写").equals(str)) {
				edittext.setText("");
			} else {
				edittext.setText(str.substring(3, str.length()));
			}
			break;

		case TypeQuest.GROUND_SETVALUE:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.GROUND_TESTVALUE:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.GROUND_CONCLUSION:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.GROUND_TESTDATE:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			break;
//		case TypeQuest.GROUND_WEATHER:
//			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
//			edittext.setText(str0);
//			break;
//		case TypeQuest.GROUND_TEMPERATURE:
//			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
//			edittext.setText(str0);
//			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
//			break;

		case TypeQuest.CORROSIVE_DAMAGE_DES:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			if ((str == null) || (getIntent().getStringExtra("Tittle") + "：未填写").equals(str)) {
				edittext.setText("");
			} else {
				edittext.setText(str.substring(7, str.length()));
			}
			break;
		case TypeQuest.CORROSIVE_OFFSET:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.CORROSIVE_SOIL:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			System.out.println(str);
			if ((str == null) || (getIntent().getStringExtra("Tittle") + "：未填写").equals(str)) {
				edittext.setText("");
			} else {
				edittext.setText(str.substring(9, str.length()));
			}
			break;
		case TypeQuest.CORROSIVE_AREA:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.CORROSIVE_REPAIR_OBJ:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.CORROSIVE_CHECK_DATE:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			break;
		case TypeQuest.CORROSIVE_CHECK_DATE2:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			break;
		case TypeQuest.CORROSIVE_MILE_LOCATION:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.CORROSIVE_APPEAR_DES:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			text_promprt.setText("包括脱落、剥离、龟裂、褶皱、起泡等");
			break;
		case TypeQuest.CORROSIVE_REPAIR_DATE:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			break;
		case TypeQuest.CORROSIVE_CORROSION_DES:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			System.out.println(str + "---------------------------------");
			if ((str == null) || (getIntent().getStringExtra("Tittle") + "：未填写").equals(str)) {
				edittext.setText("");
			} else {
				edittext.setText(str.substring(9, str.length()));
			}
			break;
		case TypeQuest.CORROSIVE_CORROSION_AREA:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.CORROSIVE_CORROSION_NUM:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.CORROSIVE_REPAIR_SITUATION:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.CORROSIVE_PILE_INFO:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.CORROSIVE_REMARKS:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			if ((str == null) || (getIntent().getStringExtra("Tittle") + "：未填写").equals(str)) {
				edittext.setText("");
			} else {
				edittext.setText(str.substring(3, str.length()));
			}
			break;
		case TypeQuest.CORROSIVE_CLOCKPOSITION1:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.CORROSIVE_CLOCKPOSITION2:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.CORROSIVE_PITDEPTHMAX:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.CORROSIVE_PITDEPTHMIN:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;

		case TypeQuest.KEYPOINT_NAME:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.KEYPOINT_DEPARTMENT:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.KEYPOINT_OFFSET:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.KEYPOINT_BUFFER:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			edittext.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		case TypeQuest.KEYPOINT_START:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		case TypeQuest.KEYPOINT_END:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			edittext.setText(str0);
			break;
		}
		edittext.setSelection(edittext.length());

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				switch (key) {
				case TypeQuest.PROTECT_TEMPERATURE:
					intent.putExtra("temperature", edittext.getText().toString());
					break;
				// case TypeQuest.PROTECT_WEATHER:
				// intent.putExtra("weather", edittext.getText().toString());
				// break;
				case TypeQuest.PROTECT_VALUE:
					intent.putExtra("value", edittext.getText().toString());
					break;
				case TypeQuest.PROBLEM_DISTANCE:
					intent.putExtra("distance", edittext.getText().toString());
					break;
				case TypeQuest.PROBLEM_COMMENT:
					intent.putExtra("comment", edittext.getText().toString());
					break;
				case TypeQuest.PROBLEM_LOCATION:
					intent.putExtra("location", edittext.getText().toString());
					break;
				case TypeQuest.PROBLEM_PLAN:
					intent.putExtra("plan", edittext.getText().toString());
					break;
				case TypeQuest.PROBLEM_RESULT:
					intent.putExtra("result", edittext.getText().toString());
					break;

				case TypeQuest.PROTECT_TESTER:
					intent.putExtra("tester", edittext.getText().toString());
					break;
				case TypeQuest.PIPELINE_RECORD_WERTHER:
					intent.putExtra("weather", edittext.getText().toString());
					break;
				case TypeQuest.PIPELINE_RECORD_ROAD:
					intent.putExtra("road", edittext.getText().toString());
					break;
				case TypeQuest.PIPELINE_RECORD_RECORD:
					intent.putExtra("record", edittext.getText().toString());
					break;
				case TypeQuest.PIPELINE_RECORD_PROBLEM:

					intent.putExtra("problem", edittext.getText().toString());
					break;
				case TypeQuest.PIPELINE_RECORD_RESULT:
					intent.putExtra("result", edittext.getText().toString());
					break;
				case TypeQuest.PIPELINE_RECORD_SENDER:
					intent.putExtra("sender", edittext.getText().toString());
					break;
				case TypeQuest.PIPELINE_RECORD_RECEIVER:
					intent.putExtra("receiver", edittext.getText().toString());
					break;
				case TypeQuest.PIPELINE_RECORD_TIME:
					intent.putExtra("time", edittext.getText().toString());
					break;
				case TypeQuest.PIPELINE_RECORD_STATION:
					intent.putExtra("station", edittext.getText().toString());
					break;
				case TypeQuest.PIPELINE_RECORD_CAR:
					intent.putExtra("car", edittext.getText().toString());
					break;
				case TypeQuest.PIPELINE_RECORD_OTHER:
					intent.putExtra("other", edittext.getText().toString());
					break;
				case TypeQuest.SETTING_PORT:
					intent.putExtra("port", edittext.getText().toString());
					break;
				case TypeQuest.SETTING_M:
					intent.putExtra("SETTING_M", edittext.getText().toString());
					break;
				case TypeQuest.SETTING_SERVER_NAME:
					intent.putExtra("server_name", edittext.getText().toString());
					break;

				case TypeQuest.PROTECT_TESTTIME:
					intent.putExtra("test_time", edittext.getText().toString());
					break;
				case TypeQuest.PROTECT_TESTTIME2:
					intent.putExtra("test_time", edittext.getText().toString());
					break;
				case TypeQuest.PROTECT_GROUND:
					intent.putExtra("ground", edittext.getText().toString());
					break;

				case TypeQuest.PROTECT_VOLTAGE:
					intent.putExtra("voltage", edittext.getText().toString());
					break;
				case TypeQuest.PROTECT_REMARKS:
					intent.putExtra("remarks", edittext.getText().toString());
					break;
				case TypeQuest.PROTECT_NATURAL:
					intent.putExtra("natural", edittext.getText().toString());
					break;
				case TypeQuest.PROTECT_IR:
					intent.putExtra("ir", edittext.getText().toString());
					break;
				case TypeQuest.NATURAL_TEMPERATURE:
					intent.putExtra("temperature", edittext.getText().toString());
					break;
				case TypeQuest.NATURAL_WEATHER:
					intent.putExtra("weather", edittext.getText().toString());
					break;
				case TypeQuest.NATURAL_VALUE:
					intent.putExtra("value", edittext.getText().toString());
					break;
				case TypeQuest.NATURAL_TESTTIME:
					intent.putExtra("test_time", edittext.getText().toString());
					break;
				case TypeQuest.NATURAL_TESTTIME2:
					intent.putExtra("test_time", edittext.getText().toString());
					break;
				case TypeQuest.NATURAL_VOLTAGE:
					intent.putExtra("voltage", edittext.getText().toString());
					break;
				case TypeQuest.NATURAL_REMARKS:
					intent.putExtra("remarks", edittext.getText().toString());
					break;

				case TypeQuest.GROUND_SETVALUE:
					intent.putExtra("set_value", edittext.getText().toString());
					break;
				case TypeQuest.GROUND_TESTVALUE:
					intent.putExtra("test_value", edittext.getText().toString());
					break;
				case TypeQuest.GROUND_CONCLUSION:
					intent.putExtra("conclusion", edittext.getText().toString());
					break;
				case TypeQuest.GROUND_TESTDATE:
					intent.putExtra("test_date", edittext.getText().toString());
					break;
//				case TypeQuest.GROUND_WEATHER:
//					intent.putExtra("weather", edittext.getText().toString());
//					break;
//				case TypeQuest.GROUND_TEMPERATURE:
//					intent.putExtra("temperature", edittext.getText().toString());
//					break;

				case TypeQuest.CORROSIVE_DAMAGE_DES:
					intent.putExtra("damage_des", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_CORROSION_DES:
					intent.putExtra("corrosion_des", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_OFFSET:
					intent.putExtra("offset", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_SOIL:
					intent.putExtra("soil", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_AREA:
					intent.putExtra("area", edittext.getText().toString());
					break;

				case TypeQuest.CORROSIVE_CORROSION_AREA:
					intent.putExtra("corrosion_area", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_CORROSION_NUM:
					intent.putExtra("corrosion_num", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_REPAIR_OBJ:
					intent.putExtra("repair_obj", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_CHECK_DATE:
					intent.putExtra("check_date", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_CHECK_DATE2:
					intent.putExtra("check_date", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_MILE_LOCATION:
					intent.putExtra("clockposition", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_APPEAR_DES:
					intent.putExtra("appear_des", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_REPAIR_DATE:
					intent.putExtra("repair_date", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_REPAIR_SITUATION:
					intent.putExtra("repair_situation", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_PILE_INFO:
					intent.putExtra("pile_info", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_REMARKS:
					intent.putExtra("remarks", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_PITDEPTHMAX:
					intent.putExtra("pitdepthmax", edittext.getText().toString());
					break;
				case TypeQuest.CORROSIVE_PITDEPTHMIN:
					intent.putExtra("pitdepthmin", edittext.getText().toString());
					break;
				case TypeQuest.KEYPOINT_NAME:
					intent.putExtra("keypointname", edittext.getText().toString());
					break;
				case TypeQuest.KEYPOINT_DEPARTMENT:
					intent.putExtra("keypointdepartment", edittext.getText().toString());
					break;
				case TypeQuest.KEYPOINT_OFFSET:
					intent.putExtra("keypointoffset", edittext.getText().toString());
					break;
				case TypeQuest.KEYPOINT_BUFFER:
					intent.putExtra("keypointbuffer", edittext.getText().toString());
					break;
				case TypeQuest.KEYPOINT_START:
					intent.putExtra("keypointstart", edittext.getText().toString());
					break;
				case TypeQuest.KEYPOINT_END:
					intent.putExtra("keypointend", edittext.getText().toString());
					break;
				}
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});

	}

}
