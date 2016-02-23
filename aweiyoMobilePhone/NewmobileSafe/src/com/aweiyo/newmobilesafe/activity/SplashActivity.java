package com.aweiyo.newmobilesafe.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aweiyo.newmobilesafe.R;
import com.aweiyo.newmobilesafe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends Activity {

	private TextView tvVersion;
	private String mVersionName;
	private int mVersionCode;
	private String mDesc;
	private String mDownloadUrl;

	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_NET_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	protected static final int CODE_ENTER_HOME = 4;// ������ҳ��

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// ���ݴ���������what���ж���ʲô���
			switch (msg.what) {
			case 0:
				showUpdateDialog();
				break;
			case 1:
				Toast.makeText(SplashActivity.this, "url����", Toast.LENGTH_SHORT)
						.show();
				enterHome();
				break;
			case 2:
				Toast.makeText(SplashActivity.this, "�������", Toast.LENGTH_SHORT)
						.show();
				enterHome();
				break;
			case 3:
				Toast.makeText(SplashActivity.this, "���ݽ�������",
						Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case 4:
				enterHome();
				break;
			default:
				break;
			}
		};
	};
	private SharedPreferences mPref;
	private RelativeLayout rlRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("�汾�ţ�" + getVersionName() + "   by_aweiyo");
		
		//����sharedpreferences���ж��ǹ���Ҫ���汾�ĸ���
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		boolean auto_update = mPref.getBoolean("auto_update", true);
		if(auto_update){
			CheckCode();
		}else{
			//����handler���ӳ����뷢����Ϣ��Ŀ����ͣ�����棬չʾlogo
			mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2000);
		}
		
		AlphaAnimation animation=new AlphaAnimation(0.3f, 1.0f);
		animation.setDuration(2000);
		rlRoot.startAnimation(animation);
		copyDB("address.db");
		copyDB("antivirus.db");
//		createShortCut();
	}

	/**
	 * ����������Ŀ�ݷ�ʽ
	 */  
//	private void createShortCut() {
//		Intent intent=new Intent();
//		Intent shortcutIntent=new Intent();
//		shortcutIntent.setAction("aweiyo");
//		shortcutIntent.addCategory("android.intent.category.DEFAULT");
//		intent.putExtra("duplicate", false);
//		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
//		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
//		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "NewMobieSafe");
//		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,shortcutIntent );
//		sendBroadcast(intent);
//	}

	/**
	 * ��ȡ�汾��
	 * 
	 * @return
	 */
	private String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			String versionName = packageInfo.versionName;
			System.out.println(versionName);
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ���ذ汾��
	 * 
	 * @return
	 */
	private int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			System.out.println(versionCode);
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * �ӷ�������ȡjson���ݣ�����json����
	 */
	private void CheckCode() {
		// �������߳��첽������������
		new Thread() {
			private HttpURLConnection connection;

			public void run() {
				Message msg = Message.obtain();
				try {
					URL url = new URL("http://192.168.1.115:8080/updata.json");
					connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(5000);// �����������ӳ�ʱʱ��
					connection.setRequestMethod("GET");
					connection.setReadTimeout(5000);// �������������ˣ�����������Ӧ��ʱʱ��
					connection.connect();

					// ��ȡ��Ӧ��Ҳ���ж��Ƿ���Ӧ�ɹ�
					int code = connection.getResponseCode();
					if (code == 200) {
						InputStream inputStream = connection.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);

						// ����json����,һ��Ҫ��Ҫ���������ݷ���object����
						JSONObject json = new JSONObject(result);
						mVersionName = json.getString("versionName");
						mVersionCode = json.getInt("versionCode");
						mDesc = json.getString("description");
						mDownloadUrl = json.getString("downloadUrl");

						// �жϷ������İ汾�����ڵİ汾��ȷ���Ƿ���Ҫ����
						// mVersionCode���������汾̖
						// getVersionCode ���ذ汾̖
						// �������Ĵ��ڱ��صİ汾�ţ�˵���и���
						if (mVersionCode > getVersionCode()) {
							// showUpdateDialog();
//							Log.d(mVersionCode + "", "mVersionCode");
//							Log.d(getVersionCode() + "", "getVersionCode");
							msg.what = CODE_UPDATE_DIALOG;
						}
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					msg.what = CODE_URL_ERROR;
				} catch (IOException e) {
					e.printStackTrace();
					msg.what = CODE_NET_ERROR;
				} catch (JSONException e) { 
					// json���ݸ�ʽʧ��
					e.printStackTrace();
					msg.what = CODE_JSON_ERROR;
				} finally {
					mHandler.sendMessage(msg);
					if (connection != null) {
						// ���_connect��朽�
						connection.disconnect();
					}
				}
			};
		}.start();
	}

	/**
	 * ����Ǹ��£��������µĽ���
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("���°汾��" + mVersionName);
		builder.setMessage(mDesc);
		builder.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//System.out.println("��������");
				downLoad();
			}
		});
		builder.setNegativeButton("�´����f", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		
		//���û��ڸ��µĶԻ�����ȡ��������������
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});

		builder.show();
	}
	
	/**
	 * ����apk�ķ���
	 * bug��ǩ�����󣬰�װ����apk
	 */
	protected void downLoad() {
		String target=Environment.getExternalStorageDirectory()+"/update.apk";
		HttpUtils utils=new HttpUtils();
		utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				System.out.println("���سɹ�");
				
			} 
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				System.out.println("����ʧ��");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				System.out.println("���ؽ���:"+current+"/"+total);
			}
			
			
		});
	}

	/**
	 * ��ת����ҳ��
	 */
	public void enterHome(){
		Intent intent=new Intent(SplashActivity.this,HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void copyDB(String dbName) {
		File destFile=new File(getFilesDir(),dbName);
		InputStream is=null;
		FileOutputStream fos=null;
		try {
			is = getAssets().open(dbName);
			fos=new FileOutputStream(destFile);
			int len=0;
			byte[] bus=new byte[1024];
			while((len=(is.read(bus)))!=-1){
				fos.write(bus, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				fos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
