package org.huge.volleytouchnetwork;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static RequestQueue queue;//请求队列
	private ImageView image;
	private String url="https://www.baidu.com/img/bdlogo.png";//要下载的图片

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//初始化组件
		image=(ImageView)findViewById(R.id.image);
		queue=Volley.newRequestQueue(getApplicationContext());//请求队列建立
		//Volley_get();//get方式请求方法
		//Volley_post();//post方式请求
		//Volley_ImageRequest();//ImageRequest加载图片
		//Volley_ImageLoader();//ImageLoader+ImageCache+LruCache加载图片
		Volley_NetworkImageView();//使用NetworkImageview加载图片
		
	}
	
	private void Volley_NetworkImageView() {
		NetworkImageView imageView=(NetworkImageView)findViewById(R.id.netimage);
		ImageLoader loader=new ImageLoader(queue, new BitmapCache());
		imageView.setDefaultImageResId(R.drawable.ic_launcher);
		imageView.setErrorImageResId(R.drawable.ic_launcher);
		imageView.setImageUrl(url, loader);
	}

	private void Volley_ImageLoader() {
		ImageLoader loader=new ImageLoader(queue, new BitmapCache());
		//监听图片加载过程(显示组件，默认加载图片，出错加载图片
		ImageListener listener=ImageLoader.getImageListener(image, R.drawable.ic_launcher, R.drawable.ic_launcher);
		loader.get(url, listener);//获取图片
	}

	@SuppressWarnings("deprecation")
	private void Volley_ImageRequest() {
		//使用imageRequest
		ImageRequest request=new ImageRequest(url, new Listener<Bitmap>() {
			@Override
			public void onResponse(Bitmap arg0) {
				image.setImageBitmap(arg0);
			}
		}, 0, 0, Config.RGB_565, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				image.setImageResource(R.drawable.ic_launcher);
			}
		});
		queue.add(request);
		queue.start();		
	}

	@Override
	protected void onStop() {
		super.onStop();
		//在onstop中取消队列里的请求
		queue.cancelAll("VolleyGet");//根据tag取消请求
		
	}

	private void Volley_post() {
		//返回string使用StringRequest
		String url="";
		StringRequest request=new StringRequest(Method.POST, url, new Listener<String>() {
			@Override
			public void onResponse(String arg0) {//网络请求成功后执行
				Toast.makeText(getApplicationContext(), arg0, 1).show();
			}
		}, new ErrorListener() {//网络请求失败后执行
			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(getApplicationContext(), arg0.toString(), 1).show();
			}
		}){//在方法体内设置POST需要的参数和值
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params=new HashMap<String, String>();
				params.put("key", "value");
				return params;
			}
		};
		request.setTag("VolleyGet");//设置tag标签，用于在请求队列中索引
		queue.add(request);//加入请求队列
	}

	private void Volley_get() {
		//返回string使用StringRequest
		String url="";
		StringRequest request=new StringRequest(Method.GET, url, new Listener<String>() {
			@Override
			public void onResponse(String arg0) {//网络请求成功后执行
				Toast.makeText(getApplicationContext(), arg0, 1).show();
			}
		}, new ErrorListener() {//网络请求失败后执行
			@Override
			public void onErrorResponse(VolleyError arg0) {
				Toast.makeText(getApplicationContext(), arg0.toString(), 1).show();
			}
		});
		request.setTag("VolleyGet");//设置tag标签，用于在请求队列中索引
		queue.add(request);//加入请求队列
		
	}
}
