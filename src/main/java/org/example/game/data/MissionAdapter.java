package org.example.game.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.game.data.entities.Mission;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MissionAdapter implements Adapter<Mission> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    public static String jsonSerialize(
            Mission mission,
            Function<Mission, ?> fn) {
        try {
            return objectMapper.writeValueAsString(fn.apply(mission));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Mission jsonDeserialize(String s) throws IOException {
        return objectMapper.readValue(s, Mission.class);
    }

    private static final List<Map.Entry<String, Function<Mission, ?>>> MAPPING = new ArrayList<>() {{
        add(Map.entry("name", Mission::getName));
        add(Map.entry("state", Mission::getState));
        add(Map.entry("reward", (Mission mission) -> jsonSerialize(mission, Mission::getReward)));
        // TODO add schedule to data definition
        add(Map.entry("owner", (Mission mission) -> mission.getOwner().getId()));
        add(Map.entry("schedule", (Mission mission) -> "{}"));
    }};

    @Override
    public List<Mission> list() {
        List<Mission> missions = new ArrayList<>();
        return Utils.execute("SELECT * FROM mission", missions, (rs, out) -> {
            try {
                out.add(Utils.deserializeMission(rs));
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    public List<Mission> listByOwner(String cId) {
        List<Mission> missions = new ArrayList<>();
        return Utils.execute(String.format("SELECT * FROM mission WHERE owner = '%s'", cId), missions, (rs, out) -> {
            try {
                out.add(Utils.deserializeMission(rs));
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    @Override
    public Mission get(final String id) {
        return Utils.execute("SELECT * FROM mission WHERE id = '" + id + "'", rs -> {
            try {
                return Utils.deserializeMission(rs);
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Mission create(final String name) {
        return null;
    }

    @Override
    public Mission create(final Mission obj) {

        StringBuilder sb = new StringBuilder();
        StringBuilder values = new StringBuilder();
        MAPPING.forEach(entry -> {
            if (sb.length() != 0) sb.append(",");
            if (values.length() != 0) values.append(",");

            sb.append(entry.getKey());
            values.append("'").append(entry.getValue().apply(obj)).append("'");
        });

        final String id = Utils.executeUpdate(String.format("INSERT INTO mission (%s) VALUES (%s)", sb, values));

        if (id != null) {
            return get(id);
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(final String id) {
        return false;
    }
}
