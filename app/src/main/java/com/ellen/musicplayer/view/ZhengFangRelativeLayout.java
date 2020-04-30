package com.ellen.musicplayer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ZhengFangRelativeLayout extends RelativeLayout {

    public ZhengFangRelativeLayout(Context context) {
        super(context);
    }

    public ZhengFangRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZhengFangRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ZhengFangRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}
