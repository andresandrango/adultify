package org.example.game.data.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.example.game.Mission.MissionState;

import java.time.LocalDate;

@Data
@Builder
@Jacksonized
public class Mission {

    @Getter @Setter
    String id;

    @Getter @Setter
    String name;

    @Getter @Setter
    MissionState state;

    @Getter @Setter
    Citizen owner;

    @Getter @Setter
    Life reward;

    @Getter @Setter
    LocalDate scheduledAt;

    @Getter @Setter
    LocalDate dueAt;

    @Getter @Setter
    LocalDate completedAt;
}
