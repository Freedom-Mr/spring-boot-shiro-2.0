package casia.isiteam.springbootshiro.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 * 
 * @author hj
 */
public class DateUtils {
	/**
	 * 比较两个日期，返回较小的那个日期 支持以下三种格式： yyyy-MM-dd HH:mm:ss yyyy-MM-dd yyyyMMdd
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static Date dateCompare(String date1, String date2) {
		if (date1 == null || date2 == null)
			return null;
		date1 = date1.trim();
		date2 = date2.trim();
		try {
			Date d1 = recoveDate(date1);
			Date d2 = recoveDate(date2);
			return d1.getTime() > d2.getTime() ? d2 : d1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 转换为日期 支持以下三种格式： yyyy-MM-dd HH:mm:ss yyyy-MM-dd yyyyMMdd
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date recoveDate(String date) {
		if (date == null)
			return null;
		date = date.trim();
		Date d = null;
		try {
			if (date.contains(" ")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				d = sdf.parse(date);
			} else if (date.contains("-")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				d = sdf.parse(date);
			} else if (date.contains("/")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				d = sdf.parse(date);
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				d = sdf.parse(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	/**
	 * 截断日期，只保留到日
	 * 
	 * @param date
	 *            日期对象
	 * @param pattern
	 *            转换格式，如yyyy-MM-dd
	 * @param suffix
	 *            后缀，00:00:00
	 * @return
	 */
	public static String truncDate(Date date, String pattern, String suffix) {
		if (date == null)
			return null;
		if (suffix == null)
			suffix = "";
		else
			suffix = " " + suffix;
		if (pattern == null)
			pattern = "yyyy-MM-dd";
		String result = new SimpleDateFormat(pattern).format(date) + suffix;
		return result;
	}

	/**
	 * 重载，无后缀
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String truncDate(Date date, String pattern) {
		return truncDate(date, pattern, null);
	}

	/**
	 * 重载，默认yyyy-MM-dd，无后缀
	 * 
	 * @param date
	 * @return
	 */
	public static String truncDate(Date date) {
		return truncDate(date, null);
	}

	public final static int SECOND = 0;
	public final static int MINUTE = 1;
	public final static int HOUR = 2;
	public final static int DAY = 3;
	public final static int MONTH = 4;
	public final static int YEAR = 5;

	/**
	 * 获取两个时间段内的间隔时间点List
	 * 
	 * @param st
	 *            开始时间
	 * @param et
	 *            结束时间
	 * @param pattern
	 *            格式化的格式，如yyyy-MM-dd HH
	 * @param blanknum
	 *            间隔数
	 * @param type
	 *            间隔类型
	 * @return
	 * @throws ParseException
	 */
	public static List<String> getTimeListByType(Object st, Object et, String pattern, int blanknum, int type) {
		List<String> list = new ArrayList<String>();
		Date stime = null;
		Date etime = null;
		if (st instanceof Date)
			stime = (Date) st;
		else if (st instanceof String) {
			String temp = ((String) st).trim();
			stime = recoveDate((String) temp);
		}
		if (et instanceof Date)
			etime = (Date) et;
		else if (et instanceof String) {
			String temp = ((String) et).trim();
			etime = recoveDate((String) temp);
		}
		Date addtime = (Date) stime.clone();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(addtime);
		while (calendar.getTime().getTime() <= etime.getTime()) {
			String stimeStr = null;
			switch (type) {
			case SECOND:
				addtime = calendar.getTime();
				stimeStr = truncDate(addtime, pattern);
				list.add(stimeStr);
				calendar.setTime(new Date(addtime.getTime() + 1000 * blanknum));
				break;
			case MINUTE:
				addtime = calendar.getTime();
				stimeStr = truncDate(addtime, pattern);
				list.add(stimeStr);
				calendar.setTime(new Date(addtime.getTime() + 60 * 1000 * blanknum));
				break;
			case HOUR:
				addtime = calendar.getTime();
				stimeStr = truncDate(addtime, pattern);
				list.add(stimeStr);
				calendar.add(Calendar.HOUR_OF_DAY, blanknum);
				break;
			case DAY:
				addtime = calendar.getTime();
				stimeStr = truncDate(addtime, pattern);
				list.add(stimeStr);
				calendar.add(Calendar.DAY_OF_YEAR, blanknum);
				break;
			case MONTH:
				addtime = calendar.getTime();
				stimeStr = truncDate(addtime, pattern);
				list.add(stimeStr);
				calendar.add(Calendar.MONTH, blanknum);
				break;
			case YEAR:
				addtime = calendar.getTime();
				stimeStr = truncDate(addtime, pattern);
				list.add(stimeStr);
				calendar.add(Calendar.YEAR, blanknum);
				break;
			}
		}
		return list;
	}

