package engine.player.ai;

import engine.board.Board;
import engine.board.Move;

public interface MoveStrategy {

    Move execute(Board board); // depth : le nombre d'étapes limite
}
