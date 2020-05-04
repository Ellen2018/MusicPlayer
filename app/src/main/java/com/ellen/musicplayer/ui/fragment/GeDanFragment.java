package com.ellen.musicplayer.ui.fragment;

import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.bean.GeDan;

import java.util.List;

public class GeDanFragment extends BaseFragment {

    private List<GeDan> geDanList;
    private RecyclerView recyclerView;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
      recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_ge_dan;
    }
}
