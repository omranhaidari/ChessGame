package Structure;

public abstract class Piece {
    private int joueur;
    abstract void deplacement();
    
    public Piece(){
        this.joueur = 0;
    }
    
    
}
