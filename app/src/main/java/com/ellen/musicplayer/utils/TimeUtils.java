package com.ellen.musicplayer.utils;

import java.text.SimpleDateFormat;

public class TimeUtils {

    public static String format(int time){
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        return formatter.format(time);
    }

}
