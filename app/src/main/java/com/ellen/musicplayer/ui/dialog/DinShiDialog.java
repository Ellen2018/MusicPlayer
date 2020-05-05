package com.ellen.musicplayer.ui.dialog;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.DinShiTimeAdapter;
import com.ellen.musicplayer.base.BaseDialogFragment;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DinShiDialog extends BaseDialogFragment {

    private RecyclerView recyclerView;
    private List<Integer> integerList;
    private DinShiTimeAdapter dinShiTimeAdapter;
    private ChooseTime chooseTime;

    public DinShiDialog(ChooseTime chooseTime) {
        this.chooseTime = chooseTime;
    }

    @Override
    protected void initData() {
        integerList = new ArrayList<>();
        integerList.add(0);
        integerList.add(1);
        integerList.add(30);
        integerList.add(60);
        integerList.add(90);
        integerList.add(120);
        recyclerView.setAdapter(dinShiTimeAdapter = new DinShiTimeAdapter(getActivity(),integerList));
        dinShiTimeAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                chooseTime.time(integerList.get(position));
                dismiss();
            }
        });
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    protected int setLayout() {
        return R.layout.dialog_din_shi;
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

    public interface ChooseTime{
        void time(int m);
    }
}
