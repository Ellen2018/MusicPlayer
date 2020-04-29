package com.ellen.musicplayer.ui.fragment;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.BannerAdapter;
import com.ellen.musicplayer.adapter.GeDanAdapter;
import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.GeDan;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.musicplayer.utils.collectionutil.CollectionUtils;
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

    private RecyclerView recyclerView,recyclerViewGeDan,recyclerViewMusicNote;
    private GeDanAdapter geDanAdapter;
    private ImageView ivGoTop;
    /**
     * 注意：这里设置过大会导致播放图片加载错位问题
     */
    private final int SUI_JI_MUSIC_COUNT = 10;

    @Override
    protected void initData() {
        geDanList = new ArrayList<>();
        geDanList.add(new GeDan());
        geDanList.add(new GeDan());
        geDanList.add(new GeDan());
        geDanList.add(new GeDan());
        geDanList.add(new GeDan());
        geDanList.add(new GeDan());
        geDanList.add(new GeDan());
        geDanList.add(new GeDan());
        geDanList.add(new GeDan());
        suiJiMusicLists = SQLManager.getInstance().getMostMusic(getActivity(),SUI_JI_MUSIC_COUNT);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        GeDanAdapter geDanAdapter = new GeDanAdapter(getActivity(), new ArrayList<GeDan>());
        recyclerView.setAdapter(geDanAdapter);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_top,null);
        geDanAdapter.addHeaderView(view);
        banner = view.findViewById(R.id.banner);
        recyclerViewGeDan = view.findViewById(R.id.recycler_view_ge_dan);
        recyclerViewMusicNote = view.findViewById(R.id.recycler_view_music_note);
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
        bannerAdapter = new BannerAdapter(getActivity(),suiJiMusicLists);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewGeDan.setLayoutManager(linearLayoutManager);
        GeDanAdapter geDanAdapter = new GeDanAdapter(getActivity(), geDanList);
        recyclerViewGeDan.setAdapter(geDanAdapter);

        //音乐日记
        LinearLayoutManager linearLayoutManagerNote = new LinearLayoutManager(getActivity());
        linearLayoutManagerNote.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewMusicNote.setLayoutManager(linearLayoutManagerNote);
        GeDanAdapter geDanAdapterNote = new GeDanAdapter(getActivity(), geDanList);
        recyclerViewMusicNote.setAdapter(geDanAdapterNote);
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
    protected int setLayout() {
        return R.layout.fragment_my;
    }
}
