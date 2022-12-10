package org.example.game;

import lombok.Builder;
import lombok.Builder.Default;

import java.util.ArrayList;
import java.util.List;

// Group of missions
@Builder
public class Campaign {

    String name;

    @Default List<Mission> missionList = new ArrayList<>();

    @Default List<MissionTemplate> missionTemplates = new ArrayList<>();
}
