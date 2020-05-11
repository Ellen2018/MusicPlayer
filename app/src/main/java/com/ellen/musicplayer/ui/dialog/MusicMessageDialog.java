package com.ellen.musicplayer.ui.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.MusicMessageMenuAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.bean.Music;
import com.ellen.musicplayer.bean.MusicMessageMenu;
import com.ellen.musicplayer.bean.Singer;
import com.ellen.musicplayer.bean.ZhuanJi;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.utils.JumpSortUtils;
import com.ellen.musicplayer.utils.LocalSDMusicUtils;
import com.ellen.musicplayer.utils.MusicBitmap;
import com.ellen.musicplayer.utils.UriUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;

public class MusicMessageDialog extends BaseBottomPopWindow {

    private Music music;

    private ImageView icMusicIcon;
    private TextView tvMusicName,tvSingerName;
    private RecyclerView recyclerView;
    private List<MusicMessageMenu> musicMessageMenuList;
    private MusicMessageMenuAdapter musicMessageMenuAdapter;
    private View parentView;
    private MusicMessageDialog.DeleteInterface deleteInterface;
    private boolean isContainsNext = true;

    public MusicMessageDialog(Activity activity,View parentView, Music music,boolean isContainsNext) {
        super(activity);
        this.music = music;
        this.parentView = parentView;
        this.isContainsNext = isContainsNext;
    }

    public MusicMessageDialog(Activity activity,View parentView, Music music) {
        super(activity);
        this.music = music;
        this.parentView = parentView;
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
        if(isContainsNext){
            musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_next,"下一曲播放",""));
        }
        musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_add_gedan,"收藏",""));
        musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_share,"分享",""));
        musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_singer,"歌手: ",music.getArtist()));
        musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_zhuan_ji,"专辑: ",music.getAlbum()));
        musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_music_message,"歌曲信息",""));
        if(deleteInterface != null) {
            musicMessageMenuList.add(new MusicMessageMenu(R.mipmap.menu_delete, "删除", ""));
        }

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
                        //添加到歌单
                        List<Music> musics = new ArrayList<>();
                        musics.add(music);
                        AddToGeDanDialog addToGeDanDialog = new AddToGeDanDialog(getActivity(),musics);
                        addToGeDanDialog.showAtLocation(parentView, Gravity.BOTTOM,0,0);
                        //临时退出暗化
                        isCanCelAnHua = true;
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
                        //先判断歌手是否需要切割
                        if(music.getArtist().contains("/")){
                            String[] singerArray = music.getArtist().split("/");
                            SelectorSingerDialog selectorSingerDialog = new SelectorSingerDialog(singerArray);
                            FragmentActivity fragmentActivity = (FragmentActivity) getActivity();
                            selectorSingerDialog.show(fragmentActivity.getSupportFragmentManager(),"");
                        }else if(music.getArtist().contains(" / ")){
                            String[] singerArray = music.getArtist().split(" / ");
                            SelectorSingerDialog selectorSingerDialog = new SelectorSingerDialog(singerArray);
                            FragmentActivity fragmentActivity = (FragmentActivity) getActivity();
                            selectorSingerDialog.show(fragmentActivity.getSupportFragmentManager(),"");
                        }else {
                            //查询歌手列表
                            Singer singer = LocalSDMusicUtils.getSinger(getActivity(), music.getArtist());
                            JumpSortUtils.jumpToSort(getActivity(), "歌手", singer.getName(), singer.getMusicList());
                        }
                        break;
                    case R.mipmap.menu_zhuan_ji:
                        ZhuanJi zhuanJi = LocalSDMusicUtils.getZhuanJi(getActivity(),music.getAlbum());
                        JumpSortUtils.jumpToSort(getActivity(),"专辑",zhuanJi.getName(),zhuanJi.getMusicList());
                        break;
                    case R.mipmap.menu_music_message:
                        MusicLocalMessageDilaog musicLocalMessageDilaog = new MusicLocalMessageDilaog(getActivity(),music);
                        musicLocalMessageDilaog.showAtLocation(parentView,Gravity.BOTTOM,0,0);
                        isCanCelAnHua = true;
                        break;
                    case R.mipmap.menu_delete:
                        if(deleteInterface != null){
                            deleteInterface.delete();
                        }
                        break;
                }
                dismiss();
            }
        });
    }

    public DeleteInterface getDeleteInterface() {
        return deleteInterface;
    }

    public void setDeleteInterface(DeleteInterface deleteInterface) {
        this.deleteInterface = deleteInterface;
    }

    public interface DeleteInterface{
        void delete();
    }
}
