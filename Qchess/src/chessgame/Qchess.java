package chessgame;

import engine.board.Board;
import gui.Table;

public class Qchess {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Board board = Board.createStandardBoard();
        System.out.println(board);
        
        Table table = new Table();
    }
    
}
