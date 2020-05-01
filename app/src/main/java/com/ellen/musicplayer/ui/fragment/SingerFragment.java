package com.ellen.musicplayer.ui.fragment;

import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.library.library.runmode.RunMode;
import com.ellen.library.library.serial.Receiver;
import com.ellen.library.library.serial.Sender;
import com.ellen.library.library.serial.commoninterface.sender.SenderController;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.adapter.SingerAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.Singer;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.ui.activity.SortActivity;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.ToastUtils;

import java.util.List;

public class SingerFragment extends BaseFragment {

    private List<Singer> singerList;
    private RecyclerView recyclerView;
    private SingerAdapter singerAdapter;

    @Override
    protected void initData() {
        new Sender<List<Singer>>(){
            @Override
            protected void handlerInstruction(SenderController<List<Singer>> senderController) {
                singerList = LocalSDMusicUtils.getArtist(getActivity());
                senderController.sendMessageToNext(singerList);
            }
        }.runOn(RunMode.NEW_THREAD)
                .setReceiver(new Receiver<List<Singer>>() {
                    @Override
                    protected void handleMessage(List<Singer> message) {
                        List<Singer> singerList = message;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(singerAdapter = new SingerAdapter(getActivity(),recyclerView,singerList));
                        singerAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                                JumpSortUtils.jumpToSort(
                                        getActivity()
                                        ,"歌手"
                                        ,singerList.get(position).getName()
                                        ,singerList.get(position).getMusicList());
                            }
                        });
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
    protected void initView() {
       recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_singer;
    }
}
