package org.example.game.data;

import org.example.game.data.entities.World;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DatabaseTest {

    @Test
    public void test_getWorld() {
        WorldAdapter worldAdapter = new WorldAdapter();

        List<org.example.game.data.entities.World> worlds = worldAdapter.list();

        for (World w: worlds) {
            System.out.println(w);
        }
    }
}
