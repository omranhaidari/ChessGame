/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engine.player.ai;

import com.google.common.annotations.VisibleForTesting;
import engine.board.Board;
import engine.pieces.Piece;
import engine.player.Player;

/**
 *
 * @author omran
 */
public class StandardBoardEvaluator implements BoardEvaluator {
    private static final int CHECK_BONUS = 50;
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 60;
    private static final int CASTLE_CAPABLE_BONUS = 25;
    //private final static int MOBILITY_BONUS = 1;

    @Override
    public int evaluate(Board board, int depth) {
        return scorePlayer(board, board.whitePlayer(), depth) - 
                scorePlayer(board, board.blackPlayer(), depth);
    }

    // score blancs - score noirs est positif (blancs ont l'avantage) ou négatif (noirs ont l'avantage)
    @VisibleForTesting
    private static int scorePlayer(Board board, Player player, int depth) {
        return pieceValue(player) + mobility(player) + check(player) + 
                checkmate(player, depth) + castled(player); //+ pawnStructure(player);
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
        return depth == 0 ? 1 : DEPTH_BONUS * depth; // plus depth grand, plus c'est tôt dans la recherche
    }

    private static int castled(Player player) {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }
    
//    private static int castleCapable(final Player player) {
//        return (player.KingSideCastle() || player.QueenSideCastleCapable()) ? 
//                CASTLE_CAPABLE_BONUS : 0;
//    }
//
//    private static int pawnStructure(final Player player) {
//        return PawnStructureAnalyzer.get().pawnStructureScore(player);
//    }
//
//    private static int kingSafety(final Player player) {
//        final KingDistance kingDistance = KingSafetyAnalyzer.get().calculateKingTropism(player);
//        return ((kingDistance.getEnemyPiece().getPieceValue() / 100) * kingDistance.getDistance());
//    }
//
//    private static int rookStructure(final Board board, final Player player) {
//        return RookStructureAnalyzer.get().rookStructureScore(board, player);
//    }
//
//    @Override
//    public int evaluate(Board board, int depth) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

}
