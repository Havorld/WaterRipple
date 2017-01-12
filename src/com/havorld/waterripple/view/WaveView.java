package com.havorld.waterripple.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.havorld.waterripple.utils.ScreenInfoUtil;

/**
 * 圆环
 */
public class WaveView extends View {

	private Paint paint;
	private int radio; // 圆环半径
	private float downX; // 按下屏幕坐标X
	private float downY; // 按下屏幕坐标Y
	private int colorMark = 0;
	private int myColor;

	public WaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	// 1、创建画笔
	private void initView() {
		radio = 0;
		paint = new Paint();
		intColorMark(); // 获取颜色
		paint.setColor(myColor);
		paint.setAntiAlias(true); // 抗锯齿
		paint.setStyle(Paint.Style.STROKE); // 样式为圆环
		paint.setStrokeWidth(radio / 2); // 圆环宽度为radio
	}

	private void intColorMark() {
		colorMark += 1;
		if (colorMark == 1) {
			myColor = Color.RED;
		} else if (colorMark == 2) {
			myColor = Color.BLUE;
		} else if (colorMark == 3) {
			myColor = Color.GREEN;
		} else {
			colorMark = 0;
			myColor = Color.BLACK;
		}
	}

	// 在第一次执行onDraw前调用的。
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		int[] screenMeasure = ScreenInfoUtil.getScreenMeasure(getContext());
		downX = screenMeasure[0] / 2;
		downY = screenMeasure[1] / 2;
	}

	// 2、开始绘制
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawCircle(downX, downY, radio, paint); // 重绘的圆环的内径A1
		if (paint.getAlpha() > 0) { // 只有当透明度大于0 才继续给handler发消息 B3
			handler.sendEmptyMessageDelayed(0, 50); // 每次重绘后，等待50毫秒，给handler发消息
													// B1
		}
	}

	// 3、让圆环动起来 实例化一个Handler
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 每次handler接到消息都要invalidate()从而onDraw(); B2
			super.handleMessage(msg);
			radio += 5; // 这里的自增会影响每一次重绘的圆环的内径A1和圆环的宽度A2
			paint.setStrokeWidth(radio / 3); // 重绘的圆环的宽度A2

			// 4、让圆环透明度递减
			int alpha = paint.getAlpha();
			alpha -= 5;
			if (alpha <= 0) {
				alpha = 0;
			}
			paint.setAlpha(alpha);

			invalidate(); // onDraw();
		}
	};

	// 5、拦截触摸事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = event.getX();
			downY = event.getY();
			initView(); // 重新初始化圆环
			invalidate(); // onDraw();
			break;
		}
		return super.onTouchEvent(event);
	}
}