package Structure;

public class Grid {
    public Cell[][] grid;
    
    public Grid(){
        boolean white = true;
        grid = new Cell[8][8];
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(white){
                    grid[i][j] = new Cell("White");
                    white = false;
                }else{
                    grid[i][j] = new Cell("Black");
                    white = true;
                }
            }
            white =!white;
        }
    }
    
    public void printGrid(){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){ 
                String c = grid[i][j].getColor();
                c = c.split("")[0] + " ";
                System.out.print(c);
            }
        System.out.println("");
        }
    }
}
