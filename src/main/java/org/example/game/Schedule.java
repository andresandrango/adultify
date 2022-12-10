package org.example.game;

import lombok.Builder;

import java.util.Date;


// Least granularity is day level
@Builder
public class Schedule {

    public enum ScheduleType {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

    public enum FrequencyType {
        ONCE,
        TWICE,
        THREE_TIMES
    }

    enum StateType {
        ACTIVE,
        INACTIVE
    }

    Date startAt;
    Date endAt;
    ScheduleType scheduleType;
    FrequencyType frequencyType;
    StateType stateType;
}
