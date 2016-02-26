package com.exe.googleplay.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.exe.googleplay.R;

/**
 * 根据宽与高的比例(ratio)，动态设置高的值，因为宽始终是充满父view的
 * @author Administrator
 *
 */
public class RatioLayout extends FrameLayout {
	// 宽和高的比例
	private float ratio = 0.0f;

	public RatioLayout(Context context) {
		this(context, null);
	}

	public RatioLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
		ratio = a.getFloat(R.styleable.RatioLayout_ratio, 0.0f);
		Log.e("RatioLayout", "ratio: "+ratio);
		a.recycle();//释放TypedArray
		
	}

	public void setRatio(float f) {
		ratio = f;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
		if (widthMode == MeasureSpec.EXACTLY  && ratio != 0.0f) {
			height = (int) (width / ratio + 0.5f);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(height + getPaddingTop() + getPaddingBottom(),
					MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
