package com.ellen.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.fragment.LocalFragment;
import com.ellen.musicplayer.fragment.MyFragment;
import com.ellen.musicplayer.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.notification.MusicNotification;
import com.ellen.musicplayer.utils.GaoShiUtils;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvTabOne,tvTabTwo,tvMusicName,tvSingerName;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private DrawerLayout drawerLayout;
    private ImageView ivUser,ivSerach,ivPlayerIcon;
    private ImageView ivPlayerBg,ivPlayerNext,ivPlayerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无ActionBar效果
        StatusUtils.setNoActionBar(this);
        StatusUtils.setTranslucentStatus(this);
        setContentView(R.layout.activity_main);
        fragmentList = new ArrayList<>();
        fragmentList.add(new LocalFragment());
        fragmentList.add(new MyFragment());
        initView();

        //走马灯设置
        tvMusicName.setSelected(true);
        tvSingerName.setSelected(true);

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        ivPlayerNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayerManager.getInstance().next();
            }
        });

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
               return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    tvTabOne.setTextColor(Color.WHITE);
                    tvTabTwo.setTextColor(Color.GRAY);
                }else {
                    tvTabOne.setTextColor(Color.GRAY);
                    tvTabTwo.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        EventBus.getDefault().register(this);
    }

    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        viewPager = findViewById(R.id.view_pager);
        tvTabOne = findViewById(R.id.tv_tab_1);
        tvTabTwo = findViewById(R.id.tv_tab_2);
        ivUser = findViewById(R.id.iv_user);
        ivSerach = findViewById(R.id.iv_serach);
        ivPlayerIcon = findViewById(R.id.iv_player_icon);
        tvMusicName = findViewById(R.id.tv_music_name);
        tvSingerName = findViewById(R.id.tv_singer_name);
        ivPlayerBg = findViewById(R.id.iv_player_bg);
        ivPlayerNext = findViewById(R.id.iv_player_next);
        ivPlayerList = findViewById(R.id.iv_player_list);
        tvTabOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        tvTabTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void openMusic(Music music) {

        //设置歌曲图片
        Bitmap bitmap = MediaPlayerManager.getInstance().getCurrentOpenMusicBitmap(this);
        if(bitmap == null){
            //设置默认图片
            ivPlayerIcon.setImageResource(R.mipmap.default_music_icon);
            ivPlayerBg.setImageResource(R.mipmap.default_bg);
        }else {
            ivPlayerIcon.setImageBitmap(bitmap);
            ivPlayerBg.setImageBitmap(MediaPlayerManager.getInstance().getGaoShiBitmap(this));
        }


        //设置歌曲名和歌手名
        tvMusicName.setText(MediaPlayerManager.getInstance().currentOpenMusic().getName());
        tvSingerName.setText(MediaPlayerManager.getInstance().currentOpenMusic().getArtist());

        //发送通知
//        MusicNotification musicNotification = new MusicNotification(this);
//        musicNotification.showNotification();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = fragmentList.get(viewPager.getCurrentItem());
        fragment.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
