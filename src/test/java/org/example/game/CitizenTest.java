package org.example.game;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CitizenTest {

    @Test
    public void test_stats() {
        Citizen citizen = Citizen.builder()
                .name("Andres")
                .build();

        Mission mission1 = Mission.builder()
                .reward(Life.builder().energy(10).time(2).build())
                .dueAt(LocalDate.now().plusDays(1))
                .build();

        mission1.assignTo(citizen);

        World world = World.builder().build();

        assertEquals(10, citizen.stats.currentlyAssigned.getEnergy());
        assertEquals(2, citizen.stats.currentlyAssigned.getTime());
        assertEquals(1, citizen.missions.size());

        world.completeMission(mission1, citizen);

        assertEquals(0, citizen.stats.currentlyAssigned.getEnergy());
        assertEquals(0, citizen.stats.currentlyAssigned.getTime());
        assertTrue(citizen.missions.isEmpty());
    }
}
