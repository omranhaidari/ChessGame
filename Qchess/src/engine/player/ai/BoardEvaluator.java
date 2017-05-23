package engine.player.ai;

import engine.board.Board;

public interface BoardEvaluator {

    // convention : positif : les blancss gagnent, négatif : les noirs gagnent et 0 : nul
    int evaluate(Board board, int depth);
}
