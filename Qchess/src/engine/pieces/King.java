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
        super(PieceType.KING, pieceAlliance, piecePosition);
    }
    
    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
                if (isFirstColumnExlusion(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExlusion(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied())
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    else {
                        Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        // si ce n'est pas une pièce de la même alliance/couleur alors c'est une pièce ennemie
                        if (this.pieceAlliance != pieceAlliance)
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    
    // Si la pièce est au bord du board, il y a des déplacements que ne sont pas autorisés
    private static boolean isFirstColumnExlusion(int currentPosition, int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 ||
                candidateOffset == -1 || candidateOffset == 7);
    }
    
    private static boolean isEighthColumnExlusion(int currentPosition, int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 ||
                candidateOffset == 1 || candidateOffset == 9);
    }
    
    @Override
    public String toString() {
        return PieceType.KING.toString();
    }
}