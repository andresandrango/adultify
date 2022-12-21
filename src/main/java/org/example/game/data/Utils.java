package org.example.game.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.game.Mission.MissionState;
import org.example.game.data.entities.Citizen;
import org.example.game.data.entities.Life;
import org.example.game.data.entities.Mission;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

// TODO refactor this with a client that is injected in the constructor
public class Utils {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    public static String executeUpdate(String query) {
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

    public static boolean executeUpdateNoKeys(String query) {
        Database db = new Database();

        try {
            Connection conn = db.init();

            PreparedStatement ps = conn.prepareStatement(query);
            int affectedRows = ps.executeUpdate();
            ps.close();

            return affectedRows != 0;

        } catch (SQLException ex) {
            System.out.println("issues with conn" + ex);
        }

        return false;
    }

    public static <T> T execute(String query, Function<ResultSet, T> fn) {
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

    public static <T> List<T> execute(String query, List<T> out, BiFunction<ResultSet, List<T>, Void> fn) {
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

    public static Citizen deserializeCitizen(ResultSet rs) throws SQLException {
        return Citizen.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .build();
    }

    public static Mission deserializeMission(ResultSet rs) throws SQLException, IOException {
        return Mission.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .state(MissionState.valueOf(rs.getString("state")))
                .reward(objectMapper.readValue(rs.getString("reward"), Life.class))
                .build();
    }
}
