package chessgame;

import engine.board.Board;
import gui.Table;

public class Qchess {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("The initial standard board :");
        Board board = Board.createStandardBoard();
        System.out.println(board);
        
        Table.get().show();
    }
    
}
