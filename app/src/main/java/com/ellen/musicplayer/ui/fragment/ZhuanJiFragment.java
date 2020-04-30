package com.ellen.musicplayer.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.ZhuanJiAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.bean.ZhuanJi;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;

import java.util.List;

public class ZhuanJiFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private List<ZhuanJi> zhuanJiList;

    @Override
    protected void initData() {
        zhuanJiList = LocalSDMusicUtils.getAlbum(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ZhuanJiAdapter(getActivity(),zhuanJiList));
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_zhuan_ji;
    }
}
