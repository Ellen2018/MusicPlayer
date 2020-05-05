package com.ellen.musicplayer.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    public static String format(int time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(time);
    }

    public static String formatDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(time);
    }

    public static String format(long time) {
        String mStr, sStr;
        long s = time / 1000;
        int m = (int) (s / 60);
        s = s - m * 60;
        if (m < 10) {
            mStr = "0" + m;
        } else {
            mStr = String.valueOf(m);
        }
        if (s < 10) {
            sStr = "0" + s;
        } else {
            sStr = String.valueOf(s);
        }
        return mStr + ":" + sStr;
    }

    public static long getTodayStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        return todayStart.getTime().getTime();
    }

}
