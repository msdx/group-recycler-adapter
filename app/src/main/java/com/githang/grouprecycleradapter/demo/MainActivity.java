package com.githang.grouprecycleradapter.demo;

import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.githang.groundrecycleradapter.GroupItemDecoration;
import com.githang.groundrecycleradapter.GroupRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<Member> members = new ArrayList<>();
            String teamName = Character.toString((char) ((int) ('A') + i)) + "队";
            for (int j = 0; j < 2; j++) {
                members.add(new Member("姓名" + j, teamName + j));
            }
            teams.add(new Team(teamName, members));
        }

        final LayoutInflater layoutInflater = LayoutInflater.from(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        GroupRecyclerAdapter<Team, TeamViewHolder, MemberViewHolder> recyclerAdapter =
                new GroupRecyclerAdapter<Team, TeamViewHolder, MemberViewHolder>(teams) {
                    @Override
                    protected TeamViewHolder onCreateGroupViewHolder(ViewGroup parent) {
                        return new TeamViewHolder(layoutInflater.inflate(R.layout.item_team_title, parent, false));
                    }

                    @Override
                    protected MemberViewHolder onCreateChildViewHolder(ViewGroup parent) {
                        return new MemberViewHolder(layoutInflater.inflate(R.layout.item_team_member, parent, false));
                    }

                    @Override
                    protected void onBindGroupViewHolder(TeamViewHolder holder, int groupPosition) {
                        holder.update(getGroup(groupPosition));
                    }

                    @Override
                    protected void onBindChildViewHolder(MemberViewHolder holder, int groupPosition, int childPosition) {
                        holder.update(getGroup(groupPosition).members.get(childPosition));
                    }

                    @Override
                    protected int getChildCount(Team group) {
                        return group.members.size();
                    }
                };
        recyclerView.setAdapter(recyclerAdapter);

        GroupItemDecoration decoration = new GroupItemDecoration(recyclerAdapter);
        decoration.setGroupDivider(ResourcesCompat.getDrawable(getResources(), R.drawable.divider_height_16_dp, null));
        decoration.setTitleDivider(ResourcesCompat.getDrawable(getResources(), R.drawable.divider_height_1_px, null));
        decoration.setChildDivider(ResourcesCompat.getDrawable(getResources(), R.drawable.divider_white_header, null));
        recyclerView.addItemDecoration(decoration);
    }
}
