package org.example.game;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * The motivations to be a good citizen is to:
 *      - complete missions on a timely fashion
 */
@SuperBuilder
public class Citizen {

    @Getter String id;

    @Getter String name;

    @Default Life life = Life.builder()
            .time(0)
            .energy(0)
            .build();

    @Default CitizenStats stats = CitizenStats.builder().build();

    // Active missions; scheduled and in progress
    @Default List<Mission> missions = new ArrayList<>();

    public Citizen(org.example.game.data.entities.Citizen data) {
        name = data.getName();
        life = new Life(data.getLife());
    }

    public void completeMission(Mission mission) throws Exception {
        completeMission(mission, 1);
    }

    public void completeMission(Mission mission, float rewardMultiplier) throws Exception {
        if (missions.remove(mission)) {
            mission.markComplete();

            Life calcReward = mission.reward.cloneWithMultiplier(rewardMultiplier);
            life.addLife(calcReward);
            // Remove the actual reward added as part of stats
            // TODO just recalc from 'missions' array.
            stats.currentlyAssigned.consumeLife(mission.reward);
            return;
        }
        throw new Exception(String.format("mission %s was not part of the active missions for citizen %s", mission.name, name));
    }

    public void assignMission(Mission mission) {
        if (mission.owner != this) mission.assignTo(this);
        if (!alreadyAssigned(mission)) {
            missions.add(mission);
            stats.currentlyAssigned.addLife(mission.reward);
        }
    }

    public boolean alreadyAssigned(Mission mission) {
        for (Mission m: missions) {
            if (m == mission) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "\nCitizen " + name + "\n\t" + life;
    }
}
