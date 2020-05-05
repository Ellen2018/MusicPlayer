package com.ellen.musicplayer.ui.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ellen.musicplayer.MessageTag;
import com.ellen.musicplayer.R;
import com.ellen.musicplayer.adapter.PlayMusicAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseRecyclerViewAdapter;
import com.ellen.musicplayer.base.adapter.recyclerview.BaseViewHolder;
import com.ellen.musicplayer.manager.mediaplayer.MediaPlayerManager;
import com.ellen.musicplayer.utils.ToastUtils;
import com.ellen.supermessagelibrary.BaseEvent;
import com.ellen.supermessagelibrary.MessageEventTrigger;
import com.ellen.supermessagelibrary.MessageManager;
import com.ellen.supermessagelibrary.SuperMessage;

public class PlayListDialog extends BaseBottomPopWindow {

    private RecyclerView recyclerView;
    private PlayMusicAdapter playMusicAdapter;
    private BaseEvent baseEvent;
    private ImageView ivClear, ivPlayMode;
    private LinearLayout llPlayMode,llAddToGeDan;
    private TextView tvPlayMode,tvCount;

    public PlayListDialog(Activity activity) {
        super(activity);
    }

    private void updatePlayModeUi() {
        switch (MediaPlayerManager.getInstance().getPlayMode()) {
            case DAN_QU:
                ivPlayMode.setImageResource(R.mipmap.playmode_dan_qu);
                tvPlayMode.setText("单曲循环");
                break;
            case SUI_JI:
                ivPlayMode.setImageResource(R.mipmap.playmode_sui_ji);
                tvPlayMode.setText("随机播放");
                break;
            case XUN_HUAN:
                ivPlayMode.setImageResource(R.mipmap.playmode_xun_huan);
                tvPlayMode.setText("循环播放");
                break;
        }
    }

    @Override
    protected View onCreateView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_play_list, null);
        recyclerView = view.findViewById(R.id.recycler_view);
        llPlayMode = view.findViewById(R.id.ll_play_mode);
        tvPlayMode = view.findViewById(R.id.tv_play_mode);
        ivPlayMode = view.findViewById(R.id.iv_play_mode);
        tvCount = view.findViewById(R.id.tv_count);
        llAddToGeDan = view.findViewById(R.id.ll_add_ge_dan);
        if(MediaPlayerManager.getInstance().checkCanPlay()) {
            tvCount.setText("("+MediaPlayerManager.getInstance().getPlayList().size()+")");
        }else {
            tvCount.setText("(0)");
        }
        updatePlayModeUi();
        llPlayMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayerManager.getInstance().adjustPlayMode();
                updatePlayModeUi();
                MessageManager.getInstance().sendEmptyMainThreadMessage(MessageTag.PLAY_MODE_ID);
            }
        });
        llAddToGeDan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MediaPlayerManager.getInstance().checkCanPlay()) {
                    dismiss();
                    AddToGeDanDialog addToGeDanDialog = new AddToGeDanDialog(getActivity(), MediaPlayerManager.getInstance().getPlayList());
                    addToGeDanDialog.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                }else {
                    ToastUtils.toast(getActivity(),"当前列表为空,不能收藏!");
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(playMusicAdapter = new PlayMusicAdapter(getActivity(), recyclerView, MediaPlayerManager.getInstance().getPlayList()));
        playMusicAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseViewHolder baseViewHolder, int position) {
                MediaPlayerManager.getInstance().open(position, MediaPlayerManager.getInstance().getPlayList());
            }
        });
        playMusicAdapter.setDeleteCallbck(new PlayMusicAdapter.DeleteCallbck() {
            @Override
            public void delete(int size) {
                if(MediaPlayerManager.getInstance().checkCanPlay()) {
                    tvCount.setText("("+MediaPlayerManager.getInstance().getPlayList().size()+")");
                }else {
                    tvCount.setText("(0)");
                }
            }
        });
        baseEvent = new MessageEventTrigger() {
            @Override
            public void handleMessage(SuperMessage message) {
                if(MediaPlayerManager.getInstance().getPlayList() == null
                        || MediaPlayerManager.getInstance().getPlayList().size() == 0){

                    playMusicAdapter.notifyDataSetChanged();
                    dismiss();

                }else {
                    playMusicAdapter.notifyDataSetChanged();
                }
            }
        };
        MessageManager.getInstance().registerMessageEvent(MessageTag.OPEN_MUSIC_ID, baseEvent);

        ivClear = view.findViewById(R.id.iv_clear);
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MediaPlayerManager.getInstance().checkCanPlay()) {
                    CommonOkCancelDialog commonOkCancelDialog = new CommonOkCancelDialog("清空列表", "是否清空播放列表？", new CommonOkCancelDialog.Callback() {
                        @Override
                        public void ok() {
                            MediaPlayerManager.getInstance().clearPlayList();
                            playMusicAdapter.notifyDataSetChanged();
                            dismiss();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                    FragmentActivity fragmentActivity = (FragmentActivity) getActivity();
                    commonOkCancelDialog.show(fragmentActivity.getSupportFragmentManager(), "");
                }else {
                    ToastUtils.toast(getActivity(),"播放列表已经为空!");
                }
            }
        });
        return view;
    }

    @Override
    protected void dismissBefore() {
        super.dismissBefore();
        MessageManager.getInstance().unRegisterMessageEvent(MessageTag.OPEN_MUSIC_ID, baseEvent);
    }
}
