package casia.isiteam.springbootshiro.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author wzy Date 2017/8/8 20:28
 */
public class Helper {
	public static Date formatDateString(String date, String pattern) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			return simpleDateFormat.parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	public static synchronized String formatDate(Date date, String pattern) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			return simpleDateFormat.format(date);
		} catch (RuntimeException e) {
			return null;
		}
	}
}
