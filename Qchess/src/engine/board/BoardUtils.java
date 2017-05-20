package engine.board;

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
    public final static int numTiles = 64;
    public final static int numTilesPerRow = 8;


    public BoardUtils() {
        throw new RuntimeException("You cannot instantiate me! ");
    }
    
    /* pour chaque case de chaque colonne, on ajoute 8 au numéro de la case,
       ce qui le fait changer de colonne
    */
    private static boolean[] initColumn(int columnNumber) {
        boolean[] column = new boolean[numTiles];
        do {
            column[columnNumber] = true;
            columnNumber += numTilesPerRow;
        } while (columnNumber < numTiles);
        return column;
    }
    
    public static boolean[] initRow(int rowNumber) {
        boolean[] row = new boolean[numTiles];
        do {
            row[rowNumber] = true;
            rowNumber ++;
        } while (rowNumber % numTilesPerRow != 0);
        return row;
    }
    
    // ici car utile pour plusieurs pièces
    // si c'est une tileCoordinate valide sur le plateau
    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < numTiles;
    }
}
