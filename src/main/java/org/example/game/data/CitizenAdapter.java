package org.example.game.data;

import org.example.game.data.entities.Citizen;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CitizenAdapter implements Adapter<Citizen> {
    @Override
    public List<Citizen> list() {
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

    @Override
    public Citizen get(String id) {
        return Utils.execute("SELECT * FROM citizen where id = '" + id + "'", rs -> {
            try {
                return Utils.deserializeCitizen(rs);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Citizen create(final String name) {
        final String id = Utils.executeUpdate("INSERT INTO citizen (name) values ('"+ name + "')");

        if (id != null) {
            return get(id);
        } else {
            return null;
        }
    }

    @Override
    public Citizen create(final Citizen obj) {
        return null;
    }

    // TODO should remove from world_citizen table as well
    @Override
    public boolean delete(final String id) {
        return Utils.executeUpdateNoKeys(String.format("DELETE FROM citizen WHERE id = '%s'", id));
    }
}
