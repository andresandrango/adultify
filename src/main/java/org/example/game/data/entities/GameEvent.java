package org.example.game.data.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

/**
 * A game engine could read all `GameEvents` that happened in a `World`
 * and recompute its fairness context.
 *
 * The game engine will respect the order provided by the `actionedAt` timestamp.
 *
 * The actual game engine does not have a notion of `GameEvents`, these are only meant to recreate
 * the state of the game engine whenever either the server crashed and we need to rebuild one OR
 * we need to edit history e.g. Bob forgot to mark his mission as completed. Alice knows Bob completed
 * his mission on time; Bob should submit his mission as completed, and his negative `Karma` can be
 * recalculated and likely removed.
 */
@Data
@Builder
@Jacksonized
public class GameEvent {

    @Getter @Setter
    String id;

    @Getter @Setter
    String worldId;

    @Getter @Setter
    String citizenId;

    @Getter @Setter
    Mission missionSnapshot;

    @Getter @Setter
    LocalDate actionedAt;       // The timestamp use to order events
}
