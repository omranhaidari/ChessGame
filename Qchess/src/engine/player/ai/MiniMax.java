package engine.player.ai;

import engine.board.Board;
import engine.board.Move;
import engine.player.MoveTransition;

public class MiniMax implements MoveStrategy{
    private BoardEvaluator boardEvaluator;

    public MiniMax() {
        this.boardEvaluator = null;
    }
    
    @Override
    public String toString() {
        return "MiniMax";
    }
    
    // retourne le meilleur mouvement grâce au score en allant visiter tous les 
    // mouvements possibles après depth étapes
    @Override
    public Move execute(Board board, int depth) {
        long startTime = System.currentTimeMillis(); // pour connâitre la durée d'éxecution
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;
        System.out.println(board.currentPlayer() + " THINKING with depth = " + depth);
//        this.freqTable = new FreqTableRow[board.currentPlayer().getLegalMoves().size()];
//        this.freqTableIndex = 0;
        int moveCounter = 1;
        int numMoves = board.currentPlayer().getLegalMoves().size();
        for (Move move : board.currentPlayer().getLegalMoves()) {
            MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
//                final FreqTableRow row = new FreqTableRow(move);
//                this.freqTable[this.freqTableIndex] = row;
                // moveTransition.getTransitionBoard() car calcule les mouvements 
                // de transition avec comme point de départ : le tableau de transition
                currentValue = board.currentPlayer().getAlliance().isWhite() ?
                                min(moveTransition.getTransitionBoard(), depth - 1) :
                                max(moveTransition.getTransitionBoard(), depth - 1);
//                System.out.println("\t" + toString() + " analyzing move (" +moveCounter + "/" +numMoves+ ") " + move +
//                                   " scores " + currentValue + " " +this.freqTable[this.freqTableIndex]);
//                this.freqTableIndex++;
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
//            else
//                System.out.println("\t" + toString() + " can't execute move (" +moveCounter+ "/" +numMoves+ ") " + move);
//            moveCounter++;
        }
        long executionTime = System.currentTimeMillis() - startTime;
//        System.out.printf("%s SELECTS %s [#boards = %d time taken = %d ms, rate = %.1f\n", board.currentPlayer(),
//                bestMove, this.boardsEvaluated, this.executionTime, (1000 * ((double)this.boardsEvaluated/this.executionTime)));
//        long total = 0;
//        for (final FreqTableRow row : this.freqTable) {
//            if(row != null) {
//                total += row.getCount();
//            }
//        }
//        if(this.boardsEvaluated != total) {
//            System.out.println("somethings wrong with the # of boards evaluated!");
//        }
        return bestMove;
    }
    
    /* Minimisation : regarde tous les mouvements possibles du joueurs à la même 
       étape, et les fait tous. Affecte des valeurs à chaque mouvement. Puis 
       trouve la valeur minimale en les comparant tous.
    */
    public int min(Board board, int depth) {
        if(depth == 0) { // ou game over
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
        if(depth == 0) {
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
