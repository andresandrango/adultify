package org.example.game;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.example.game.data.WorldAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Every world is unique as far as point system is concerned.
 * The idea of the game is that whenever a world start becoming "unbalanced", the world is smart
 * enough to self-heal e.i. the world will reward good citizens with say 1.5x good karma multiplier or extra
 * time to complete missions, etc.
 */
@SuperBuilder
public class World {

    @Getter String name;

    @Getter List<Citizen> citizens;

    @Setter List<Mission> completedMissions;
    @Setter List<Mission> unassignedMissions;
    @Setter List<Mission> assignedMissions;

    List<MissionTemplate> missionTemplates;

    public World(org.example.game.data.entities.World data) {
        name = data.getName();
        citizens = data.getCitizens().stream().map(Citizen::new).collect(Collectors.toList());
    }

    public List<Mission> findLatestSuccessfulCompletedMission(String missionName, int n) {
        return List.of();
    }

    @Override
    public String toString() {
        return name + "\n citizens:" + Arrays.toString(citizens.toArray());
    }

    public boolean assignMission(Mission mission) {
        if (mission.owner != null) return false; // already assigned

        if (mission.authorizedCrew.size() == 1) {
            mission.assignTo(mission.authorizedCrew.get(0));
        } else {
            // There are more than 1 citizen that can complete this mission;
            // we need to figure out how to fairly assign this
            // TODO Move this to fair engine
            Citizen citizen = findLeastBusyCitizen(mission.authorizedCrew);
            mission.assignTo(citizen);
        }
        return true;
    }

    public boolean completeMission(Mission mission, Citizen citizen) {
        // TODO move this to a fair engine
        boolean isOverdue = mission.isOverdue();
        float rewardMultiplier = isOverdue ? 0.75f : 1f; // -25%

        mission.complete();
        try {
            citizen.completeMission(mission, rewardMultiplier);
        } catch (Exception ex) {
            // TODO handle exception
        }

        return true;
    }

    public boolean scheduleNextMission(MissionTemplate template) {

        try {
            Mission nextMissionFromTemplate = template.createMission(this);
            unassignedMissions.add(nextMissionFromTemplate);
        } catch (Exception e) {
            // TODO handle this
            return false;
        }
        return true;
    }

    private Citizen findLeastBusyCitizen(List<Citizen> citizens) {
        Citizen out = citizens.get(0);
        for (Citizen c : citizens) {
            if (c.stats.workAssignedForCurrentCycle().compareTo(out.stats.workAssignedForCurrentCycle()) == -1) out = c;
        }
        return out;
    }
}
