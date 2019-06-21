package casia.isiteam.springbootshiro.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author wzy
 * Date 2018/1/16 9:06
 */
public class TimeUtil {
    public static SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat sdf_2 = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat sdf_3 = new SimpleDateFormat("HH:mm:ss");


    /**
     * 返回格式化时间
     * @param date 参数
     * @return
     */
    public static synchronized String timeFotmat(Date date){
        if(  !Validator.check(date) ){
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 获取当前时间前或者后N天时间
     * @param hour 参数
     * @return
     */
    public static synchronized Date addSubtractHour(int hour){
        Date dt = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
         rightNow.add(Calendar.DAY_OF_YEAR,hour);
        Date dt1 = rightNow.getTime();
        return dt1;
    }
    /**
     * 获取当前时间前或者后N月时间
     * @param month 参数
     * @return
     */
    public static synchronized Date addSubtractMonth(int month){
        Date dt = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH, month);
        Date dt1 = rightNow.getTime();
        return dt1;
    }
    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static synchronized String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        String result = format.format(today);
        return result;
    }
}
