package orghuge.webviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
/**
 * 在应用中通过webview加载网页，并且获取title
 * 设置webview的下载监听器
 * 自定义处理加载页面出现错误
 * @author Tiger
 *
 */
public class MyActivity extends Activity implements OnClickListener{
    private WebView webView;
    private Button exit,refresh;
    private TextView titleView;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        webView=(WebView)findViewById(R.id.webview);
        exit=(Button)findViewById(R.id.exit);
        refresh=(Button)findViewById(R.id.reflash);
        titleView=(TextView)findViewById(R.id.title);
        
        webView.loadUrl("http://www.baidu.com");
        //设置新的wevview客户端，
        webView.setWebViewClient(new WebViewClient(){
        	//URL加载时重写加载页面的方法，防止去调用自带的浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            //当遇到错误时
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            	titleView.setText("加载错误");
            	view.setVisibility(View.GONE);
            }
        });
        //设置chromeclient，获取网页内容的相关信息
        webView.setWebChromeClient(new WebChromeClient(){//当获取到标题时
        	@Override
        	public void onReceivedTitle(WebView view, String title) {
        		titleView.setText(title);
        	}
        });
        //设置webview的下载监听
        webView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String arg0, String arg1, String arg2, String arg3, long arg4) {
				if (arg0.endsWith(".apk")) {
					new HttpDownloadThread(arg0).start();//自定义的下载线程
				}
			}
		});
        //按钮的监听
        exit.setOnClickListener(this);
        refresh.setOnClickListener(this);
    }
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.exit:
			this.finish();
			break;
		case R.id.reflash:
			webView.reload();
		}
	}
}
