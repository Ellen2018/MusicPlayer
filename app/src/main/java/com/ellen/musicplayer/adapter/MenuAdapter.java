package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Menu;

import java.util.List;

public class MenuAdapter extends BaseSingleRecyclerViewAdapter<Menu, MenuAdapter.MenuViewHolder> {

    private MenuClickListener menuClickListener;


    public MenuAdapter(Context context, List<Menu> dataList){
        super(context,dataList);
    }

    public MenuClickListener getMenuClickListener() {
        return menuClickListener;
    }

    public void setMenuClickListener(MenuClickListener menuClickListener) {
        this.menuClickListener = menuClickListener;
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_menu;
    }

    @Override
    protected MenuViewHolder getNewViewHolder(View view) {
        return new MenuViewHolder(view);
    }

    @Override
    protected void showData(MenuViewHolder menuViewHolder, final Menu data, int position) {
        menuViewHolder.ivMenuIcon.setImageResource(data.getIconId());
        menuViewHolder.tvMenuName.setText(data.getMenuName());
        if(menuClickListener != null) {
            menuViewHolder.llMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     menuClickListener.onClick(data);
                }
            });
        }
    }

    static class MenuViewHolder extends BaseViewHolder {
        TextView tvMenuName;
        ImageView ivMenuIcon;
        LinearLayout llMenu;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMenuName = findViewById(R.id.tv_menu_name);
            ivMenuIcon = findViewById(R.id.iv_menu_icon);
            llMenu = findViewById(R.id.ll_menu);
        }
    }

    public interface MenuClickListener{
        void onClick(Menu menu);
    }
}
