/*
 * Copyright (c) 2018. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */

package com.githang.groundrecycleradapter;

import android.view.View;

/**
 * Child 被点击的回调事件。
 *
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @since 2018-01-09 0.2
 */
public interface OnChildClickListener {
    /**
     * Callback when the child item was clicked.
     *
     * @param itemView      the itemView of the child item.
     * @param groupPosition the position of the group that the child item was clicked.
     * @param childPosition the position of the child in group.
     */
    void onChildClick(View itemView, int groupPosition, int childPosition);
}
