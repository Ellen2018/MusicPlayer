package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.PiFu;

import java.util.List;

public class PiFuSelectorAdapter extends BaseSingleRecyclerViewAdapter<PiFu, PiFuSelectorAdapter.PiFuSelectorViewHolder> {

    private PiFu selectorPiFu;

    public PiFu getSelectorPiFu() {
        return selectorPiFu;
    }

    public void setSelectorPiFu(PiFu selectorPiFu) {
        this.selectorPiFu = selectorPiFu;
    }

    public PiFuSelectorAdapter(Context context, List<PiFu> dataList) {
        super(context, dataList);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_pi_fu_selector;
    }

    @Override
    protected PiFuSelectorViewHolder getNewViewHolder(View view) {
        return new PiFuSelectorViewHolder(view);
    }

    @Override
    protected void showData(PiFuSelectorViewHolder piFuSelectorViewHolder, PiFu data, int position) {
         if(position == getDataList().size() - 1){
             //对应添加item的逻辑
             piFuSelectorViewHolder.ivAddPiFu.setVisibility(View.VISIBLE);
             piFuSelectorViewHolder.ivIcon.setVisibility(View.GONE);
         }else {
             piFuSelectorViewHolder.ivAddPiFu.setVisibility(View.GONE);
             piFuSelectorViewHolder.ivIcon.setVisibility(View.VISIBLE);
             if(data.isGuDinPiFu()){
                 piFuSelectorViewHolder.ivIcon.setImageResource(data.getPiFuIconId());
             }else {
                 //使用Glide进行加载
                 Glide.with(getContext()).load(data.getImagePath()).into(piFuSelectorViewHolder.ivIcon);
             }
         }

         //选中的逻辑
        if(selectorPiFu.getPiFuId() == data.getPiFuId()){
            piFuSelectorViewHolder.viewSelector.setVisibility(View.VISIBLE);
        }else {
            piFuSelectorViewHolder.viewSelector.setVisibility(View.GONE);
        }
    }

    static class PiFuSelectorViewHolder extends BaseViewHolder{

        ImageView ivIcon,ivAddPiFu;
        View viewSelector;

        public PiFuSelectorViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = findViewById(R.id.iv_icon);
            viewSelector = findViewById(R.id.view_selector);
            ivAddPiFu = findViewById(R.id.iv_add_pi_fu);
        }
    }

}
