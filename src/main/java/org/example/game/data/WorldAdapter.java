package org.example.game.data;

import org.example.game.data.entities.Citizen;
import org.example.game.data.entities.World;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorldAdapter implements Adapter<World> {

    @Override
    public World get(String wUuid) {

        return Utils.execute("SELECT * FROM world where id = '" + wUuid + "'", rs -> {
            try {
                return deserializeWorld(rs);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public World create(String name) {
        final String id = Utils.executeUpdate("INSERT INTO world (name) values ('"+ name + "')");

        if (id != null) {
            return get(id);
        } else {
            return null;
        }
    }

    @Override
    public World create(final World obj) {
        return null;
    }

    @Override
    public boolean delete(String wId) {
        return Utils.executeUpdateNoKeys(String.format("DELETE FROM world where id = '%s'", wId));
    }

    public boolean addCitizen(String wId, String cId) {
        return Utils.executeUpdateNoKeys(String.format("INSERT INTO world_citizen (world,citizen) values ('%s','%s')", wId, cId));
    }

    public boolean removeCitizen(String wId, String cId) {
        return Utils.executeUpdateNoKeys(String.format("DELETE FROM world_citizen WHERE world='%s' and citizen='%s'", wId, cId));
    }

    @Override
    public List<World> list() {
        List<World> worlds = new ArrayList<>();
        return Utils.execute("SELECT * FROM world", worlds, (rs, out) -> {
            try {
                out.add(deserializeWorld(rs));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    // TODO This query is executed per world; change this to be part of a particular
    // query that needs this data
    public List<Citizen> getWorldCitizens(String wUUID) {
        Database db = new Database();

        List<Citizen> citizens = new ArrayList<>();

        return Utils.execute("SELECT citizen.* FROM world " +
                "JOIN world_citizen on world_citizen.world = world.id " +
                "JOIN citizen on world_citizen.citizen = citizen.id " +
                "WHERE world.id = '" + wUUID + "'", citizens, (rs, cs) -> {
            try {
                cs.add(Utils.deserializeCitizen(rs));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    public World deserializeWorld(ResultSet rs) throws SQLException {
        return World.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .citizens(getWorldCitizens(rs.getString("id")))
                .build();
    }


}
