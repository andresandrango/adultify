package org.example.game.data.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Citizen {

    @Getter String id;

    @Getter String name;

    @Getter Life life;
}
