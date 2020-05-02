package com.ellen.musicplayer.ui.fragment;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.BannerAdapter;
import com.ellen.musicplayer.adapter.GeDanAdapter;
import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.GeDanMusic;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.NearMusic;
import com.ellen.musicplayer.dialog.CreateGeDanDialog;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.manager.sql.GeDanMusicTable;
import com.ellen.musicplayer.manager.sql.GeDanTable;
import com.ellen.musicplayer.manager.sql.NearMusicTable;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.musicplayer.utils.collectionutil.CollectionUtils;
import com.ellen.sqlitecreate.createsql.order.Order;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.AlphaPageTransformer;
import com.youth.banner.transformer.DepthPageTransformer;
import com.youth.banner.transformer.RotateDownPageTransformer;
import com.youth.banner.transformer.RotateUpPageTransformer;
import com.youth.banner.transformer.ScaleInTransformer;
import com.youth.banner.transformer.ZoomOutPageTransformer;
import com.youth.banner.util.BannerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类
 */
public class SortFragment extends BaseFragment {

    private Banner banner;
    private List<Music> suiJiMusicLists;
    private List<GeDan> geDanList;
    private BannerAdapter bannerAdapter;
    private SmartRefreshLayout smartRefreshLayout;

    private RecyclerView recyclerView,recyclerViewGeDan;
    private GeDanAdapter geDanAdapter;
    private ImageView ivGoTop;
    private BaseEvent geDanBaseEvent,likeBaseEvent,nearEvent;
    private LinearLayout llLike,llNear;
    private ImageView ivLikeIcon,ivNearIcon;
    private TextView tvLikeCount,tvNearCount;
    /**
     * 注意：这里设置过大会导致播放图片加载错位问题
     */
    private final int SUI_JI_MUSIC_COUNT = 10;

