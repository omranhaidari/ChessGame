package engine.board;

import engine.board.Board.Builder;
import engine.pieces.Pawn;
import engine.pieces.Piece;
import engine.pieces.Rook;
import java.util.Objects;

public abstract class Move {
    protected Board board;
    protected Piece movedPiece;
    protected int destinationCoordinate;
    public static final Move NULL_MOVE = new NullMove();
    

    private  Move(Board board, Piece movedPiece, int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }
    
    // crée un nouveau plateau de jeu avec le déplacement de la pièce en sélectionnée
    public Board execute() {
        Builder builder = new Builder();
        for (Piece piece : this.board.currentPlayer().getActivePieces()) {
            ///
            if (!this.movedPiece.equals(piece))
                builder.setPiece(piece);
        }
        for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        // déplacement de la pièce actuelle
        builder.setPiece(this.movedPiece.movePiece(this));
        // à l'adversaire de jouer ensuite
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
        return builder.build();
    }
    
    public boolean isAttack() {
        return false;
    }
    
    public boolean isCastlingMove() {
        return false;
    }
    
    public Piece getAttackedPiece() {
        return null;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }
    
    
    private int getCurrentCoordinate() {
        return this.getMovedPiece().getPiecePosition();
    }
    
    public Piece getMovedPiece() {
        return this.movedPiece;
    }
    
    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) 
            return true;
        if(!(other instanceof Move))
            return false;
        Move otherMove = (Move) other;
        return getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }
    
    // sous classe de Move : mouvement de déplacement normale d'une pièce
    public static class MajorMove extends Move {
        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    
    // sous classe de Move : mouvement d'attaque d'une pièce
    public static class AttackMove extends Move {
        private Piece attackedPiece;
            
        public AttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }
        
        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other)
                return true;
            if(!(other instanceof AttackMove))
                return false;
            AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
            
        }
        
        @Override
        public Board execute() {
            return null;
        }
        
        @Override
        public boolean isAttack() {
            return true;
        }
        
        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }
    
    // sous classe de Move : mouvement de déplacement normale d'un pion
    public static class PawnMove extends Move {
        public PawnMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }
    
    // sous classe de Move : mouvement de saut d'un pion
    public static class PawnJump extends Move {
        public PawnJump(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        
        @Override
        public Board execute() {
            Builder builder = new Builder();
            for (Piece piece : this.board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece))
                    builder.setPiece(piece);
            }
            for(Piece piece : this.board.currentPlayer().getOpponent().getActivePieces())
                builder.setPiece(piece);
            Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }
    
    // sous classe de AttackMove : mouvement d'attaque d'un pion
    public static class PawnAttackMove extends AttackMove {
        private Piece attackedPiece;
            
        public PawnAttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
    }
    
    // sous classe de Move : mouvement de roque
    public static abstract class CastleMove extends Move {
        protected Rook castleRook;
        protected int castleRookStart;
        protected int castleRookDestination;
        
        public CastleMove(Board board, Piece movedPiece, int destinationCoordinate,
                Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }
        
        public Rook getCastkeRook() {
            return this.castleRook;
        }
        
        @Override
        public boolean isCastlingMove() {
            return true;
        }
        
        @Override
        public Board execute() {
            Builder builder = new Builder();
            for (Piece piece : this.board.currentPlayer().getActivePieces()) {
                ///
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece))
                    builder.setPiece(piece);
            }
            for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            // déplacement de la tour vers la bonne case
            builder.setPiece(this.movedPiece.movePiece(this));
            // déplacement de la tour vers la bonne case
            ///
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination));
            // à l'adversaire de jouer ensuite
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
    }
    
    // sous classe de CastleMove : mouvement de petit roque (du côté du roi)
    public static class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate, 
                Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
        
        @Override
        public String toString() {
            return "0-0";
        }
    }
    
    // sous classe de CastleMove : mouvement de grand roque (du côté de la reine)
    public static class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate, 
                Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
        
        @Override
        public String toString() {
            return "0-0-0";
        }
    }
    
    // sous classe de Move : mouvement non valide
    public static class NullMove extends Move {
        public NullMove() {
            super(null, null, -1);
        }
        
        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute the null move!");
        }
    }
    
    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not instnatiable!");
        }
        
        // permet de créer un mouvement
        public static Move createMove(Board board, int currentCoordinate, int destinationCoordinate) {
            for (Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate)
                    return move;
            }
            return NULL_MOVE;
        }
    }
}
