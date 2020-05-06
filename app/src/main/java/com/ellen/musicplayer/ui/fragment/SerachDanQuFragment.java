package com.ellen.musicplayer.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.library.library.runmode.RunMode;
import com.ellen.library.library.serial.Receiver;
import com.ellen.library.library.serial.Sender;
import com.ellen.library.library.serial.commoninterface.sender.SenderController;
import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.SerachMessage;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.PermissionUtils;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.List;

public class SerachDanQuFragment extends BaseFragment implements BaseFragment.LazyLoadInterface {

    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    private BaseEvent baseEvent;
    private List<Music> musicList;
    private String currentSerachTag = null,newSerachTag = null;
    private BaseEvent serachEvent;
    private TextView tvSerachIng;
    private boolean isFirstLoadData = false;

    @Override
    protected void initData() {
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                musicAdapter.notifyDataSetChanged();
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID, baseEvent);
        updateUi(newSerachTag);
        isFirstLoadData = true;
    }

    public void setNewSerachTag(String serachTag) {
        if(isVisibleToUser){
            this.newSerachTag = serachTag;
            updateUi(serachTag);
        }else {
            this.newSerachTag = serachTag;
        }
    }

    @Override
    protected void initView() {
        tvSerachIng = findViewById(R.id.tv_s_tips);
        recyclerView = findViewById(R.id.recycler_view);
    }

    public void updateUi(String newSerachTag){
        if(newSerachTag == null){
            currentSerachTag = "";
            tvSerachIng.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            //请求数据
            startSerach();
        }else {
            if(!newSerachTag.equals(currentSerachTag)) {
                currentSerachTag = newSerachTag;
                tvSerachIng.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                //请求数据
                startSerach();
            }else {
                //不需要显示
                tvSerachIng.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void startSerach(){
        new Sender<List<Music>>() {
            @Override
            protected void handlerInstruction(SenderController<List<Music>> senderController) {
                List<Music> musicList = LocalSDMusicUtils.serachMusics(getActivity(),currentSerachTag);
                senderController.sendMessageToNext(musicList);
            }
        }.runOn(RunMode.NEW_THREAD)
                .setReceiver(new Receiver<List<Music>>() {
                    @Override
                    protected void handleMessage(List<Music> message) {
                        List<Music> musicList = message;
                        if(musicAdapter == null){
                            SerachDanQuFragment.this.musicList = musicList;
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            musicAdapter = new MusicAdapter(getActivity(),recyclerView,SerachDanQuFragment.this.musicList);
                            musicAdapter.setSerachTag(currentSerachTag);
                            recyclerView.setAdapter(musicAdapter);
                            musicAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                                    MediaPlayerManager.getInstance().open(position,musicAdapter.getDataList());
                                }
                            });
                            musicAdapter.setOnItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(BaseViewHolder baseViewHolder, int position) {
                                    JumpSortUtils.jumpToMusicList(getActivity(),musicAdapter.getDataList());
                                    return true;
                                }
                            });
                        }else {
                            musicAdapter.getDataList().clear();
                            musicAdapter.getDataList().addAll(musicList);
                            musicAdapter.setSerachTag(currentSerachTag);
                            musicAdapter.notifyDataSetChanged();
                        }
                        tvSerachIng.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected void handleErrMessage(Throwable throwable) {

                    }

                    @Override
                    protected void complete() {

                    }
                }).runOn(RunMode.MAIN_THREAD).start();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.OPEN_MUSIC_ID, baseEvent);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_dan_qu;
    }

    @Override
    public void lazyLoad() {
        if(isFirstLoadData) {
            updateUi(newSerachTag);
        }
    }
}
