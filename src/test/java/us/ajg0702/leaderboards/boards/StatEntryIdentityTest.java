package us.ajg0702.leaderboards.boards;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class StatEntryIdentityTest {
    @Test
    public void playerEntryKeepsPlayerUuidAsEntryId() {
        UUID playerId = UUID.randomUUID();
        StatEntry entry = new StatEntry(1, "kills", "", "Player", "Player", playerId, "", 42, TimedType.ALLTIME);

        assertTrue(entry.hasPlayer());
        assertTrue(entry.hasEntry());
        assertEquals(playerId, entry.getPlayerID());
        assertEquals(playerId.toString(), entry.getEntryId());
        assertEquals("42", entry.getScorePretty());
    }

    @Test
    public void customEntryHasEntryWithoutPlayer() {
        StatEntry entry = new StatEntry(1, "guild_kills", "", "Guild", "Guild", "guild-alpha", null, "", 42, TimedType.ALLTIME);

        assertFalse(entry.hasPlayer());
        assertTrue(entry.hasEntry());
        assertEquals("guild-alpha", entry.getEntryId());
        assertEquals("42", entry.getScorePretty());
    }
}
