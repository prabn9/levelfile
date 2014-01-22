
package moveit;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author prabin
 */


public class Ball {
    
    private int row, col;
    private int color;//this is actuly just a integer value used by game to draw various distinct color 
    Map<Point, Integer> map;
   
    public Ball(int row, int col, int color){
        map = new HashMap<Point, Integer>();
       
        this.row = row;
        this.col = col;
        this.color = color;
        
        map.put(new Point(row, col), color);
       
    }
    
    public int row(){
    
        return row;
    }
    
    public int col(){
        
        return col;
    }
    
    public void row(int row){
    
        this.row = row;
    }
    
    public void col(int col){
        this.col = col;
    }
    
    public int color(){
    
        return color;
    }
    
    public int get(int row, int col){
    
        if(map.containsKey(new Point(row, col))){
            return map.get(new Point (row, col));
        }
        return 0;
    }
    
    
   
    
    
}
