package us.ajg0702.leaderboards.commands.main.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import us.ajg0702.commands.CommandSender;
import us.ajg0702.commands.SubCommand;
import us.ajg0702.leaderboards.LeaderboardPlugin;
import us.ajg0702.leaderboards.cache.Cache;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static us.ajg0702.leaderboards.LeaderboardPlugin.message;

public class CleanupCustomKeys extends SubCommand {
    private final LeaderboardPlugin plugin;
    private final HashMap<Object, String> confirmCleanups = new HashMap<>();

    public CleanupCustomKeys(LeaderboardPlugin plugin) {
        super(
                "cleanupcustomkeys",
                Arrays.asList("prunecustomkeys", "cleanupcustomkey"),
                "ajleaderboards.use",
                "Remove stale rows from a custom-key leaderboard."
        );
        this.plugin = plugin;
    }

    @Override
    public List<String> autoComplete(CommandSender sender, String[] args) {
        if(args.length == 1) {
            return filterCompletion(
                    plugin.getCustomKeyBoards().getBoards().keySet().stream()
                            .filter(plugin.getTopManager()::boardExists)
                            .collect(Collectors.toList()),
                    args[0]
            );
        }
        return Collections.emptyList();
    }

    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        if(args.length < 1) {
            sender.sendMessage(message("&cPlease provide a custom-key board.\n&7Usage: /" + label + " cleanupcustomkeys <board>"));
            return;
        }

        String board = args[0];
        if(!plugin.getCache().boardExists(board)) {
            sender.sendMessage(message("&cThe board '" + board + "' does not exist."));
            return;
        }
        if(!plugin.getCustomKeyBoards().isCustomKeyBoard(board)) {
            sender.sendMessage(message("&cThe board '" + board + "' is not a custom-key board."));
            return;
        }

        boolean confirmed = confirmCleanups.containsKey(sender.getHandle()) && confirmCleanups.get(sender.getHandle()).equals(board);

        plugin.getScheduler().runTaskAsynchronously(() -> {
            OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
            Cache.CustomKeyCleanupResult result = plugin.getCache().cleanupCustomKeyBoard(board, Arrays.asList(offlinePlayers), confirmed);

            if(!result.isCustomKeyBoard()) {
                sender.sendMessage(message("&cThe board '" + result.getBoard() + "' is not a custom-key board."));
                return;
            }
            if(result.isError()) {
                sender.sendMessage(message("&cSomething went wrong while scanning " + board + ". Check the console for more info."));
                return;
            }

            String summary = "&6Custom-key cleanup for &e" + board + "\n" +
                    "&7Scanned players: &e" + result.getScannedPlayers() + "\n" +
                    "&7Players with current keys: &e" + result.getPlayersWithKeys() + "\n" +
                    "&7Current custom entries found: &e" + result.getCurrentEntries() + "\n" +
                    "&7Leaderboard rows checked: &e" + result.getCheckedRows() + "\n" +
                    "&7Stale rows " + (confirmed ? "removed" : "found") + ": &e" + (confirmed ? result.getRemovedRows() : result.getStaleRows());

            if(confirmed) {
                confirmCleanups.remove(sender.getHandle());
                sender.sendMessage(message(summary + "\n&aCleanup complete."));
                return;
            }

            if(result.getStaleRows() == 0) {
                sender.sendMessage(message(summary + "\n&aNo stale rows were found."));
                return;
            }

            String noKeysWarning = result.getPlayersWithKeys() == 0
                    ? "\n&cWARNING: &eNo players returned a current custom key. If your guild placeholder does not support offline players, confirming may remove valid rows."
                    : "";
            sender.sendMessage(message(summary + noKeysWarning + "\n" +
                    "&eRepeat this command within 30 seconds to remove the stale rows.\n" +
                    "&7Or click: <click:run_command:'/" + label + " cleanupcustomkeys " + board + "'><green><b>" +
                    "<hover:show_text:'<gray>Click to confirm custom-key cleanup'>[CONFIRM]</hover>" +
                    "</b></green></click>"));

            confirmCleanups.put(sender.getHandle(), board);
            plugin.getScheduler().runTaskLaterAsynchronously(() -> {
                if(confirmCleanups.containsKey(sender.getHandle()) && confirmCleanups.get(sender.getHandle()).equals(board)) {
                    confirmCleanups.remove(sender.getHandle());
                }
            }, 30 * 20);
        });
    }
}
