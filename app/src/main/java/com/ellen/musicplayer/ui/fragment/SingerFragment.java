package com.ellen.musicplayer.ui.fragment;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.SingerAdapter;
import com.ellen.musicplayer.base.BaseFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Singer;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.ToastUtils;

import java.util.List;

public class SingerFragment extends BaseFragment {

    private List<Singer> singerList;
    private RecyclerView recyclerView;
    private SingerAdapter singerAdapter;

    @Override
    protected void initData() {
        singerList = LocalSDMusicUtils.getArtist(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(singerAdapter = new SingerAdapter(getActivity(),singerList));
        singerAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                ToastUtils.toast(getActivity(),singerList.get(position).getName());
            }
        });
    }

    @Override
    protected void initView() {
       recyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_singer;
    }
}
