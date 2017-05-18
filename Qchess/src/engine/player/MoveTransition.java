package engine.player;

import engine.board.Board;
import engine.board.Move;

public class MoveTransition {
    private Board transitionBoard;
    private Move move;
    private MoveStatus moveStatus; // dit si on peut ou pas faire le mouvement en échec par exemple

    public MoveTransition(Board transitionBoard, Move move, MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }
    
    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }
}
