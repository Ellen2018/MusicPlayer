package com.ellen.musicplayer.notification;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseNotification;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.ui.activity.PlayActivity;

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
        Bitmap bitmap = MediaPlayerManager.getInstance().getCurrentOpenMusicBitmap(getContext());
        if(bitmap != null) {
            remoteViews.setImageViewBitmap(R.id.iv_music_icon,bitmap );
            remoteViews.setImageViewBitmap(R.id.iv_bg, MediaPlayerManager.getInstance().getGaoShiBitmap(getContext()));
        }else {
            remoteViews.setImageViewResource(R.id.iv_music_icon,R.mipmap.default_music_icon );
            remoteViews.setImageViewResource(R.id.iv_bg,R.mipmap.default_bg);
        }
        remoteViews.setTextViewText(R.id.tv_music_name, MediaPlayerManager.getInstance().currentOpenMusic().getName());
        remoteViews.setTextViewText(R.id.tv_singer_name, MediaPlayerManager.getInstance().currentOpenMusic().getArtist());

        //更新播放/暂停状态
        if (MediaPlayerManager.getInstance().getMediaPlayer().isPlaying()) {
            remoteViews.setImageViewResource(R.id.iv_pause,R.mipmap.play);
        }else {
            remoteViews.setImageViewResource(R.id.iv_pause,R.mipmap.pause);
        }

        //设置点击事件
        remoteViews.setOnClickPendingIntent(R.id.iv_pause, getBroadcastIntent(1,ACTION_PAUSE));//点击的id，点击事件
        remoteViews.setOnClickPendingIntent(R.id.iv_next, getBroadcastIntent(2,ACTION_NEXT));//点击的id，点击事件
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
        Intent intent = new Intent(getContext(), PlayActivity.class);
        return intent;
    }

    @Override
    protected void cancelBefore() {

    }
}
