package org.example.game;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
public class Task {

    public enum TaskState {
        SCHEDULED,
        STARTED,
        PAUSED,
        COMPLETED,
        CANCELLED,
        NONE
    }

    String name;

    Life cost;

    @Builder.Default @Getter List<TaskEvent> recentHistory = new ArrayList<>(); // we should only care about the last 100 or so

    Schedule schedule;

    private TaskState state;

    public boolean start(Citizen citizen) {
        return changeState(TaskState.STARTED, "started by " + citizen.getName());
    }

    public boolean complete(Citizen citizen) {
       return changeState(TaskState.COMPLETED, "completed by " + citizen.getName());
    }

    public boolean changeState(TaskState newState, String description) {
        // TODO check state transitions! return false when there is an invalid state transition
        state = newState;

        recentHistory.add(TaskEvent.builder()
                .description(description)
                .timestamp(new Date())
                .build());

        return true;
    }
}
