package com.ellen.musicplayer;

import com.ellen.musicplayer.base.BaseApplication;
import com.ellen.musicplayer.bean.NearMusic;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.message.MusicPlay;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;
import com.tencent.mmkv.MMKV;

public class App extends BaseApplication {

    private BaseEvent baseEvent;

    @Override
    public void onCreate() {
        super.onCreate();
        SQLManager.getInstance().initLibrary(getApplicationContext());
        MMKV.initialize(App.this);
        SQLManager.getInstance();
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                //存储最近播放的歌曲
                MusicPlay musicPlay = (MusicPlay) message.object;
                if(musicPlay.isQieHuan()) {
                    NearMusic nearMusic = new NearMusic();
                    nearMusic.setMusic(musicPlay.getMusic());
                    nearMusic.setPlayTime(System.currentTimeMillis());
                    SQLManager.getInstance().getNearMusicTable().saveData(nearMusic);
                }
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);
    }

    @Override
    protected Boolean isListenerActivity() {
        return false;
    }

    @Override
    protected void initLibraySetting() {

    }
}
