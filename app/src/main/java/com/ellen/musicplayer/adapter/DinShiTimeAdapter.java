package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;

import java.util.List;

public class DinShiTimeAdapter extends BaseSingleRecyclerViewAdapter<Integer, DinShiTimeAdapter.DinShiViewHolder> {


    public DinShiTimeAdapter(Context context, List<Integer> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_din_shi;
    }

    @Override
    protected DinShiViewHolder getNewViewHolder(View view) {
        return new DinShiViewHolder(view);
    }

    @Override
    protected void showData(DinShiViewHolder dinShiViewHolder, Integer data, int position) {
        if(data == 0){
            dinShiViewHolder.tvDinShi.setText("关闭定时");
        }else {
            dinShiViewHolder.tvDinShi.setText(String.valueOf(data));
        }
    }

    static class DinShiViewHolder extends BaseViewHolder{
        TextView tvDinShi;
        public DinShiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDinShi = findViewById(R.id.tv_time);
        }
    }

}
