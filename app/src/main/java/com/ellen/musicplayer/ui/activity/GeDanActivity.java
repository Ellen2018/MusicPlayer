package com.ellen.musicplayer.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.GeDanMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.PiFu;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.ui.dialog.CommonOkCancelDialog;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.supermessagelibrary.MessageManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GeDanActivity extends BaseMediaPlayerActivity implements View.OnClickListener {

    public static String GE_DAN_JSON = "ge_dan_name";

    private GeDan geDan;
    private List<Music> musicList;
    private MusicAdapter musicAdapter;

    private RecyclerView recyclerView;
    private RelativeLayout rl;
    private TextView tvTitle, tvContent;
    private ImageView ivBack;

    @Override
    protected int layoutId() {
        return R.layout.activity_sort;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handlerIntent(intent);
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        rl = findViewById(R.id.rl);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("歌单");
        tvContent = findViewById(R.id.tv_content);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        handlerIntent(getIntent());
    }

    @Override
    protected void destory() {

    }

    private void handlerIntent(Intent intent){
        geDan = new Gson().fromJson(intent.getStringExtra(GE_DAN_JSON),GeDan.class);
        tvContent.setText(geDan.getGeDanName());
        List<GeDanMusic> geDanMusicList = SQLManager.getInstance().getGeDanMusicListByName(geDan);
        musicList = new ArrayList<>();
        for(GeDanMusic geDanMusic:geDanMusicList){
            musicList.add(geDanMusic.getMusic());
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(musicAdapter = new MusicAdapter(this, rl, musicList));
        musicAdapter.setDeleteCallback(new MusicAdapter.DeleteCallback() {
            @Override
            public void delelte(Music music) {
                String tilte = "移除歌曲";
                String content = "确定从<"+geDan.getGeDanName()+">移除此歌曲吗?";
                CommonOkCancelDialog commonOkCancelDialog = new CommonOkCancelDialog(tilte, content, new CommonOkCancelDialog.Callback() {
                    @Override
                    public void ok() {
                        SQLManager.getInstance().deleteMusicFromGeDan(geDan,music);
                        List<GeDanMusic> geDanMusicList = SQLManager.getInstance().getGeDanMusicListByName(geDan);
                        musicList.clear();
                        for(GeDanMusic geDanMusic:geDanMusicList){
                            musicList.add(geDanMusic.getMusic());
                        }
                        musicAdapter.notifyDataSetChanged();
                        MessageManager.getInstance().sendEmptyMainThreadMessage(MessageTag.GE_DAN_ID);
                    }

                    @Override
                    public void cancel() {

                    }
                });
                commonOkCancelDialog.show(getSupportFragmentManager(),"");
            }
        });
        musicAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                MediaPlayerManager.getInstance().open(position,musicList);
            }
        });
        musicAdapter.setOnItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseViewHolder baseViewHolder, int position) {
                JumpSortUtils.jumpToMusicList(GeDanActivity.this,musicList);
                return true;
            }
        });
    }

    @Override
    protected Boolean isSetVerticalScreen() {
        return true;
    }

    @Override
    protected void update() {
        musicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
