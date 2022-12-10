package org.example.game;

import org.example.game.Schedule.FrequencyType;
import org.example.game.Schedule.ScheduleType;

import java.util.Date;

public class Factory {

    public static Citizen getDefaultCitizen(String name) {
        return Citizen.builder().name(name).life(Life.builder().energy(100).time(24) // hours
                .build()).build();
    }

    public static Mission getDefaultTask() {
        return Mission.builder().name("clean bathroom (room)").reward(Life.builder().energy(10).time(2).build()).schedule(
                Schedule.builder().startAt(new Date()).scheduleType(ScheduleType.MONTHLY)
                        .frequencyType(FrequencyType.ONCE).build()).build();
    }
}
