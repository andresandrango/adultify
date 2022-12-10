package org.example.game;

import org.example.game.Schedule.FrequencyType;
import org.example.game.Schedule.ScheduleType;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

// TODO Refactor code that uses LocalDate.now() to take in a clock.
// Need to introduce a dependency injection framework + a clock for both prod and test
public class MissionTemplateTest {

    @Test
    public void test_create_mission_weekly_first_instance() throws Exception {
        MissionTemplate missionTemplate = MissionTemplate.builder()
                .name("bath1")
                .reward(Life.builder()
                        .energy(10)
                        .time(2)
                        .build())
                .schedule(Schedule.builder()
                        .scheduleType(ScheduleType.WEEKLY)
                        .frequencyType(FrequencyType.ONCE)
                        .build())
                .build();


        Mission mission = missionTemplate.createMission(World.builder().build());

        assertEquals(mission.scheduledAt, LocalDate.now());
        assertEquals(mission.dueAt, LocalDate.now().with(DayOfWeek.SUNDAY));
    }

    @Test
    public void test_create_mission_monthly_first_instance() throws Exception {
        MissionTemplate missionTemplate = MissionTemplate.builder()
                .name("bath1")
                .reward(Life.builder()
                        .energy(10)
                        .time(2)
                        .build())
                .schedule(Schedule.builder()
                        .scheduleType(ScheduleType.MONTHLY)
                        .frequencyType(FrequencyType.ONCE)
                        .build())
                .build();


        Mission mission = missionTemplate.createMission(World.builder().build());

        assertEquals(LocalDate.now(), mission.scheduledAt);
        assertEquals(LocalDate.now().withDayOfMonth(LocalDate.now().getMonth().length(LocalDate.now().isLeapYear())), mission.dueAt);
    }

    @Test
    public void test_create_mission_yearly_first_instance() throws Exception {
        MissionTemplate missionTemplate = MissionTemplate.builder()
                .name("bath1")
                .reward(Life.builder()
                        .energy(10)
                        .time(2)
                        .build())
                .schedule(Schedule.builder()
                        .scheduleType(ScheduleType.YEARLY)
                        .frequencyType(FrequencyType.ONCE)
                        .build())
                .build();


        Mission mission = missionTemplate.createMission(World.builder().build());

        assertEquals(LocalDate.now(), mission.scheduledAt);
        assertEquals(LocalDate.now().withDayOfYear(1).plusYears(1).minusDays(1), mission.dueAt);
    }
}
