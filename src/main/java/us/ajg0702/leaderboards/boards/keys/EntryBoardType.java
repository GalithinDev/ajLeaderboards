package us.ajg0702.leaderboards.boards.keys;

import us.ajg0702.leaderboards.boards.TimedType;

import java.util.Objects;

public class EntryBoardType {
    private final String entryId;
    private final String board;
    private final TimedType type;

    public EntryBoardType(String entryId, String board, TimedType type) {
        this.entryId = entryId;
        this.board = board;
        this.type = type;
    }

    public String getEntryId() {
        return entryId;
    }

    public String getBoard() {
        return board;
    }

    public TimedType getType() {
        return type;
    }

    public BoardType getBoardType() {
        return new BoardType(board, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntryBoardType)) return false;
        EntryBoardType that = (EntryBoardType) o;
        return getEntryId().equals(that.getEntryId()) && getBoard().equals(that.getBoard()) && getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEntryId(), getBoard(), getType());
    }

    @Override
    public String toString() {
        return "EntryBoardType{" +
                "entryId='" + entryId + '\'' +
                ", board='" + board + '\'' +
                ", type=" + type +
                '}';
    }
}
