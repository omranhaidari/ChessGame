package Structure;

public class Cell {
    private String couleur;
    
    public Cell(String c){
        this.couleur=c;
    }
    
    public String getCouleur(){
        return this.couleur;
    }
    
    public void setCouleur(String coul){
        this.couleur = coul;
    }
}
