package engine;

import engine.board.BoardUtils;
import engine.player.BlackPlayer;
import engine.player.Player;
import engine.player.WhitePlayer;

public enum Alliance {
    WHITE {
        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public int getOppositeDirection() {
            return 1;
        }
        
        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.eighthRank[position];
        }
        
        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return whitePlayer;
        }
    },
    
    BLACK {
        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public int getOppositeDirection() {
            return -1;
        }
        
        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardUtils.firstRank[position];
        }
        
        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return blackPlayer;
        }
    };
    
    public abstract int getDirection();
    public abstract int getOppositeDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract boolean isPawnPromotionSquare(int position);
    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
