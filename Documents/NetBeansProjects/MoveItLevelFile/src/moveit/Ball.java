
package moveit;

import java.awt.Point;

/**
 *
 * @author prabin
 */


public class Ball {
    
    private int row, col;
    private int color;//this is actuly just a integer value used by game to draw various distinct color 
   
    public Ball(int row, int col, int color){
        
        this.row = row;
        this.col = col;
        this.color = color;
    }
    
    public int row(){
    
        return row;
    }
    
    public int col(){
        
        return col;
    }
    
    public int color(){
    
        return color;
    }
    
    
}
