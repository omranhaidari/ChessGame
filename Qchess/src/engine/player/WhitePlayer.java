package engine.player;

import engine.Alliance;
import engine.board.Board;
import engine.board.Move;
import engine.board.Move.KingSideCastleMove;
import engine.board.Move.QueenSideCastleMove;
import engine.board.Tile;
import engine.pieces.Piece;
import engine.pieces.Rook;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WhitePlayer extends Player{

    public WhitePlayer(Board board, Collection<Move> whiteStandardLegalMoves, 
            Collection<Move> blackStandardLegalMoves) {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }
    
    // Roque blanc
    @Override
    protected Collection<Move> calculateKingCastles(Collection<Move> playerLegals, Collection<Move> opponentsLegals) {
        List<Move> kingCastles = new ArrayList<>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            // petit roque du côté du roi blanc
            if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
                Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    // vérifie qu'aucune des cases libres n'est attaquée
                    if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() && 
                            Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty() && 
                            rookTile.getPiece().getPieceType().isRook()) {
                        kingCastles.add(new KingSideCastleMove(this.board, 
                            this.playerKing, 62, (Rook) rookTile.getPiece(), 
                            rookTile.getTileCoordinate(), 61));
                    }
                }
            }
            // petit roque du côté de la reine blanche
            if (!this.board.getTile(59).isTileOccupied() &&
                    !this.board.getTile(58).isTileOccupied() &&
                    !this.board.getTile(57).isTileOccupied()) {
                Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove() &&
                        Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() &&
                        Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()) {
                    kingCastles.add(new QueenSideCastleMove(this.board, 
                            this.playerKing, 58, (Rook) rookTile.getPiece(), 
                            rookTile.getTileCoordinate(), 59));
                }

            }
        }
        return Collections.unmodifiableList(kingCastles);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }
}
