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
class MemberViewHolder extends RecyclerView.ViewHolder {

    private final TextView mNameView;
    private final TextView mMemoView;

    MemberViewHolder(View itemView) {
        super(itemView);
        mNameView = itemView.findViewById(R.id.name);
        mMemoView = itemView.findViewById(R.id.memo);
    }

    void update(Member member) {
        mNameView.setText(member.name);
        mMemoView.setText(member.memo);
    }
}
