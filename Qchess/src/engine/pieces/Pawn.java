package engine.pieces;

import engine.Alliance;
import engine.board.Board;
import engine.board.BoardUtils;
import engine.board.Move;
import engine.board.Move.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Pawn extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {7, 8, 9, 16}; 

    public Pawn(Alliance pieceAlliance, int piecePosition) {
        super(PieceType.PAWN, pieceAlliance, piecePosition, true);
    }
    
    public Pawn(Alliance pieceAlliance, int piecePosition, boolean isFirstMove) {
        super(PieceType.PAWN, pieceAlliance, piecePosition, isFirstMove);
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
                // promotion de la pièce
                if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate))
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                else 
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
            }
            // saut de 2 cases pour le premier déplacement
            else if (currentCandidateOffset == 16 && this.isFirstMove() && 
                    ((BoardUtils.seventhRank[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.sixthRank[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.thirdRank[this.piecePosition] && this.getPieceAlliance().isWhite()) ||
                    (BoardUtils.secondRank[this.piecePosition] && this.getPieceAlliance().isWhite()))) {
                int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                            legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            }
            // attaque en diagonale à droite
            else if (currentCandidateOffset == 7 && 
                    !((BoardUtils.eighthFile[this.piecePosition] && this.pieceAlliance.isWhite() ||
                    (BoardUtils.firstFile[this.piecePosition] && this.pieceAlliance.isBlack())))) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        // attaque pour la promotion de la pièce
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, 
                                candidateDestinationCoordinate, pieceOnCandidate)));
                        }
                        else {
                            legalMoves.add(new PawnAttackMove(board, this, 
                                candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
            // attaque en diagonale à gauche
            else if (currentCandidateOffset == 9 && 
                    !((BoardUtils.firstFile[this.piecePosition] && this.pieceAlliance.isWhite() ||
                    (BoardUtils.eighthFile[this.piecePosition] && this.pieceAlliance.isBlack())))) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        // attaque pour la promotion de la pièce
                        if (this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, 
                                candidateDestinationCoordinate, pieceOnCandidate)));
                        }
                        else {
                            legalMoves.add(new PawnAttackMove(board, this, 
                                candidateDestinationCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }
    
    @Override
    public Pawn movePiece(Move move) {
        return PieceUtils.INSTANCE.getMovedPawn(move);
    }
    
    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
    
    public Piece getPromotionPiece() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Choose the piece for the pawn promotion! 0 for Bishop, 1 for Knight, 2 for Rook : ");
        int number = sc.nextInt();
        Piece str = null;
        switch (number) { 
            case 0:
                str = new Bishop(this.pieceAlliance, this.piecePosition, false);
                System.out.println("The pawn is promoted to a Bishop");
                break; 
            case 1:
                str = new Knight(this.pieceAlliance, this.piecePosition, false);
                System.out.println("The pawn is promoted to a Knight");
                break; 
            case 2:
                str = new Rook(this.pieceAlliance, this.piecePosition, false);
                System.out.println("The pawn is promoted to a Rook");
                break;
            default:
                System.out.println("Not a valid integer!");
                break;
        }
        return str;
    }
}
