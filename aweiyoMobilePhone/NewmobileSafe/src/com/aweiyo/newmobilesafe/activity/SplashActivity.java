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
	protected static final int CODE_ENTER_HOME = 4;// 进入主页面

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 根据传递上来的what来判断是什么情况
			switch (msg.what) {
			case 0:
				showUpdateDialog();
				break;
			case 1:
				Toast.makeText(SplashActivity.this, "url错误", Toast.LENGTH_SHORT)
						.show();
				enterHome();
				break;
			case 2:
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT)
						.show();
				enterHome();
				break;
			case 3:
				Toast.makeText(SplashActivity.this, "数据解析错误",
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
		tvVersion.setText("版本号：" + getVersionName() + "   by_aweiyo");
		
		//根据sharedpreferences来判断是够需要检测版本的更新
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		boolean auto_update = mPref.getBoolean("auto_update", true);
		if(auto_update){
			CheckCode();
		}else{
			//利用handler来延迟两秒发送消息。目的是停留界面，展示logo
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
	 * 创建着桌面的快捷方式
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
	 * 获取版本名
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
	 * 获取本地版本号
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
	 * 从服务器获取json数据，解析json数据
	 */
	private void CheckCode() {
		// 开启子线程异步加载网络数据
		new Thread() {
			private HttpURLConnection connection;

			public void run() {
				Message msg = Message.obtain();
				try {
					URL url = new URL("http://192.168.1.115:8080/updata.json");
					connection = (HttpURLConnection) url.openConnection();
					connection.setConnectTimeout(5000);// 服务器的连接超时时间
					connection.setRequestMethod("GET");
					connection.setReadTimeout(5000);// 服务器连接上了，服务器的响应超时时间
					connection.connect();

					// 获取响应码也来判断是否响应成功
					int code = connection.getResponseCode();
					if (code == 200) {
						InputStream inputStream = connection.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);

						// 解析json数据,一定要把要解析的数据放在object里面
						JSONObject json = new JSONObject(result);
						mVersionName = json.getString("versionName");
						mVersionCode = json.getInt("versionCode");
						mDesc = json.getString("description");
						mDownloadUrl = json.getString("downloadUrl");

						// 判断服务器的版本和现在的版本来确定是否是要更新
						// mVersionCode：服務器版本號
						// getVersionCode 本地版本號
						// 服务器的大于本地的版本号，说明有更新
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
					// json数据格式失败
					e.printStackTrace();
					msg.what = CODE_JSON_ERROR;
				} finally {
					mHandler.sendMessage(msg);
					if (connection != null) {
						// 斷開connect的鏈接
						connection.disconnect();
					}
				}
			};
		}.start();
	}

	/**
	 * 如果是更新，弹出更新的界面
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("最新版本：" + mVersionName);
		builder.setMessage(mDesc);
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//System.out.println("立即更新");
				downLoad();
			}
		});
		builder.setNegativeButton("下次再說", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		
		//当用户在更新的对话框点击取消，跳回主界面
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});

		builder.show();
	}
	
	/**
	 * 下载apk的方法
	 * bug：签名错误，安装不了apk
	 */
	protected void downLoad() {
		String target=Environment.getExternalStorageDirectory()+"/update.apk";
		HttpUtils utils=new HttpUtils();
		utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				System.out.println("下载成功");
				
			} 
			
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				System.out.println("下载失败");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				System.out.println("下载进度:"+current+"/"+total);
			}
			
			
		});
	}

	/**
	 * 跳转到主页面
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
