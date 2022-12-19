package org.example.game.data;

import org.example.game.data.entities.Citizen;
import org.example.game.data.entities.World;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CitizenAdapter {

    // TODO set pagination
    public List<Citizen> getCitizens() {
        List<Citizen> citizens = new ArrayList<>();
        return Utils.execute("SELECT * FROM citizen", citizens, (rs, out) -> {
            try {
                out.add(Utils.deserializeCitizen(rs));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }
    public Citizen getCitizen(String cUuid) {
        return Utils.execute("SELECT * FROM citizen where id = '" + cUuid + "'", rs -> {
            try {
                return Utils.deserializeCitizen(rs);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Citizen createCitizen(String name) {
        final String id = Utils.executeUpdate("INSERT INTO citizen (name) values ('"+ name + "')");

        if (id != null) {
            return getCitizen(id);
        } else {
            return null;
        }
    }
}
