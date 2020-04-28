package com.ellen.musicplayer.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.adapter.SerachMusicAdapter;
import com.ellen.musicplayer.base.BaseActivity;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.notification.MusicNotification;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.ArrayList;
import java.util.List;

public class SerachActivity extends BaseActivity implements View.OnClickListener {

    //播放界面控件
    private TextView tvMusicName, tvSingerName;
    private ImageView ivPlayerBg, ivPlayerPause, ivPlayerList,ivPlayerIcon;
    private RelativeLayout rlPlayerMb;
    private BaseEvent baseEvent;

    private RecyclerView recyclerView;

    private ImageView ivBack,ivCancel;
    private EditText editText;

    private List<Music> serachMusicLists;
    private SerachMusicAdapter serachMusicAdapter;

    @Override
    protected void setStatus() {
        StatusUtils.setNoActionBar(this);
        StatusUtils.setTranslucentStatus(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_serach;
    }

    @Override
    protected void initView() {
        tvMusicName = findViewById(R.id.tv_music_name);
        tvSingerName = findViewById(R.id.tv_singer_name);
        ivPlayerBg = findViewById(R.id.iv_player_bg);
        ivPlayerPause = findViewById(R.id.iv_player_pause);
        ivPlayerList = findViewById(R.id.iv_player_list);
        rlPlayerMb = findViewById(R.id.rl_player_mb);
        ivPlayerIcon = findViewById(R.id.iv_player_icon);
        rlPlayerMb.setOnClickListener(this);
        ivPlayerPause.setOnClickListener(this);

        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view);
        editText = findViewById(R.id.edit_text);
        ivCancel = findViewById(R.id.iv_cancel);
        ivCancel.setOnClickListener(this);

        serachMusicAdapter = new SerachMusicAdapter(this,serachMusicLists = LocalSDMusicUtils.getLocalAllMusic(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        serachMusicAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
                MediaPlayerManager.getInstance().open(position,serachMusicLists);
            }
        });
        recyclerView.setAdapter(serachMusicAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString())){
                    ivCancel.setVisibility(View.GONE);
                }else {
                    ivCancel.setVisibility(View.VISIBLE);
                }
                //进行搜索
                serachMusicLists = LocalSDMusicUtils.serachMusics(SerachActivity.this,s.toString());
                serachMusicAdapter = new SerachMusicAdapter(SerachActivity.this,serachMusicLists);
                serachMusicAdapter.setSerachTag(s.toString());
                recyclerView.setLayoutManager(new LinearLayoutManager(SerachActivity.this));
                serachMusicAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
                        MediaPlayerManager.getInstance().open(position,serachMusicLists);
                    }
                });
                recyclerView.setAdapter(serachMusicAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        if(MediaPlayerManager.getInstance().checkCanPlay()) {
            updateUi(MediaPlayerManager.getInstance().currentOpenMusic());
        }
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                updateUi((Music) message.object);
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);
    }

    @Override
    protected void destory() {
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);
    }

    @Override
    protected Boolean isSetVerticalScreen() {
        return true;
    }

    private void updateUi(Music music){
        //设置歌曲图片
        Bitmap bitmap = MediaPlayerManager.getInstance().getCurrentOpenMusicBitmap(this);
        if (bitmap == null) {
            //设置默认图片
            ivPlayerIcon.setImageResource(R.mipmap.default_music_icon);
            ivPlayerBg.setImageResource(R.mipmap.default_bg);
        } else {
            ivPlayerIcon.setImageBitmap(bitmap);
            ivPlayerBg.setImageBitmap(MediaPlayerManager.getInstance().getGaoShiBitmap(this));
        }

        //更新播放/暂停状态
        if (MediaPlayerManager.getInstance().getMediaPlayer().isPlaying()) {
            ivPlayerPause.setImageResource(R.mipmap.play);
        } else {
            ivPlayerPause.setImageResource(R.mipmap.pause);
        }

        //设置歌曲名和歌手名
        tvMusicName.setText(MediaPlayerManager.getInstance().currentOpenMusic().getName());
        tvSingerName.setText(MediaPlayerManager.getInstance().currentOpenMusic().getArtist());

        //发送通知
        MusicNotification musicNotification = new MusicNotification(this);
        musicNotification.showNotification();

        serachMusicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_player_mb:
                Intent intent = new Intent(SerachActivity.this,PlayActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_player_pause:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().pauseAndPlay();
                } else {

                }
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cancel:
                editText.setText("");
                break;
        }
    }
}
