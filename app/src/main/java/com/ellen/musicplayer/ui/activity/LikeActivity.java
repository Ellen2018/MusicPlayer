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
import com.ellen.musicplayer.bean.GeDanMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.PiFu;
import com.ellen.musicplayer.dialog.CommonOkCancelDialog;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.manager.sql.GeDanMusicTable;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.sqlitecreate.createsql.order.Order;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.ArrayList;
import java.util.List;

public class LikeActivity extends BaseMediaPlayerActivity implements View.OnClickListener {

    private String content;
    private List<Music> musicList;
    private MusicAdapter musicAdapter;

    private RecyclerView recyclerView;
    private RelativeLayout rl;
    private TextView tvTitle, tvContent;
    private ImageView ivBack;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_sort;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        rl = findViewById(R.id.rl);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        tvTitle.setText("歌单");
        tvContent.setText("我喜欢");
    }

    @Override
    protected void initData() {
        registerLike(new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                GeDanMusicTable geDanMusicTable = SQLManager.getInstance().getLikeGeDanMusicTable();
                //获取最新喜欢的Music类
                List<GeDanMusic> geDanMusicList = geDanMusicTable.getAllDatas(Order.getInstance(false)
                        .setFirstOrderFieldName("likeTime")
                        .setIsDesc(true).createSQL());
                musicList.clear();
                if (geDanMusicList != null && geDanMusicList.size() > 0) {
                    for (GeDanMusic geDanMusic : geDanMusicList) {
                        musicList.add(geDanMusic.getMusic());
                    }
                }
                musicAdapter.notifyDataSetChanged();
            }
        });
        GeDanMusicTable geDanMusicTable = SQLManager.getInstance().getLikeGeDanMusicTable();
        //获取最新喜欢的Music类
        List<GeDanMusic> geDanMusicList = geDanMusicTable.getAllDatas(Order.getInstance(false)
                .setFirstOrderFieldName("likeTime")
                .setIsDesc(true).createSQL());

        if (geDanMusicList != null && geDanMusicList.size() > 0) {
            musicList = new ArrayList<>();
            for (GeDanMusic geDanMusic : geDanMusicList) {
                musicList.add(geDanMusic.getMusic());
            }
        } else {
            musicList = new ArrayList<>();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        musicAdapter = new MusicAdapter(this, rl, musicList);
        musicAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                MediaPlayerManager.getInstance().open(position, musicList);
            }
        });
        musicAdapter.setDeleteCallback(new MusicAdapter.DeleteCallback() {
            @Override
            public void delelte(Music music) {
                CommonOkCancelDialog commonOkCancelDialog = new CommonOkCancelDialog("移除歌曲", "确定从<我喜欢>列表中移除此歌曲？", new CommonOkCancelDialog.Callback() {
                    @Override
                    public void ok() {
                        //从喜欢列表中删除该歌曲
                        SQLManager.getInstance().removeLikeMusic(music);
                        MessageManager.getInstance().sendMainThreadMessage(MessageTag.LIKE_ID);
                        ToastUtils.toast(LikeActivity.this,"从<我喜欢>列表中移除此歌曲成功!");
                    }

                    @Override
                    public void cancel() {

                    }
                });
                commonOkCancelDialog.show(getSupportFragmentManager(),"");

            }
        });
        musicAdapter.setOnItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseViewHolder baseViewHolder, int position) {
                JumpSortUtils.jumpToMusicList(LikeActivity.this,musicList);
                return true;
            }
        });
        recyclerView.setAdapter(musicAdapter);
    }


    @Override
    protected void destory() {

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
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
