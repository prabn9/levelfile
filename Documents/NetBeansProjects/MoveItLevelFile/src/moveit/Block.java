
package moveit;

/**
 *
 * @author prabin
 */
public class Block {
    
    private int row, col;
    
    public Block(int row, int col){
    
        this.row = row;
        this.col = col;
    }
    
     public int row(){
    
        return row;
    }
    
    public int col(){
        
        return col;
    }
    
}
