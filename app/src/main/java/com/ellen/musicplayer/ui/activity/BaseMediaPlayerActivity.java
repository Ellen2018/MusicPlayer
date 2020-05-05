package com.ellen.musicplayer.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseActivity;
import com.ellen.musicplayer.bean.PiFu;
import com.ellen.musicplayer.ui.dialog.PlayListDialog;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.manager.pifu.PiFuManager;
import com.ellen.musicplayer.message.MusicPlay;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

/**
 * 播放面板类，方便快速搭建底部播放面板界面
 */
public abstract class BaseMediaPlayerActivity extends BaseActivity {

    protected ImageView ivMusicIcon, ivBg, ivPause, ivPlayList;
    protected TextView tvMusicName, tvSingerName;
    protected BaseEvent baseEvent;
    protected RelativeLayout rlMainBan;
    protected ImageView ivPiFuIcon;
    private RelativeLayout rlContentView;
    private  View contentView;
    private  BaseEvent baseEventLike,baseEventGeDan;

    @Override
    protected void setStatus() {
        StatusUtils.setNoActionBar(this);
        StatusUtils.setTranslucentStatus(this);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        T t = super.findViewById(id);
        if(t == null){
            t = contentView.findViewById(id);
        }
        return t;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(baseEventLike != null){
            MessageManager.getInstance().unRegisterMessageEvent(MessageTag.LIKE_ID,baseEventLike);
        }
        if(baseEventGeDan != null){
            MessageManager.getInstance().unRegisterMessageEvent(MessageTag.GE_DAN_ID,baseEventGeDan);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        if(layoutId() != 0) {
            rlContentView = findViewById(R.id.rl_layout);
            contentView = LayoutInflater.from(this).inflate(layoutId(), null);
            rlContentView.addView(contentView);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //控件绑定
        rlMainBan = findViewById(R.id.rl_player_mb);
        ivMusicIcon = findViewById(R.id.iv_player_icon);
        tvMusicName = findViewById(R.id.tv_music_name);
        tvSingerName = findViewById(R.id.tv_singer_name);
        ivPause = findViewById(R.id.iv_player_pause);
        ivPlayList = findViewById(R.id.iv_player_list);
        ivBg = findViewById(R.id.iv_player_bg);
        ivPiFuIcon = findViewById(R.id.iv_pi_fu_icon);
        tvMusicName.setSelected(true);

        rlMainBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseMediaPlayerActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });

        ivPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().pauseAndPlay();
                }
            }
        });

        ivPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayListDialog playListDialog = new PlayListDialog(BaseMediaPlayerActivity.this);
                playListDialog.showAtLocation(rlMainBan, Gravity.BOTTOM, 0, 0);
            }
        });

        //注册消息事件
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                //接收到播放事件
                MusicPlay musicPlay = (MusicPlay) message.object;
                updatePlayerMianUi(musicPlay);
                update();
            }

            @Override
            public FragmentActivity bindActivity() {
                return BaseMediaPlayerActivity.this;
            }
        };

        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID, baseEvent);

        updatePlayerMianUi(null);
        updatePiFu(PiFuManager.getInstance().getPiFu());
    }


    protected abstract void update();

    @Override
    protected int setLayoutId() {
        return R.layout.activity_base;
    }

    protected void updatePlayerMianUi(MusicPlay musicPlay) {
        if (musicPlay != null && musicPlay.isClear()) {
            //恢复至默认
            rlMainBan.setVisibility(View.GONE);
            ivMusicIcon.setImageResource(R.mipmap.default_music_icon);
            ivBg.setImageResource(R.mipmap.default_bg);
            tvSingerName.setText("歌手名");
            tvMusicName.setText("歌曲名");
            ivPause.setImageResource(R.mipmap.pause);
        } else {
            if (musicPlay == null || musicPlay.isQieHuan()) {
                if(MediaPlayerManager.getInstance().checkCanPlay()) {
                    rlMainBan.setVisibility(View.VISIBLE);
                    //设置歌曲名和歌手名
                    tvMusicName.setText(MediaPlayerManager.getInstance().currentOpenMusic().getName());
                    tvSingerName.setText(MediaPlayerManager.getInstance().currentOpenMusic().getArtist());

                    //设置歌曲图片
                    Bitmap bitmap = MediaPlayerManager.getInstance().getCurrentOpenMusicBitmap(this);
                    if (bitmap == null) {
                        //设置默认图片
                        ivMusicIcon.setImageResource(R.mipmap.default_music_icon);
                        ivBg.setImageResource(R.mipmap.default_bg);
                    } else {
                        ivMusicIcon.setImageBitmap(bitmap);
                        ivBg.setImageBitmap(MediaPlayerManager.getInstance().getGaoShiBitmap(this));
                    }
                }
            }

            //更新播放/暂停状态
            if (MediaPlayerManager.getInstance().getMediaPlayer().isPlaying()) {
                ivPause.setImageResource(R.mipmap.play);
            } else {
                ivPause.setImageResource(R.mipmap.pause);
            }

        }

    }

    private void updatePiFu(PiFu piFu) {
        if (piFu != null) {
            if (piFu.isGuDinPiFu()) {
                ivPiFuIcon.setImageResource(piFu.getPiFuIconId());
            } else {
                //使用Glide加载本地图片
                Glide.with(this).load(piFu.getImagePath()).into(ivPiFuIcon);
            }
        }
    }

    @Override
    protected Boolean isSetVerticalScreen() {
        return true;
    }

    protected void registerLike(BaseEvent baseEvent){
        this.baseEventLike = baseEvent;
        MessageManager.getInstance().registerMessageEvent(MessageTag.LIKE_ID,baseEvent);
    }

    protected void registerGeDan(BaseEvent baseEvent){
        this.baseEventGeDan = baseEvent;
        MessageManager.getInstance().registerMessageEvent(MessageTag.GE_DAN_ID,baseEvent);
    }

    protected int layoutId(){
        return 0;
    }
}
