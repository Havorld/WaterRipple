package com.havorld.waterripple.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.havorld.waterripple.utils.ScreenInfoUtil;
/**
 * 
 * 圆环
 * 
 * 博客地址: http://blog.csdn.net/xiaohao0724/article/details/54375779
 *
 */
public class RingView extends View {

	protected static final String TAG = "Havorld";
	/**
	 * 圆心的X坐标
	 */
	private float cx;
	/**
	 * 圆心的Y坐标
	 */
	private float cy;
	/**
	 * 圆环半径
	 */
	private float radius;
	/**
	 * 默认画笔
	 */
	private Paint paint;
	private int[] colors = new int[] { Color.BLUE, Color.RED, Color.YELLOW,
			Color.GREEN, Color.DKGRAY };
	protected boolean isRunning = false;

	public RingView(Context context) {
		this(context, null);
	}

	public RingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	private void initView() {

		radius = 0;
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE); // 空心圆
		paint.setStrokeWidth(radius / 4); // 画笔宽度 半径4分之一
		paint.setColor(colors[(int) (Math.random() * 5)]); // 画笔颜色
		paint.setAlpha(255); // 不透明

	}

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			// 设置透明度
			int alpha = paint.getAlpha();
			if (alpha == 0) {
				isRunning = false;
			}

			// 透明度每次-10， 慢慢变透明
			alpha = Math.max(0, alpha - 10);

			paint.setAlpha(alpha);

			// 设置半径
			radius += 5; // 半径越来越大
			paint.setStrokeWidth(radius / 3);

			invalidate();

			if (isRunning) {
				handler.sendEmptyMessageDelayed(0, 50); // 继续递归调用
			}

		};
	};

	/**
	 * 执行动画
	 */
	private void startAnim() {
		isRunning = true;
		handler.sendEmptyMessageDelayed(0, 100);
	}

	// 在第一次执行onDraw前调用的。
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		int[] screenMeasure = ScreenInfoUtil.getScreenMeasure(getContext());
		cx = screenMeasure[0] / 2;
		cy = screenMeasure[1] / 2;
		startAnim();
	}

	// 销毁View的时候调用
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		isRunning = false;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		canvas.drawCircle(cx, cy, radius, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			cx = event.getX();
			cy = event.getY();
			initView();
			startAnim();

		}
		return true;

	}

}
