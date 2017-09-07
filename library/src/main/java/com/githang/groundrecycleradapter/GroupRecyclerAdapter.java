/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.githang.groundrecycleradapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 分组的RecyclerViewAdapter
 *
 * @param <G>   Group类型
 * @param <GVH> ViewHolder of the group
 * @param <CVH> ViewHolder of the child
 * @author 黄浩杭 (msdx.android@qq.com)
 * @since 2017-04-28 0.1
 */
public abstract class GroupRecyclerAdapter<G, GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter {

    private static final int TYPE_GROUP = 1;
    private static final int TYPE_CHILD = 2;

    private List<G> mGroups;
    private int mItemCount;

    public GroupRecyclerAdapter(List<G> groups) {
        mGroups = groups == null ? new ArrayList<G>() : groups;
        updateItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_GROUP) {
            return onCreateGroupViewHolder(parent);
        } else {
            return onCreateChildViewHolder(parent);
        }
    }

    protected abstract GVH onCreateGroupViewHolder(ViewGroup parent);

    protected abstract CVH onCreateChildViewHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemCount = 0;
        int groupPosition = 0;
        int childPosition = 0;
        int childCount = 0;
        for (G g : mGroups) {
            if (position == itemCount) {
                onBindGroupViewHolder((GVH) holder, groupPosition);
                return;
            }
            itemCount++;
            childPosition = position - itemCount;
            childCount = getChildCount(g);
            if (childPosition < childCount) {
                onBindChildViewHolder((CVH) holder, groupPosition, childPosition);
                return;
            }
            itemCount += childCount;
            groupPosition++;
        }
    }

    protected abstract void onBindGroupViewHolder(GVH holder, int groupPosition);

    protected abstract void onBindChildViewHolder(CVH holder, int groupPosition, int childPosition);

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemType(position) == ItemType.GROUP_TITLE ? TYPE_GROUP : TYPE_CHILD;
    }

    private void updateItemCount() {
        int count = 0;
        for (G group : mGroups) {
            count += getChildCount(group) + 1;
        }
        mItemCount = count;
    }

    public int getGroupCount() {
        return mGroups.size();
    }

    protected abstract int getChildCount(G group);

    public void add(List<G> groups) {
        int lastCount = getItemCount();
        addGroups(groups);
        updateItemCount();
        notifyItemRangeInserted(lastCount, mItemCount - lastCount);
    }

    public void update(List<G> groups) {
        mGroups.clear();
        addGroups(groups);
        updateItemCount();
        notifyDataSetChanged();
    }

    private void addGroups(List<G> groups) {
        if (groups != null) {
            mGroups.addAll(groups);
        }
    }

    public G getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    public ItemType getItemType(final int itemPosition) {
        int count = 0;
        for (G g : mGroups) {
            if (itemPosition == count) {
                return ItemType.GROUP_TITLE;
            }
            count += 1;
            if (itemPosition == count) {
                return ItemType.FIRST_CHILD;
            }
            count += getChildCount(g);
            if (itemPosition < count) {
                return ItemType.NOT_FIRST_CHILD;
            }
        }
        throw new IllegalStateException("Could not find item type for item position " + itemPosition);
    }

    enum ItemType {
        GROUP_TITLE,
        FIRST_CHILD,
        NOT_FIRST_CHILD
    }
}
