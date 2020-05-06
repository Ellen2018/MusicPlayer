package com.ellen.musicplayer.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import com.ellen.musicplayer.adapter.SxAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.PermissionUtils;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.ArrayList;
import java.util.List;

public class DanQuFragment extends BaseFragment implements BaseFragment.LazyLoadInterface {

    private RecyclerView recyclerView;
    private PermissionUtils permissionUtils;
    private MusicAdapter musicAdapter;
    private ImageView ivDinWei;
    private BaseEvent baseEvent;
    private List<Music> musicList;
    private RecyclerView recyclerViewSx;

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewSx = findViewById(R.id.recycler_view_sy);
        recyclerViewSx.setVisibility(View.GONE);
        List<String> stringList = new ArrayList<>();
        for (int i = 65; i < 91; i++) {
            char c = (char) i;
            stringList.add(String.valueOf(c));
        }

        LinearLayoutManager linearLayoutManager;
        recyclerViewSx.setLayoutManager(linearLayoutManager = new LinearLayoutManager(getContext()));
        SxAdapter sxAdapter;
        recyclerViewSx.setAdapter(sxAdapter = new SxAdapter(getActivity(), stringList));
        sxAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                ToastUtils.toast(getActivity(),"点击了"+stringList.get(position));
                char c1 = stringList.get(position).charAt(0);
                char c2 = (char) (c1 + 32);
                for (int i = 0; i < musicAdapter.getDataList().size(); i++) {
                    String musicNamePy = musicAdapter.getDataList().get(i).getPyName();
                    char c = musicNamePy.charAt(0);
                    if (c == c1 || c == c2) {
                        recyclerView.scrollToPosition(i);
                        break;
                    }
                }
            }
        });
    }

    public void updateUi() {
        new Sender<List<Music>>() {
            @Override
            protected void handlerInstruction(SenderController<List<Music>> senderController) {
                if (musicList == null) {
                    musicList = LocalSDMusicUtils.getLocalAllMusic(getActivity());
                    senderController.sendMessageToNext(musicList);
                }
            }
        }.runOn(RunMode.NEW_THREAD)
                .setReceiver(new Receiver() {
                    @Override
                    protected void handleMessage(Object message) {
                        List<Music> musicList = (List<Music>) message;
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        musicAdapter = new MusicAdapter(getActivity(), recyclerView, musicList);
                        musicAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                                //开始播放
                                MediaPlayerManager.getInstance().open(position, musicList);
                            }
                        });
                        musicAdapter.setOnItemLongClickListener(new BaseRecyclerViewAdapter.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(BaseViewHolder baseViewHolder, int position) {
                                JumpSortUtils.jumpToMusicList(getActivity(), musicList);
                                return true;
                            }
                        });
                        recyclerView.setAdapter(musicAdapter);
                    }

                    @Override
                    protected void handleErrMessage(Throwable throwable) {

                    }

                    @Override
                    protected void complete() {

                    }
                }).runOn(RunMode.MAIN_THREAD).start();

        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                musicAdapter.notifyDataSetChanged();
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID, baseEvent);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void lazyLoad() {
        updateUi();
    }
}
