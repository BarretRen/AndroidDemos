package org.huge.matertialdesign;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
/*
 * 使用CardView，通过设置圆角半径和阴影高度实现材料设计
 */
public class CardViewActivity extends Activity {
	private CardView cardview;
	private SeekBar radius,elevation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cartview_main);
		cardview=(CardView)findViewById(R.id.cardview);
		radius=(SeekBar)findViewById(R.id.Radius);
		elevation=(SeekBar)findViewById(R.id.Elevation);
		
		radius.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				cardview.setRadius(progress);
			}
		});
		elevation.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				cardview.setElevation(progress);
			}
		});
	}
	
}
