package engine.pieces;

import engine.Alliance;
import engine.board.Board;
import engine.board.Move;
import java.util.List;

public abstract class Piece {
//    private int player;
//    abstract void movement();
//    
//    public Piece(){
//        this.player = 0;
//    }
    
    protected final int piecePosition;
    protected final Alliance pieceAlliance; // La piece est soit noire, soit blanche

    public Piece(final int piecePosition, final Alliance pieceAlliance) {
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
    }
    
    public abstract List<Move> calculateLegalMoves(final Board board);
    
    
} 
