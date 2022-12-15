package org.example.game.data.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Builder
public class World {

    @Getter String id;

    @Getter String name;

    @Getter List<Citizen> citizens;
}
