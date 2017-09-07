/*
 * Copyright (c) 2017. Xi'an iRain IOT Technology service CO., Ltd (ShenZhen). All Rights Reserved.
 */

package com.githang.grouprecycleradapter.demo;

import java.util.List;

/**
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 * @since 2017-09-07
 */
public class Team {
    public final String title;
    public final List<Member> members;

    public Team(String title, List<Member> members) {
        this.title = title;
        this.members = members;
    }
}
