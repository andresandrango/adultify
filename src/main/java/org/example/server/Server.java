package org.example.server;

import io.javalin.Javalin;
import org.example.game.data.CitizenAdapter;
import org.example.game.data.WorldAdapter;

public class Server {
    public static void main(String[] args) {

        WorldAdapter worldAdapter = new WorldAdapter();
        CitizenAdapter citizenAdapter = new CitizenAdapter();

        var app = Javalin.create(/*config*/);

        app.get("/", ctx -> ctx.result("Hello World"));

        app.get("/worlds", ctx -> {
            var worlds = worldAdapter.getWorlds();
            ctx.json(worlds);
        });

        app.get("/worlds/{id}", ctx -> {
            ctx.json(worldAdapter.getWorld(ctx.pathParam("id")));
        });

        app.delete("/worlds/{id}", ctx -> {
           if (worldAdapter.removeWorld(ctx.pathParam("id"))) {
               ctx.status(200);
           } else {
               ctx.status(400);
           }
        });

        app.post("/worlds/create", ctx -> {
            ctx.json(worldAdapter.createWorld(ctx.formParam("name")));
        });

        app.get("/citizens", ctx -> {
            var citizens = citizenAdapter.getCitizens();
            ctx.json(citizens);
        });

        app.get("/citizens/{id}", ctx -> {
            ctx.json(citizenAdapter.getCitizen(ctx.pathParam("id")));
        });

        app.post("/citizens/create", ctx -> {
            ctx.json(citizenAdapter.createCitizen(ctx.formParam("name")));
        });

        app.delete("/citizens/{id}", ctx -> {
            if (citizenAdapter.deleteCitizen(ctx.pathParam("id"))) {
                ctx.status(200);
            } else {
                ctx.status(400);
            }
        });

        app.post("/worlds/{wId}/add-citizen/{cId}", ctx -> {
           if (worldAdapter.addCitizen(ctx.pathParam("wId"), ctx.pathParam("cId"))) {
               ctx.json(worldAdapter.getWorld(ctx.pathParam("wId")));
           } else {
               ctx.status(400);
           }
        });

        app.post("/worlds/{wId}/remove-citizen/{cId}", ctx -> {
            if (worldAdapter.removeCitizen(ctx.pathParam("wId"), ctx.pathParam("cId"))) {
                ctx.json(worldAdapter.getWorld(ctx.pathParam("wId")));
            } else {
                ctx.status(400);
            }
        });

        app.start(7070);
    }
}