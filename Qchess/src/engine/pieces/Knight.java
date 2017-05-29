package engine.pieces;

import engine.Alliance;
import engine.board.Board;
import engine.board.BoardUtils;
import engine.board.Move;
import engine.board.Move.*;
import engine.board.Tile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Knight extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 };

    public Knight(Alliance alliance, int piecePosition) {
        super(PieceType.KNIGHT, alliance, piecePosition, true);
    }

    public Knight(Alliance alliance, int piecePosition, boolean isFirstMove) {
        super(PieceType.KNIGHT, alliance, piecePosition, isFirstMove);
    }

    // si la pièce est au bord du board, il y a des déplacements qui ne sont pas autorisés
    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.firstFile[currentPosition] && ((candidateOffset == -17) ||
                (candidateOffset == -10) || (candidateOffset == 6) || (candidateOffset == 15));
    }

    private static boolean isSecondColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.secondFile[currentPosition] && ((candidateOffset == -10) || (candidateOffset == 6));
    }

    private static boolean isSeventhColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.seventhFile[currentPosition] && ((candidateOffset == -6) || (candidateOffset == 10));
    }

    private static boolean isEighthColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.eighthFile[currentPosition] && ((candidateOffset == -15) || (candidateOffset == -6) ||
                (candidateOffset == 10) || (candidateOffset == 17));
    }

    /* On regarde selon chaque vecteur de direction, on vérifie si c'est une 
       tile valide sur le board pour le déplacement. Si c'est le cas, on vérifie
       si la tile est occupée : c'est une MajorMove. Si elle est occupée par un
       ennemie, c'est une AttackMove. On continue jusqu'à qu'il n'y ait plus de
       tile valide dans la direction. Cela permet de trouver les différentes positions possibles.
    */
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        for (int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
               isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
               isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
               isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }
            int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied())
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                else {
                    Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    Alliance pieceAtDestinationAlliance = pieceAtDestination.getPieceAlliance();
                    // si ce n'est pas une pièce de la même alliance/couleur alors c'est une pièce ennemie
                    if (this.pieceAlliance != pieceAtDestinationAlliance) {
                        legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate,
                                pieceAtDestination));
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Knight movePiece(Move move) {
        return PieceUtils.INSTANCE.getMovedKnight(move);
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }
}