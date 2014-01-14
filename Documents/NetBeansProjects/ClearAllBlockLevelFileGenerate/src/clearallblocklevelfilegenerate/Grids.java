/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clearallblocklevelfilegenerate;

/**
 *
 * @author prabin
 */
public class Grids {
    
    private static String[] grids = new String[]{"3x3","3x4","3x5","4x3","4x4","4x5","5x3","5x4","5x5"};
 
    
    public static String getGrids(int id){
        
        return grids[id];
    }
}
