package engine.board;

import engine.Alliance;
import engine.pieces.*;
import engine.player.Player;
import engine.player.WhitePlayer;
import engine.player.BlackPlayer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Iterables;

public class Board {
    private List<Tile> gameBoard;
    private Collection<Piece> whitePieces;
    private Collection<Piece> blackPieces;
    private WhitePlayer whitePlayer;
    private BlackPlayer blackPlayer;
    public Player currentPlayer;
    

    private Board(Builder builder) {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Alliance.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Alliance.BLACK);
        Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);
        Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }
    
    // retourne toutes les pièces par couleur dans le jeu
    private static Collection<Piece> calculateActivePieces(List<Tile> gameBoard, Alliance alliance) {
        List<Piece> activePieces = new ArrayList<>();
        for (Tile tile : gameBoard) {
            if (tile.isTileOccupied()) {
                Piece piece = tile.getPiece();
                if (piece.getPieceAlliance() == alliance)
                    activePieces.add(piece);
            } 
        }
        return Collections.unmodifiableList(activePieces);
    }
    
    // calcule les différents mouvements possibles en fonction des pièces de la même couleur
    private Collection<Move> calculateLegalMoves(Collection<Piece> pieces) {
        List<Move> legalMoves = new ArrayList<>();
        for (Piece piece : pieces) {
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return Collections.unmodifiableList(legalMoves);
    }
    
    /* En utilisant cette liste de Tile de 0 à 63, qui va représenter le plateau
       de jeu, on va pouvoir associer une pièce à une certaine case. La boucle 
       va retourner la pièce associée à une certaine tile. Cela créé une tile.
    */
    private static List<Tile> createGameBoard(Builder builder) {
        Tile[] tiles = new Tile[BoardUtils.numTiles];
        for (int i = 0 ; i < BoardUtils.numTiles ; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
        }
        List<Tile> tilesList = Arrays.asList(tiles);
        return Collections.unmodifiableList(tilesList);
    }
    
    // utilise la classe Builder pour créer le jeu initial avec les pièces positionnées correctement au départ
    public static Board createStandardBoard() {
        Builder builder = new Builder();
        // création des pièces noires sur leurs cases respectives
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new King(Alliance.BLACK, 4));
        builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));
        builder.setPiece(new Pawn(Alliance.BLACK, 16));
        builder.setPiece(new Pawn(Alliance.BLACK, 18));
        builder.setPiece(new Pawn(Alliance.BLACK, 20));
        builder.setPiece(new Pawn(Alliance.BLACK, 22));
        
        // création des pièces blanches sur leurs cases respectives
        builder.setPiece(new Pawn(Alliance.WHITE, 40));
        builder.setPiece(new Pawn(Alliance.WHITE, 42));
        builder.setPiece(new Pawn(Alliance.WHITE, 44));
        builder.setPiece(new Pawn(Alliance.WHITE, 46));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 51));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new King(Alliance.WHITE, 60));
        builder.setPiece(new Knight(Alliance.WHITE, 62));
        // aux blancs de jouer en premier
        builder.setMoveMaker(Alliance.WHITE);
        // construit le tableau
        return builder.build();
    }
    
    // concaténation des joueurs blanc et noir
    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }
    
    public Player whitePlayer() {
        return this.whitePlayer;
    }
    
    public Player blackPlayer() {
        return this.blackPlayer;
    }
    
    public Player currentPlayer() {
        return this.currentPlayer;
    }
        
    public Tile getTile(int tileCoordinate) {
        return gameBoard.get(tileCoordinate);
    }
    
    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }
    
    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }
     
    // print les pièces noires et les pièces blanches différemment
    private static String prettyPrint(Tile tile) {
        return tile.toString();
    }
     
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0 ; i < BoardUtils.numTiles ; i ++) {
            String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % BoardUtils.numTilesPerRow == 0)
                builder.append("\n");
        }
        return builder.toString();
    }
    
    // créer une instance de la classe Board
    public static class Builder {
         Map<Integer, Piece> boardConfig; // numéro d'une case avec une pièce
         Alliance nextMoveMaker; // la personne qui est en train de jouer
        
        public Builder() {
            this.boardConfig = new HashMap<>();
        }
        
        public Builder setPiece(Piece piece) {
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        
        public Builder setMoveMaker(Alliance nextMoveMaker) {
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }
        
        public Board build() {
            return new Board(this);
        }
    }
}