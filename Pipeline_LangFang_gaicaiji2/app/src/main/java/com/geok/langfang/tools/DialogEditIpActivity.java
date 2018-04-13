package com.geok.langfang.tools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geok.langfang.pipeline.R;

public class DialogEditIpActivity extends Activity {
	TextView text, text_promprt;
	EditText editText1, editText2, editText3, editText4;
	Button button;
	boolean ipIsRight = true;

	private int key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialogactivity_edit_ip);
		/*
		 * init
		 */
		text = (TextView) findViewById(R.id.dialogactivity_tittle);
		button = (Button) findViewById(R.id.dialogactivity_button);
		text_promprt = (TextView) findViewById(R.id.dialogactivity_promprt);

		editText1 = (EditText) findViewById(R.id.dialogactivity_edit1);
		editText2 = (EditText) findViewById(R.id.dialogactivity_edit2);
		editText3 = (EditText) findViewById(R.id.dialogactivity_edit3);
		editText4 = (EditText) findViewById(R.id.dialogactivity_edit4);
		editText1.addTextChangedListener(new MyTextWatch());
		editText2.addTextChangedListener(new MyTextWatch());
		editText3.addTextChangedListener(new MyTextWatch());
		editText4.addTextChangedListener(new MyTextWatch());
		String ip = null;
		if (getIntent().getStringExtra("ip") != null) {
			ip = getIntent().getStringExtra("ip");
		}
		// if(editText1.getText().toString().equals("")||editText2.getText().toString().equals("")||
		// editText3.getText().toString().equals("")||editText4.getText().toString().equals("")){
		// String ip1 = editText1.getText().toString();
		// String ip2 = editText2.getText().toString();
		// String ip3 = editText3.getText().toString();
		// String ip4 = editText4.getText().toString();
		// ip=ip1 + "." + ip2 + "." + ip3 + "." + ip4;
		// }

		if (ip != null) {
			String[] s = ip.split("\\.");
			editText1.setText(s[0]);
			editText2.setText(s[1]);
			editText3.setText(s[2]);
			editText4.setText(s[3]);
		}

		key = getIntent().getIntExtra("flag", 4);
		switch (key) {

		case TypeQuest.SETTING_IP_ADDRESS:
			text.setText("请输入" + getIntent().getStringExtra("Tittle"));
			break;

		}
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				switch (key) {

				case TypeQuest.SETTING_IP_ADDRESS:
					if(editText1.getText().toString().equals("")||
							editText2.getText().toString().equals("")||
							editText3.getText().toString().equals("")||
							editText4.getText().toString().equals("")){
						Toast.makeText(DialogEditIpActivity.this, "请输入正确Ip地址", 1000).show();
					}else{
					int ip1 = Integer.parseInt(editText1.getText().toString());
					int ip2 = Integer.parseInt(editText2.getText().toString());
					int ip3 = Integer.parseInt(editText3.getText().toString());
					int ip4 = Integer.parseInt(editText4.getText().toString());
					if (ip1 > 255 || ip2 > 255 || ip3 > 255 || ip4 > 255) {
						text_promprt.setVisibility(View.VISIBLE);
						text_promprt.setText("Ip地址不合法！");
						text_promprt.setTextColor(Color.RED);
						ipIsRight = false;
					} else {
						ipIsRight = true;
					}
					if (ipIsRight) {

						Intent intent = new Intent();
						intent.putExtra("ip_address", ip1 + "." + ip2 + "." + ip3 + "." + ip4);
						setResult(Activity.RESULT_OK, intent);
						finish();
					} else {
						Toast.makeText(DialogEditIpActivity.this, "请输入正确Ip地址", 1000).show();
					}
				}
					break;
				}

			}
		});

	}

	/*
	 * 监听ip地址状态
	 */

	class MyTextWatch implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub

			if (editText1.getText().toString().length() >= 3 && editText1.hasFocus()) {

				editText2.requestFocus();
			}
			if (editText2.getText().toString().length() >= 3 && editText2.hasFocus()) {
				editText3.requestFocus();
			}
			if (editText3.getText().toString().length() >= 3 && editText3.hasFocus()) {

				editText4.requestFocus();
			}
			if (editText4.getText().toString().length() >= 3 && editText4.hasFocus()) {
				int ip1 = Integer.parseInt(editText1.getText().toString());
				int ip2 = Integer.parseInt(editText2.getText().toString());
				int ip3 = Integer.parseInt(editText3.getText().toString());
				int ip4 = Integer.parseInt(editText4.getText().toString());

				if (ip1 > 255 || ip2 > 255 || ip3 > 255 || ip4 > 255) {
					text_promprt.setVisibility(View.VISIBLE);
					text_promprt.setText("Ip地址不合法！");
					text_promprt.setTextColor(Color.RED);
					ipIsRight = false;
				} else {
					ipIsRight = true;
				}
			}
		}
	}
}
