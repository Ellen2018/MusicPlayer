package com.ellen.musicplayer.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.GeDanAdapter;
import com.ellen.musicplayer.adapter.GeDanManagerAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.GeDanMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.sqlitecreate.createsql.order.Order;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.ArrayList;
import java.util.List;

public class GeDanManagerActivity extends BaseMediaPlayerActivity {

    private List<GeDan> geDanList;
    private ImageView ivBack;
    private RecyclerView recyclerView;
    private RelativeLayout rl;
    private BaseEvent baseEvent;
    private GeDanManagerAdapter geDanManagerAdapter;

    @Override
    protected void update() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_ge_dan_manager;
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.iv_back);
        rl = findViewById(R.id.rl);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void initData() {
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                geDanList.clear();
                geDanList.addAll(SQLManager.getInstance().getGeDanTable()
                        .getAllDatas(Order.getInstance(false)
                                .setFirstOrderFieldName("geDanSqlTableName")
                                .setIsDesc(true).createSQL()));
                geDanManagerAdapter.notifyDataSetChanged();
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.GE_DAN_ID,baseEvent);
        geDanList = SQLManager.getInstance().getGeDanTable()
                .getAllDatas(Order.getInstance(false)
                        .setFirstOrderFieldName("geDanSqlTableName")
                        .setIsDesc(true).createSQL());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(geDanManagerAdapter = new GeDanManagerAdapter(this,rl, geDanList));
        geDanManagerAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                List<GeDanMusic> geDanMusicList = SQLManager.getInstance().getGeDanMusicListByName(geDanList.get(position));
                List<Music> musicList = new ArrayList<>();
                for(GeDanMusic geDanMusic:geDanMusicList){
                    musicList.add(geDanMusic.getMusic());
                }
                JumpSortUtils.jumpToSort(GeDanManagerActivity.this,"歌单",geDanList.get(position).getGeDanName(),musicList);
            }
        });
    }

    @Override
    protected void destory() {
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.GE_DAN_ID,baseEvent);
    }
}
