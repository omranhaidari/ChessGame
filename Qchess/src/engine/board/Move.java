package engine.board;

import engine.pieces.Piece;

public abstract class Move {
    protected Board board;
    protected Piece movedPiece;
    protected int destinationCoordinate;


    private  Move(Board board, Piece movedPiece, int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }
    
    // sous classe de Move : mouvement de déplacement normale d'un pièce
    public static class MajorMove extends Move {
        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    
    // sous classe de Move : mouvement d'attaque d'un pièce
    public static class AttackMove extends Move {
        private Piece attackedPiece;
            
        public AttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
    }
}
