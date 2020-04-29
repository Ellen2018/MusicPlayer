package com.ellen.musicplayer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.musicplayer.view.YuanJiaoImageView;

import org.w3c.dom.Text;

import java.util.List;

public class BannerAdapter extends com.youth.banner.adapter.BannerAdapter<Music, BannerAdapter.BannerViewHolder> {


    private Context context;
    private OnItemClickListener onItemClickListener;

    public BannerAdapter(Context context,List<Music> datas) {
        super(datas);
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_layout,null);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindView(BannerViewHolder holder, Music data, final int position, int size) {
        Bitmap bitmap = MusicBitmap.getArtwork(context,data.getMusicId(),data.getAlbumId());
        Glide.with(context).load(bitmap).into(holder.imageView);
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
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tvMusicName,tvSingerName,tvProgress;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_banner_icon);
            tvMusicName = itemView.findViewById(R.id.tv_music_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);
            tvProgress = itemView.findViewById(R.id.tv_progress);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

}
