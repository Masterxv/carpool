package com.campussay.carpool.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Create by Zhangfan on 2019/4/20
 **/
public class DateUtil {
    /**
     * 获取系统时间戳
     * @return
     */
    public long getCurTimeLong(){
        long time=System.currentTimeMillis();
        return time;
    }
    /**
     * 获取当前时间
     * @param pattern
     * @return
     */
    public static String getCurDate(String pattern){
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new java.util.Date());
    }

    /**
     * 时间戳转换成字符窜
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将字符串转为时间戳
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static Date getDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }


    public static String getShortTime(String time) {
        String shortString = null;
        long now = Calendar.getInstance().getTimeInMillis();
        Date date = getDate(time);
        if(date == null) {
            return shortString;
        }
        long delTime = (now - date.getTime()) / 1000;
        if (delTime > 365 * 24 * 60 * 60) {
            shortString = (int) (delTime / (365 * 24 * 60 * 60)) + "年前";
        } else if (delTime > 24 * 60 * 60) {
            shortString = (int) (delTime / (24 * 60 * 60)) + "天前";
        } else if (delTime > 60 * 60) {
            shortString = (int) (delTime / (60 * 60)) + "小时前";
        } else if (delTime > 60) {
            shortString = (int) (delTime / (60)) + "分前";
        } else if (delTime > 1) {
            shortString = delTime + "秒前";
        } else {
            shortString = "1秒前";
        }
        return shortString;
    }


}

