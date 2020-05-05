package com.ellen.musicplayer.adapter;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.ui.dialog.MusicMessageDialog;
import com.ellen.musicplayer.utils.MusicBitmap;

import java.util.List;

public class BannerAdapter extends com.youth.banner.adapter.BannerAdapter<Music, BannerAdapter.BannerViewHolder> {

    private Activity activity;
    private View prentView;
    private OnItemClickListener onItemClickListener;

    public BannerAdapter(View prentView,Activity activity,List<Music> datas) {
        super(datas);
        this.activity = activity;
        this.prentView = prentView;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.banner_layout,null);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindView(BannerViewHolder holder, Music data, final int position, int size) {
        Glide.with(activity)
                .load(MusicBitmap.getArtwork(activity,data.getMusicId(),data.getAlbumId()))
                .error(R.mipmap.default_music_icon)
                .into(holder.imageView);
        holder.tvMusicName.setText(data.getName());
        holder.tvSingerName.setText(data.getArtist());
        holder.tvProgress.setText((position+1)+"/"+(getItemCount()-2));
        if(onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
        holder.rlMusicMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicMessageDialog musicMessageDialog = new MusicMessageDialog(activity,prentView,data);
                musicMessageDialog.showAtLocation(prentView, Gravity.BOTTOM,0,0);
            }
        });
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tvMusicName,tvSingerName,tvProgress;
        RelativeLayout rlMusicMessage;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_banner_icon);
            tvMusicName = itemView.findViewById(R.id.tv_music_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);
            tvProgress = itemView.findViewById(R.id.tv_progress);
            rlMusicMessage = itemView.findViewById(R.id.rl_music_message);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

}
