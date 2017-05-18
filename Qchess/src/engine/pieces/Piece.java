package engine.pieces;

import engine.Alliance;
import engine.board.Board;
import engine.board.Move;
import java.util.Collection;

public abstract class Piece {
    protected PieceType pieceType;
    protected int piecePosition;
    protected Alliance pieceAlliance; // La piece est soit noire, soit blanche
    protected boolean isFirstMove;

    public Piece(PieceType pieceType, Alliance pieceAlliance, int piecePosition) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
    }
    
    public PieceType getPieceType() {
        return this.pieceType;
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
        PAWN("P") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
        };
        
        private String pieceName;
        
        PieceType(String pieceName){
            this.pieceName = pieceName;
        }
        
        public abstract boolean isKing();
        
        @Override
        public String toString() {
            return this.pieceName;
        }
    }
} 
