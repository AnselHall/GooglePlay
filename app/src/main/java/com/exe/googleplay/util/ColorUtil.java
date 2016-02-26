package com.exe.googleplay.util;

import android.graphics.Color;

import java.util.Random;

public class ColorUtil {
	/**
	 * 使用rgb生成一个比较中庸的颜色，
	 * @return
	 */
	public static int generateBeautifulColor(){
		Random random = new Random();
		//为了保证混合生成的颜色不至于太暗和太亮，所以对rgb的值进行限定
		int red = random.nextInt(150)+50;//50-200
		int green = random.nextInt(150)+50;
		int blue = random.nextInt(150)+50;
		//使用rgb混合出一种新的颜色
		return Color.rgb(red, green, blue);
	}
}
