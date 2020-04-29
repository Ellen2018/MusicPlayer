package com.ellen.musicplayer.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.youth.banner.Banner;

public class ZhengFangBanner extends Banner {

    public ZhengFangBanner(@NonNull Context context) {
        super(context);
    }

    public ZhengFangBanner(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public ZhengFangBanner(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
