package org.example.game;

import lombok.Builder;
import org.example.game.Schedule.ScheduleType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Builder
public class MissionTemplate {

    String name; // assume this is unique
    Life reward;
    Schedule schedule;

    public Mission createMission(World world) throws Exception {
        var today = LocalDate.now();
        LocalDate scheduledAt = today;
        LocalDate dueAt = today;
        return createMission(world, scheduledAt, dueAt);
    }
    public Mission createMission(World world, LocalDate scheduledAt, LocalDate dueAt) throws Exception {
        var missionBuilder = Mission.builder()
                .template(this)
                .name(name)
                .reward(reward)
                .schedule(schedule);

        var today = LocalDate.now();

        if (schedule == null) {
            // This is a one-off
            return missionBuilder
                    .scheduledAt(scheduledAt)
                    .dueAt(dueAt)
                    .build();
        }

        List<Mission> latestSuccessfulCompletedMissions = world.findLatestSuccessfulCompletedMission(this.name, 3);

        boolean hasMissions = !latestSuccessfulCompletedMissions.isEmpty();

        // TODO This assumes frequency is once, but need to resolve for more frequencies
        if (schedule.scheduleType.equals(ScheduleType.WEEKLY)) {
            var weekStart = today.with(DayOfWeek.MONDAY);

            // There is already a mission completed this week, schedule one for next week.
            // Default to schedule at the beginning of period.
            if (hasMissions && (latestSuccessfulCompletedMissions.get(0).completedAt.isEqual(weekStart) ||
                    latestSuccessfulCompletedMissions.get(0).completedAt.isAfter(weekStart))) {
                scheduledAt = today.plusWeeks(1).with(DayOfWeek.MONDAY);
                dueAt = scheduledAt.with(DayOfWeek.SUNDAY);
            } else {
                // It hasn't happened this week; the mission should be completed by the
                // end of the week
                dueAt = today.with(DayOfWeek.SUNDAY);
            }


        } else if (schedule.scheduleType.equals(ScheduleType.MONTHLY)) {
            var monthStart = today.withDayOfMonth(1);
            if (hasMissions && (latestSuccessfulCompletedMissions.get(0).completedAt.isEqual(monthStart) ||
                    latestSuccessfulCompletedMissions.get(0).completedAt.isAfter(monthStart))) {
                scheduledAt = today.plusMonths(1).withDayOfMonth(1);
                dueAt = scheduledAt.plusDays(scheduledAt.getMonth().length(scheduledAt.isLeapYear()) - 1);
            } else {
                // It hasn't happened this month; the mission should be completed by the
                // end of this month
                dueAt = today.withDayOfMonth(today.getMonth().length(today.isLeapYear()));
            }

        } else if (schedule.scheduleType.equals(ScheduleType.YEARLY)) {
            var yearStart = today.withDayOfYear(1);
            if (hasMissions && (latestSuccessfulCompletedMissions.get(0).completedAt.isEqual(yearStart) ||
                    latestSuccessfulCompletedMissions.get(0).completedAt.isAfter(yearStart))) {
                scheduledAt = today.plusYears(1).withDayOfYear(1);
                dueAt = scheduledAt.plusYears(1).minusDays(1);
            } else {
                // It hasn't happened this year; the mission should be completed by the
                // end of this year
                dueAt = today.withDayOfYear(1).plusYears(1).minusDays(1);
            }

        } else if (schedule.scheduleType.equals(ScheduleType.DAILY)) {
            if (hasMissions && latestSuccessfulCompletedMissions.get(0).completedAt.isEqual(today)) {
                scheduledAt = today.plusDays(1);
                dueAt = scheduledAt;
            }
        } else {
            throw new Exception("Unknown schedule type" + schedule.scheduleType.toString());
        }

        return missionBuilder
                .scheduledAt(scheduledAt)
                .dueAt(dueAt)
                .build();
    }
}
