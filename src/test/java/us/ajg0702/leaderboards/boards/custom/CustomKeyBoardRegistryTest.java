package us.ajg0702.leaderboards.boards.custom;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;

import java.util.logging.Logger;

import static org.junit.Assert.*;

public class CustomKeyBoardRegistryTest {
    @Test
    public void loadsValidCustomKeyBoard() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("custom-key-boards.guild_kills.type", "custom-key");
        config.set("custom-key-boards.guild_kills.key-placeholder", "%guilds_id%");
        config.set("custom-key-boards.guild_kills.name-placeholder", "%guilds_name%");
        config.set("custom-key-boards.guild_kills.value-placeholder", "%guilds_total_experience%");

        CustomKeyBoardRegistry registry = CustomKeyBoardRegistry.fromConfig(config, Logger.getLogger("test"));
        CustomKeyBoard board = registry.get("guild_kills");

        assertNotNull(board);
        assertEquals("%guilds_id%", board.getKeyPlaceholder());
        assertEquals("%guilds_name%", board.getNamePlaceholder());
        assertEquals("%guilds_total_experience%", board.getValuePlaceholder());
    }

    @Test
    public void skipsInvalidCustomKeyBoard() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("custom-key-boards.guild_kills.type", "custom-key");
        config.set("custom-key-boards.guild_kills.key-placeholder", "%guilds_id%");
        config.set("custom-key-boards.guild_kills.name-placeholder", "%guilds_name%");

        CustomKeyBoardRegistry registry = CustomKeyBoardRegistry.fromConfig(config, Logger.getLogger("test"));

        assertFalse(registry.isCustomKeyBoard("guild_kills"));
    }
}
