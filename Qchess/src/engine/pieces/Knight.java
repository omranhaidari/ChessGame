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
    
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10 -6, 
        6, 10, 15, 17}; // déplacements possibles par rapport à la position de la pièce

    public Knight(Alliance pieceAlliance, int piecePosition) {
        super(PieceType.KNIGHT, pieceAlliance, piecePosition, true);
    }
    
    public Knight(Alliance pieceAlliance, int piecePosition, boolean isForstMove) {
        super(PieceType.KNIGHT, pieceAlliance, piecePosition, isForstMove);
    }
    
    /* Si la pièce est sur la première/deuxième/septième/huitième colonne et
       si le mouvement correspond à une de ces cases destinataires, on ne 
       peut pourra pas faire ce qu'il y a dans le if(BoardUtils...)
    */
    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.firstFile[currentPosition] && ((candidateOffset == -17) ||
                (candidateOffset == -10) || (candidateOffset == 6) || (candidateOffset == 15));
    }

    private static boolean isSecondColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.secondFile[currentPosition] && ((candidateOffset == -10) || 
                (candidateOffset == 6));
    }

    private static boolean isSeventhColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.seventhFile[currentPosition] && ((candidateOffset == -6) || 
                (candidateOffset == 10));
    }

    private static boolean isEighthColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardUtils.eighthFile[currentPosition] && ((candidateOffset == -15) || 
                (candidateOffset == -6) || (candidateOffset == 10) || (candidateOffset == 17));
    }
    
    /* Pour chaque case destinataire possible, on calcule pour chaque mouvement possible. 
       si c'est une case (coordonée / numéro de case) valide
       et si c'est pas une case occupée, on ajoute dans la liste des mouvements légaux
       si c'est une case occupée, on récupére juste la couleur de la pièce
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
    public Knight movePiece(Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }
    
    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }
}
