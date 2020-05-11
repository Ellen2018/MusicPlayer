package com.ellen.musicplayer.ui.dialog;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BasePopwindow;

public class ManySelectorMusicDialog extends BasePopwindow {

    public ManySelectorMusicDialog(Activity activity) {
        super(activity);
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_many_selector_music,null);
        return view;
    }

    @Override
    protected int setWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected int setHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
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
    protected void setOtherSetting(PopupWindow popupWindow) {

    }

    @Override
    protected Boolean isSetShowBackgroundBlack() {
        return false;
    }

    @Override
    protected void dismissBefore() {

    }

    @Override
    protected void showBefore() {

    }
}
