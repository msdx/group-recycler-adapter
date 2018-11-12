/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.githang.groundrecycleradapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
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
 * @version 2018-4-28 0.2.1
 * @since 2017-04-28 0.1
 */
public abstract class GroupRecyclerAdapter<G, GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter {

    public static final int INVALID_POSITION = -1;

    private static final int TYPE_GROUP = 1;
    private static final int TYPE_CHILD = 2;

    private List<G> mGroups;
    private int mItemCount;

    private OnGroupClickListener mOnGroupClickListener;
    private OnChildClickListener mOnChildClickListener;

    public GroupRecyclerAdapter(List<G> groups) {
        mGroups = groups == null ? new ArrayList<G>() : groups;
        updateItemCount();
    }

    public OnGroupClickListener getOnGroupClickListener() {
        return mOnGroupClickListener;
    }

    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener) {
        mOnGroupClickListener = onGroupClickListener;
    }

    public OnChildClickListener getOnChildClickListener() {
        return mOnChildClickListener;
    }

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        mOnChildClickListener = onChildClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_GROUP) {
            final GVH viewHolder = onCreateGroupViewHolder(parent);
            if (mOnGroupClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnGroupClickListener != null) {
                            final int itemPosition = viewHolder.getAdapterPosition();
                            final int groupPosition = getGroupChildPosition(itemPosition).group;
                            mOnGroupClickListener.onGroupItemClick(v, groupPosition);
                        }
                    }
                });
            }
            return viewHolder;
        } else {
            final CVH viewHolder = onCreateChildViewHolder(parent);
            if (mOnChildClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnChildClickListener != null) {
                            final int itemPosition = viewHolder.getAdapterPosition();
                            final Position position = getGroupChildPosition(itemPosition);
                            mOnChildClickListener.onChildClick(v, position.group, position.child);
                        }
                    }
                });
            }
            return viewHolder;
        }
    }

    protected abstract GVH onCreateGroupViewHolder(ViewGroup parent);

    protected abstract CVH onCreateChildViewHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int itemPosition) {
        Position position = getGroupChildPosition(itemPosition);
        if (position.child == INVALID_POSITION) {
            onBindGroupViewHolder((GVH) holder, position.group);
        } else {
            onBindChildViewHolder((CVH) holder, position.group, position.child);
        }
    }

    public Position getGroupChildPosition(int itemPosition) {
        int itemCount = 0;
        int childCount;
        final Position position = new Position();
        for (G g : mGroups) {
            if (itemPosition == itemCount) {
                position.child = INVALID_POSITION;
                return position;
            }
            itemCount++;
            childCount = getChildCount(g);
            if (childCount > 0) {
                position.child = itemPosition - itemCount;
                if (position.child < childCount) {
                    return position;
                }
                itemCount += childCount;
            }
            position.group++;
        }
        return position;
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
        int childCount;
        for (G g : mGroups) {
            if (itemPosition == count) {
                return ItemType.GROUP_TITLE;
            }
            childCount = getChildCount(g);
            count += 1;
            if (itemPosition == count && childCount != 0) {
                return ItemType.FIRST_CHILD;
            }
            count += childCount;
            if (itemPosition < count) {
                return ItemType.NOT_FIRST_CHILD;
            }
        }
        throw new IllegalStateException("Could not find item type for item position " + itemPosition);
    }

    public enum ItemType {
        GROUP_TITLE,
        FIRST_CHILD,
        NOT_FIRST_CHILD
    }

    public static class Position {
        public int group;
        public int child = INVALID_POSITION;
    }
}
