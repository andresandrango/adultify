package org.example.game.data.entities;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Life {

    @Getter @Setter int energy;

    @Getter @Setter int time;
}
