package com.ellen.musicplayer.notification;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.ellen.musicplayer.MainActivity;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseNotification;
import com.ellen.musicplayer.mediaplayer.MediaPlayerManager;

public class MusicNotification extends BaseNotification {

    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_NEXT = "action_next";

    public MusicNotification(Activity activity) {
        super(activity);
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
        remoteViews.setImageViewBitmap(R.id.iv_music_icon, MediaPlayerManager.getInstance().getCurrentOpenMusicBitmap(getContext()));
        remoteViews.setImageViewBitmap(R.id.iv_bg, MediaPlayerManager.getInstance().getGaoShiBitmap(getContext()));
        remoteViews.setTextViewText(R.id.tv_music_name, MediaPlayerManager.getInstance().currentOpenMusic().getName());
        remoteViews.setTextViewText(R.id.tv_singer_name, MediaPlayerManager.getInstance().currentOpenMusic().getArtist());

        //更新播放/暂停状态
        if (MediaPlayerManager.getInstance().getMediaPlayer().isPlaying()) {
            remoteViews.setImageViewResource(R.id.iv_pause,R.mipmap.play);
        }else {
            remoteViews.setImageViewResource(R.id.iv_pause,R.mipmap.pause);
        }

        //设置点击事件
        PendingIntent pauseIntent = PendingIntent.getBroadcast(getContext(), 1,
                new Intent(ACTION_PAUSE), PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent nextIntent = PendingIntent.getBroadcast(getContext(), 1,
                new Intent(ACTION_NEXT), PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.iv_pause, pauseIntent);//点击的id，点击事件
        remoteViews.setOnClickPendingIntent(R.id.iv_next, nextIntent);//点击的id，点击事件
    }

    @Override
    protected int setIconResourecId() {
        return R.mipmap.app_icon;
    }

    @Override
    protected Boolean isAutoCancel() {
        return false;
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
        Intent intent = new Intent(getContext(), MainActivity.class);
        return intent;
    }

    @Override
    protected void cancelBefore() {

    }
}
