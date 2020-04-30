package com.ellen.musicplayer.dialog;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.MusicMessageMenuAdapter;
import com.ellen.musicplayer.base.BasePopwindow;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.MusicMessageMenu;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.musicplayer.utils.UriUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class MusicMessageDialog extends BaseBottomPopWindow {

    private Music music;

    private ImageView icMusicIcon;
    private TextView tvMusicName,tvSingerName;
    private RecyclerView recyclerView;
    private List<MusicMessageMenu> musicMessageMenuList;
    private MusicMessageMenuAdapter musicMessageMenuAdapter;

    public MusicMessageDialog(Activity activity, Music music) {
        super(activity);
        this.music = music;
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_music_message,null);
        icMusicIcon = view.findViewById(R.id.iv_music_icon);
        tvMusicName = view.findViewById(R.id.tv_music_name);
        tvSingerName = view.findViewById(R.id.tv_singer_name);
        recyclerView = view.findViewById(R.id.recycler_view);
        initData();
        return view;
    }

    private void initData() {
        Glide.with(getContext())
                .load(MusicBitmap.getArtwork(getContext(),music.getMusicId(),music.getAlbumId()))
                .error(R.mipmap.default_music_icon)
                .into(icMusicIcon);
        tvSingerName.setText(music.getArtist());
        tvMusicName.setText(music.getName());
        tvMusicName.setSelected(true);
        tvSingerName.setSelected(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        musicMessageMenuList = new ArrayList<>();
        musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_next,"下一曲播放",""));
        musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_add_gedan,"收藏",""));
        musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_share,"分享",""));
        musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_singer,"歌手: ",music.getArtist()));
        musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_zhuan_ji,"专辑: ",music.getAlbum()));
        musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_music_message,"歌曲信息",""));
        musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_delete,"删除",""));
        recyclerView.setAdapter(musicMessageMenuAdapter = new MusicMessageMenuAdapter(getActivity(),musicMessageMenuList));
        musicMessageMenuAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                switch (musicMessageMenuList.get(position).getMenuIconId()){
                    case R.mipmap.menu_next:
                        //下一曲播放
                        MediaPlayerManager.getInstance().addNextPlayMusic(music);
                        break;
                    case R.mipmap.menu_add_gedan:
                        break;
                    case R.mipmap.menu_share:
                        new Share2.Builder(getActivity())
                                .setContentType(ShareContentType.AUDIO)
                                .setShareFileUri(UriUtils.getFileUri(getActivity(), ShareContentType.AUDIO, new File(music.getPath())))
                                .setTextContent(music.getName() + "-" + music.getArtist())
                                .setTitle("Share Music")
                                .build()
                                .shareBySystem();
                        break;
                    case R.mipmap.menu_singer:
                        break;
                    case R.mipmap.menu_zhuan_ji:
                        break;
                    case R.mipmap.menu_music_message:
                        break;
                    case R.mipmap.menu_delete:
                        break;
                }
                dismiss();
            }
        });



    }
}
