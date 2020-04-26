package com.ellen.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.utils.ContentProviderUtils;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.musicplayer.utils.PermissionUtils;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        //设置无ActionBar效果
        StatusUtils.setNoActionBar(this);

        //申请文件读写权限
        PermissionUtils permissionUtils = new PermissionUtils(this);
        permissionUtils.startCheckFileReadWritePermission(0, new PermissionUtils.PermissionCallback() {
            @Override
            public void success() {
                List<Music> musicList = LocalSDMusicUtils.getLocalAllMusic(MainActivity.this);
                MusicAdapter musicAdapter = new MusicAdapter(MainActivity.this, musicList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(musicAdapter);
            }

            @Override
            public void failure() {

            }
        });
    }
}
