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

public class Bishop extends Piece {
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -7, 7, 9};

    public Bishop(Alliance pieceAlliance, int piecePosition) {
        super(PieceType.BISHOP, pieceAlliance, piecePosition);
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

        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExlusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                        isEighthColumnExlusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;
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
                        // indique que ce n'est pas occupé et que l'on peut encore se déplacer, sinon on arrete la boucle si il y a déjà une pièce
                        break; 
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    
    // Si la pièce est au bord du board, il y a des déplacements que ne sont pas autorisés
    private static boolean isFirstColumnExlusion(int currentPosition, int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && candidateOffset == -9 ||
                candidateOffset == 7;
    }
    
    private static boolean isEighthColumnExlusion(int currentPosition, int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 ||
                candidateOffset == 9);
    }
    
    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }
}