package org.example;

import org.example.game.Life;
import org.example.game.Mission;
import org.example.game.data.MissionAdapter;

public class Main {
    public static void main(String[] args) {

        Mission mission = Mission.builder()
                .name("bathroom")
                .reward(Life.builder().time(10).build())
                .build();

        MissionAdapter missionAdapter = new MissionAdapter();

        var mData = org.example.game.data.entities.Mission.builder()
                .name(mission.getName())
                .state(mission.getState())
                .reward(org.example.game.data.entities.Life.builder()
                        .energy(mission.getReward().getEnergy())
                        .time(mission.getReward().getTime())
                        .build())
                .build();

        var mDataUpdated = missionAdapter.create(mData);
        System.out.println(mDataUpdated);
    }
}