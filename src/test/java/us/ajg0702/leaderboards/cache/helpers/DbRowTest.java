package us.ajg0702.leaderboards.cache.helpers;

import com.google.gson.JsonObject;
import org.junit.Test;
import us.ajg0702.leaderboards.boards.TimedType;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class DbRowTest {
    @Test
    public void roundTripsStringId() {
        DbRow row = new DbRow("guild-alpha", 12.5, deltas(), lastTotals(), timestamps(), "Guild", "", "", "Guild");

        JsonObject json = row.toJsonObject();
        DbRow restored = DbRow.fromJsonObject(json);

        assertEquals("guild-alpha", restored.getId());
        assertEquals(12.5, restored.getValue(), 0.001);
        assertEquals("Guild", restored.getNamecache());
    }

    @Test
    public void keepsUuidIdCompatibility() {
        UUID id = UUID.randomUUID();
        DbRow row = new DbRow(id, 7, deltas(), lastTotals(), timestamps(), "Player", "", "", "Player");

        assertEquals(id.toString(), row.getId());
        assertEquals(id.toString(), DbRow.fromJsonObject(row.toJsonObject()).getId());
    }

    private Map<TimedType, Double> deltas() {
        Map<TimedType, Double> values = new EnumMap<>(TimedType.class);
        for(TimedType type : TimedType.values()) {
            if(type != TimedType.ALLTIME) values.put(type, 1.0);
        }
        return values;
    }

    private Map<TimedType, Double> lastTotals() {
        Map<TimedType, Double> values = new EnumMap<>(TimedType.class);
        for(TimedType type : TimedType.values()) {
            if(type != TimedType.ALLTIME) values.put(type, 2.0);
        }
        return values;
    }

    private Map<TimedType, Long> timestamps() {
        Map<TimedType, Long> values = new EnumMap<>(TimedType.class);
        for(TimedType type : TimedType.values()) {
            if(type != TimedType.ALLTIME) values.put(type, 3L);
        }
        return values;
    }
}
