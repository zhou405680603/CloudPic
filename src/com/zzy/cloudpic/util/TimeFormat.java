package com.zzy.cloudpic.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormat {

	public final static String FORMAT_CHINA = "yy年MM月dd日  HH:mm";

	public static String timeFormat(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(TimeFormat.FORMAT_CHINA);
		return sdf.format(new Date(time));
	}

}
