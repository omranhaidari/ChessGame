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

public class Rook extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDINATES = { -8, -1, 1, 8 };

    public Rook(Alliance alliance, int piecePosition) {
        super(PieceType.ROOK, alliance, piecePosition, true);
    }

    public Rook(Alliance alliance, int piecePosition, boolean isFirstMove) {
        super(PieceType.ROOK, alliance, piecePosition, isFirstMove);
    }

    // si la pièce est au bord du board, il y a des déplacements qui ne sont pas autorisés
    private static boolean isColumnExclusion(int currentCandidate, int candidateDestinationCoordinate) {
        return (BoardUtils.firstFile[candidateDestinationCoordinate] && (currentCandidate == -1)) ||
               (BoardUtils.eighthFile[candidateDestinationCoordinate] && (currentCandidate == 1));
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
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isColumnExclusion(currentCandidateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCandidateOffset;
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
                        break;
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Rook movePiece(Move move) {
        return PieceUtils.INSTANCE.getMovedRook(move);
    }

    @Override
    public String toString() {
        return this.pieceType.toString();
    }
}