package com.ellen.musicplayer.dialog;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BasePopwindow;

public abstract class BaseBottomPopWindow extends BasePopwindow {

    public BaseBottomPopWindow(Activity activity) {
        super(activity);
    }

    @Override
    protected void setOtherSetting(PopupWindow popupWindow) {
        popupWindow.setAnimationStyle(R.style.popwindow_anim_style);
    }

    @Override
    protected int setWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected int setHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }


    @Override
    protected boolean isGetFocus() {
        return true;
    }

    @Override
    protected Boolean isSetBackgroundDrawable() {
        return false;
    }

    @Override
    protected Drawable setBackgroundDrawable() {
        return null;
    }

    @Override
    protected boolean isResponseOutsideTouchable() {
        return false;
    }

    @Override
    protected boolean isResponseTouchable() {
        return true;
    }

    @Override
    protected Boolean isSetShowBackgroundBlack() {
        return true;
    }

    @Override
    protected void dismissBefore() {
    }

}