	/**
	 * 获取指定日期前的某一天日期 支持以下三种格式： Date yyyy-MM-dd HH:mm:ss yyyy-MM-dd yyyyMMdd
	 * 
	 * @param date
	 * @param blanknum
	 * @return yyyy-MM-dd
	 */
	public static String getDayBefore(Object date, int blanknum) {
		Date dates = null;
		if (date instanceof Date)
			dates = (Date) date;
		else if (date instanceof String) {
			String temp = ((String) date).trim();
			dates = recoveDate((String) temp);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(dates);
		cal.add(Calendar.DAY_OF_WEEK, blanknum);
		String resultDate = truncDate(cal.getTime());
		return resultDate;
	}

	public static String getRangeTime(String refTime, int calendarType, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(recoveDate(refTime));
		cal.add(calendarType, offset);
		return dateToString(cal.getTime());
	}

	public static String getRangeTime(Date refTime, int calendarType, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(refTime);
		cal.add(calendarType, offset);
		return dateToString(cal.getTime());
	}

	/** 将日期类型转换成String类型，file的格式 */
	public static String dateToStringByFile(Date date) {
		String sDate = new SimpleDateFormat("/yyyy/MM/dd/").format(date);
		return sDate;
	}

	/** 将日期类型转换成String类型，yyyy-MM-dd HH:mm:ss格式 */
	public static String dateToString(Date date) {
		String sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		return sDate;
	}

	// 格式化日期 chenbw 20140818
	public static String formatDate(Date date, String pattern) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			return simpleDateFormat.format(date);
		} catch (RuntimeException e) {
			return null;
		}
	}

	/**
	 * 根据间隔数和间隔类型 获取时间点 map
	 * 
	 * @param st
	 *            开始时间
	 * @param et
	 *            结束时间
	 * @param blanknum
	 *            间隔数
	 * @param type
	 *            间隔类型 “hour” 小时 "day" 天
	 * @throws Exception
	 */
	public static List<String> getTimeListByType(Object st, Object et, Integer blanknum, String type) throws Exception {
		List<String> list = new ArrayList<String>();
		Date stime = null;
		Date etime = null;
		if (st instanceof Date) {
			stime = (Date) st;
		}
		if (et instanceof Date) {
			etime = (Date) et;
		}
		if (st instanceof String) {
			stime = Helper.formatDateString((String) st, "yyyy-MM-dd");
		}
		if (et instanceof String) {
			etime = Helper.formatDateString((String) et + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			Date now_date = new Date();
			if (etime.after(now_date)) {
				etime = now_date;
			} else {
				etime = Helper.formatDateString((String) et + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			}
		}
		Date addtime = Helper.formatDateString((String) st, "yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(addtime);
		while (calendar.getTime().getTime() <= etime.getTime()) {
			if ("hour".equals(type)) {
				addtime = calendar.getTime();
				String stimeStr = formatDate(addtime, "yyyy-MM-dd HH:mm:ss");
				list.add(stimeStr);
				calendar.add(Calendar.HOUR_OF_DAY, blanknum);
			} else {
				addtime = calendar.getTime();
				String stimeStr = formatDate(addtime, "yyyyMMdd");
				list.add(stimeStr);
				calendar.add(Calendar.DAY_OF_YEAR, blanknum);
			}

		}
		return list;
	}

	/** 将日期类型转换成String类型，yyyyMMddHHmmss格式 */
	public static String dateToStringWithExcel(Date date) {
		String sDate = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
		return sDate;
	}

	public static Date dealTimeThreshold(String timeType) {
		Date timeThreshold = null;
		Calendar caln = null;
		switch (timeType) {
		case "0":// 今天
			caln = Calendar.getInstance();
			caln.set(Calendar.HOUR_OF_DAY, 0);
			caln.set(Calendar.MINUTE, 0);
			caln.set(Calendar.SECOND, 0);
			caln.set(Calendar.MILLISECOND, 0);
			timeThreshold = caln.getTime();
			break;
		case "1":// 一小时
			caln = Calendar.getInstance();
			caln.add(Calendar.HOUR, -1);
			timeThreshold = caln.getTime();
			break;
		case "2":// 三小时
			caln = Calendar.getInstance();
			caln.add(Calendar.HOUR, -3);
			timeThreshold = caln.getTime();
			break;
		case "3":// 六小时
			caln = Calendar.getInstance();
			caln.add(Calendar.HOUR, -4);
			timeThreshold = caln.getTime();
			break;
		case "4":// 十二小时
			caln = Calendar.getInstance();
			caln.add(Calendar.HOUR, -12);
			timeThreshold = caln.getTime();
			break;
		case "5":// 二十四小时
			caln = Calendar.getInstance();
			caln.add(Calendar.HOUR, -24);
			timeThreshold = caln.getTime();
			break;
		default:// 默认今天
			caln = Calendar.getInstance();
			caln.set(Calendar.HOUR_OF_DAY, 0);
			caln.set(Calendar.MINUTE, 0);
			caln.set(Calendar.SECOND, 0);
			caln.set(Calendar.MILLISECOND, 0);
			timeThreshold = caln.getTime();
			break;
		}
		return timeThreshold;
	}

	/*
	 * public static void main(String[] args) { try {
	 * System.out.println(DateUtils.getTimeListByType("2017-01-01", "2017-01-02",
	 * "yyyy-MM-dd HH:mm:ss", 6, DateUtils.SECOND)); } catch (ParseException e) {
	 * e.printStackTrace(); } }
	 */
}
