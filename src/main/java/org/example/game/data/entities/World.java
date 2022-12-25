package org.example.game.data.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class World {

    @Getter String id;

    @Getter String name;

    @Getter List<Citizen> citizens;
}
