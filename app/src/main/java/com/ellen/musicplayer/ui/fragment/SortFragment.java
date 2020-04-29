package com.ellen.musicplayer.ui.fragment;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.BannerAdapter;
import com.ellen.musicplayer.adapter.MusicAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.manager.sql.SQLManager;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.DepthPageTransformer;
import com.youth.banner.util.BannerUtils;

import java.util.List;

/**
 * 分类
 */
public class SortFragment extends BaseFragment {

    private Banner banner;
    private List<Music> mostFiveMusicLists;
    private BannerAdapter bannerAdapter;


    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;

    @Override
    protected void initData() {
        mostFiveMusicLists = SQLManager.getInstance().getMostFiveMusic(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        musicAdapter = new MusicAdapter(getActivity(),mostFiveMusicLists);
        recyclerView.setAdapter(musicAdapter);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_top,null);
        musicAdapter.addHeaderView(view);
        banner = view.findViewById(R.id.banner);
        bannerAdapter = new BannerAdapter(getActivity(),mostFiveMusicLists);
        bannerAdapter.setOnItemClickListener(new BannerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                MediaPlayerManager.getInstance().open(position,mostFiveMusicLists);
            }
        });
        banner.setAdapter(bannerAdapter);
        banner.setIndicator(new CircleIndicator(getActivity()));
        banner.setIndicatorGravity(IndicatorConfig.Direction.RIGHT);
        banner.setIndicatorMargins(new IndicatorConfig.Margins((int) BannerUtils.dp2px(10)));
        banner.setIndicatorWidth(10,20);
        banner.start();

    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_my;
    }
}
