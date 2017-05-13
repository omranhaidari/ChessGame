package engine.board;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);
    public static final boolean[] SECOND_ROW = null;
    public static final boolean[] THIRD_ROW = null;
    public static final boolean[] SIXTH_ROW = null;
    public static final boolean[] SEVENTH_ROW = null;
    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;


    public BoardUtils() {
        throw new RuntimeException("You cannot instantiate me! ");
    }
    
    /* pour chaque case de chaque colonne, on ajoute 8 au numéro de la case,
       ce qui le fait changer de colonne
    */
    private static boolean[] initColumn(int columnNumber) {
        boolean[] column = new boolean[64];
        do {
            column[columnNumber] = true;
            columnNumber += 8;
        } while (columnNumber < 64);
        return column;
    }
    
    // ici car utile pour plusieurs pièces
    // si c'est une tileCoordinate valide sur le plateau
    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }
    
    
}
