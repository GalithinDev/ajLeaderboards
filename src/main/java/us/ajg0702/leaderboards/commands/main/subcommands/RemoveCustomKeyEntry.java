package us.ajg0702.leaderboards.commands.main.subcommands;

import us.ajg0702.commands.CommandSender;
import us.ajg0702.commands.SubCommand;
import us.ajg0702.leaderboards.LeaderboardPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static us.ajg0702.leaderboards.LeaderboardPlugin.message;

public class RemoveCustomKeyEntry extends SubCommand {
    private final LeaderboardPlugin plugin;

    public RemoveCustomKeyEntry(LeaderboardPlugin plugin) {
        super(
                "removecustomkeyentry",
                Collections.singletonList("rmcustomkeyentry"),
                "ajleaderboards.use",
                "Remove an entry id from a custom-key leaderboard."
        );
        this.plugin = plugin;
    }

    @Override
    public List<String> autoComplete(CommandSender sender, String[] args) {
        if(args.length == 1) {
            List<String> boards = new ArrayList<>(plugin.getCustomKeyBoards().getBoards().keySet());
            boards.add("*");
            return filterCompletion(boards, args[0]);
        }
        return Collections.emptyList();
    }

    @Override
    public void execute(CommandSender sender, String[] args, String label) {
        if(args.length < 2) {
            sender.sendMessage(message("&cPlease provide a custom-key board and entry id.\n&7Usage: /" + label + " removecustomkeyentry <board|*> <entry-id>"));
            return;
        }

        String board = args[0];
        String entryId = args[1];
        List<String> boards;

        if("*".equals(board)) {
            boards = new ArrayList<>(plugin.getCustomKeyBoards().getBoards().keySet());
        } else {
            if(!plugin.getCache().boardExists(board)) {
                sender.sendMessage(message("&cThe board '" + board + "' does not exist."));
                return;
            }
            if(!plugin.getCustomKeyBoards().isCustomKeyBoard(board)) {
                sender.sendMessage(message("&cThe board '" + board + "' is not a custom-key board."));
                return;
            }
            boards = Collections.singletonList(board);
        }

        plugin.getScheduler().runTaskAsynchronously(() -> {
            int removed = 0;
            for(String customKeyBoard : boards) {
                if(!plugin.getCache().boardExists(customKeyBoard)) continue;
                if(plugin.getCache().removeCustomKeyEntry(customKeyBoard, entryId)) {
                    removed++;
                }
            }
            sender.sendMessage(message("&aRemoved custom-key entry &f" + entryId + " &afrom &f" + removed + " &aboard(s)."));
        });
    }
}
