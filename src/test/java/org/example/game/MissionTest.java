package org.example.game;

import org.example.game.Mission.MissionState;
import org.example.game.Schedule.FrequencyType;
import org.example.game.Schedule.ScheduleType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MissionTest {

    @Test
    public void test_daysLeft_daily_overdue() {

        LocalDate tenDaysAgo = LocalDate.now().minusDays(10);

        Mission mission = Mission.builder()
                .schedule(Schedule.builder()
                        .scheduleType(ScheduleType.DAILY)
                        .frequencyType(FrequencyType.ONCE)
                        .build())
                .scheduledAt(tenDaysAgo)
                .build();

        assert mission.daysLeft() == -10;
    }

    @Test
    public void test_daysLeft_daily_now() {

        LocalDate now = LocalDate.now();

        Mission mission = Mission.builder()
                .schedule(Schedule.builder()
                        .scheduleType(ScheduleType.DAILY)
                        .frequencyType(FrequencyType.ONCE)
                        .build())
                .scheduledAt(now)
                .build();

        assert mission.daysLeft() == 0;
    }

    @Test
    public void test_daysLeft_daily_scheduled() {

        LocalDate tenDaysFromToday = LocalDate.now().plusDays(10);

        Mission mission = Mission.builder()
                .schedule(Schedule.builder()
                        .scheduleType(ScheduleType.DAILY)
                        .frequencyType(FrequencyType.ONCE)
                        .build())
                .scheduledAt(tenDaysFromToday)
                .build();

        assert mission.daysLeft() == 10;
    }

    @Test
    public void test_isOverdue() {

        LocalDate tenDaysAgo = LocalDate.now().minusDays(10);
        LocalDate fiveDaysAgo = LocalDate.now().minusDays(5);

        Mission mission = Mission.builder()
                .schedule(Schedule.builder()
                        .scheduleType(ScheduleType.DAILY)
                        .frequencyType(FrequencyType.ONCE)
                        .build())
                .scheduledAt(tenDaysAgo)
                .dueAt(fiveDaysAgo)
                .state(MissionState.STARTED)
                .build();

        assertTrue(mission.isOverdue());
    }

    @Test
    public void test_done_not_overdue() {

        LocalDate tenDaysAgo = LocalDate.now().minusDays(10);
        LocalDate fiveDaysAgo = LocalDate.now().minusDays(5);

        Mission mission = Mission.builder()
                .schedule(Schedule.builder()
                        .scheduleType(ScheduleType.DAILY)
                        .frequencyType(FrequencyType.ONCE)
                        .build())
                .scheduledAt(tenDaysAgo)
                .dueAt(fiveDaysAgo)
                .state(MissionState.DONE)
                .build();

        assertEquals(MissionState.DONE, mission.getState());
        assertFalse(mission.isOverdue());
    }
}
