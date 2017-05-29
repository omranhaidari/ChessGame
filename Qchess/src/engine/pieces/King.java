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

public class King extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9}; 

    public King(Alliance pieceAlliance, int piecePosition) {
        super(PieceType.KING, pieceAlliance, piecePosition, true);
    }
    
    public King(Alliance pieceAlliance, int piecePosition, boolean isFirstMove) {
        super(PieceType.KING, pieceAlliance, piecePosition, isFirstMove);
    }
    
    // si la pièce est au bord du board, il y a des déplacements qui ne sont pas autorisés
    private static boolean isFirstColumnExclusion(int currentCandidate, int candidateDestinationCoordinate) {
        return BoardUtils.firstFile[currentCandidate] &&  ((candidateDestinationCoordinate == -9) || 
                (candidateDestinationCoordinate == -1) || (candidateDestinationCoordinate == 7));
    }

    private static boolean isEighthColumnExclusion(int currentCandidate, int candidateDestinationCoordinate) {
        return BoardUtils.eighthFile[currentCandidate] &&  ((candidateDestinationCoordinate == -7) || 
                (candidateDestinationCoordinate == 1) || (candidateDestinationCoordinate == 9));
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
            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
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
                    Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    // si ce n'est pas une pièce de la même alliance/couleur alors c'est une pièce ennemie
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new MajorAttackMove(board, this, 
                                candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public King movePiece(Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
}