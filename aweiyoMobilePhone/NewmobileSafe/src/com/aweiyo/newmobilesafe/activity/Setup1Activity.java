package com.aweiyo.newmobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aweiyo.newmobilesafe.R;

public class Setup1Activity extends BaseSetupActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	// ��һҳ

	@Override
	public void showNextPage() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		//���������л��Ķ���
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);//���붯�����˳�����
	}

	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		
	}
}
