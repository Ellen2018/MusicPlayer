package com.ellen.musicplayer.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.MenuAdapter;
import com.ellen.musicplayer.bean.Menu;
import com.ellen.musicplayer.message.MusicPlay;
import com.ellen.musicplayer.ui.fragment.LocalFragment;
import com.ellen.musicplayer.ui.fragment.MyFragment;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.notification.MusicNotification;
import com.ellen.musicplayer.utils.statusutil.StatusUtils;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvTabOne, tvTabTwo, tvMusicName, tvSingerName;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private DrawerLayout drawerLayout;
    private ImageView ivUser, ivSerach, ivPlayerIcon;
    private ImageView ivPlayerBg, ivPlayerPause, ivPlayerList;
    private IntentFilter intentFilterPause,intentFilterNext;
    private RelativeLayout rlPlayerMb;
    private RecyclerView recyclerViewMenu;

    /**
     * 取代EventBus
     */
    private BaseEvent baseEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无ActionBar效果
        StatusUtils.setNoActionBar(this);
        StatusUtils.setTranslucentStatus(this);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                if (position == 0) {
                    tvTabOne.setTextColor(Color.WHITE);
                    tvTabTwo.setTextColor(Color.GRAY);
                } else {
                    tvTabOne.setTextColor(Color.GRAY);
                    tvTabTwo.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                 MusicPlay musicPlay = (MusicPlay) message.object;
                 updateUi(musicPlay);
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);

        //播放暂停通知广播注册
        intentFilterPause = new IntentFilter();
        intentFilterPause.addAction(MusicNotification.ACTION_PAUSE);
        NotificationPauseBroadcast notificationPauseBroadcast = new NotificationPauseBroadcast();
        registerReceiver(notificationPauseBroadcast, intentFilterPause);

        //下一曲广播注册
        intentFilterNext = new IntentFilter();
        intentFilterNext.addAction(MusicNotification.ACTION_NEXT);
        NotificationNextBroadcast notificationNextBroadcast = new NotificationNextBroadcast();
        registerReceiver(notificationNextBroadcast, intentFilterNext);

        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        List<Menu> menus = new ArrayList<>();
        menus.add(new Menu(R.mipmap.pi_fu,"皮肤"));
        MenuAdapter menuAdapter = new MenuAdapter(this,menus);
        menuAdapter.setMenuClickListener(new MenuAdapter.MenuClickListener() {
            @Override
            public void onClick(Menu menu) {
               switch (menu.getIconId()){
                   case R.mipmap.pi_fu:
                       Intent intent = new Intent(MainActivity.this,PiFuSettingActivity.class);
                       startActivity(intent);
                       break;
               }
            }
        });
        recyclerViewMenu.setAdapter(menuAdapter);
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
        ivPlayerPause = findViewById(R.id.iv_player_pause);
        ivPlayerList = findViewById(R.id.iv_player_list);
        rlPlayerMb = findViewById(R.id.rl_player_mb);
        recyclerViewMenu = findViewById(R.id.recycler_view_menu);
        tvTabOne.setOnClickListener(this);
        tvTabTwo.setOnClickListener(this);
        rlPlayerMb.setOnClickListener(this);
        ivSerach.setOnClickListener(this);
        ivPlayerPause.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.OPEN_MUSIC_ID,baseEvent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = fragmentList.get(viewPager.getCurrentItem());
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_tab_1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_tab_2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.rl_player_mb:
                Intent intent = new Intent(MainActivity.this,PlayActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_serach:
                intent = new Intent(MainActivity.this,SerachActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_player_pause:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().pauseAndPlay();
                } else {

                }
                break;
        }

    }

    static class NotificationPauseBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(MediaPlayerManager.getInstance().checkCanPlay()) {
                MediaPlayerManager.getInstance().pauseAndPlay();
            }
        }
    }

    static class NotificationNextBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(MediaPlayerManager.getInstance().checkCanPlay()) {
                MediaPlayerManager.getInstance().next();
            }
        }
    }

    private void updateUi(MusicPlay musicPlay){
        if(musicPlay.isQieHuan()) {
            //设置歌曲名和歌手名
            tvMusicName.setText(MediaPlayerManager.getInstance().currentOpenMusic().getName());
            tvSingerName.setText(MediaPlayerManager.getInstance().currentOpenMusic().getArtist());

            //设置歌曲图片
            Bitmap bitmap = MediaPlayerManager.getInstance().getCurrentOpenMusicBitmap(this);
            if (bitmap == null) {
                //设置默认图片
                ivPlayerIcon.setImageResource(R.mipmap.default_music_icon);
                ivPlayerBg.setImageResource(R.mipmap.default_bg);
            } else {
                ivPlayerIcon.setImageBitmap(bitmap);
                ivPlayerBg.setImageBitmap(MediaPlayerManager.getInstance().getGaoShiBitmap(this));
            }
        }

        //更新播放/暂停状态
        if (MediaPlayerManager.getInstance().getMediaPlayer().isPlaying()) {
            ivPlayerPause.setImageResource(R.mipmap.play);
        } else {
            ivPlayerPause.setImageResource(R.mipmap.pause);
        }

        //发送通知
        MusicNotification musicNotification = new MusicNotification(this);
        musicNotification.showNotification();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //返回桌面
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}