package com.ellen.musicplayer.notification;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseNotification;

public class MusicNotification extends BaseNotification {

    public MusicNotification(Context context) {
        super(context);
    }

    @Override
    protected int setNotificationLayoutId() {
        return R.layout.layout_notification;
    }

    @Override
    protected String setTicker() {
        return "null";
    }

    @Override
    protected void initView(RemoteViews remoteViews) {
       remoteViews.setTextViewText(R.id.tv_noti_music_name,"呵呵");
    }

    @Override
    protected int setIconResourecId() {
        return R.mipmap.app_icon;
    }

    @Override
    protected Boolean isAutoCancel() {
        return null;
    }

    @Override
    protected String setChannelName() {
        return "com.ellen.music";
    }

    @Override
    protected String setTitle() {
        return "dsadsa";
    }

    @Override
    protected String titleContent() {
        return "null";
    }

    @Override
    protected int setNotificationId() {
        return 1;
    }

    @Override
    protected Intent setNotificationIntent() {
        return null;
    }

    @Override
    protected void cancelBefore() {

    }
}
