package com.ellen.musicplayer.ui.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseActivity;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.mediaplayer.PlayMode;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

public class PlayActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvMusicName, tvSingerName, tvMusicName1, tvSingerName1, tvAlbumName;
    private ImageView ivBack, ivShare, ivBg, ivMusicIcon, ivPre, ivNext, ivPause,ivPlayMode;
    private BaseEvent baseEvent;

    @Override
    protected void setStatus() {
        StatusUtils.setNoActionBar(this);
        StatusUtils.setTranslucentStatus(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    protected void initView() {
        tvMusicName = findViewById(R.id.tv_music_name);
        tvSingerName = findViewById(R.id.tv_singer_name);
        ivBack = findViewById(R.id.iv_back);
        ivShare = findViewById(R.id.iv_share);
        ivMusicIcon = findViewById(R.id.iv_music_icon);
        ivPre = findViewById(R.id.iv_play_pre);
        ivPause = findViewById(R.id.iv_play_pause);
        ivNext = findViewById(R.id.iv_play_next);
        tvMusicName1 = findViewById(R.id.tv_music_name_1);
        tvSingerName1 = findViewById(R.id.tv_singer_name_1);
        tvAlbumName = findViewById(R.id.tv_album_name);
        ivPlayMode = findViewById(R.id.iv_play_mode);
        ivBg = findViewById(R.id.iv_bg);
        tvMusicName.setSelected(true);
        tvSingerName.setSelected(true);
        tvMusicName1.setSelected(true);
        tvSingerName1.setSelected(true);
        tvAlbumName.setSelected(true);
        ivBack.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivPre.setOnClickListener(this);
        ivPause.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivPlayMode.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (MediaPlayerManager.getInstance().checkCanPlay()) {
            updateUi(MediaPlayerManager.getInstance().currentOpenMusic());
        }
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                updateUi((Music) message.object);
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID, baseEvent);
    }

    @Override
    protected void destory() {
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.OPEN_MUSIC_ID, baseEvent);
    }

    @Override
    protected Boolean isSetVerticalScreen() {
        return true;
    }

    private void updateUi(Music music) {
        tvMusicName.setText(music.getName());
        tvSingerName.setText(music.getArtist());
        tvMusicName1.setText(music.getName());
        tvSingerName1.setText(music.getArtist());
        tvAlbumName.setText(music.getAlbum());
        Bitmap bitmap = MediaPlayerManager.getInstance().getCurrentOpenMusicBitmap(this);
        if (bitmap == null) {
            //显示默认
            ivBg.setImageResource(R.mipmap.default_bg);
        } else {
            ivMusicIcon.setImageBitmap(bitmap);
            ivBg.setImageBitmap(MediaPlayerManager.getInstance().getGaoShiBitmap(this));
        }

        //更新播放/暂停状态
        if (MediaPlayerManager.getInstance().getMediaPlayer().isPlaying()) {
            ivPause.setImageResource(R.mipmap.play_start);
        } else {
            ivPause.setImageResource(R.mipmap.play_pause);
        }

        //更新播放模式
        PlayMode playMode = MediaPlayerManager.getInstance().getPlayMode();
        if(playMode == PlayMode.XUN_HUAN){
            ivPlayMode.setImageResource(R.mipmap.playmode_xun_huan);
        }else if(playMode == PlayMode.SUI_JI){
            ivPlayMode.setImageResource(R.mipmap.playmode_sui_ji);
        }else {
            ivPlayMode.setImageResource(R.mipmap.playmode_dan_qu);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_play_pre:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().pre();
                }
                break;
            case R.id.iv_play_pause:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().pauseAndPlay();
                }
                break;
            case R.id.iv_play_next:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().next();
                }
                break;
            case R.id.iv_play_mode:
                MediaPlayerManager.getInstance().adjustPlayMode();
                PlayMode playMode = MediaPlayerManager.getInstance().getPlayMode();
                if(playMode == PlayMode.XUN_HUAN){
                    ivPlayMode.setImageResource(R.mipmap.playmode_xun_huan);
                }else if(playMode == PlayMode.SUI_JI){
                    ivPlayMode.setImageResource(R.mipmap.playmode_sui_ji);
                }else {
                    ivPlayMode.setImageResource(R.mipmap.playmode_dan_qu);
                }
                break;
        }
    }
}
