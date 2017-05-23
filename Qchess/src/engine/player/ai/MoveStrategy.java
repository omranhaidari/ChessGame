package engine.player.ai;

import engine.board.Board;
import engine.board.Move;

public interface MoveStrategy {

    Move execute(Board board, int depth); // depth : le nombre d'étapes limite
}
