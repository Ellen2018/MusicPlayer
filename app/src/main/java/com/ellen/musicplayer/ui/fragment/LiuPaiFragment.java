package com.ellen.musicplayer.ui.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.LiuPaiAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.bean.LiuPai;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;

import java.util.List;

public class LiuPaiFragment extends BaseFragment {

    private List<LiuPai> liuPaiList;
    private RecyclerView recyclerView;

    @Override
    protected void initData() {
        liuPaiList = LocalSDMusicUtils.getLiuPais(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new LiuPaiAdapter(getActivity(),liuPaiList));
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_liu_pai;
    }
}
