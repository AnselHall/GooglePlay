package com.exe.googleplay.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtil {
	/**
	 * 根据颜色生成drawable
	 * @param rgb
	 * @return
	 */
	public static GradientDrawable generateDrawable(int rgb){
		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setColor(rgb);//设置颜色
		drawable.setCornerRadius(5);
		return drawable;
	}
	
	/**
	 * 生成selector
	 * @param pressed
	 * @param normal
	 * @return
	 */
	public static StateListDrawable generateSelector(Drawable pressed,Drawable normal){
		StateListDrawable stateListDrawable = new StateListDrawable();
		stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
		stateListDrawable.addState(new int[]{}, normal);
		return stateListDrawable;
	}
}
