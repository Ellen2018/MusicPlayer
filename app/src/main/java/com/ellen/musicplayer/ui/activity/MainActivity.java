package com.ellen.musicplayer.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.MenuAdapter;
import com.ellen.musicplayer.bean.Menu;
import com.ellen.musicplayer.bean.PiFu;
import com.ellen.musicplayer.ui.dialog.DinShiDialog;
import com.ellen.musicplayer.ui.dialog.PlayListDialog;
import com.ellen.musicplayer.manager.pifu.PiFuManager;
import com.ellen.musicplayer.message.MusicPlay;
import com.ellen.musicplayer.message.PiFuMessage;
import com.ellen.musicplayer.ui.fragment.LocalFragment;
import com.ellen.musicplayer.ui.fragment.SortFragment;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.notification.MusicNotification;
import com.ellen.musicplayer.utils.PermissionUtils;
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
    private IntentFilter intentFilterPause, intentFilterNext;
    private RelativeLayout rlPlayerMb;
    private RecyclerView recyclerViewMenu;
    private ImageView ivPiFu;
    private PermissionUtils permissionUtils;
    private RelativeLayout rl;
    private MusicNotification musicNotification;

    /**
     * 取代EventBus
     */
    private BaseEvent musicEvent, piFuEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无ActionBar效果
        StatusUtils.setNoActionBar(this);
        StatusUtils.setTranslucentStatus(this);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        initData();
    }

    private void initData() {
        updatePiFu(PiFuManager.getInstance().getPiFu());
        //走马灯设置
        tvMusicName.setSelected(true);
        //tvSingerName.setSelected(true);
        permissionUtils = new PermissionUtils(this);
        permissionUtils.startCheckFileReadWritePermission(0, new PermissionUtils.PermissionCallback() {
            @Override
            public void success() {

                fragmentList = new ArrayList<>();
                fragmentList.add(new SortFragment());
                fragmentList.add(new LocalFragment());

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
            }

            @Override
            public void failure() {

            }
        });

        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        List<Menu> menus = new ArrayList<>();
        menus.add(new Menu(R.mipmap.pi_fu, "皮肤"));
        menus.add(new Menu(R.mipmap.din_shi, "定时"));
        MenuAdapter menuAdapter = new MenuAdapter(this, menus);
        menuAdapter.setMenuClickListener(new MenuAdapter.MenuClickListener() {
            @Override
            public void onClick(Menu menu) {
                drawerLayout.closeDrawers();
                switch (menu.getIconId()) {
                    case R.mipmap.pi_fu:
                        Intent intent = new Intent(MainActivity.this, PiFuSettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.mipmap.din_shi:
                        DinShiDialog dinShiDialog = new DinShiDialog();
                        dinShiDialog.show(getSupportFragmentManager(),"");
                        drawerLayout.closeDrawers();
                        break;
                }
            }
        });
        recyclerViewMenu.setAdapter(menuAdapter);


        musicEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                MusicPlay musicPlay = (MusicPlay) message.object;
                updateUi(musicPlay);
            }

            @Override
            public FragmentActivity bindActivity() {
                return MainActivity.this;
            }
        };

        piFuEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                PiFuMessage piFuMessage = (PiFuMessage) message.object;
                updatePiFu(piFuMessage.getPiFu());
            }

            @Override
            public FragmentActivity bindActivity() {
                return MainActivity.this;
            }
        };

        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID, musicEvent);
        MessageManager.getInstance().registerMessageEvent(MessageTag.PI_FU_ID, piFuEvent);

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
        rl = findViewById(R.id.rl);
        ivPiFu = findViewById(R.id.iv_pi_fu);
        tvTabOne.setOnClickListener(this);
        tvTabTwo.setOnClickListener(this);
        rlPlayerMb.setOnClickListener(this);
        ivSerach.setOnClickListener(this);
        ivPlayerPause.setOnClickListener(this);
        ivPlayerList.setOnClickListener(this);

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tab_1:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_tab_2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.rl_player_mb:
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_serach:
                intent = new Intent(MainActivity.this, SerachActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_player_pause:
                if (MediaPlayerManager.getInstance().checkCanPlay()) {
                    MediaPlayerManager.getInstance().pauseAndPlay();
                } else {

                }
                break;
            case R.id.iv_player_list:
                PlayListDialog playListDialog = new PlayListDialog(MainActivity.this);
                playListDialog.showAtLocation(rl, Gravity.BOTTOM, 0, 0);
                break;
        }

    }

    static class NotificationPauseBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MediaPlayerManager.getInstance().checkCanPlay()) {
                MediaPlayerManager.getInstance().pauseAndPlay();
            }
        }
    }

    static class NotificationNextBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MediaPlayerManager.getInstance().checkCanPlay()) {
                MediaPlayerManager.getInstance().nextByUser();
            }
        }
    }

    private void updateUi(MusicPlay musicPlay) {
        if (musicPlay != null) {
            if(musicPlay.isClear()){
                rlPlayerMb.setVisibility(View.GONE);
                tvMusicName.setText("歌曲名");
                tvSingerName.setText("歌手名");
                ivPlayerIcon.setImageResource(R.mipmap.default_music_icon);
                ivPlayerBg.setImageResource(R.mipmap.default_bg);
                ivPlayerPause.setImageResource(R.mipmap.pause);
                musicNotification.cancelNotification();
                return;
            }
            rlPlayerMb.setVisibility(View.VISIBLE);
            //设置歌曲名和歌手名
            tvMusicName.setText(MediaPlayerManager.getInstance().currentOpenMusic().getName());
            tvSingerName.setText(MediaPlayerManager.getInstance().currentOpenMusic().getArtist());
            if (musicPlay.isQieHuan()) {
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

            rlPlayerMb.setVisibility(View.VISIBLE);
            //发送通知
            musicNotification = new MusicNotification(this);
            musicNotification.showNotification();
        } else {
            rlPlayerMb.setVisibility(View.GONE);
            tvMusicName.setText("歌曲名");
            tvSingerName.setText("歌手名");
            ivPlayerIcon.setImageResource(R.mipmap.default_music_icon);
            ivPlayerBg.setImageResource(R.mipmap.default_bg);
            ivPlayerPause.setImageResource(R.mipmap.pause);
            musicNotification.cancelNotification();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //返回桌面
            if(viewPager.getCurrentItem() == 0) {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
            }else {
                viewPager.setCurrentItem(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void updatePiFu(PiFu piFu) {
        if (piFu != null) {
            if (piFu.isGuDinPiFu()) {
                ivPiFu.setImageResource(piFu.getPiFuIconId());
            } else {
                //使用Glide加载本地图片
                Glide.with(MainActivity.this).load(piFu.getImagePath()).into(ivPiFu);
            }
        }
    }
}
