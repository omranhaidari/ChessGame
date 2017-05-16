package engine.pieces;

import engine.Alliance;
import engine.board.Board;
import engine.board.Move;
import java.util.Collection;

public abstract class Piece {
    protected int piecePosition;
    protected Alliance pieceAlliance; // La piece est soit noire, soit blanche
    protected boolean isFirstMove;

    public Piece(Alliance pieceAlliance, int piecePosition) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
    }
    
    public int getPiecePosition() {
        return this.piecePosition;
    }
    
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }
    
    public boolean isFirstMove() {
        return this.isFirstMove;
    }
    
    // calcule tous les mouvements possibles et retourne une collection de mouvements autorisés
    public abstract Collection<Move> calculateLegalMoves(Board board);
    
    public enum PieceType {
        PAWN("P"),
        KNIGHT("N"),
        BISHOP("B"),
        ROOK("R"),
        KING("K");
        
        private String pieceName;
        
        PieceType(String pieceName){
            this.pieceName = pieceName;
        }
        
        @Override
        public String toString() {
            return this.pieceName;
        }
    }
} 
