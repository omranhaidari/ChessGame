package engine.player.ai;

import engine.board.Board;
import engine.board.Move;
import engine.player.MoveTransition;

public class MiniMax implements MoveStrategy {
    private BoardEvaluator boardEvaluator;
    private int searchDepth;

    public MiniMax(int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }
    
    // retourne le meilleur mouvement grâce au score en allant visiter tous les 
    // mouvements possibles après depth étapes
    @Override
    public Move execute(Board board) {
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.currentPlayer().getAlliance() + " is thinking with a depth = " + this.searchDepth);
        for (Move move : board.currentPlayer().getLegalMoves()) {
            MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                // moveTransition.getTransitionBoard() car calcule les mouvements 
                // de transition avec comme point de départ : le tableau de transition
                currentValue = board.currentPlayer().getAlliance().isWhite() ?
                                min(moveTransition.getTransitionBoard(), this.searchDepth - 1) :
                                max(moveTransition.getTransitionBoard(), this.searchDepth - 1);
                if (board.currentPlayer().getAlliance().isWhite() && 
                        currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } 
                else if (board.currentPlayer().getAlliance().isBlack() &&
                        currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        return bestMove;
    }
    
    private static boolean isEndGameScenario(Board board) {
        return board.currentPlayer().isInCheckMate() || 
                board.currentPlayer().isInStaleMate();
    }
    
    /* Minimisation : regarde tous les mouvements possibles du joueurs à la même 
       étape, et les fait tous. Affecte des valeurs à chaque mouvement. Puis 
       trouve la valeur minimale en les comparant tous.
    */
    public int min(Board board, int depth) {
        if(depth == 0  || isEndGameScenario(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (Move move : board.currentPlayer().getLegalMoves()) {
            MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue <= lowestSeenValue)
                    lowestSeenValue = currentValue;
            }
        }
        return lowestSeenValue;
    }
    
    // même principe mais recherche la valeur maximale
    public int max(Board board, int depth) {
        if(depth == 0 || isEndGameScenario(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for (Move move : board.currentPlayer().getLegalMoves()) {
            MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);
                if (currentValue >= highestSeenValue)
                    highestSeenValue = currentValue;
            }
        }
        return highestSeenValue;
    }
}
