package org.example.server;

import io.javalin.Javalin;
import org.example.game.Mission.MissionState;
import org.example.game.data.CitizenAdapter;
import org.example.game.data.MissionAdapter;
import org.example.game.data.WorldAdapter;
import org.example.game.data.entities.Life;
import org.example.game.data.entities.Mission;

public class Server {
    public static void main(String[] args) {

        WorldAdapter worldAdapter = new WorldAdapter();
        CitizenAdapter citizenAdapter = new CitizenAdapter();
        MissionAdapter missionAdapter = new MissionAdapter();

        var app = Javalin.create(/*config*/);

        app.get("/", ctx -> ctx.result("Hello World"));

        app.get("/worlds", ctx -> {
            var worlds = worldAdapter.list();
            ctx.json(worlds);
        });

        app.get("/worlds/{id}", ctx -> {
            ctx.json(worldAdapter.get(ctx.pathParam("id")));
        });

        app.delete("/worlds/{id}", ctx -> {
           if (worldAdapter.delete(ctx.pathParam("id"))) {
               ctx.status(200);
           } else {
               ctx.status(400);
           }
        });

        app.post("/worlds/create", ctx -> {
            ctx.json(worldAdapter.create(ctx.formParam("name")));
        });

        app.post("/worlds/{wId}/add-citizen/{cId}", ctx -> {
            if (worldAdapter.addCitizen(ctx.pathParam("wId"), ctx.pathParam("cId"))) {
                ctx.json(worldAdapter.get(ctx.pathParam("wId")));
            } else {
                ctx.status(400);
            }
        });

        app.post("/worlds/{wId}/remove-citizen/{cId}", ctx -> {
            if (worldAdapter.removeCitizen(ctx.pathParam("wId"), ctx.pathParam("cId"))) {
                ctx.json(worldAdapter.get(ctx.pathParam("wId")));
            } else {
                ctx.status(400);
            }
        });

        app.get("/citizens", ctx -> {
            var citizens = citizenAdapter.list();
            ctx.json(citizens);
        });

        app.get("/citizens/{id}", ctx -> {
            ctx.json(citizenAdapter.get(ctx.pathParam("id")));
        });

        app.get("/citizens/{id}/worlds", ctx -> {
            ctx.json(worldAdapter.listByCitizen(ctx.pathParam("id")));
        });

        app.get("/citizens/{id}/missions", ctx -> {
           ctx.json(missionAdapter.listByOwner(ctx.pathParam("id")));
        });

        app.post("/citizens/create", ctx -> {
            ctx.json(citizenAdapter.create(ctx.formParam("name")));
        });

        app.delete("/citizens/{id}", ctx -> {
            if (citizenAdapter.delete(ctx.pathParam("id"))) {
                ctx.status(200);
            } else {
                ctx.status(400);
            }
        });

        app.get("/missions", ctx -> {
            var missions = missionAdapter.list();
            ctx.json(missions);
        });

        app.get("/missions/{id}", ctx -> {
            ctx.json(missionAdapter.get(ctx.pathParam("id")));
        });

        app.post("/missions/{id}/complete", ctx -> {
            // 1. see if there is a game engine up for this world!
            // 1.1. Else, load on up from the database
            // 2. simulate the completion

            // Architecture decision: Given that the interactions with each world daily are very low e.g.
            // one family will complete up to 20 chores per day max, we will always boot up the instance
            // from cold (DB), the main advantage is keeping a stateless pool of app servers; If this is
            // to change or too expensive somehow, we can explore keeping instances HOT (in memory) and
            // adding a gateway layer to help route to the right app server (more of a stateful architecture)

            Mission mission = MissionAdapter.jsonDeserialize(ctx.body());
//            if (mission.getWorld()) {
//                // use world fairness to attribute reward to citizen
//
//                var gameEngine = GameEngineFactory(mission.getWorld());
//
//                gameEngine.completeMission(
//                )
//
//            } else {
//                // only attribute reward to citizen with default fairness
//            }
        });

        app.post("/missions/create", ctx -> {
            Mission mission = MissionAdapter.jsonDeserialize(ctx.body());
            mission.setState(MissionState.CREATED);

            mission = missionAdapter.create(mission);
            ctx.json(mission);
        });

        app.start(7070);
    }
}