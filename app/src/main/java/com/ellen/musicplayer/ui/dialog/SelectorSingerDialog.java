package com.ellen.musicplayer.ui.dialog;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.SelectorSingerAdapter;
import com.ellen.musicplayer.base.BaseDialogFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Singer;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;

import java.util.Arrays;

public class SelectorSingerDialog extends BaseDialogFragment {

    private String[] singNameArray;
    private RecyclerView recyclerView;
    private SelectorSingerAdapter selectorSingerAdapter;

    public SelectorSingerDialog(String[] singNameArray) {
        this.singNameArray = singNameArray;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(selectorSingerAdapter = new SelectorSingerAdapter(getContext(), Arrays.asList(singNameArray)));
        selectorSingerAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                //查询歌手列表
                Singer singer = LocalSDMusicUtils.getSinger(getActivity(), singNameArray[position]);
                JumpSortUtils.jumpToSort(getActivity(), "歌手", singer.getName(), singer.getMusicList());
                dismiss();
            }
        });
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_selector_singer;
    }

    @Override
    protected Boolean setCancelable() {
        return true;
    }

    @Override
    protected Boolean setCanceledOnTouchOutside() {
        return true;
    }

    @Override
    protected Boolean setWinowTransparent() {
        return true;
    }
}
