package orghuge.webviewtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpConnection;

import android.os.Environment;

public class HttpDownloadThread extends Thread{
	private String url;
	
	public HttpDownloadThread(String url) {
		this.url=url;
	}
	
	@Override
	public void run() {
		System.out.println("开始下载");
		URL httpUrl;
		try {
			httpUrl = new URL(url);
			HttpURLConnection connection=(HttpURLConnection) httpUrl.openConnection();
			File downloadFile;
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				downloadFile=new File(Environment.getExternalStorageDirectory(),"webview.apk");
				byte[] buffer=new byte[6*1024];
				int length;
				InputStream in=connection.getInputStream();
				FileOutputStream out=new FileOutputStream(downloadFile);
				while((length=in.read(buffer))!=-1){
					if (out!=null) {
						out.write(buffer, 0, length);
					}
				}
				if (out!=null) {
					out.close();
				}
				if (in!=null) {
					in.close();
				}
				System.out.println("下载完成");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
