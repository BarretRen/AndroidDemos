package org.huge.volleytouchnetwork;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapCache implements ImageCache {
	private  LruCache<String, Bitmap> cache;
	private int max=10*1024*1024;//用于设置最大缓存大小
	public BitmapCache() {
		//实例化lrucache
		cache=new LruCache<String, Bitmap>(max){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes()*value.getHeight();
			}
		};
	}
	@Override
	public Bitmap getBitmap(String arg0) {
		return cache.get(arg0);
	}
	@Override
	public void putBitmap(String arg0, Bitmap arg1) {
		cache.put(arg0, arg1);
	}
}
