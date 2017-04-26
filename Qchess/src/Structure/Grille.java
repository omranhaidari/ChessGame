package Structure;


public class Grille {
    public Cell[][] grid;
    
    public Grille(){
        boolean blanc=true;
        grid = new Cell[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(blanc){
                    grid[i][j] = new Cell("Blanc");
                    blanc=false;
                }else{
                    grid[i][j] = new Cell("Noir");
                    blanc=true;
                }
            }
            blanc=!blanc;
        }
    }
    
    public void printGrid(){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){ 
                String a = grid[i][j].getCouleur();
                a = a.split("")[0] + " ";
                System.out.print(a);
            }
        System.out.println("");
        }
    }
}
