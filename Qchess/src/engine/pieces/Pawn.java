package engine.pieces;

import engine.Alliance;
import engine.board.Board;
import engine.board.BoardUtils;
import engine.board.Move;
import engine.board.Move.MajorMove;
import engine.board.Tile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Pawn extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {7, 8, 9, 16}; 

    public Pawn(int piecePosition, Alliance pieceAlliance) {
        super(piecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            // la direction des déplacements dépend de la couleur du pion : -i pour les blancs et i pour les noirs
            int candidateDestinationCoordinate = this.piecePosition + 
                    (this.pieceAlliance.getDirection() * currentCandidateOffset);
            /* si c'est une tileCoordinate valide sur le plateau et que ce n'est 
               pas une exclusion, on continue 
            */
            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate))
                continue;
            // déplacement normal vers l'avant
            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                // ********** faire la promotion de la pièce **********
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            }
            // saut de 2 cases pour le premier déplacement
            else if (currentCandidateOffset == 16 && this.isFirstMove() && 
                    (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.THIRD_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SIXTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()) ||
                    (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite())) {
                int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() 
                        && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }
            }
            // attaque en diagonale à droite
            else if (currentCandidateOffset == 7 && 
                    !(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                    BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        // ********** attaque pour la promotion de la pièce **********
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }
            // attaque en diagonale à gauche
            else if (currentCandidateOffset == 9 && 
                    !(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                    BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        // ********** attaque pour la promotion de la pièce **********
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);

    }  
}
