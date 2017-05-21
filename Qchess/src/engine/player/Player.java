package engine.player;

import engine.Alliance;
import engine.board.Board;
import engine.board.Move;
import engine.pieces.King;
import engine.pieces.Piece;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class Player {
    protected Board board;
    protected King playerKing;
    protected Collection<Move> legalMoves;
    private boolean isInCheck;

    public Player(Board board, Collection<Move> legalMoves, Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        // concaténation pour savoir quelles sont les attaques ennemies afin de trouver les mouvement possibles
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves)));
        // les autres pièces attaquent le roi et que la liste de mouvement qui attaquent le roi n'est pas vide
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    // parcourt toutes les pièces présentes sur le plateau de jeu puis retourne 
    // le roi, sinon retourne une exception car ce n'est pas un jeu valide
    private King establishKing() {
        for (Piece piece : getActivePieces()) {
            if (piece.getPieceType().isKing())
                return (King) piece;
        }
        throw new RuntimeException("Should not reach here! Not a valid board!!");
    }
    
    // si les mouvements possibles des pièces ennemies correspondent arrivent sur le roi alors elles attaquent le roi
    // retourne une collection de mouvements qui attanquent le roi
    protected static Collection<Move> calculateAttacksOnTile(int piecePosition, Collection<Move> moves) {
        List<Move> attackMoves = new ArrayList<>();
        for (Move move : moves) {
            if(piecePosition == move.getDestinationCoordinate())
                attackMoves.add(move);
        }
        return Collections.unmodifiableList(attackMoves);
    }
    
    public MoveTransition makeMove(Move move) {
        if(!isMoveLegal(move))
            return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
        Board transitionBoard = move.execute();
        // calcule les attaques adverses par rapport au roi pour savoir si on peut jouer la pièce que l'on veut
        Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
                transitionBoard.currentPlayer().getLegalMoves());
        if(!kingAttacks.isEmpty())
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
    }
    
    // calcule les différents mouvements des pièces du joueur en échec, pour savoir si le roi peut s'en échapper
    protected boolean hasEscapeMoves() {
        for (Move move : this.legalMoves) {
            MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone())
                return true;
        }
        return false;
    }
    
    public boolean isMoveLegal(Move move) {
        return !(move.isCastlingMove() && isInCheck()) && this.legalMoves.contains(move);
    }
    
    public boolean isInCheck() {
        return this.isInCheck;
    }
    
    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }
    
    // le pat : pour celui qui a le trait (= dont c'est le tour) ne peut pas faire autrement que de mettre son roi en échec
    // le roi n'est pas en échec avant le mouvement, mais l'est après. La partie est alors déclarée nulle
    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves() ;
    }
    
    public boolean isCastled() {
        return false;
    }
        
    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }
    
    private King getPlayerKing() {
        return this.playerKing;
    }
    
    public abstract Collection<Piece> getActivePieces();
    public abstract Alliance getAlliance();
    public abstract Player getOpponent();
    protected abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals);
}