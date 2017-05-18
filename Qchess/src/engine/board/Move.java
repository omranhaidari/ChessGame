package engine.board;

import engine.board.Board.Builder;
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

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }
    
    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public abstract Board execute();
    
    // sous classe de Move : mouvement de déplacement normale d'un pièce
    public static class MajorMove extends Move {
        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        // crée un nouveau plateau de jeu avec le déplacement de la pièce en sélectionnée
        @Override
        public Board execute() {
            Builder builder = new Builder();
            for (Piece piece : this.board.currentPlayer().getActivePieces()) {
                ///
                if (!this.movedPiece.equals(piece))
                    builder.setPiece(piece);
            }
            for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces())
                builder.setPiece(piece);
            // déplacement de la pièce actuelle
            builder.setPiece(this.movedPiece.movePiece(this));
            // à l'adversaire de jouer ensuite
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }
    
    // sous classe de Move : mouvement d'attaque d'un pièce
    public static class AttackMove extends Move {
        private Piece attackedPiece;
            
        public AttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public Board execute() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
