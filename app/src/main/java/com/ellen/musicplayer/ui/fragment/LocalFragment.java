package com.ellen.musicplayer.ui.fragment;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.PermissionUtils;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.List;

public class LocalFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private PermissionUtils permissionUtils;
    private MusicAdapter musicAdapter;
    private ImageView ivDinWei;
    private BaseEvent baseEvent;

    @Override
    protected void initData() {
        //申请文件读写权限
        permissionUtils = new PermissionUtils(getActivity());
        permissionUtils.startCheckFileReadWritePermission(0, new PermissionUtils.PermissionCallback() {
            @Override
            public void success() {
                //发送消息去扫描本地所有歌曲
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                final List<Music> musicList = LocalSDMusicUtils.getLocalAllMusic(getActivity());
                musicAdapter = new MusicAdapter(getActivity(), musicList);
                recyclerView.setAdapter(musicAdapter);
                musicAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView.ViewHolder viewHolder, int position) {
                        //开始播放
                        MediaPlayerManager.getInstance().open(position, musicList);
                    }
                });
            }

            @Override
            public void failure() {
            }
        });

        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                musicAdapter.notifyDataSetChanged();
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        ivDinWei = findViewById(R.id.iv_din_wei);
        ivDinWei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行定位
                for (int i = 0; i < musicAdapter.getDataList().size(); i++) {
                    if (musicAdapter.getDataList().get(i).getPath()
                            .equals(MediaPlayerManager.getInstance().currentOpenMusic().getPath())) {
                        recyclerView.scrollToPosition(i);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_local;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
