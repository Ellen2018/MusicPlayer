package com.ellen.musicplayer.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;

import java.util.List;

public class SortActivity extends BaseMediaPlayerActivity implements View.OnClickListener {

    public static String SORT_TITLE = "sort_title";
    public static String SORT_CONTENT = "sort_content";
    public static String SORT_MUSIC_LIST = "sort_music_list";

    private String titileName;
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
        handlerIntent(intent);
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        rl = findViewById(R.id.rl);
        tvTitle = findViewById(R.id.tv_title);
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
        titileName = intent.getStringExtra(SORT_TITLE);
        content = intent.getStringExtra(SORT_CONTENT);
        musicList = (List<Music>) intent.getSerializableExtra(SORT_MUSIC_LIST);
        tvContent.setText(content);
        tvTitle.setText(titileName);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(musicAdapter = new MusicAdapter(this, rl, musicList));
        musicAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                MediaPlayerManager.getInstance().open(position,musicList);
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
