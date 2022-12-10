package org.example.game;

import lombok.Builder;
import lombok.Getter;

@Builder
public class Life {
    // express in points (roughly 5 points to an hour; though some tasks that are time intensive
    // are not energy intensive and vice-versa)
    @Getter private int energy;

    @Getter private int time;   // express in hours

    public void addLife(Life life) {
        // Energy could be unbounded but time is bounded by exercise period
        // TODO figure out how to bound time or explain why it could be unbounded
        energy += life.energy;
        time += life.time;
    }

    public void consumeLife(Life life) {
        // TODO Allow for negative energy/time. However, in the future, these negative states
        // should be handled differently e.g. operating at a negative time state should trigger a text message
        energy -= life.energy;
        time -= life.time;
    }

    public Life cloneWithMultiplier(float multiplier) {
        return Life.builder()
                .energy((int) Math.max(1, Math.round((float) energy) * multiplier))
                .time((int) Math.max(1, Math.round((float) time) * multiplier))
                .build();
    }

    public int compareTo(Life life) {
        if (energy > life.energy && time > life.time) return 1;
        if (energy == life.energy || time == life.time) return 0;
        return -1;
    }
}
