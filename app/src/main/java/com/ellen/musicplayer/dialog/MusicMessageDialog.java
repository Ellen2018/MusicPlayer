package com.ellen.musicplayer.dialog;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.PopupWindow;

import com.ellen.musicplayer.base.BasePopwindow;

public class MusicMessageDialog extends BasePopwindow {

    public MusicMessageDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected View onCreateView() {
        return null;
    }

    @Override
    protected int setWidth() {
        return 0;
    }

    @Override
    protected int setHeight() {
        return 0;
    }

    @Override
    protected boolean isGetFocus() {
        return false;
    }

    @Override
    protected Boolean isSetBackgroundDrawable() {
        return null;
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
        return false;
    }

    @Override
    protected void setOtherSetting(PopupWindow popupWindow) {

    }

    @Override
    protected Boolean isSetShowBackgroundBlack() {
        return null;
    }

    @Override
    protected void dismissBefore() {

    }
}
