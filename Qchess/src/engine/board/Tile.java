package engine.board;

import engine.pieces.Piece;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public abstract class Tile {
    protected int tileCoordinate;
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        
        for (int i = 0; i < BoardUtils.NUM_TILES; i++)
            emptyTileMap.put(i, new EmptyTile(i));
        return Collections.unmodifiableMap(emptyTileMap);
    }
        
    public abstract boolean isTileOccupied();
   
    public abstract Piece getPiece();
    
    // si la case est occupée, elle retourne case occupée, sinon elle créé une case vide
    public static Tile createTile(int tileCoordinate, Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : 
                EMPTY_TILES_CACHE.get(tileCoordinate);
    }
    
    private Tile(int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }
   
   // Sous-classe de Tile
   public static class EmptyTile extends Tile{

        private EmptyTile(int coordinate) {
            super(coordinate);
        }
        
        // si la case n'est pas occupée, on affiche : -
        @Override
        public String toString() {
            return "-";
        }
        
        @Override
        public boolean isTileOccupied() {
            return false;
        }
        
        // Il n'y a rien sur la tuile à retourner
        public Piece getPiece() {
            return null;
        }
   }
   
   // Sous-classe de Tile
   public static class OccupiedTile extends Tile {
        private final Piece pieceOnTile;
       
        private OccupiedTile(int tileCoordinate, Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        // si la case est occupée, on affiche la pièce les noires en lettre minuscule et les blanches en lettre majuscule
        @Override
        public String toString() {
            return getPiece().getPieceAlliance().isBlack() ? 
                    getPiece().toString().toLowerCase() : getPiece().toString();
        }
        
        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.pieceOnTile;
        }
   }
}
