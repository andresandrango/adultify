package org.example.server;

import io.javalin.Javalin;
import org.example.game.data.WorldAdapter;

public class Server {
    public static void main(String[] args) {

        WorldAdapter worldAdapter = new WorldAdapter();

        var app = Javalin.create(/*config*/);

        app.get("/", ctx -> ctx.result("Hello World"));

        app.get("/worlds", ctx -> {
            var worlds = worldAdapter.getWorlds();
            ctx.json(worlds);
        });

        app.get("/worlds/{id}", ctx -> {
            ctx.json(worldAdapter.getWorld(ctx.pathParam("id")));
        });

        app.post("/worlds/create", ctx -> {
            ctx.json(worldAdapter.addWorld(ctx.formParam("name")));
        });

        app.start(7070);
    }
}