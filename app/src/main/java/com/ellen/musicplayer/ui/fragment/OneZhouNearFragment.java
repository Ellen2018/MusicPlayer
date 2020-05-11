package com.ellen.musicplayer.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.NearDateMusicAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.NearMusic;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.sqlitecreate.createsql.order.Order;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.ArrayList;
import java.util.List;

public class OneZhouNearFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private List<NearMusic> nearMusicList;
    private BaseEvent baseEvent;
    private NearDateMusicAdapter nearMusicAdapter;

    @Override
    protected void initData() {
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                nearMusicList.clear();
                nearMusicList.addAll(SQLManager.getInstance().getNearMusicTable().getAllDatas(Order.getInstance(false)
                        .setFirstOrderFieldName("playTime")
                        .setIsDesc(true).createSQL()));
                nearMusicAdapter.notifyDataSetChanged();
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);
        nearMusicList = SQLManager.getInstance().getNearMusicTable().getAllDatas(Order.getInstance(false)
                .setFirstOrderFieldName("playTime")
                .setIsDesc(true).createSQL());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(nearMusicAdapter = new NearDateMusicAdapter(getActivity(),recyclerView,nearMusicList));
        nearMusicAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                List<Music> musicList = new ArrayList<>();
                for(NearMusic nearMusic:nearMusicList){
                    musicList.add(nearMusic.getMusic());
                }
                MediaPlayerManager.getInstance().open(position,musicList);
            }
        });
        nearMusicAdapter.setOnItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseViewHolder baseViewHolder, int position) {
                List<Music> musicList = new ArrayList<>();
                for(NearMusic nearMusic:nearMusicList){
                    musicList.add(nearMusic.getMusic());
                }
                JumpSortUtils.jumpToMusicList(getActivity(),musicList,false,null);
                return true;
            }
        });
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_near;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);
    }
}
