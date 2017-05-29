package engine.board;

import engine.board.Board.Builder;
import engine.pieces.Pawn;
import engine.pieces.Piece;
import engine.pieces.Rook;

public abstract class Move {
    protected Board board;
    protected Piece movedPiece;
    protected int destinationCoordinate;
    protected boolean isFirstMove;
    public static final Move NULL_MOVE = new NullMove();

    private  Move(Board board, Piece movedPiece, int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    public Move(Board board, int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }
    
    // crée un nouveau plateau de jeu avec le déplacement de la pièce en la sélectionnée
    public Board execute() {
        Builder builder = new Builder();
        for (Piece piece : this.board.currentPlayer().getActivePieces()) {
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
    
    public Board getBoard() {
        return this.board;
    }
    
    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
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
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) 
            return true;
        if(!(other instanceof Move))
            return false;
        Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }
    
    // sous classe de Move : mouvement de déplacement normale d'une pièce
    public static class MajorMove extends Move {
        public MajorMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        
        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof MajorMove && super.equals(other);
        }
        
        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(movedPiece.getPiecePosition()) +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
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
        public boolean isAttack() {
            return true;
        }
        
        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }
    }
    
        
    // sous classe de AttackMove : mouvement d'attaque d'une pièce importante
    public static class MajorAttackMove extends AttackMove {
        public MajorAttackMove(Board board, Piece pieceMoved, int destinationCoordinate, Piece pieceAttacked) {
            super(board, pieceMoved, destinationCoordinate, pieceAttacked);
        }
        
        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }
        
        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(movedPiece.getPiecePosition()) +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    
    // sous classe de Move : mouvement de déplacement normale d'un pion
    public static class PawnMove extends Move {
        public PawnMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
        
        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof PawnMove && super.equals(other);
        }
        
        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(movedPiece.getPiecePosition()) + 
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
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
        
        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(movedPiece.getPiecePosition()) +
                    BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }
    
    // sous classe de AttackMove : mouvement d'attaque d'un pion
    public static class PawnAttackMove extends AttackMove {
        private Piece attackedPiece;
            
        public PawnAttackMove(Board board, Piece movedPiece, int destinationCoordinate, Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }
        
        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }
        
        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) 
                    + "x" + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);  // x : norme du format PNG de l'attaque d'un pion
        }
    }
    
    // sous classe de AttackMove : mouvement d'attaque d'un pion
    public static class PawnPromotion extends Move {
        private Move decoratedMove;
        private Pawn promotedPawn;
            
        public PawnPromotion(Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }
        
        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }
        
        @Override
        public Board execute() {
            Board pawnMovedBoard = this.decoratedMove.execute();
            Board.Builder builder = new Builder();
            for (Piece piece : pawnMovedBoard.currentPlayer().getActivePieces()) {
                if (!this.promotedPawn.equals(piece))
                    builder.setPiece(piece);
            }
            for(Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces())
                builder.setPiece(piece);
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());
            return builder.build();
        }
        
        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }
        
        @Override
        public Piece getAttackedPiece() {
            return this.decoratedMove.getAttackedPiece();
        }
        
        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof PawnPromotion && (super.equals(other));
        }
        
        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) 
                    + "=" + BoardUtils.getPositionAtCoordinate(this.destinationCoordinate);
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
        
        public Rook getCastleRook() {
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
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece))
                    builder.setPiece(piece);
            }
            for (Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            // déplacement de la tour vers la bonne case
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination));
            // à l'adversaire de jouer ensuite
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
            return builder.build();
        }
        
        @Override
        public int hashCode() {
            int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }
        
        @Override
        public boolean equals(Object other) {
            if (this == other)
                return true;
            if(!(other instanceof CastleMove))
                return false;
            CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }
    
    // sous classe de CastleMove : mouvement de petit roque (du côté du roi)
    public static class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate, 
                Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
        
        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof KingSideCastleMove  && super.equals(other);
        }
        
        @Override
        public String toString() {
            return "0-0"; // norme du format PNG du petit roque
        }
    }
    
    // sous classe de CastleMove : mouvement de grand roque (du côté de la reine)
    public static class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(Board board, Piece movedPiece, int destinationCoordinate, 
                Rook castleRook, int castleRookStart, int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }
        
        @Override
        public boolean equals(Object other) {
            return this == other || other instanceof QueenSideCastleMove  && super.equals(other);
        }
        
        @Override
        public String toString() {
            return "0-0-0"; // norme du format PNG du grand roque
        }
    }
    
    // sous classe de Move : mouvement non valide
    public static class NullMove extends Move {
        public NullMove() {
            super(null, 65); // destination invalide
        }
        
        @Override
        public Board execute() {
            throw new RuntimeException("Cannot execute the null move!");
        }
        
        @Override
        public int getCurrentCoordinate() {
            return -1; // coordonée invalide
        }
    }
    
    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not instantiable!");
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
