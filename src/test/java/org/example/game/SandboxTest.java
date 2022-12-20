package org.example.game;

import org.example.game.Mission.MissionState;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SandboxTest {

    @Test
    public void testCompletingMission() throws Exception {
        Citizen andres = Citizen.builder()
                .name("andres")
                .life(Life.builder()
                        .time(10).build())
                .build();

        World earth = World.builder()
                .name("earth")
                .citizens(List.of(andres))
                .build();

        Mission m1 = Mission.builder()
                .authorizedCrew(List.of(andres))
                .name("bathroom")
                .reward(Life.builder().time(2).build())
                .dueAt(LocalDate.of(2022, 12, 23))
                .build();

        m1.assignTo(andres);
        andres.assignMission(m1); // This should not do anything!

        andres.completeMission(m1);
        m1.complete();

        assertEquals(0, andres.missions.size());
        assertEquals(12, andres.life.getTime());
        assertEquals(0, andres.life.getEnergy());
        assertEquals(MissionState.DONE, m1.getState());
    }
}
