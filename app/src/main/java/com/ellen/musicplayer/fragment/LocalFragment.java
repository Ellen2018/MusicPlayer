package com.ellen.musicplayer.fragment;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.PermissionUtils;

import java.util.List;

public class LocalFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private PermissionUtils permissionUtils;

    @Override
    protected void initData() {
        //申请文件读写权限
        permissionUtils = new PermissionUtils(getActivity());
        permissionUtils.startCheckFileReadWritePermission(0, new PermissionUtils.PermissionCallback() {
            @Override
            public void success() {
                //发送消息去扫描本地所有歌曲
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                List<Music> musicList = LocalSDMusicUtils.getLocalAllMusic(getActivity());
                MusicAdapter musicAdapter = new MusicAdapter(getActivity(),musicList);
                recyclerView.setAdapter(musicAdapter);
            }

            @Override
            public void failure() {
            }
        });
    }

    @Override
    protected void initView() {
       recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_local;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
