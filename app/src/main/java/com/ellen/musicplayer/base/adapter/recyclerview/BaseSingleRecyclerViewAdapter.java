package com.ellen.musicplayer.base.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseSingleRecyclerViewAdapter<T, VH extends BaseViewHolder> extends BaseRecyclerViewAdapter<BaseViewHolder> {

    private WeakReference<Context> contextWeakReference;
    private List<T> dataList;
    private int headerType = 0;
    private int footerType = 0;
    private Map<Integer, BaseViewHolder> headerMap;
    private Map<View, Integer> headerViewMap;
    private List<View> headerViewList;
    private Map<Integer, BaseViewHolder> footerMap;
    private Map<View, Integer> footerViewMap;
    private List<View> footerViewList;
    private RefreshLoadMoreCallback refreshLoadMoreCallback;

    public RefreshLoadMoreCallback getRefreshLoadMoreCallback() {
        return refreshLoadMoreCallback;
    }

    public void setRefreshLoadMoreCallback(RefreshLoadMoreCallback refreshLoadMoreCallback) {
        this.refreshLoadMoreCallback = refreshLoadMoreCallback;
    }

    public void setDataList(List<T> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //防止第一次刷新回调接口不调用的问题
        if (refreshLoadMoreCallback != null) {
            refreshLoadMoreCallback.refreshSuccess(true);
        }
    }

    public void addHeaderView(View headerView) {
        if (headerView == null) return;
        headerType--;
        if (headerMap == null) {
            headerMap = new HashMap<>();
        }
        if (headerViewMap == null) {
            headerViewMap = new HashMap<>();
        }
        if (headerViewList == null) {
            headerViewList = new ArrayList<>();
        }
        headerMap.put(headerType, new HeaderViewHolder(headerView));
        headerViewMap.put(headerView, headerType);
        headerViewList.add(headerView);
        notifyDataSetChanged();
    }

    public void removeHeaderView(View headerView) {
        if (headerView == null) return;
        if (headerViewMap == null) return;
        if (!headerViewMap.containsKey(headerView)) return;
        int value = headerViewMap.get(headerView);
        headerViewMap.remove(headerView);
        headerMap.remove(value);
        headerViewList.remove(headerView);
        notifyDataSetChanged();
    }

    public void addFooterView(View footerView) {
        if (footerView == null) return;
        footerType++;
        if (footerMap == null) {
            footerMap = new HashMap<>();
        }
        if (footerViewMap == null) {
            footerViewMap = new HashMap<>();
        }
        if (footerViewList == null) {
            footerViewList = new ArrayList<>();
        }
        footerMap.put(footerType, new FooterViewHeader(footerView));
        footerViewMap.put(footerView, footerType);
        footerViewList.add(footerView);
        notifyDataSetChanged();
    }

    public void removeFootView(View footerView) {
        if (footerView == null) return;
        if (footerViewMap == null) return;
        if (!footerViewMap.containsKey(footerView)) return;
        int value = footerViewMap.get(footerView);
        footerViewMap.remove(footerView);
        footerMap.remove(value);
        footerViewList.remove(footerView);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        if (headerViewList != null && headerViewList.size() > 0) {
            if ((position + 1) <= headerViewList.size()) {
                if (headerViewList != null && headerViewList.size() > 0) {
                    return headerViewMap.get(headerViewList.get(position));
                }
            }
        }

        if (footerViewList != null && footerViewList.size() > 0) {
            int headerItemCount = headerViewList == null ? 0 : headerViewList.size();
            if ((position + 1) > headerItemCount + getDataList().size()) {
                int current = ((position + 1) - headerItemCount - getDataList().size()) - 1;
                return footerViewMap.get(footerViewList.get(current));
            }
        }

        return 0;
    }

    public BaseSingleRecyclerViewAdapter(Context context, List<T> dataList) {
        contextWeakReference = new WeakReference<>(context);
        this.dataList = dataList;
    }

    public Context getContext() {
        return contextWeakReference.get();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {
        if (itemType < 0) {
            return headerMap.get(itemType);
        }
        if (itemType > 0) {
            return footerMap.get(itemType);
        }
        View view = LayoutInflater.from(getContext()).inflate(getItemLayoutId(), null);
        return getNewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, final int position) {
        //判断position是否为数据位置
        int headerItemCount = headerViewList == null ? 0 : headerViewList.size();
        if ((position + 1) > headerItemCount && (position + 1) <= headerItemCount + getDataList().size()) {
            final VH vh = (VH) baseViewHolder;
            int truePosition = position;
            if (headerType != 0) {
                truePosition = position + headerType;
            }
            showData(vh, getDataList().get(truePosition), truePosition);
            if (onItemClickListener != null) {
                final int finalTruePosition = truePosition;
                vh.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(vh, finalTruePosition);
                    }
                });
            }
            if(onItemLongClickListener != null){
                final int finalTruePosition = truePosition;
                vh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return onItemLongClickListener.onItemLongClick(vh,finalTruePosition);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        int headerItemSize = headerViewList == null ? 0 : headerViewList.size();
        int footerItemSize = footerViewList == null ? 0 : footerViewList.size();
        int itemSize = dataList == null ? 0 : dataList.size();
        return itemSize + headerItemSize + footerItemSize;
    }

    public List<T> getDataList() {
        return dataList;
    }

    protected abstract int getItemLayoutId();

    protected abstract VH getNewViewHolder(View view);

    protected abstract void showData(VH vh, T data, int position);

    private static class HeaderViewHolder extends BaseViewHolder {
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private static class FooterViewHeader extends BaseViewHolder {
        public FooterViewHeader(@NonNull View itemView) {
            super(itemView);
        }
    }

    /**
     * 下拉刷新成功
     *
     * @param dataList
     */
    public void refreshSuccess(List<T> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
        if (refreshLoadMoreCallback != null) {
            refreshLoadMoreCallback.refreshSuccess(false);
        }
    }

    /**
     * 上拉加载更多成功
     *
     * @param dataList
     */
    public void loadMoreSuccess(List<T> dataList) {
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
        if (refreshLoadMoreCallback != null) {
            refreshLoadMoreCallback.loadMoreSuccess();
        }
    }

    /**
     * 下拉刷新失败
     */
    public void refreshFailure(String errMessage) {
        if (refreshLoadMoreCallback != null) {
            refreshLoadMoreCallback.refreshFailure(errMessage);
        }
    }

    /**
     * 上拉加载更多失败
     *
     * @param errMessage
     */
    public void loadMoreFailure(String errMessage) {
        if (refreshLoadMoreCallback != null) {
            refreshLoadMoreCallback.loadMoreFailure(errMessage);
        }
    }

    /**
     * 重置(RecyclerView恢复到最初始状态)
     */
    public void reSet() {
        this.dataList.clear();
        headerType = 0;
        footerType = 0;
        headerMap = null;
        headerViewMap = null;
        headerViewList = null;
        footerMap = null;
        footerViewMap = null;
        footerViewList = null;
        notifyDataSetChanged();
    }

    /**
     * 仅仅只清除数据(不包含Header和Footer)
     */
    public void clearData(boolean isRefresh) {
        this.dataList.clear();
        if (isRefresh) {
            notifyDataSetChanged();
        }
    }

    public void clearHeaderAndFooter() {
        headerType = 0;
        footerType = 0;
        headerMap = null;
        headerViewMap = null;
        headerViewList = null;
        footerMap = null;
        footerViewMap = null;
        footerViewList = null;
        notifyDataSetChanged();
    }

    public interface RefreshLoadMoreCallback {
        void refreshSuccess(boolean isFirstRefresh);

        void refreshFailure(String errMessage);

        void loadMoreSuccess();

        void loadMoreFailure(String errMessage);
    }
}
