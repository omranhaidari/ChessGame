package engine.player.ai;

import com.google.common.annotations.VisibleForTesting;
import engine.board.Board;
import engine.pieces.Piece;
import engine.player.Player;

public class StandardBoardEvaluator implements BoardEvaluator {
    private static final int CHECK_BONUS = 50;
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 60;

    @Override
    public int evaluate(Board board, int depth) {
        return scorePlayer(board, board.whitePlayer(), depth) - 
                scorePlayer(board, board.blackPlayer(), depth);
    }

    // score blancs - score noirs est positif (blancs ont l'avantage) ou négatif (noirs ont l'avantage)
    @VisibleForTesting
    private static int scorePlayer(Board board, Player player, int depth) {
        return pieceValue(player) + mobility(player) + check(player) + 
                checkmate(player, depth) + castled(player);
    }

    // ajoute les valeurs de chaque pièce de chaque joueur
    private static int pieceValue(Player player) {
        int pieceValueScore = 0;
        for (Piece piece : player.getActivePieces())
            pieceValueScore += piece.getPieceValue();
        return pieceValueScore;
    }

    private static int mobility(Player player) {
        return player.getLegalMoves().size();
    }
    
    private static int check(Player player) {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    // prend en compte le nombre d'étapes à atteindre un échec et mat
    private static int checkmate(Player player, int depth) {
        return player.getOpponent().isInCheckMate() ? 
                CHECK_MATE_BONUS  * depthBonus(depth) : 0;
    }

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int castled(Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }
}
