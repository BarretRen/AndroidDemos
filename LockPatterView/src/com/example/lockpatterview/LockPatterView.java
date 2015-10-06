package com.example.lockpatterview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LockPatterView extends View {
	private static final int POINT_SIZE = 5;
	private Point[][] points = new Point[3][3];
	private Matrix matrix = new Matrix();
	private float width, height, offstartY, moveX, moveY;;
	private Bitmap bitmap_pressed, bitmap_normal, bitmap_error, bitmap_line,bitmap_line_error;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private List<Point> pointList = new ArrayList<LockPatterView.Point>();
	private OnPatterChangeLister onPatterChangeLister;

	/**
	 * ���캯��
	 */
	public LockPatterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LockPatterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LockPatterView(Context context) {
		super(context);
	}

	/*********************************************************
	 * ����9����
	 * movePoint����������ƶ������ǲ���9��������ĵ�
	 * isInit�Ƿ��ʼ����9����
	 * isSelect ��λ�Ƿ�ѡ��״̬
	 * isFinish �Ƿ�������
	 */
	private boolean isInit, isSelect, isFinish, movePoint;

	@Override
	protected void onDraw(Canvas canvas) {
		// ��һ��û�г�ʼ���ͽ��г�ʼ����һ����ʼ���Ͳ��ڳ�ʼ�������ˣ�isInit����˼��---Ĭ��û�г�ʼ����
		if (!isInit) {
			// ��ʼ��9����
			initPoints();
		}
		// ����9����
		points2Canvas(canvas);

		if (pointList.size() > 0) {
			Point a = pointList.get(0);
			// ���ƾŹ��������
			for (int i = 0; i < pointList.size(); i++) {
				Point b = pointList.get(i);
				line2Canvas(canvas, a, b);
				a = b;
			}
			// ������������
			if (movePoint) {
				line2Canvas(canvas, a, new Point(moveX, moveY));
			}
		}
	}

	/**
	 * ��ʼ��9����λ ��ȡ��λ��3��״̬ �ߵ�2��״̬ �Լ�9�������λ�� �Լ���ʼ��������� isInit=
	 * true����״̬--�´β��س�ʼ����������
	 */
	private void initPoints() {

		// ��ȡ���ֿ��
		width = getWidth();
		height = getHeight();

		// ����������

		offstartY = (height - width) / 2;

		// ͼƬ��Դ
		bitmap_normal = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_circle_normal);
		bitmap_pressed = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_circle_pressed);
		bitmap_error = BitmapFactory.decodeResource(getResources(),
				R.drawable.btn_circle_selected);
		bitmap_line = BitmapFactory.decodeResource(getResources(),
				R.drawable.ddd);
		bitmap_line_error = BitmapFactory.decodeResource(getResources(),
				R.drawable.qqq);

		points[0][0] = new Point(width / 4, offstartY + width / 4);
		points[0][1] = new Point(width / 2, offstartY + width / 4);
		points[0][2] = new Point(width / 4 * 3, offstartY + width / 4);

		points[1][0] = new Point(width / 4, offstartY + width / 4 * 2);
		points[1][1] = new Point(width / 2, offstartY + width / 4 * 2);
		points[1][2] = new Point(width / 4 * 3, offstartY + width / 4 * 2);

		points[2][0] = new Point(width / 4, offstartY + width / 4 * 3);
		points[2][1] = new Point(width / 2, offstartY + width / 4 * 3);
		points[2][2] = new Point(width / 4 * 3, offstartY + width / 4 * 3);

		// ��������1--9
		int index = 1;
		for (Point[] points : this.points) {
			for (Point point : points) {
				point.index = index;
				index++;
			}
		}
		// ��ʼ�����
		isInit = true;
	}

	/**
	 * ��9������Ƶ����� ѭ������9����λ�� ����3�ֲ�ͬ��״̬����3�ֲ�ͬ��9����λ
	 */
	private void points2Canvas(Canvas canvas) {
		// ѭ������9����λ
		for (int i = 0; i < points.length; i++) {
			// ѭ������ÿ�е�3����λ
			for (int j = 0; j < points[i].length; j++) {
				// ��ȡ���ε�ĳ����λ
				Point point = points[i][j];
				if (point.state == Point.STATE_PRESSED) {
					// (Bitmap bitmap, float left, float top, Paint paint)
					canvas.drawBitmap(bitmap_pressed,
							point.x - bitmap_normal.getWidth() / 2, point.y
									- bitmap_normal.getHeight() / 2, paint);
				} else if (point.state == Point.STATE_ERROR) {
					canvas.drawBitmap(bitmap_error,
							point.x - bitmap_normal.getWidth() / 2, point.y
									- bitmap_normal.getHeight() / 2, paint);
				} else {
					canvas.drawBitmap(bitmap_normal,
							point.x - bitmap_normal.getWidth() / 2, point.y
									- bitmap_normal.getHeight() / 2, paint);
				}
			}
		}
	}

	/**
	 * ����
	 */
	public void line2Canvas(Canvas canvas, Point a, Point b) {
		// �ߵĳ���--2��֮��ľ���
		float linelength = (float) Point.distance(a, b);
		// ��ȡ2��֮��ĽǶ�
		float degress = getDegrees(a, b);
		//����a�������ת
		canvas.rotate(degress, a.x, a.y);

		if (a.state == Point.STATE_PRESSED) {
			// xy�����ϵ����ű���
			matrix.setScale(linelength / bitmap_line.getWidth(), 1);
			matrix.postTranslate(a.x - bitmap_line.getWidth() / 2, a.y
					- bitmap_line.getHeight() / 2);
			canvas.drawBitmap(bitmap_line, matrix, paint);
		} else {
			matrix.setScale(linelength / bitmap_line.getWidth(), 1);
			matrix.postTranslate(a.x - bitmap_line.getWidth() / 2, a.y
					- bitmap_line.getHeight() / 2);
			canvas.drawBitmap(bitmap_line_error, matrix, paint);
		}
		//������ϻع�Ƕ�
		canvas.rotate(-degress, a.x, a.y);
	}

	// ��ȡ�Ƕ�
	public float getDegrees(Point pointA, Point pointB) {
		return (float) Math.toDegrees(Math.atan2(pointB.y - pointA.y, pointB.x
				- pointA.x));
	}

	/****************************************************************************
	 * onTouch�¼�����
	 */

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		moveX = event.getX();
		moveY = event.getY();

		movePoint = false;
		isFinish = false;

		Point point = null;

		switch (event.getAction()) {
		//ֻҪ���²������ʹ������»��ƽ���
		case MotionEvent.ACTION_DOWN:
			if (onPatterChangeLister != null) {
				onPatterChangeLister.onPatterStart(true);
			}
			// ÿ�ΰ��£�����Ҫ���֮ǰ�ļ���
			resetPoint();
			// ����ǲ����ھŹ�����
			point = chechSelectPoint();
			if (point != null) {
				//������µ�λ����9�����ڣ��͸ĳ�״̬Ϊtrue
				isSelect = true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isSelect) {
				// ����ǲ����ھŹ�����
				point = chechSelectPoint();
				if (point == null) {
					movePoint = true;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			//������ϣ���λ״̬��Ϊδѡ��
			isFinish = true;
			isSelect = false;
			break;

		}
		// ���û�л�����ϣ�����Ź�����ѡ��״̬
		if (!isFinish && isSelect && point != null) {
			// �����
			if (crossPoint(point)) {
				movePoint = true;
			} else {// �µ�
				point.state = Point.STATE_PRESSED;
				pointList.add(point);
			}
		}

		// ���ƽ���
		if (isFinish) {
			// ���Ʋ�����
			if (pointList.size() == 1) {
				// resetPoint();
				errorPoint();
			} else if (pointList.size() < POINT_SIZE && pointList.size() > 0) {// ���ƴ���
				errorPoint();
				if (onPatterChangeLister != null) {
					onPatterChangeLister.onPatterChange(null);
				}
			} else {
				if (onPatterChangeLister != null) {
					String pass = "";
					for (int i = 0; i < pointList.size(); i++) {
						pass = pass + pointList.get(i).index;
					}
					if (!TextUtils.isEmpty(pass)) {
						onPatterChangeLister.onPatterChange(pass);
					}
				}
			}
		}

		postInvalidate();
		return true;
	}

	/**
	 * ���»���
	 */
	public void resetPoint() {
		for (int i = 0; i < pointList.size(); i++) {
			Point point = pointList.get(i);
			point.state = Point.STATE_NORMAL;
		}
		pointList.clear();
	}

	/**
	 * ����Ƿ�ѡ��
	 */
	private Point chechSelectPoint() {
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				Point point = points[i][j];
				if (Point.with(point.x, point.y, bitmap_normal.getWidth() / 2,
						moveX, moveY)) {
					return point;
				}
			}
		}

		return null;
	}

	/**
	 * �����
	 */
	private boolean crossPoint(Point point) {
		if (pointList.contains(point)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ���ƴ���
	 */
	public void errorPoint() {
		for (Point point : pointList) {
			point.state = Point.STATE_ERROR;
		}
	}

	/***********************************************************************
	 * �Զ���ĵ�
	 */
	public static class Point {
		// ����
		public static int STATE_NORMAL = 0;
		// ѡ��
		public static int STATE_PRESSED = 1;
		// ����
		public static int STATE_ERROR = 2;
		public float x, y;
		public int index = 0, state = 0;

		public Point() {
		};

		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}

		/**
		 * ����֮��ľ���
		 */
		public static double distance(Point a, Point b) {
			return Math.sqrt(Math.abs(a.x - b.x) * Math.abs(a.x - b.x)
					+ Math.abs(a.y - b.y) * Math.abs(a.y - b.y));
		}

		/**
		 */
		public static boolean with(float paintX, float pointY, float r,
				float moveX, float moveY) {
			return Math.sqrt((paintX - moveX) * (paintX - moveX)
					+ (pointY - moveY) * (pointY - moveY)) < r;
		}
	}

	/**
	 * ͼ��������
	 */
	public static interface OnPatterChangeLister {
		void onPatterChange(String passwordStr);

		void onPatterStart(boolean isStart);
	}

	/**
	 * ����ͼ��������
	 */
	public void SetOnPatterChangeLister(OnPatterChangeLister changeLister) {
		if (changeLister != null) {
			this.onPatterChangeLister = changeLister;
		}
	}
}
