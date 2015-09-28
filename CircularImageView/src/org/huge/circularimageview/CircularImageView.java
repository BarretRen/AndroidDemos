package org.huge.circularimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CircularImageView extends ImageView {
	private int borderWidth;
	private int canvasSize;
	private Bitmap image;
	private Paint paint;
	private Paint paintBorder;

	public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//初始化画笔
		paint=new Paint();
		paint.setAntiAlias(true);
		paintBorder=new Paint();
		paintBorder.setAntiAlias(true);
		
		TypedArray attributes=context.obtainStyledAttributes(attrs, R.styleable.CircularImageView, defStyle, 0);
		//有边框
		if (attributes.getBoolean(R.styleable.CircularImageView_border, true)) {
			//默认宽度
			int defaultBorderSize=(int)(4*getContext().getResources().getDisplayMetrics().density+0.5f);
			setBorderWidth(attributes.getDimensionPixelOffset(R.styleable.CircularImageView_border_width, defaultBorderSize));
			setBorderColor(attributes.getColor(R.styleable.CircularImageView_border_color, Color.WHITE));
		}
		//无边框
		if (attributes.getBoolean(R.styleable.CircularImageView_shadow, false))  
		    addShadow();  

	}

	public CircularImageView(Context context, AttributeSet attrs) {
		this(context, attrs,R.attr.circularImageViewStyle);
	}

	public CircularImageView(final Context context) {
		this(context,null);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		image=drawableToBitmap(getDrawable());
		if (image!=null) {
			canvasSize=canvas.getWidth();
			if (canvas.getHeight()<canvasSize) {//取画布宽高中较短的为默认大小
				canvasSize=canvas.getHeight();
			}
			BitmapShader shader=new BitmapShader(Bitmap.createScaledBitmap(image, canvasSize, canvasSize, false),TileMode.CLAMP, TileMode.CLAMP);
			paint.setShader(shader);
			//计算圆心并画圆
			int circleCenter=(canvasSize-(borderWidth*2))/2;
			canvas.drawCircle(circleCenter+borderWidth, circleCenter+borderWidth, circleCenter+borderWidth-4.0f, paintBorder);
			canvas.drawCircle(circleCenter+borderWidth, circleCenter+borderWidth, circleCenter-4.0f, paint);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width=measureWidth(widthMeasureSpec);
		int height=measureHeight(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	//设置边框宽度
	public void setBorderWidth(int width){
		this.borderWidth=width;
		this.requestLayout();
		this.invalidate();
	}
	//设置边框颜色
	public void setBorderColor(int color){
		if (paintBorder!=null) {
			paintBorder.setColor(color);
		}
		this.invalidate();
	}
	//添加无边框阴影
	public void addShadow(){
		setLayerType(LAYER_TYPE_SOFTWARE, paintBorder);
		paintBorder.setShadowLayer(4.0f, 0.0f, 2.0f, Color.BLACK);
	}
	
	private int measureWidth(int spec){
		int result=0;
		int specMode=MeasureSpec.getMode(spec);
		int specSize=MeasureSpec.getSize(spec);
		if (specMode == MeasureSpec.EXACTLY) {  
			result = specSize;  
		}else if (specMode == MeasureSpec.AT_MOST) {  
			 result = specSize;  
		} else {  
			result = canvasSize;  
		}  

		return result;
	}
	
	private int measureHeight(int spec){
		int result=0;
		int specMode=MeasureSpec.getMode(spec);
		int specSize=MeasureSpec.getSize(spec);
		if (specMode == MeasureSpec.EXACTLY) {  
			result = specSize;  
		}else if (specMode == MeasureSpec.AT_MOST) {  
			 result = specSize;  
		} else {  
			result = canvasSize;  
		}  
		return (result+2);
	}
	
	//获取bitmap
	public Bitmap drawableToBitmap(Drawable drawable){
		if (drawable==null) {
			return null;
		}else if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable)drawable).getBitmap();
		}
		
		Bitmap bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		
		return bitmap;
		
	}
	
}
