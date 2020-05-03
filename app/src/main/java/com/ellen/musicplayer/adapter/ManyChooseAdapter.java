package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ellen.musicplayer.R;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseSingleRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ManyChooseAdapter extends BaseSingleRecyclerViewAdapter<Music, ManyChooseAdapter.ManyChooseViewHolder> {

    private Map<Integer,Music> musicTreeMap;

    public Map<Integer, Music> getMusicTreeMap() {
        return musicTreeMap;
    }

    public void setMusicTreeMap(Map<Integer, Music> musicTreeMap) {
        this.musicTreeMap = musicTreeMap;
    }

    public ManyChooseAdapter(Context context, List<Music> dataList) {
        super(context, dataList);
        musicTreeMap = new TreeMap<>();
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_many_choose;
    }

    @Override
    protected ManyChooseViewHolder getNewViewHolder(View view) {
        return new ManyChooseViewHolder(view);
    }

    @Override
    protected void showData(ManyChooseViewHolder manyChooseViewHolder, Music data, int position) {
        manyChooseViewHolder.tvMusicName.setText(data.getName());
        manyChooseViewHolder.tvSingerName.setText(data.getArtist());

        manyChooseViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //选中
                    musicTreeMap.put(position,data);
                }else {
                    //没有选中
                    musicTreeMap.remove(position);
                }
            }
        });

        //更新复选框ui
        if(musicTreeMap.containsKey(position)){
            manyChooseViewHolder.checkBox.setChecked(true);
        }else {
            manyChooseViewHolder.checkBox.setChecked(false);
        }
    }

    class ManyChooseViewHolder extends BaseViewHolder{

        TextView tvMusicName, tvSingerName;
        CheckBox checkBox;

        public ManyChooseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMusicName = findViewById(R.id.tv_music_name);
            tvSingerName = findViewById(R.id.tv_singer_name);
            checkBox = findViewById(R.id.checkbox);
        }
    }

    public interface  ChooseListener{
        void choose(int size);
    }

}