    @Override
    protected void initData() {
        geDanList = SQLManager.getInstance().getGeDanTable()
                .getAllDatas(Order.getInstance(false)
                        .setFirstOrderFieldName("geDanSqlTableName")
                        .setIsDesc(true).createSQL());
        geDanBaseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                geDanList = SQLManager.getInstance().getGeDanTable()
                        .getAllDatas( Order.getInstance(false)
                                .setFirstOrderFieldName("geDanSqlTableName")
                                .setIsDesc(true).createSQL());
                geDanAdapter.setDataList(geDanList);
                geDanAdapter.notifyDataSetChanged();
            }
        };
        likeBaseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                     updateLikeUi();
            }
        };
        nearEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
               updateNearUi();
            }
        };

        //注册消息事件
        MessageManager.getInstance().registerMessageEvent(MessageTag.GE_DAN_ID,geDanBaseEvent);
        MessageManager.getInstance().registerMessageEvent(MessageTag.LIKE_ID,likeBaseEvent);
        MessageManager.getInstance().registerMessageEvent(MessageTag.NEAR_ID,nearEvent);

        suiJiMusicLists = SQLManager.getInstance().getMostMusic(getActivity(),SUI_JI_MUSIC_COUNT);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        GeDanAdapter geDanAdapter = new GeDanAdapter(getActivity(), new ArrayList<GeDan>());
        recyclerView.setAdapter(geDanAdapter);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_top,null);
        geDanAdapter.addHeaderView(view);
        banner = view.findViewById(R.id.banner);
        recyclerViewGeDan = view.findViewById(R.id.recycler_view_ge_dan);
        llLike = view.findViewById(R.id.ll_like);
        llNear = view.findViewById(R.id.ll_near);
        ivLikeIcon = view.findViewById(R.id.iv_like_icon);
        ivNearIcon = view.findViewById(R.id.iv_near_icon);
        tvLikeCount = view.findViewById(R.id.tv_like_count);
        tvNearCount = view.findViewById(R.id.tv_near_count);
        updateLikeUi();
        updateNearUi();
        llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeDanMusicTable geDanMusicTable = SQLManager.getInstance().getLikeGeDanMusicTable();
                //获取最新喜欢的Music类
                List<GeDanMusic> geDanMusicList = geDanMusicTable.getAllDatas(Order.getInstance(false)
                        .setFirstOrderFieldName("likeTime")
                        .setIsDesc(true).createSQL());

                if(geDanMusicList != null && geDanMusicList.size() > 0){
                    List<Music> musicList = new ArrayList<>();
                    for(GeDanMusic geDanMusic:geDanMusicList){
                        musicList.add(geDanMusic.getMusic());
                    }
                    JumpSortUtils.jumpToSort(getActivity(),"歌单","我喜欢",musicList);
                }else {
                    List<Music> musicList = new ArrayList<>();
                    JumpSortUtils.jumpToSort(getActivity(),"歌单","我喜欢",musicList);
                }
            }
        });

        llNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击最近
                ToastUtils.toast(getActivity(),"最近");
            }
        });

        initBanner();
        geDanAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {

            }
        });
        smartRefreshLayout.setRefreshHeader(new MaterialHeader(getActivity()));
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                suiJiMusicLists = SQLManager.getInstance().getMostMusic(getActivity(),SUI_JI_MUSIC_COUNT);
                initBanner();
                smartRefreshLayout.finishRefresh();
                ToastUtils.toast(getActivity(),"随机歌单产生成功!");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stop();
    }

    private void initBanner(){
        bannerAdapter = new BannerAdapter(recyclerView,getActivity(),suiJiMusicLists);
        bannerAdapter.setOnItemClickListener(new BannerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MediaPlayerManager.getInstance().open(position,suiJiMusicLists);
            }
        });
        banner.setAdapter(bannerAdapter);
        banner.setPageTransformer(new ScaleInTransformer());
        banner.setIndicator(new CircleIndicator(getActivity()));
        banner.setIndicatorSelectedColor(Color.WHITE);
        banner.setIndicatorNormalColor(Color.GRAY);
        banner.setIndicatorGravity(IndicatorConfig.Direction.RIGHT);
        banner.setIndicatorMargins(new IndicatorConfig.Margins((int) BannerUtils.dp2px(10)));
        banner.setIndicatorWidth(10,20);
        banner.start();

        //歌单设置
        GridLayoutManager gridLayoutManagerGeDan = new GridLayoutManager(getActivity(),2);
        recyclerViewGeDan.setLayoutManager(gridLayoutManagerGeDan);
        geDanAdapter = new GeDanAdapter(getActivity(), geDanList);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_create_ge_dan,null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateGeDanDialog createGeDanDialog = new CreateGeDanDialog();
                createGeDanDialog.show(getFragmentManager(),"");
            }
        });
        geDanAdapter.addHeaderView(view);
        geDanAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                List<GeDanMusic> geDanMusicList = SQLManager.getInstance().getGeDanMusicListByName(geDanList.get(position));
                List<Music> musicList = new ArrayList<>();
                for(GeDanMusic geDanMusic:geDanMusicList){
                    musicList.add(geDanMusic.getMusic());
                }
                JumpSortUtils.jumpToSort(getActivity(),"歌单",geDanList.get(position).getGeDanName(),musicList);
            }
        });
        recyclerViewGeDan.setAdapter(geDanAdapter);
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        smartRefreshLayout = findViewById(R.id.refreshLayout);
        ivGoTop = findViewById(R.id.iv_go_top);
        ivGoTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(0);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.GE_DAN_ID,geDanBaseEvent);
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.LIKE_ID,likeBaseEvent);
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.NEAR_ID,nearEvent);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_my;
    }

    private void updateLikeUi(){
        //更新图片
        GeDanMusicTable geDanMusicTable = SQLManager.getInstance().getLikeGeDanMusicTable();
        //获取最新喜欢的Music类
        List<GeDanMusic> geDanMusicList = geDanMusicTable.getAllDatas(Order.getInstance(false)
                .setFirstOrderFieldName("likeTime")
                .setIsDesc(true).createSQL());
        if(geDanMusicList != null && geDanMusicList.size() > 0) {
            Music music = geDanMusicList.get(0).getMusic();
            Glide.with(getContext())
                    .load(MusicBitmap.getArtwork(getContext(), music.getMusicId(), music.getAlbumId()))
                    .error(R.mipmap.default_music_icon)
                    .into(ivLikeIcon);
        }
        tvLikeCount.setText(String.valueOf(geDanMusicList.size()));
    }

    private void updateNearUi(){
        //更新图片
        NearMusicTable nearMusicTable = SQLManager.getInstance().getNearMusicTable();
        //获取最新喜欢的Music类
        List<NearMusic> nearMusicList = nearMusicTable.getAllDatas(Order.getInstance(false)
                .setFirstOrderFieldName("playTime")
                .setIsDesc(true).createSQL());
        if(nearMusicList != null && nearMusicList.size() > 0) {
            Music music = nearMusicList.get(0).getMusic();
            Glide.with(getContext())
                    .load(MusicBitmap.getArtwork(getContext(), music.getMusicId(), music.getAlbumId()))
                    .error(R.mipmap.default_music_icon)
                    .into(ivNearIcon);
        }
        tvNearCount.setText(String.valueOf(nearMusicList.size()));
    }
}
