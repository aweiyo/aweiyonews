	package com.aweiyo.zzbj;
	
	import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.TextSize;
import android.widget.ImageButton;
import android.widget.ProgressBar;
	
	public class NewDetailActivity extends Activity implements OnClickListener{
		
		private WebView mWebView;
		private ImageButton btnBack;
		private ImageButton btnSize;
		private ImageButton btnShare;
		private ProgressBar pbProgress;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_news_detail);
			initUI();
			initData();
		}
	 
		private void initData() {
			String url=getIntent().getStringExtra("url");
			WebSettings settings = mWebView.getSettings();
			settings.setJavaScriptEnabled(true);//设置js可用
			settings.setBuiltInZoomControls(true);//放大缩小功能
			settings.setUseWideViewPort(true);
			mWebView.loadUrl(url);//加载网页，传入网页的url即可
			
			//启动手机浏览器来打开新的url
			mWebView.setWebViewClient(new WebViewClient(){

				/**
				 * 网页开始
				 */
				@Override
				public void onPageStarted(WebView view, String url,
						Bitmap favicon) {
					// TODO Auto-generated method stub
					super.onPageStarted(view, url, favicon);
					pbProgress.setVisibility(View.VISIBLE);
				}

				/**
				 * 网页结束
				 */
				@Override
				public void onPageFinished(WebView view, String url) {
					// TODO Auto-generated method stub
					super.onPageFinished(view, url);
					pbProgress.setVisibility(View.GONE);
				}

				/**
				 * shouldOverrideUrlLoading：对网页中超链接按钮的响应
				 */
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// TODO Auto-generated method stub
					mWebView.loadUrl(url);
					return true;
					//return super.shouldOverrideUrlLoading(view, url);
				}
			});
			
			mWebView.setWebChromeClient(new WebChromeClient(){

				/**
				 * 进度
				 */
				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					// TODO Auto-generated method stub
					super.onProgressChanged(view, newProgress);
					System.out.println("进度"+newProgress);
				}

				/**
				 * 标题
				 */
				@Override
				public void onReceivedTitle(WebView view, String title) {
					// TODO Auto-generated method stub
					super.onReceivedTitle(view, title);
					System.out.println("标题"+title);
				}
				
			});
			
		}
	
		private void initUI() {
			mWebView = (WebView) findViewById(R.id.wv_web);
			btnBack = (ImageButton) findViewById(R.id.btn_back);
			btnSize = (ImageButton) findViewById(R.id.btn_size);
			btnShare = (ImageButton) findViewById(R.id.btn_share);
			pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
			btnBack.setOnClickListener(this);
			btnSize.setOnClickListener(this);
			btnShare.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			//返回
			case R.id.btn_back:
				mWebView.goBack();
				break;
			case R.id.btn_share:
				//分享	
				break;
			case R.id.btn_size:
				//调整字体大小
//System.out.println("有了.....................................");
				showChooseDialog();
				break;
			default:
				break;
			}
		}

		private int mCurrentChooseItem;// 记录当前选中的item, 点击确定前
		private int mCurrentItem = 2;// 记录当前选中的item, 点击确定后
		
		private void showChooseDialog() {
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			String[] checkedItem=new String[]{"超大号字体", "大号字体", "正常字体", "小号字体",
					"超小号字体" };
			builder.setTitle("字体设置");
			builder.setSingleChoiceItems(checkedItem, mCurrentItem, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					mCurrentChooseItem=which;
				}
			});
			
			//注意加DialogInterface！！！！
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//利用WebSettings设置字体的大小
					WebSettings settings=mWebView.getSettings();
					switch (mCurrentChooseItem) {//判断目前是选择的哪一个
					case 0:
						settings.setTextSize(TextSize.LARGEST)	;	
						break;
					case 1:
						settings.setTextSize(TextSize.LARGER)	;				
						break;
					case 2:
						settings.setTextSize(TextSize.NORMAL)	;	
						break;
					case 3:
						settings.setTextSize(TextSize.SMALLER)	;	
						break;
					case 4:
						settings.setTextSize(TextSize.SMALLEST)	;	
						break;

					default:
						break;
					}
					mCurrentItem=mCurrentChooseItem;//设置下次进来默认的选择的字体
				}
			});
			
			builder.setNegativeButton("取消", null);
			
			builder.show();
			
		}
		
	}
