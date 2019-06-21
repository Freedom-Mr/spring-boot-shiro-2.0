package casia.isiteam.springbootshiro.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeartorUtils {
	// 关键字颜色
	public static final String RED_MARKER_TAG_BEGIN = "<font style='color:red;'>";
	public static final String RED_MARKER_TAG_END = "</font>";
	// 关键字组织颜色
	public static final String BLUE_MARKER_TAG_BEGIN = "<font style='color:blue;'>";
	public static final String BLUE_MARKER_TAG_END = "</font>";
	// 关键字地点颜色
	public static final String GREEN_MARKER_TAG_BEGIN = "<font style='color:green;'>";
	public static final String GREEN_MARKER_TAG_END = "</font>";
	// 关键字人物颜色
	public static final String PLUM_MARKER_TAG_BEGIN = "<font style='color:plum;'>";
	public static final String PLUM_MARKER_TAG_END = "</font>";
	// 关键字时间颜色
	public static final String PURPLE_MARKER_TAG_BEGIN = "<font style='color:#daa612;'>";
	public static final String PURPLE_MARKER_TAG_END = "</font>";
	// 关键字观点颜色
	public static final String ORANGE_MARKER_TAG_BEGIN = "<font style='color:orange;'>";
	public static final String ORANGE_MARKER_TAG_END = "</font>";

	/**
     * 	<B>转成中文标点符号</B>
     * 	<P>全角字符串 
     *	全角空格为12288,半角空格为32 
     *	其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248 
     * @param speartor 标点符号
     * @return 
     */
	public static String ToChSpeartor(String speartor) { // 半角转全角：
		char[] c = speartor.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
				continue;
			}
			if (c[i] < 127) {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}
	
	/**
     * 	<B>转成英文标点符号</B>
     * 	<P>半角字符串
     *	全角空格为12288，半角空格为32
     *	其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     * @param speartor 标点符号
     * @return 
     */
	public static String ToEnglishSpeartor(String speartor) {
		char[] c = speartor.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375) {
				c[i] = (char) (c[i] - 65248);
			}
		}
		return new String(c);
	}

	/**
	 * 关键词高亮处理
	 * @param kws
	 * @param content
	 * @param split
	 * @return
	 */
	public static String RedMaker(String kws, String content, String split) {
		String str = content;
		String[] kw;
		if(kws != null) {
			kw = kws.split(split);
			if(kw != null && kw.length > 0) {
				for(String s : kw) {
					if(s != null && !s.trim().isEmpty()) str = IgnoreCaseReplace(str, s.trim(), RED_MARKER_TAG_BEGIN + s + RED_MARKER_TAG_END);
				}
			}
		}
		return str;

	}
	/**
	 * 组织高亮处理
	 * @param kws
	 * @param content
	 * @param split
	 * @return
	 */
	public static String BlueMaker(String kws, String content, String split) {
		String str = content;
		String[] kw;
		if(kws != null) {
			kw = kws.split(split);
			if(kw != null && kw.length > 0) {
				for(String s : kw) {
					if(s != null && !s.trim().isEmpty()) str = IgnoreCaseReplace(str, s.trim(), BLUE_MARKER_TAG_BEGIN + s + BLUE_MARKER_TAG_END);
				}
			}
		}
		return str;

	}
	/**
	 * 地点高亮处理
	 * @param kws
	 * @param content
	 * @param split
	 * @return
	 */
	public static String GreenMaker(String kws, String content, String split) {
		String str = content;
		String[] kw;
		if(kws != null) {
			kw = kws.split(split);
			if(kw != null && kw.length > 0) {
				for(String s : kw) {
					if(s != null && !s.trim().isEmpty()) str = IgnoreCaseReplace(str, s.trim(), GREEN_MARKER_TAG_BEGIN + s + GREEN_MARKER_TAG_END);
				}
			}
		}
		return str;

	}
	/**
	 * 人物高亮处理
	 * @param kws
	 * @param content
	 * @param split
	 * @return
	 */
	public static String PlumMaker(String kws, String content, String split) {
		String str = content;
		String[] kw;
		if(kws != null) {
			kw = kws.split(split);
			if(kw != null && kw.length > 0) {
				for(String s : kw) {
					if(s != null && !s.trim().isEmpty()) str = IgnoreCaseReplace(str, s.trim(), PLUM_MARKER_TAG_BEGIN + s + PLUM_MARKER_TAG_END);
				}
			}
		}
		return str;

	}
	/**
	 * 时间高亮处理
	 * @param kws
	 * @param content
	 * @param split
	 * @return
	 */
	public static String PurpleMaker(String kws, String content, String split) {
		String str = content;
		String[] kw;
		if(kws != null) {
			kw = kws.split(split);
			if(kw != null && kw.length > 0) {
				for(String s : kw) {
					if(s != null && !s.trim().isEmpty()) str = IgnoreCaseReplace(str, s.trim(), PURPLE_MARKER_TAG_BEGIN + s + PURPLE_MARKER_TAG_END);
				}
			}
		}
		return str;

	}

	public static String IgnoreCaseReplace(String source, String oldstring, String newstring) {
		// Pattern p = Pattern.compile(oldstring, Pattern.CASE_INSENSITIVE);
		Pattern p = Pattern.compile(oldstring);
		Matcher m = p.matcher(source);
		String ret = m.replaceAll(newstring);
		return ret;
	}
}
