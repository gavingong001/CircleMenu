package com.gavin.circlemenudemo;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * 扇形布局
 * 
 * @author gavin
 * 
 */
public class CircleMenuView extends View {

	private int suggestW = 40;// default
	private int suggestH = 40;// default
	private int mTotal;// 子菜单格格式
	private Paint paint;

	public Bitmap bgBitmap;
	public Bitmap bgBitmapInner;
	public Bitmap centerBitmap;

	private ArrayList<MenuItem> list = new ArrayList<CircleMenuView.MenuItem>();
	private ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
	private ArrayList<Bitmap> bitmapFocuse = new ArrayList<Bitmap>();
	private ArrayList<Integer> mImgs = new ArrayList<Integer>();
	private ArrayList<Integer> mImgs_fouse = new ArrayList<Integer>();

	private SelectListener listener = null;
	public int screenW = 0;
	public int screenH = 0;

	public int startAngle = 0;// 开始角度
	public int distanceR;// 左上角坐标

	public CircleMenuView(Context context) {
		super(context);
	}

	public CircleMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public CircleMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		return true;
	}

	public void setItemsSrcs(ArrayList<Integer> imgs,
			ArrayList<Integer> imgs_fouse, Integer bg_circle, Integer bg_inner,
			Integer center, int distance) {

		if (bg_circle != null) {
			bgBitmap = BitmapFactory.decodeResource(getResources(), bg_circle);// 背景图
		}
		if (bg_inner != null) {
			bgBitmapInner = BitmapFactory.decodeResource(getResources(),
					bg_inner);// 内部图
		}
		if (center != null) {
			centerBitmap = BitmapFactory.decodeResource(getResources(), center);// 中心点
		}
		mImgs = imgs;
		mImgs_fouse = imgs_fouse;
		if (mImgs != null && imgs_fouse != null) {
			mTotal = Math.min(imgs.size(), imgs_fouse.size());
		}

		distanceR = distance;
		init();
	}

	@SuppressLint("NewApi")
	public void init() {

		paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setStrokeWidth(3);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);

		// 添加图片
		if (mImgs != null && mImgs.size() > 0) {
			Bitmap temp;
			for (int i = 0; i < mImgs.size(); i++) {
				temp = BitmapFactory.decodeResource(getResources(),
						mImgs.get(i));
				bitmaps.add(temp);
			}
		}
		if (mImgs_fouse != null && mImgs_fouse.size() > 0) {
			Bitmap temp;
			for (int i = 0; i < mImgs_fouse.size(); i++) {
				temp = BitmapFactory.decodeResource(getResources(),
						mImgs_fouse.get(i));
				bitmapFocuse.add(temp);
			}
		}
		MenuItem mItem;
		for (int i = 0; i < mTotal; i++) {
			mItem = new MenuItem();
			mItem.bmp = bitmaps.get(i);
			mItem.bmp_fouse = bitmapFocuse.get(i);
			list.add(mItem);
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) (suggestW + getPaddingLeft() + getPaddingRight());
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureHeight(int measureSpec) {
		int result;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = suggestH + getPaddingTop() + getPaddingBottom();
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			float downX = event.getX();
			float downY = event.getY();

			float angle = (int) getAngle(downX, downY);
			if (angle == -1) {
				mCurrent = -1;
			} else {

				int res1 = (int) (angle / sweepAngle);
				mCurrent = res1;
			}

			invalidate();
			return true;
		case MotionEvent.ACTION_MOVE:
			float upX2 = event.getX();
			float upY2 = event.getY();
			Log.v("move", "ACTION_MOVE" + upX2 + "&" + upX2);
			float angle2 = (int) getAngle(upX2, upY2);
			if (angle2 == -1) {
				mCurrent = -1;
			} else {

				int res1 = (int) (angle2 / sweepAngle);
				mCurrent = res1;
			}

			invalidate();
			return true;
		case MotionEvent.ACTION_UP:
			float upX = event.getX();
			float upY = event.getY();

			float angle3 = (int) getAngle(upX, upY);
			if (angle3 == -1) {
				mCurrent = -1;
			} else {

				int res = (int) (angle3 / sweepAngle);
				mCurrent = res;
			}
			if (listener != null) {
				listener.onSelect(mCurrent);
			}
			mCurrent = -1;

			Log.v("move", "ACTION_MOVE" + upX + "&" + upY);
			invalidate();
			return true;
		default:
			break;
		}
		return true;

	}

	// 360 分成了n份
	private int sweepAngle;// 每个item的角度
	private int mCurrent = -1;

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		screenW = getMeasuredWidth();// 屏幕宽高
		screenH = getMeasuredHeight();

		RectF dst = new RectF(0, 0, screenW, screenH);
		paint.reset();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		// 绘制底图
		if (bgBitmap != null) {
			canvas.drawBitmap(bgBitmap, null, dst, paint);
		}
		if (bgBitmapInner != null) {
			canvas.drawBitmap(bgBitmapInner, null, dst, paint);
		}
		// 绘制各个图标及扇形
		darwItems(canvas, mTotal);
		// 绘制中间按钮
		paint.reset();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setColor(Color.WHITE);
		if (centerBitmap != null) {
			canvas.drawBitmap(centerBitmap,
					screenW / 2 - centerBitmap.getWidth() / 2, screenH / 2
							- centerBitmap.getHeight() / 2, paint);
		}

	}

	/**
	 * 绘制每个item
	 */
	private void darwItems(Canvas canvas, int total) {

		Log.v("paint", "distanceR = " + distanceR);

		RectF arcRect = new RectF(distanceR, distanceR, screenW - distanceR,
				screenH - distanceR);// 矩形四个角
		sweepAngle = 360 / total;

		int centerx = screenH / 2;
		int centery = screenH / 2;

		float dis = screenH / 3.1f;

		for (int j = 0; j < total; j++) {

			MenuItem menuItem = list.get(j);
			// 画扇形
			paint.reset();
			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			if (j == mCurrent) {
				paint.setColor(0xFF00A9EF);
				// 先画实心扇形
				canvas.drawArc(arcRect, startAngle + j * sweepAngle,
						sweepAngle, true, paint);
				// 再画空心扇形
				/*
				 * paint.reset(); paint.setStyle(Style.STROKE);
				 * paint.setStrokeWidth(5); paint.setColor(Color.BLUE);
				 * canvas.drawArc(arcRect, startAngle + j * sweepAngle,
				 * sweepAngle, true, paint);
				 */

			} else {
				paint.reset();
				paint.setAntiAlias(true);
				paint.setFilterBitmap(true);
				paint.setStyle(Style.FILL);
				paint.setColor(Color.TRANSPARENT);
				canvas.drawArc(arcRect, startAngle + j * sweepAngle,
						sweepAngle, true, paint);
			}

			// Log.v("paint", "startAngle" + (startAngle + j * sweepAngle) +
			// "||sweepAngle" + sweepAngle);

			double radians = Math.toRadians(startAngle + sweepAngle / 2 + j
					* sweepAngle);

			int x = (int) (centerx + dis * Math.cos(radians))
					- menuItem.bmp.getWidth() / 2;
			int y = (int) (centery + dis * Math.sin(radians))
					- menuItem.bmp.getHeight() / 2;

			// 画图板
			paint.reset();
			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setColor(Color.WHITE);
			if (j == mCurrent) {
				canvas.drawBitmap(menuItem.bmp_fouse, x, y, paint);
			} else {
				canvas.drawBitmap(menuItem.bmp, x, y, paint);
			}
			menuItem.startAngle = startAngle + j * sweepAngle;
			menuItem.endAngle = menuItem.startAngle + sweepAngle;
		}
	}

	/**
	 * 根据触摸的位置，计算角度
	 * 
	 * @param xTouch
	 * @param yTouch
	 * @return
	 */
	private float getAngle(float xTouch, float yTouch) {
		double mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());
		double x = Math.abs(xTouch - (mRadius / 2d));
		double y = Math.abs(yTouch - (mRadius / 2d));

		if (centerBitmap != null) {
			if ((Math.abs(x) < centerBitmap.getWidth() / 2)
					&& (Math.abs(y) < centerBitmap.getHeight() / 2))
				// 点击的是中间位置
				return -1;
		}

		/*
		 * Log.v("paint", "xTouch"+xTouch + " yTouch"+yTouch); Log.v("paint",
		 * "Quadrant>" + getQuadrant(xTouch, yTouch,mRadius));
		 */

		float degree = (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);

		switch (getQuadrant(xTouch, yTouch, mRadius)) {
		case 1:
			degree = 360 - degree;
			break;
		case 2:
			degree = 180 + degree;
			break;
		case 3:
			degree = 180 - degree;
			break;
		case 4:
			break;

		default:
			break;
		}

		return (float) degree;
	}

	/**
	 * 根据当前位置计算象限
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int getQuadrant(float x, float y, double mRadius) {
		int tmpX = (int) (x - mRadius / 2);
		int tmpY = (int) (y - mRadius / 2);

		// Log.v("paint", "tmpX"+tmpX + "tmpY"+tmpY);

		if (tmpX >= 0) {
			return tmpY >= 0 ? 4 : 1;
		} else {
			return tmpY >= 0 ? 3 : 2;
		}

	}

	public void resetView() {
		mCurrent = -1;
		invalidate();
	}

	/**
	 * item bean
	 * 
	 * @author gavin
	 * 
	 */
	class MenuItem {
		public Bitmap bmp;
		public Bitmap bmp_fouse;
		public int startAngle;
		public int endAngle;
	}

	public interface SelectListener {
		public void onSelect(int chooseItem);
	}

	public void setOnSelectListener(SelectListener listener) {
		this.listener = listener;
	}

}
