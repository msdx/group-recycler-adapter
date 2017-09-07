/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */

package com.githang.grouprecycleradapter.demo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * @author 黄浩杭 (msdx.android@qq.com)
 * @since 2017-09-07
 */
class TeamViewHolder extends RecyclerView.ViewHolder {
    private final TextView mTitleView;

    TeamViewHolder(View itemView) {
        super(itemView);
        mTitleView = itemView.findViewById(R.id.title);
    }

    void update(Team team) {
        mTitleView.setText(team.title);
    }
}
