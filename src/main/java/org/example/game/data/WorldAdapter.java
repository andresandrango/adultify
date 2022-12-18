package org.example.game.data;

import org.example.game.data.entities.Citizen;
import org.example.game.data.entities.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class WorldAdapter {

    public World getWorld(String wUuid) {

        return execute("SELECT * FROM world where id = '" + wUuid + "'", rs -> {
            try {
                return deserializeWorld(rs);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public World addWorld(String name) {
        final String id = executeUpdate("INSERT INTO world (name) values ('"+ name + "')");

        if (id != null) {
            return getWorld(id);
        } else {
            return null;
        }
    }
//
//    public boolean addCitizen(String cUuid) {
//
//    }
//
//    public boolean removeCitizen(String cUuid) {
//
//    }

    public List<World> getWorlds() {
        List<World> worlds = new ArrayList<>();
        return execute("SELECT * FROM world", worlds, (rs, out) -> {
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

        return execute("SELECT citizen.* FROM world " +
                "JOIN world_citizen on world_citizen.world = world.id " +
                "JOIN citizen on world_citizen.citizen = citizen.id " +
                "WHERE world.id = '" + wUUID + "'", citizens, (rs, cs) -> {
            try {
                cs.add(deserializeCitizen(rs));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    public Citizen deserializeCitizen(ResultSet rs) throws SQLException {
        return Citizen.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .build();
    }

    public World deserializeWorld(ResultSet rs) throws SQLException {
        return World.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .citizens(getWorldCitizens(rs.getString("id")))
                .build();
    }

    private String executeUpdate(String query) {
        Database db = new Database();

        try {
            Connection conn = db.init();

            PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("could not update:" + query);
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getString(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            } finally {
                ps.close();
            }

        } catch (SQLException ex) {
            System.out.println("issues with conn" + ex);
        }

        return null;
    }

    private <T> T execute(String query, Function<ResultSet, T> fn) {
        Database db = new Database();

        T obj = null;

        try {
            Connection conn = db.init();

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                System.out.print("Column 1 returned ");

                obj = fn.apply(rs);
                System.out.println(rs.getString(1));
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            System.out.println("issues with conn" + ex);
        }

        return obj;
    }

    private <T> List<T> execute(String query, List<T> out, BiFunction<ResultSet, List<T>, Void> fn) {
        Database db = new Database();

        try {
            Connection conn = db.init();

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                System.out.print("Column 1 returned ");

                fn.apply(rs, out);
                System.out.println(rs.getString(1));
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            System.out.println("issues with conn" + ex);
        }

        return out;
    }
}
