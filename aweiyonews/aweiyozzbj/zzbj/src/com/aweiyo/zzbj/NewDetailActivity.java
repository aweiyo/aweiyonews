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
			settings.setJavaScriptEnabled(true);//����js����
			settings.setBuiltInZoomControls(true);//�Ŵ���С����
			settings.setUseWideViewPort(true);
			mWebView.loadUrl(url);//������ҳ��������ҳ��url����
			
			//�����ֻ�����������µ�url
			mWebView.setWebViewClient(new WebViewClient(){

				/**
				 * ��ҳ��ʼ
				 */
				@Override
				public void onPageStarted(WebView view, String url,
						Bitmap favicon) {
					// TODO Auto-generated method stub
					super.onPageStarted(view, url, favicon);
					pbProgress.setVisibility(View.VISIBLE);
				}

				/**
				 * ��ҳ����
				 */
				@Override
				public void onPageFinished(WebView view, String url) {
					// TODO Auto-generated method stub
					super.onPageFinished(view, url);
					pbProgress.setVisibility(View.GONE);
				}

				/**
				 * shouldOverrideUrlLoading������ҳ�г����Ӱ�ť����Ӧ
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
				 * ����
				 */
				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					// TODO Auto-generated method stub
					super.onProgressChanged(view, newProgress);
					System.out.println("����"+newProgress);
				}

				/**
				 * ����
				 */
				@Override
				public void onReceivedTitle(WebView view, String title) {
					// TODO Auto-generated method stub
					super.onReceivedTitle(view, title);
					System.out.println("����"+title);
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
			//����
			case R.id.btn_back:
				mWebView.goBack();
				break;
			case R.id.btn_share:
				//����	
				break;
			case R.id.btn_size:
				//���������С
//System.out.println("����.....................................");
				showChooseDialog();
				break;
			default:
				break;
			}
		}

		private int mCurrentChooseItem;// ��¼��ǰѡ�е�item, ���ȷ��ǰ
		private int mCurrentItem = 2;// ��¼��ǰѡ�е�item, ���ȷ����
		
		private void showChooseDialog() {
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			String[] checkedItem=new String[]{"���������", "�������", "��������", "С������",
					"��С������" };
			builder.setTitle("��������");
			builder.setSingleChoiceItems(checkedItem, mCurrentItem, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					mCurrentChooseItem=which;
				}
			});
			
			//ע���DialogInterface��������
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//����WebSettings��������Ĵ�С
					WebSettings settings=mWebView.getSettings();
					switch (mCurrentChooseItem) {//�ж�Ŀǰ��ѡ�����һ��
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
					mCurrentItem=mCurrentChooseItem;//�����´ν���Ĭ�ϵ�ѡ�������
				}
			});
			
			builder.setNegativeButton("ȡ��", null);
			
			builder.show();
			
		}
		
	}
