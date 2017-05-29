package engine.pieces;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import engine.Alliance;
import engine.board.BoardUtils;
import engine.board.Move;

enum PieceUtils {

    INSTANCE;

    private final Table<Alliance, Integer, Pawn> ALL_POSSIBLE_PAWNS = PieceUtils.createAllPossibleMovedPawns();
    private final Table<Alliance, Integer, Knight> ALL_POSSIBLE_KNIGHTS = PieceUtils.createAllPossibleMovedKnights();
    private final Table<Alliance, Integer, Bishop> ALL_POSSIBLE_BISHOPS = PieceUtils.createAllPossibleMovedBishops();
    private final Table<Alliance, Integer, Rook> ALL_POSSIBLE_ROOKS = PieceUtils.createAllPossibleMovedRooks();

    Pawn getMovedPawn(Move move) {
        return ALL_POSSIBLE_PAWNS.get(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    Knight getMovedKnight(Move move) {
        return ALL_POSSIBLE_KNIGHTS.get(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    Bishop getMovedBishop(Move move) {
        return ALL_POSSIBLE_BISHOPS.get(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    Rook getMovedRook(Move move) {
        return ALL_POSSIBLE_ROOKS.get(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    private static Table<Alliance, Integer, Pawn> createAllPossibleMovedPawns() {
        ImmutableTable.Builder<Alliance, Integer, Pawn> pieces = ImmutableTable.builder();
        for(Alliance alliance : Alliance.values()) {
            for(int i = 0; i < BoardUtils.numTiles; i++) {
                pieces.put(alliance, i, new Pawn(alliance, i, false));
            }
        }
        return pieces.build();
    }

    private static Table<Alliance, Integer, Knight> createAllPossibleMovedKnights() {
        ImmutableTable.Builder<Alliance, Integer, Knight> pieces = ImmutableTable.builder();
        for(Alliance alliance : Alliance.values()) {
            for(int i = 0; i < BoardUtils.numTiles; i++) {
                pieces.put(alliance, i, new Knight(alliance, i, false));
            }
        }
        return pieces.build();
    }

    private static Table<Alliance, Integer, Bishop> createAllPossibleMovedBishops() {
        ImmutableTable.Builder<Alliance, Integer, Bishop> pieces = ImmutableTable.builder();
        for(Alliance alliance : Alliance.values()) {
            for(int i = 0; i < BoardUtils.numTiles; i++) {
                pieces.put(alliance, i, new Bishop(alliance, i, false));
            }
        }
        return pieces.build();
    }

    private static Table<Alliance, Integer, Rook> createAllPossibleMovedRooks() {
        ImmutableTable.Builder<Alliance, Integer, Rook> pieces = ImmutableTable.builder();
        for(Alliance alliance : Alliance.values()) {
            for(int i = 0; i < BoardUtils.numTiles; i++) {
                pieces.put(alliance, i, new Rook(alliance, i, false));
            }
        }
        return pieces.build();
    }
}