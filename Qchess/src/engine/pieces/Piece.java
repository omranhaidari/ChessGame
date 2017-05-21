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

    public Piece(PieceType pieceType, Alliance pieceAlliance, int piecePosition, boolean isFirstMove) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
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
    
    public int getPieceValue() {
        return this.pieceType.getPieceValue();
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
        PAWN(100, "P") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        KNIGHT(300, "N") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        BISHOP(300, "B") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        },
        ROOK(500, "R") {
            @Override
            public boolean isKing() {
                return false;
            }

            @Override
            public boolean isRook() {
                return true;
            }
        },
        KING(10000, "K") {
            @Override
            public boolean isKing() {
                return true;
            }

            @Override
            public boolean isRook() {
                return false;
            }
        };      
        public abstract boolean isKing();
        public abstract boolean isRook();
        
        private String pieceName;
        private int pieceValue; // pour donner une certaine valeur d'importance à une pièce
        
        PieceType(int pieceValue, String pieceName){
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }
        
        @Override
        public String toString() {
            return this.pieceName;
        }
        
        public int getPieceValue() {
            return this.pieceValue;
        }
    }
} 
