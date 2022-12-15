package org.example.game.data;

import org.example.game.data.entities.Citizen;
import org.example.game.data.entities.World;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class WorldAdapter {

    public World getWorld(String wUuid) {
        Database db = new Database();

        World w = null;

        try {
            Connection conn = db.init();

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM world where id = '" + wUuid + "'");
            while (rs.next()) {
                System.out.print("Column 1 returned ");
                w = deserialize(rs);
                System.out.println(rs.getString(1));
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            System.out.println("issues with conn" + ex);
        }

        return w;
    }

    public List<World> getWorlds() {
        Database db = new Database();

        List<World> worlds = new ArrayList<>();

        try {
            Connection conn = db.init();

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM world");
            while (rs.next()) {
                System.out.print("Column 1 returned ");

                worlds.add(deserialize(rs));
                System.out.println(rs.getString(1));
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            System.out.println("issues with conn" + ex);
        }

        return worlds;
    }

    // TODO This query is executed per world; change this to be part of a particular
    // query that needs this data
    public List<Citizen> getWorldCitizens(String wUUID) {
        Database db = new Database();

        List<Citizen> citizens = new ArrayList<>();

        try {
            Connection conn = db.init();

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT citizen.* FROM world " +
                    "JOIN world_citizen on world_citizen.world = world.id " +
                    "JOIN citizen on world_citizen.citizen = citizen.id " +
                    "WHERE world.id = '" + wUUID + "'");
            while (rs.next()) {
                System.out.print("Column 1 returned ");
                citizens.add(deserializeCitizen(rs));
                System.out.println(rs.getString(1));
            }
            rs.close();
            st.close();

        } catch (SQLException ex) {
            System.out.println("issues with conn" + ex);
        }

        return citizens;
    }

    public Citizen deserializeCitizen(ResultSet rs) throws SQLException {
        return Citizen.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .build();
    }

    public World deserialize(ResultSet rs) throws SQLException {
        return World.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .citizens(getWorldCitizens(rs.getString("id")))
                .build();
    }
}
