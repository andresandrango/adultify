package org.example.game;

import org.example.game.Mission.MissionState;
import org.example.game.data.MissionAdapter;
import org.example.game.data.WorldAdapter;
import org.example.game.data.entities.Citizen;
import org.example.game.data.entities.GameEvent;
import org.example.game.data.entities.Mission;

import java.util.List;
import java.util.stream.Collectors;

public class GameEngine {

    void loadFromDB(String wId) {

        var worldAdapter = new WorldAdapter();

        var worldData = worldAdapter.get(wId);

        var world = new World(worldData);

        // TODO create a snapshot entity so we don't need to load the entire history

        var missionAdapter = new MissionAdapter();

        List<Mission> missionsData = missionAdapter.listByWorld(wId);

        world.setUnassignedMissions(missionsData.stream()
                .filter(mission -> mission.getState().equals(MissionState.CREATED))
                .map(org.example.game.Mission::new)
                .collect(Collectors.toList()));

        world.setAssignedMissions(missionsData.stream()
                .filter(mission -> mission.getState().equals(MissionState.STARTED))
                .map(org.example.game.Mission::new)
                .collect(Collectors.toList()));

        world.setCompletedMissions(missionsData.stream()
                .filter(mission -> mission.getState().equals(MissionState.DONE))
                .map(org.example.game.Mission::new)
                .collect(Collectors.toList()));

        // TODO
//        var gameEventAdapter = new GameEventAdapter();
//        List<GameEvent> gameEvents = gameEventAdapter.listByWorld(wId);
//
//
//        gameEvents.forEach(event -> {
//            try {
//                replayEvent(world, event);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
    }

    void replayEvent(
            World world,
            GameEvent gEvent
    ) throws Exception {
        var oldMission = gEvent.getMissionSnapshot();

        var citizen = world.citizens.stream()
                .filter(c -> c.getId().equals(gEvent.getCitizenId()))
                .collect(Collectors.toList())
                .get(0);

        if (oldMission.getCompletedAt().isAfter(gEvent.getActionedAt())) {
            // this mission was completed late
            citizen.completeMission(new org.example.game.Mission(gEvent.getMissionSnapshot()), 0.75f);
        } else {
            // this mission was completed on time
            citizen.completeMission(new org.example.game.Mission(gEvent.getMissionSnapshot()), 1.0f);
        }
    }
}
