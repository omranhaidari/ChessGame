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
    private int cachedHashCode;

    public Piece(PieceType pieceType, Alliance pieceAlliance, int piecePosition) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        int result = pieceType.hashCode();
        result = 31 * result + pieceAlliance.hashCode();
        result = 31 * result + piecePosition;
        result = 31 * result + (isFirstMove ? 1 : 8);
        return result;
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
    
    // rendre deux objets (pièces) égaux d'un point de vue caractérisitque et non référenciel
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if(!(other instanceof Piece))
            return false;
        Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() &&
                pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() &&
                isFirstMove == otherPiece.isFirstMove();
    }
    
    @Override
    public int hashCode() {
        return this.cachedHashCode;
    }
    
    // calcule tous les mouvements possibles et retourne une collection de mouvements autorisés
    public abstract Collection<Move> calculateLegalMoves(Board board);
    // retourne la même pièce avec son déplacement
    public abstract Piece movePiece(Move move);
    
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
