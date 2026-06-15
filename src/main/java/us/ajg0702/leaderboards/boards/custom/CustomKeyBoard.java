package us.ajg0702.leaderboards.boards.custom;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

public class CustomKeyBoard {
    private final String board;
    private final String keyPlaceholder;
    private final String namePlaceholder;
    private final String valuePlaceholder;

    public CustomKeyBoard(String board, String keyPlaceholder, String namePlaceholder, String valuePlaceholder) {
        this.board = board;
        this.keyPlaceholder = normalizePlaceholder(keyPlaceholder);
        this.namePlaceholder = normalizePlaceholder(namePlaceholder);
        this.valuePlaceholder = normalizePlaceholder(valuePlaceholder);
    }

    public String getBoard() {
        return board;
    }

    public String getKeyPlaceholder() {
        return keyPlaceholder;
    }

    public String getNamePlaceholder() {
        return namePlaceholder;
    }

    public String getValuePlaceholder() {
        return valuePlaceholder;
    }

    @Nullable
    public String resolveKey(OfflinePlayer player) {
        String key = resolve(player, keyPlaceholder);
        if(key == null || key.trim().isEmpty() || key.equals(keyPlaceholder)) {
            return null;
        }
        return key.trim();
    }

    public String resolveName(OfflinePlayer player, String fallback) {
        String name = resolve(player, namePlaceholder);
        if(name == null || name.trim().isEmpty() || name.equals(namePlaceholder)) {
            return fallback;
        }
        return name;
    }

    public String resolveValue(OfflinePlayer player) {
        return resolve(player, valuePlaceholder);
    }

    private String resolve(OfflinePlayer player, String placeholder) {
        return PlaceholderAPI.setPlaceholders(player, placeholder);
    }

    private static String normalizePlaceholder(String raw) {
        String out = raw == null ? "" : raw.trim();
        if(out.isEmpty()) return out;
        if(!out.startsWith("%")) out = "%" + out;
        if(!out.endsWith("%")) out = out + "%";
        return out;
    }
}
