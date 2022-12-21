package org.example.game.data.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.example.game.Mission.MissionState;

import java.time.LocalDate;

@Data
@Builder
public class Mission {

    @Getter String id;

    @Getter String name;

    @Getter MissionState state;

    @Getter Citizen owner;

    @Getter Life reward;

    @Getter LocalDate scheduledAt;

    @Getter LocalDate dueAt;

    @Getter LocalDate completedAt;
}
