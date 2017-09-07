/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */
package com.githang.groundrecycleradapter;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 分组列表的分割线
 *
 * @author 黄浩杭 (msdx.android@qq.com)
 * @since 2017-05-02 0.1
 */
public class GroupItemDecoration extends RecyclerView.ItemDecoration {
    private GroupRecyclerAdapter mAdapter;
    private Drawable mGroupDivider;
    private Drawable mTitleDivider;
    private Drawable mChildDivider;
    private boolean mFirstDividerEnabled = true;
    private final Rect mBounds = new Rect();

    public GroupItemDecoration(GroupRecyclerAdapter adapter) {
        mAdapter = adapter;
    }

    public void setGroupDivider(Drawable groupDivider) {
        mGroupDivider = groupDivider;
    }

    public void setTitleDivider(Drawable titleDivider) {
        mTitleDivider = titleDivider;
    }

    public void setChildDivider(Drawable childDivider) {
        mChildDivider = childDivider;
    }

    public void setFirstDividerEnabled(boolean enabled) {
        mFirstDividerEnabled = enabled;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (!mFirstDividerEnabled) {
            if (position == 0) {
                return;
            }
        }

        Drawable drawable = getDividerDrawable(position);
        if (drawable != null) {
            outRect.top = drawable.getIntrinsicHeight();
        }
        view.setTag(R.id.item_divider, drawable);
    }

    @Nullable
    private Drawable getDividerDrawable(int position) {
        GroupRecyclerAdapter.ItemType type = mAdapter.getItemType(position);
        switch (type) {
            case GROUP_TITLE:
                return mGroupDivider;
            case FIRST_CHILD:
                return mTitleDivider;
            case NOT_FIRST_CHILD:
                return mChildDivider;
            default:
                return null;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        canvas.save();
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            Object object = child.getTag(R.id.item_divider);
            if (object != null && object instanceof Drawable) {
                Drawable drawable = (Drawable) object;
                final int top = mBounds.top + Math.round(child.getTranslationX());
                final int bottom = mBounds.top + drawable.getIntrinsicHeight();
                drawable.setBounds(left, top, right, bottom);
                drawable.draw(canvas);
            }
        }
        canvas.restore();
    }
}
