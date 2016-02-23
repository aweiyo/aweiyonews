package com.aweiyo.newmobilesafe.activity;

import com.aweiyo.newmobilesafe.R;
import com.aweiyo.newmobilesafe.db.dao.AddressDao;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

public class AddressActivity extends Activity {
	private EditText etNumber;
	private TextView tvResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		etNumber=(EditText) findViewById(R.id.et_number);
		tvResult=(TextView) findViewById(R.id.tv_result);
		etNumber.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String address = AddressDao.getAddress(s.toString());
				tvResult.setText(address);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void query(View view){
		String number=etNumber.getText().toString();
		if(!TextUtils.isEmpty(number)){
			String address = AddressDao.getAddress(number);
			tvResult.setText(address);
		}else {
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	        etNumber.startAnimation(shake);
		}
	}
}
