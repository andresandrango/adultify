package org.example.game.data.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class Citizen {

    @Getter String id;

    @Getter String name;
}
