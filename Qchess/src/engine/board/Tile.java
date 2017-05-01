package engine.board;

import engine.pieces.Piece;
import java.util.HashMap;
import java.util.Map;
import jdk.nashorn.internal.ir.annotations.Immutable;
import com.google.common.collect.ImmutableMap;
// import java.util.Collections;

public abstract class Tile {
//    private String color;
//    
//    public Tile(String color){
//        this.color = color;
//    }
//    
//    public String getColor(){
//        return this.color;
//    }
//    
//    public void setColor(String newColor){
//        this.color = newColor;
//    }
    
    protected final int tileCoordinate;
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();

    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
        
        for (int i = 0; i < 64; i++)
            emptyTileMap.put(i, new EmptyTile(i));
        // Collections.unmodifiableMap(emptyTileMap);
        return ImmutableMap.copyOf(emptyTileMap);
    }
        
    public abstract boolean isTileOccupied();
   
    public abstract Piece getPiece();
    
    public static Tile createTile(final int tileCoordinate, final Piece piece) {
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : 
                EMPTY_TILES_CACHE.get(tileCoordinate);
    }
    
    private Tile(int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }
   
   // Sous-classe de Tile
   public static final class EmptyTile extends Tile{

        private EmptyTile(final int coordinate) {
            super(coordinate);
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
   public static final class OccupiedTile extends Tile {
        private final Piece pieceOnTile;
       
        private OccupiedTile(int tileCoordinate, Piece pieceOnTile) {
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
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
