package engine.pieces;

import engine.Alliance;
import engine.board.Board;
import engine.board.Move;
import java.util.Collection;

public abstract class Piece {
//    private int player;
//    abstract void movement();
//    
//    public Piece(){
//        this.player = 0;
//    }
    
    protected int piecePosition;
    protected Alliance pieceAlliance; // La piece est soit noire, soit blanche
    protected boolean isFirstMove;

    public Piece(int piecePosition, Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = false;
    }
    
    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }
    
    public boolean isFirstMove() {
        return this.isFirstMove;
    }
    
    // calcule tous les mouvements possibles et retourne une collection de mouvements autorisés
    public abstract Collection<Move> calculateLegalMoves(Board board);
    
    
} 
