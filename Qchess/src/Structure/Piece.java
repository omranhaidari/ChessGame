package Structure;

public abstract class Piece {
    private int player;
    abstract void movement();
    
    public Piece(){
        this.player = 0;
    }
    
    
}
