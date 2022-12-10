package org.example.game;

import lombok.Builder;
import lombok.Getter;

@Builder
public class Player {

    @Getter String name;

    Life life;
}
