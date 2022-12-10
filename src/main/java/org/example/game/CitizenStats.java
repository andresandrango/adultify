package org.example.game;

import lombok.Builder;
import lombok.Builder.Default;

@Builder
public class CitizenStats {

    @Default Life currentlyAssigned = Life.builder().energy(0).time(0).build();

    public Life workAssignedForCurrentCycle() {
        return currentlyAssigned;
    }
}
