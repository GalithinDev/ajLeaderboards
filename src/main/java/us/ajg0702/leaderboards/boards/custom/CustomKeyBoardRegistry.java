package us.ajg0702.leaderboards.boards.custom;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.spongepowered.configurate.ConfigurationNode;
import us.ajg0702.leaderboards.LeaderboardPlugin;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class CustomKeyBoardRegistry {
    private static final String SECTION = "custom-key-boards";

    private final Map<String, CustomKeyBoard> boards;
    private final String debugInfo;

    public CustomKeyBoardRegistry(Map<String, CustomKeyBoard> boards) {
        this(boards, "");
    }

    private CustomKeyBoardRegistry(Map<String, CustomKeyBoard> boards, String debugInfo) {
        this.boards = Collections.unmodifiableMap(new LinkedHashMap<>(boards));
        this.debugInfo = debugInfo;
    }

    public static CustomKeyBoardRegistry load(LeaderboardPlugin plugin) {
        Map<String, CustomKeyBoard> boards = new LinkedHashMap<>();
        addFromNode(plugin.getAConfig().getNode(), plugin.getLogger(), boards);

        File configFile = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration fileConfig = YamlConfiguration.loadConfiguration(configFile);
        boolean fileHasSection = fileConfig.isConfigurationSection(SECTION);
        addFromConfig(fileConfig, plugin.getLogger(), boards);

        String debugInfo = "path=" + configFile.getAbsolutePath() +
                ", exists=" + configFile.exists() +
                ", file-has-custom-key-boards=" + fileHasSection;
        return new CustomKeyBoardRegistry(boards, debugInfo);
    }

    public static CustomKeyBoardRegistry fromNode(ConfigurationNode config, Logger logger) {
        Map<String, CustomKeyBoard> boards = new LinkedHashMap<>();
        addFromNode(config, logger, boards);
        return new CustomKeyBoardRegistry(boards);
    }

    private static void addFromNode(ConfigurationNode config, Logger logger, Map<String, CustomKeyBoard> boards) {
        ConfigurationNode section = config.node(SECTION);

        for(Map.Entry<Object, ? extends ConfigurationNode> entry : section.childrenMap().entrySet()) {
            String board = String.valueOf(entry.getKey());
            ConfigurationNode boardSection = entry.getValue();

            String type = boardSection.node("type").getString("custom-key");
            if(!"custom-key".equalsIgnoreCase(type)) continue;

            String keyPlaceholder = boardSection.node("key-placeholder").getString("");
            String namePlaceholder = boardSection.node("name-placeholder").getString("");
            String valuePlaceholder = boardSection.node("value-placeholder").getString("");

            addBoard(logger, boards, board, keyPlaceholder, namePlaceholder, valuePlaceholder);
        }
    }

    public static CustomKeyBoardRegistry fromConfig(YamlConfiguration config, Logger logger) {
        Map<String, CustomKeyBoard> boards = new LinkedHashMap<>();
        addFromConfig(config, logger, boards);
        return new CustomKeyBoardRegistry(boards);
    }

    private static void addFromConfig(YamlConfiguration config, Logger logger, Map<String, CustomKeyBoard> boards) {
        ConfigurationSection section = config.getConfigurationSection(SECTION);

        if(section == null) {
            return;
        }

        for(String board : section.getKeys(false)) {
            ConfigurationSection boardSection = section.getConfigurationSection(board);
            if(boardSection == null) continue;

            String type = boardSection.getString("type", "custom-key");
            if(!"custom-key".equalsIgnoreCase(type)) continue;

            String keyPlaceholder = boardSection.getString("key-placeholder", "");
            String namePlaceholder = boardSection.getString("name-placeholder", "");
            String valuePlaceholder = boardSection.getString("value-placeholder", "");

            addBoard(logger, boards, board, keyPlaceholder, namePlaceholder, valuePlaceholder);
        }
    }

    private static void addBoard(Logger logger, Map<String, CustomKeyBoard> boards, String board, String keyPlaceholder, String namePlaceholder, String valuePlaceholder) {
        if(keyPlaceholder.trim().isEmpty() || namePlaceholder.trim().isEmpty() || valuePlaceholder.trim().isEmpty()) {
            logger.warning("Skipping custom-key board '" + board + "' because key-placeholder, name-placeholder, and value-placeholder are required.");
            return;
        }

        boards.put(board, new CustomKeyBoard(board, keyPlaceholder, namePlaceholder, valuePlaceholder));
    }

    public boolean isCustomKeyBoard(String board) {
        return boards.containsKey(board);
    }

    public CustomKeyBoard get(String board) {
        return boards.get(board);
    }

    public Map<String, CustomKeyBoard> getBoards() {
        return boards;
    }

    public String getDebugInfo() {
        return debugInfo;
    }
}
