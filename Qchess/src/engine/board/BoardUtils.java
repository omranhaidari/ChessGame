package engine.board;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardUtils {
    public static boolean[] firstFile = initColumn(0);
    public static boolean[] secondFile = initColumn(1);
    public static boolean[] seventhFile = initColumn(6);
    public static boolean[] eighthFile = initColumn(7);
    
    public static boolean[] eighthRank = initRow(0);
    public static boolean[] seventhRank = initRow(8);
    public static boolean[] sixthRank = initRow(16);
    public static boolean[] fifthRank = initRow(24);
    public static boolean[] fourthRank = initRow(32);
    public static boolean[] thirdRank = initRow(40);
    public static boolean[] secondRank = initRow(48);
    public static boolean[] firstRank = initRow(56);
    public static final int numTiles = 64;
    public static final int numTilesPerRow = 8;
    public static final String[] algebraicNotation = initializeAlgebraicNotation();
    public final Map<String, Integer> positionToCoordinate = initializePositionToCoordinateMap();


    public BoardUtils() {
        throw new RuntimeException("You cannot instantiate me! ");
    }
    
    /* pour chaque case de chaque colonne, on ajoute 8 au numéro de la case,
       ce qui le fait changer de colonne
    */
    private static boolean[] initColumn(int columnNumber) {
        boolean[] column = new boolean[numTiles];
        for(int i = 0; i < column.length; i++) {
            column[i] = false;
        }
        do {
            column[columnNumber] = true;
            columnNumber += numTilesPerRow;
        } while(columnNumber < numTiles);
        return column;
    }
    
    public static boolean[] initRow(int rowNumber) {
        boolean[] row = new boolean[numTiles];
        for(int i = 0; i < row.length; i++) {
            row[i] = false;
        }
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while(rowNumber % numTilesPerRow != 0);
        return row;
    }
    
    private static Map<String, Integer> initializePositionToCoordinateMap() {
        final Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0 ; i < numTiles; i++) {
            positionToCoordinate.put(algebraicNotation[i], i);
        }
        return ImmutableMap.copyOf(positionToCoordinate);
    }

    private static String[] initializeAlgebraicNotation() {
        return new String[]{
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
    }
    
    // ici car utile pour plusieurs pièces
    // si c'est une tileCoordinate valide sur le plateau
    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < numTiles;
    }
    
    public int getCoordinateAtPosition(String position) {
        return positionToCoordinate.get(position);
    }
    
    public static String getPositionAtCoordinate(int coordinate) {
        return algebraicNotation[coordinate];
    }
}
