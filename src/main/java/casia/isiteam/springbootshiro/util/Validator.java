package casia.isiteam.springbootshiro.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created by casia.wzy on 2018/3/11.
 * 判断是否为空：空-false；非空-true;
 */
public class Validator {
	public static synchronized boolean check(String string) {
		return (string == null || "".equals(string.trim())) ? false : true;
	}
	public static synchronized boolean check(String[] string) {
		return (string == null || string.length == 0) ? false : true;
	}
	public static synchronized boolean check(Collection collection) {
		return (collection == null || collection.isEmpty()) ? false : true;
	}
	public static synchronized boolean check(Map map) {
		return (map == null || map.isEmpty()) ? false : true;
	}
	public static synchronized boolean check(Object o) {
		return o == null ? false : true;
	}
	public static synchronized boolean check(Object[] o) {
		return (null == o || o.length == 0) ? false : true;
	}

}
