package com.ellen.musicplayer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ellen.musicplayer.base.BaseApplication;
import com.ellen.musicplayer.bean.NearMusic;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.message.MusicPlay;
import com.ellen.sqlitecreate.createsql.helper.WhereSymbolEnum;
import com.ellen.sqlitecreate.createsql.where.Where;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;
import com.tencent.mmkv.MMKV;

public class App extends Application {

    private BaseEvent baseEvent;
    private Activity activity;

    public Activity getActivity() {
        return activity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SQLManager.getInstance().initLibrary(getApplicationContext());
        MMKV.initialize(App.this);
        SQLManager.getInstance();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                App.this.activity = activity;
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                //存储最近播放的歌曲
                MusicPlay musicPlay = (MusicPlay) message.object;
                if (musicPlay.isQieHuan()) {
                    //此首歌是否已经存在于最近歌单中
                    if (!SQLManager.getInstance().isContansNearMusic(musicPlay.getMusic())) {
                        NearMusic nearMusic = new NearMusic();
                        nearMusic.setMusic(musicPlay.getMusic());
                        nearMusic.setNearTag(musicPlay.getMusic().getWeiOneTag());
                        nearMusic.setPlayTime(System.currentTimeMillis());
                        nearMusic.setPlayTimes(1);
                        SQLManager.getInstance().getNearMusicTable().saveData(nearMusic);
                    } else {
                        NearMusic nearMusic = SQLManager.getInstance().getNearMusicByTag(musicPlay.getMusic().getWeiOneTag());
                        nearMusic.setPlayTime(System.currentTimeMillis());
                        nearMusic.setPlayTimes(nearMusic.getPlayTimes() + 1);
                        SQLManager.getInstance().getNearMusicTable().update(nearMusic, Where
                                .getInstance(false)
                                .addAndWhereValue("nearTag", WhereSymbolEnum.EQUAL, musicPlay.getMusic().getWeiOneTag())
                                .createSQL());
                    }
                    MessageManager.getInstance().sendEmptyMainThreadMessage(MessageTag.NEAR_ID);
                }
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID, baseEvent);
    }
}
