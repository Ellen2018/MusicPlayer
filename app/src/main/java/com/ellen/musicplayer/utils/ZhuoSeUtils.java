package com.ellen.musicplayer.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

public class ZhuoSeUtils {

    public static Spannable getSpannable(String content,String serachTag){
        Spannable spannable = new SpannableString(content);
        int position = content.indexOf(serachTag);
        spannable.setSpan(new ForegroundColorSpan(Color.RED), position, position + serachTag.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

}
