package com.aweiyo.newmobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.aweiyo.newmobilesafe.R;
import com.aweiyo.newmobilesafe.utils.SmsUtils;

public class AToolsActivity extends Activity{
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	public void numberAddressQuery(View view){
		startActivity(new Intent(AToolsActivity.this, AddressActivity.class));
	}
	
	/**
	 * 备份短信
	 * @param view
	 */
	public void backUpSms(View view){
		
		pd = new ProgressDialog(AToolsActivity.this);
		pd.setTitle("提示");
		pd.setMessage("正在备份哟，请耐心等待");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置成水平的样式
		pd.show();
		
		new Thread(){
			public void run() {
				boolean result = SmsUtils.backUp(AToolsActivity.this,pd);
				if(result){
					Looper.prepare();
					Toast.makeText(AToolsActivity.this, "success", 0).show();
					Looper.loop();
					}else{
					Looper.prepare();
					Toast.makeText(AToolsActivity.this, "fail", 0).show();
					Looper.loop();
					}
				//备份短信完把进度条消失掉
				pd.dismiss();
			};
		
		}.start();
		
	}
}
