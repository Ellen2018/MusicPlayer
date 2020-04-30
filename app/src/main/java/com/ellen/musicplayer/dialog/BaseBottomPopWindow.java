package com.ellen.musicplayer.dialog;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
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

}
