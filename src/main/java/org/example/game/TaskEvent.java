package org.example.game;

import lombok.Builder;

import java.util.Date;

@Builder
public class TaskEvent {
    String description;
    Date timestamp;

    @Override
    public String toString() {
        return description + " at " + timestamp.toString();
    }
}
